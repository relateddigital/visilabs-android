package com.visilabs.capture

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/** Batch isteğini gönderen katman. Bloklayan çağrı; ağ thread'inde çalışır. */
internal fun interface VisilabsCaptureTransport {
    /** @return HTTP durum kodu (ağ hatasında null) ve yanıt gövdesi. */
    fun post(url: String, body: String): Pair<Int?, String>
}

internal class HttpCaptureTransport(private val userAgent: String) : VisilabsCaptureTransport {
    override fun post(url: String, body: String): Pair<Int?, String> {
        var connection: HttpURLConnection? = null
        return try {
            connection = (URL(url).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                connectTimeout = TIMEOUT_MS
                readTimeout = TIMEOUT_MS
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("User-Agent", userAgent)
            }
            connection.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val response = stream?.bufferedReader()?.use { it.readText() } ?: ""
            Pair(code, response)
        } catch (e: IOException) {
            Pair(null, e.message ?: e.javaClass.simpleName)
        } finally {
            connection?.disconnect()
        }
    }

    private companion object {
        const val TIMEOUT_MS = 30_000
    }
}

/**
 * Diske yazılan event kuyruğu. Her event `<uuidv7>.json` olarak saklanır (UUIDv7 zaman sıralı
 * olduğu için dosya adı sırası = event sırası). Uygulama kapanınca veya internet yokken event
 * kaybolmaz; `/batch/` endpoint'ine toplu gönderilir.
 *
 * Tüm metotlar `worker` executor'ından çağrılmalı; HTTP isteği ayrı `network` executor'ında
 * yapılır, böylece yavaş bir istek yeni event'lerin kuyruğa girmesini bekletmez.
 */
internal class VisilabsCaptureQueue(
    private val worker: ScheduledExecutorService,
    private val network: ExecutorService,
    private val transport: VisilabsCaptureTransport,
    private val directory: File?,
    private val logBodies: Boolean
) {
    var apiKey: String? = null

    private val fileNames = ArrayList<String>()
    private var isFlushing = false
    private var retryCount = 0
    private var pausedUntil = 0L
    private var timer: ScheduledFuture<*>? = null
    private val pendingCompletions = ArrayList<() -> Unit>()

    init {
        directory?.listFiles()?.filter { it.name.endsWith(".tmp") }?.forEach { it.delete() }
        directory?.list()?.filter { it.endsWith(".json") }?.sorted()?.let { fileNames.addAll(it) }
    }

    val count: Int get() = fileNames.size

    fun startTimer() {
        if (timer != null) return
        timer = worker.scheduleWithFixedDelay({
            try {
                flush()
            } catch (e: Exception) {
                Log.e(CAPTURE_LOG_TAG, "Zamanlanmış gönderim başarısız: ${e.message}")
            }
        }, FLUSH_INTERVAL_MS, FLUSH_INTERVAL_MS, TimeUnit.MILLISECONDS)
    }

    fun add(event: JSONObject) {
        val directory = directory
        val uuid = event.optString("uuid")
        if (directory == null || uuid.isEmpty()) {
            Log.e(CAPTURE_LOG_TAG, "Event kuyruğa yazılamadı: ${event.optString("event")}")
            return
        }

        while (fileNames.size >= MAX_QUEUE_SIZE) {
            Log.w(CAPTURE_LOG_TAG, "Kuyruk dolu, en eski event siliniyor.")
            deleteFiles(listOf(fileNames[0]))
        }

        val fileName = "$uuid.json"
        try {
            // Yarım yazılmış dosya kuyruğa girmesin diye önce geçici dosyaya yazılıp taşınıyor.
            val temp = File(directory, "$uuid.tmp")
            temp.writeText(event.toString())
            if (!temp.renameTo(File(directory, fileName))) throw IOException("rename failed")
            fileNames.add(fileName)
        } catch (e: IOException) {
            Log.e(CAPTURE_LOG_TAG, "Event diske yazılamadı: ${e.message}")
            return
        }

        if (fileNames.size >= FLUSH_AT) {
            flush()
        }
    }

    /** `completion` gönderim bittiğinde (başarılı ya da başarısız) worker thread'inde çağrılır. */
    fun flush(completion: (() -> Unit)? = null) {
        completion?.let { pendingCompletions.add(it) }
        if (isFlushing) return
        val apiKey = apiKey
        if (apiKey.isNullOrEmpty() || fileNames.isEmpty() || System.currentTimeMillis() < pausedUntil) {
            finishFlush()
            return
        }

        val batchFileNames = fileNames.take(MAX_BATCH_SIZE)
        val events = JSONArray()
        val unreadable = ArrayList<String>()
        for (fileName in batchFileNames) {
            try {
                events.put(JSONObject(File(directory, fileName).readText()))
            } catch (e: Exception) {
                unreadable.add(fileName)
            }
        }
        deleteFiles(unreadable)
        val sentFileNames = batchFileNames.filter { it !in unreadable }
        if (sentFileNames.isEmpty()) {
            finishFlush()
            return
        }

        val body = JSONObject()
            .put("api_key", apiKey)
            .put("batch", events)
            .put("sent_at", VisilabsCaptureUtils.iso8601(System.currentTimeMillis()))
        if (logBodies) {
            Log.d(CAPTURE_LOG_TAG, "${events.length()} event gönderiliyor\n${body.toString(2)}")
        }

        isFlushing = true
        val payload = body.toString()
        network.execute {
            val (statusCode, responseBody) = transport.post(HOST + BATCH_PATH, payload)
            worker.execute { handleResponse(statusCode, responseBody, sentFileNames) }
        }
    }

    private fun handleResponse(statusCode: Int?, responseBody: String, sentFileNames: List<String>) {
        isFlushing = false
        when {
            statusCode != null && statusCode in 200..299 -> {
                Log.i(CAPTURE_LOG_TAG, "${sentFileNames.size} event gönderildi.")
                deleteFiles(sentFileNames)
                retryCount = 0
                pausedUntil = 0
                // Kuyrukta hala event varsa devam et.
                if (fileNames.isNotEmpty()) {
                    flush()
                    return
                }
            }
            statusCode == null || statusCode == 408 || statusCode == 429 || statusCode >= 500 -> {
                // Ağ hatası / sunucu hatası: event'ler kuyrukta kalır, artan beklemeyle tekrar denenir.
                retryCount += 1
                val delay = minOf(FLUSH_INTERVAL_MS shl minOf(retryCount - 1, 10), MAX_RETRY_DELAY_MS)
                pausedUntil = System.currentTimeMillis() + delay
                Log.w(CAPTURE_LOG_TAG, "Gönderim başarısız (${statusCode ?: responseBody}), ${delay / 1000} sn sonra tekrar denenecek.")
            }
            else -> {
                // 400 vb.: tekrar denemek sonucu değiştirmez, event'ler atılır.
                Log.e(CAPTURE_LOG_TAG, "HTTP $statusCode, ${sentFileNames.size} event atıldı. $responseBody")
                deleteFiles(sentFileNames)
                retryCount = 0
                pausedUntil = 0
            }
        }
        finishFlush()
    }

    private fun finishFlush() {
        val completions = ArrayList(pendingCompletions)
        pendingCompletions.clear()
        completions.forEach { it() }
    }

    private fun deleteFiles(names: List<String>) {
        if (names.isEmpty()) return
        names.forEach { File(directory, it).delete() }
        fileNames.removeAll(names.toSet())
    }

    companion object {
        const val HOST = "https://event.visilabs.net"
        const val BATCH_PATH = "/batch/"

        /** Bu kadar event birikince beklemeden gönderilir. */
        const val FLUSH_AT = 20
        /** Kuyrukta bekleyen event'ler en geç bu sürede gönderilir. */
        const val FLUSH_INTERVAL_MS = 10_000L
        const val MAX_BATCH_SIZE = 50
        const val MAX_QUEUE_SIZE = 1000
        const val MAX_RETRY_DELAY_MS = 5 * 60 * 1000L
    }
}
