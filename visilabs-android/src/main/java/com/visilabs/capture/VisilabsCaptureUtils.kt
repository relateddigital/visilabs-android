package com.visilabs.capture

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

internal const val CAPTURE_LOG_TAG = "VisilabsCapture"

internal object VisilabsCaptureUtils {

    private val random = SecureRandom()

    private val iso8601Format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    /**
     * RFC 9562 UUIDv7: ilk 48 bit unix milisaniye, kalanı rastgele.
     * PostHog `$session_id` için UUIDv7 şart koşuyor (aksi halde event session hesaplarına girmiyor).
     * Zaman sıralı olduğu için kuyruk dosya adı olarak da kullanılıyor.
     */
    fun uuidV7(timeMillis: Long = System.currentTimeMillis()): String {
        val bytes = ByteArray(16)
        random.nextBytes(bytes)
        val milliseconds = maxOf(0L, timeMillis)
        for (index in 0 until 6) {
            bytes[index] = (milliseconds ushr (8 * (5 - index))).toByte()
        }
        bytes[6] = ((bytes[6].toInt() and 0x0F) or 0x70).toByte() // version 7
        bytes[8] = ((bytes[8].toInt() and 0x3F) or 0x80).toByte() // RFC 4122 variant
        val buffer = ByteBuffer.wrap(bytes)
        return UUID(buffer.long, buffer.long).toString()
    }

    /**
     * PostHog `timestamp` / `sent_at` alanları ISO 8601 bekliyor. Unix saniye string'i
     * ("1790922526") HTTP 200 dönse de ingestion'da geçersiz sayılıp sunucu saatiyle değiştiriliyor.
     */
    fun iso8601(timeMillis: Long): String = synchronized(iso8601Format) {
        iso8601Format.format(Date(timeMillis))
    }

    /** Değerleri JSON'a yazılabilir hale getirir; desteklenmeyen tipler loglanıp atılır. */
    fun sanitize(properties: Map<String, Any?>?): JSONObject {
        val result = JSONObject()
        properties?.forEach { (key, value) ->
            val sanitized = sanitizeValue(value)
            if (sanitized == null) {
                Log.w(CAPTURE_LOG_TAG, "'$key' property'si desteklenmeyen tipte (${value?.javaClass?.name}), gönderilmeyecek.")
            } else {
                result.put(key, sanitized)
            }
        }
        return result
    }

    private fun sanitizeValue(value: Any?): Any? = when (value) {
        null -> JSONObject.NULL
        is String, is Boolean, is Int, is Long, is Short, is Byte -> value
        // NaN / Infinity JSON'a yazılamaz.
        is Double -> value.takeIf { it.isFinite() }
        // 4.8f.toDouble() = 4.800000190734863 olur; string üzerinden çevrilerek 4.8 korunuyor.
        is Float -> value.takeIf { it.isFinite() }?.toString()?.toDouble()
        is Number -> value.takeIf { it.toDouble().isFinite() }
        is Char -> value.toString()
        is Date -> iso8601(value.time)
        is UUID, is java.net.URL, is java.net.URI, is android.net.Uri -> value.toString()
        is JSONObject, is JSONArray -> value
        is Map<*, *> -> JSONObject().also { json ->
            value.forEach { (key, item) -> sanitizeValue(item)?.let { json.put(key.toString(), it) } }
        }
        is Iterable<*> -> JSONArray().also { json -> value.forEach { item -> sanitizeValue(item)?.let { json.put(it) } } }
        is Array<*> -> JSONArray().also { json -> value.forEach { item -> sanitizeValue(item)?.let { json.put(it) } } }
        else -> null
    }

    /** Kaynaktaki değerleri hedefe yazar; aynı anahtarda kaynak kazanır. */
    fun merge(target: JSONObject, source: Map<String, Any?>) {
        source.forEach { (key, value) -> target.put(key, value ?: JSONObject.NULL) }
    }

    fun merge(target: JSONObject, source: JSONObject) {
        val keys = source.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            target.put(key, source.get(key))
        }
    }
}
