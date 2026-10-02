package com.visilabs.capture

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import org.json.JSONObject
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * PostHog uyumlu `capture` altyapısı. posthog-ios / posthog-android'in kimlik, session ve
 * uygulama yaşam döngüsü davranışını taklit eder, event'leri `event.visilabs.net/batch/`
 * adresine gönderir. SDK içi kullanım içindir; uygulamalar `Visilabs` üzerindeki metotları kullanır.
 *
 * Kimlik akışı:
 * 1. İlk açılış: `distinct_id` = anonim UUIDv7.
 * 2. `login`/`signUp` (exVisitorId): `$identify` event'i `distinct_id` = exVisitorId ve
 *    `$anon_distinct_id` = önceki anonim id ile gider. PostHog iki kimliği bu event'te birleştirir;
 *    login öncesi anonim event'ler de kullanıcıya bağlanır. Sonraki tüm event'ler exVisitorId ile gider.
 * 3. `logout`: yeni anonim id + yeni session. `$device_id` değişmez.
 */
class VisilabsCapture internal constructor(
    private val identity: VisilabsCaptureIdentity,
    private val versionPrefs: SharedPreferences,
    private val environment: VisilabsCaptureEnvironment,
    transport: VisilabsCaptureTransport,
    directory: File?
) {
    private val session = VisilabsCaptureSession()
    private val worker: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor { runnable ->
        Thread(runnable, "VisilabsCapture").also {
            it.isDaemon = true
            workerThread = it
        }
    }
    private val network: ExecutorService = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "VisilabsCaptureNetwork").apply { isDaemon = true }
    }
    @Volatile
    private var workerThread: Thread? = null
    private val queue = VisilabsCaptureQueue(worker, network, transport, directory, environment.isDebuggable)

    private var apiKey: String? = null
    private var didRecordAppVersion = false

    // Uygulama yaşam döngüsü; sadece main thread'den erişilir.
    private var lifecycleRegistered = false
    private var isAppInBackground = true
    private var isFreshLaunch = true

    private val isEnabled: Boolean get() = apiKey != null

    // region Kurulum

    /**
     * Visilabs her oluşturulduğunda çalışır. Tekrar çağrılması güvenlidir.
     *
     * @param exVisitorId Visilabs'ta kayıtlı kullanıcı. SDK güncellemesinden önce login olmuş
     * kullanıcıların capture tarafında da tanınması için kullanılır.
     * @param isExistingInstall Visilabs daha önce bu cihazda çalıştıysa `true`. Capture özelliğini
     * içeren SDK sürümüne geçişte tüm mevcut kullanıcılar için sahte `Application Installed`
     * gönderilmesini engeller.
     */
    fun configure(apiKey: String?, exVisitorId: String?, isExistingInstall: Boolean) {
        val key = apiKey?.trim()?.takeIf { it.isNotEmpty() }
        execute {
            this.apiKey = key
            queue.apiKey = key
            val installOrUpdate = recordAppVersion(isExistingInstall)

            if (!isEnabled) {
                Log.i(CAPTURE_LOG_TAG, "captureApiKey verilmediği için capture kapalı.")
                return@execute
            }
            if (!exVisitorId.isNullOrBlank()) {
                identifyOnWorker(exVisitorId)
            }
            installOrUpdate?.let { (event, properties) ->
                enqueue(event, JSONObject(properties), System.currentTimeMillis())
            }
            queue.startTimer()
            queue.flush()
        }
        registerLifecycle()
    }

    // endregion

    // region Public API'nin karşılıkları

    fun capture(event: String, properties: Map<String, Any?>?) {
        val timestamp = System.currentTimeMillis()
        val name = event.trim()
        if (name.isEmpty()) {
            Log.e(CAPTURE_LOG_TAG, "Event adı boş olamaz.")
            return
        }
        val sanitized = VisilabsCaptureUtils.sanitize(properties)
        execute {
            if (!isEnabled) {
                Log.w(CAPTURE_LOG_TAG, "captureApiKey verilmediği için '$name' gönderilmedi.")
                return@execute
            }
            // Event içinde `$set` / `$set_once` varsa kişi profili işlenmeli (posthog ile aynı).
            if (sanitized.has("\$set") || sanitized.has("\$set_once")) {
                identity.personProcessing = true
            }
            enqueue(name, sanitized, timestamp)
        }
    }

    fun identify(distinctId: String) {
        execute {
            if (isEnabled) identifyOnWorker(distinctId)
        }
    }

    fun setPersonProperties(properties: Map<String, Any?>?, setOnce: Map<String, Any?>?) {
        val set = VisilabsCaptureUtils.sanitize(properties)
        val once = VisilabsCaptureUtils.sanitize(setOnce)
        if (set.length() == 0 && once.length() == 0) return
        val timestamp = System.currentTimeMillis()
        execute {
            if (!isEnabled) return@execute
            identity.personProcessing = true
            enqueue("\$set", JSONObject(), timestamp, userProperties = set, userPropertiesSetOnce = once)
        }
    }

    /** Logout: yeni anonim kimlik ve yeni session. */
    fun reset() {
        execute {
            identity.reset()
            session.reset()
        }
    }

    @JvmOverloads
    fun flush(completion: (() -> Unit)? = null) {
        execute { queue.flush(completion) }
    }

    val distinctId: String get() = read { identity.distinctId }
    val anonymousId: String get() = read { identity.anonymousId }
    val deviceId: String get() = read { identity.deviceId }

    // endregion

    // region Kimlik

    /** posthog `identify` mantığı. Worker thread'inde çalışmalı. */
    private fun identifyOnWorker(newDistinctId: String) {
        val newId = newDistinctId.trim()
        if (newId.isEmpty()) return

        if (identity.isIdentified && identity.distinctId != newId) {
            // posthog bu durumda hiçbir şey yapmaz (önce reset bekler). Visilabs'ta exVisitorId
            // değişince yeni ziyaretçi kabul edildiği için burada da önce kimlik sıfırlanıyor;
            // böylece iki farklı kullanıcı aynı kişi profilinde birleşmez.
            Log.i(CAPTURE_LOG_TAG, "Farklı bir kullanıcı ile login, kimlik sıfırlanıyor.")
            identity.reset()
            session.reset()
        }

        val previousDistinctId = identity.distinctId
        if (!identity.isIdentified && previousDistinctId != newId) {
            identity.distinctId = newId
            identity.isIdentified = true
            identity.personProcessing = true
            // PostHog anonim ve tanımlı kişiyi sadece `$identify` event'indeki `$anon_distinct_id`
            // ile birleştirir. Başka event'lere `$anon_distinct_id` eklemenin etkisi yoktur.
            enqueue(
                "\$identify",
                JSONObject().put("distinct_id", newId).put("\$anon_distinct_id", previousDistinctId),
                System.currentTimeMillis()
            )
        } else if (!identity.isIdentified) {
            // Anonim id ile aynı değerle identify edildi: birleştirilecek başka kimlik yok.
            identity.isIdentified = true
            identity.personProcessing = true
            enqueue("\$set", JSONObject(), System.currentTimeMillis(), userProperties = JSONObject())
        }
        // Aynı kullanıcıyla tekrar login: bir şey yapmaya gerek yok.
    }

    // endregion

    // region Event oluşturma

    /** Worker thread'inde çalışmalı. */
    private fun enqueue(
        event: String,
        properties: JSONObject,
        timestamp: Long,
        userProperties: JSONObject? = null,
        userPropertiesSetOnce: JSONObject? = null
    ) {
        val props = JSONObject()
        VisilabsCaptureUtils.merge(props, environment.staticProperties())
        VisilabsCaptureUtils.merge(props, environment.dynamicProperties())
        props.put("\$device_id", identity.deviceId)
        props.put("\$session_id", session.sessionId(timestamp))
        props.put("\$is_identified", identity.isIdentified)
        // posthog varsayılanı (identified only): anonim kullanıcılar için kişi profili oluşturulmaz,
        // event'leri daha ucuzdur. identify sonrası anonim event'ler yine kişiye bağlanır.
        props.put("\$process_person_profile", identity.isIdentified || identity.personProcessing)
        userProperties?.let { props.put("\$set", it) }
        userPropertiesSetOnce?.takeIf { it.length() > 0 }?.let { props.put("\$set_once", it) }
        // Event'in kendi alanları context'i ezer.
        VisilabsCaptureUtils.merge(props, properties)

        queue.add(
            JSONObject()
                .put("event", event)
                .put("distinct_id", identity.distinctId)
                .put("properties", props)
                .put("timestamp", VisilabsCaptureUtils.iso8601(timestamp))
                // PostHog aynı uuid'li event'i tekrar yazmaz; yeniden denemelerde çift kayıt oluşmaz.
                .put("uuid", VisilabsCaptureUtils.uuidV7(timestamp))
        )
    }

    // endregion

    // region Uygulama yaşam döngüsü

    /**
     * `Application Installed` / `Application Updated` kararını verir ve sürümü kaydeder.
     * Capture kapalıyken de sürüm kaydedilir; ileride açıldığında yanlış "Installed" gitmez.
     */
    private fun recordAppVersion(isExistingInstall: Boolean): Pair<String, Map<String, Any?>>? {
        if (didRecordAppVersion) return null
        didRecordAppVersion = true

        val version = environment.appVersion
        val build = environment.appBuild
        val previousVersion = versionPrefs.getString(KEY_APP_VERSION, null)
        val previousBuild = if (versionPrefs.contains(KEY_APP_BUILD)) versionPrefs.getLong(KEY_APP_BUILD, 0) else null
        versionPrefs.edit().apply {
            if (version != null) putString(KEY_APP_VERSION, version) else remove(KEY_APP_VERSION)
            if (build != null) putLong(KEY_APP_BUILD, build) else remove(KEY_APP_BUILD)
        }.apply()

        val properties = mutableMapOf<String, Any?>("version" to version, "build" to build)
        if (previousVersion == null && previousBuild == null) {
            return if (isExistingInstall) null else Pair("Application Installed", properties)
        }
        if (previousVersion == version && previousBuild == build) return null
        properties["previous_version"] = previousVersion
        properties["previous_build"] = previousBuild
        return Pair("Application Updated", properties)
    }

    /**
     * Ön plan / arka plan geçişleri ProcessLifecycleOwner ile izleniyor: ekran döndürme gibi
     * yapılandırma değişikliklerinde yanlış event üretmez ve geç eklenen gözlemciye mevcut
     * durumu (ör. uygulama zaten açıksa ON_START) hemen bildirir.
     */
    private fun registerLifecycle() {
        val register = Runnable {
            if (lifecycleRegistered) return@Runnable
            lifecycleRegistered = true
            try {
                ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_START -> onForeground()
                        Lifecycle.Event.ON_STOP -> onBackground()
                        else -> Unit
                    }
                })
            } catch (e: Throwable) {
                Log.w(CAPTURE_LOG_TAG, "Uygulama yaşam döngüsü izlenemiyor: ${e.message}")
            }
        }
        if (Looper.myLooper() == Looper.getMainLooper()) register.run() else Handler(Looper.getMainLooper()).post(register)
    }

    internal fun onForeground() {
        if (!isAppInBackground) return
        isAppInBackground = false

        val properties = JSONObject().put("from_background", !isFreshLaunch)
        if (isFreshLaunch) {
            environment.appVersion?.let { properties.put("version", it) }
            environment.appBuild?.let { properties.put("build", it) }
            isFreshLaunch = false
        }
        val timestamp = System.currentTimeMillis()
        execute {
            if (!isEnabled) return@execute
            enqueue("Application Opened", properties, timestamp)
            queue.flush()
        }
    }

    /** Arka plana geçerken kuyruk boşaltılır; süreç öldürülse bile event'ler diskte kalır. */
    internal fun onBackground() {
        if (isAppInBackground) return
        isAppInBackground = true

        val timestamp = System.currentTimeMillis()
        execute {
            if (!isEnabled) return@execute
            enqueue("Application Backgrounded", JSONObject(), timestamp)
            queue.flush()
        }
    }

    // endregion

    // region Thread yardımcıları

    private fun execute(block: () -> Unit) {
        worker.execute {
            try {
                block()
            } catch (e: Exception) {
                Log.e(CAPTURE_LOG_TAG, "capture hatası: ${e.message}", e)
            }
        }
    }

    private fun <T> read(block: () -> T): T =
        if (Thread.currentThread() === workerThread) block() else worker.submit(Callable { block() }).get()

    // endregion

    companion object {
        private const val PREFS_NAME = "visilabs_capture"
        private const val KEY_APP_VERSION = "appVersion"
        private const val KEY_APP_BUILD = "appBuild"
        private const val QUEUE_DIRECTORY = "visilabs_capture_queue"
        private const val LIB_NAME = "visilabs-android"

        @Volatile
        private var instance: VisilabsCapture? = null

        @JvmStatic
        fun shared(context: Context, sdkVersion: String): VisilabsCapture =
            instance ?: synchronized(this) {
                instance ?: create(context.applicationContext ?: context, sdkVersion).also { instance = it }
            }

        private fun create(context: Context, sdkVersion: String): VisilabsCapture {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val environment = AndroidCaptureEnvironment(context, LIB_NAME, sdkVersion)
            // noBackupFilesDir: kuyruk Auto Backup ile başka cihaza taşınmasın.
            val directory = File(context.noBackupFilesDir, QUEUE_DIRECTORY).takeIf { it.exists() || it.mkdirs() }
            return VisilabsCapture(
                VisilabsCaptureIdentity(prefs),
                prefs,
                environment,
                HttpCaptureTransport("$LIB_NAME/$sdkVersion"),
                directory
            )
        }
    }
}
