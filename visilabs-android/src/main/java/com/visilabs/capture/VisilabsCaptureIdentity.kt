package com.visilabs.capture

import android.content.SharedPreferences

/**
 * posthog-ios / posthog-android kimlik yönetiminin karşılığı. Üç farklı kimlik var:
 *
 * - `anonymousId`: İlk açılışta üretilen UUIDv7. Kullanıcı login olana kadar `distinct_id` budur.
 *   `reset()` (logout) ile yenilenir.
 * - `distinctId`: Event'lerin gittiği kişi. Login öncesi `anonymousId`, login sonrası exVisitorId.
 * - `deviceId` (`$device_id`): Kurulum başına sabit; login/logout'tan etkilenmez, sadece
 *   uygulama silinip yüklenince değişir.
 *
 * Sadece `VisilabsCapture`'ın tek thread'li executor'ından erişilir.
 */
internal class VisilabsCaptureIdentity(private val prefs: SharedPreferences) {

    var anonymousId: String
        get() = prefs.getString(KEY_ANONYMOUS_ID, null)?.takeIf { it.isNotEmpty() }
            ?: VisilabsCaptureUtils.uuidV7().also { prefs.edit().putString(KEY_ANONYMOUS_ID, it).apply() }
        set(value) = prefs.edit().putString(KEY_ANONYMOUS_ID, value).apply()

    var distinctId: String
        get() = prefs.getString(KEY_DISTINCT_ID, null)?.takeIf { it.isNotEmpty() } ?: anonymousId
        set(value) = prefs.edit().putString(KEY_DISTINCT_ID, value).apply()

    val deviceId: String
        get() = prefs.getString(KEY_DEVICE_ID, null)?.takeIf { it.isNotEmpty() }
            ?: anonymousId.also { prefs.edit().putString(KEY_DEVICE_ID, it).apply() }

    var isIdentified: Boolean
        get() = prefs.getBoolean(KEY_IS_IDENTIFIED, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_IDENTIFIED, value).apply()

    /**
     * Anonim kullanıcı için `setPersonProperties` çağrıldığında açılır; o andan itibaren
     * event'ler `$process_person_profile: true` ile gider.
     */
    var personProcessing: Boolean
        get() = prefs.getBoolean(KEY_PERSON_PROCESSING, false)
        set(value) = prefs.edit().putBoolean(KEY_PERSON_PROCESSING, value).apply()

    /** Logout: yeni anonim kimlik. `deviceId` korunur. */
    fun reset() {
        deviceId
        prefs.edit()
            .remove(KEY_DISTINCT_ID)
            .remove(KEY_IS_IDENTIFIED)
            .remove(KEY_PERSON_PROCESSING)
            .putString(KEY_ANONYMOUS_ID, VisilabsCaptureUtils.uuidV7())
            .apply()
    }

    private companion object {
        const val KEY_ANONYMOUS_ID = "anonymousId"
        const val KEY_DISTINCT_ID = "distinctId"
        const val KEY_DEVICE_ID = "deviceId"
        const val KEY_IS_IDENTIFIED = "isIdentified"
        const val KEY_PERSON_PROCESSING = "personProcessing"
    }
}

/**
 * posthog-ios / posthog-android session yönetiminin karşılığı: 30 dk hareketsizlikte veya
 * 24 saati geçince yeni `$session_id` (UUIDv7). Bellekte tutulur; soğuk açılış yeni session'dır.
 */
internal class VisilabsCaptureSession {

    private var sessionId: String? = null
    private var startedAt = 0L
    private var lastActivityAt = 0L

    fun sessionId(timeMillis: Long): String {
        val current = sessionId
        if (current != null &&
            timeMillis - lastActivityAt < INACTIVITY_TIMEOUT_MS &&
            timeMillis - startedAt < MAX_LENGTH_MS
        ) {
            lastActivityAt = maxOf(lastActivityAt, timeMillis)
            return current
        }
        // UUIDv7'nin zaman kısmı session'ın ilk event'inden sonra olmamalı; event zamanıyla üretiliyor.
        val newSessionId = VisilabsCaptureUtils.uuidV7(timeMillis)
        sessionId = newSessionId
        startedAt = timeMillis
        lastActivityAt = timeMillis
        return newSessionId
    }

    fun reset() {
        sessionId = null
    }

    companion object {
        const val INACTIVITY_TIMEOUT_MS = 30 * 60 * 1000L
        const val MAX_LENGTH_MS = 24 * 60 * 60 * 1000L
    }
}
