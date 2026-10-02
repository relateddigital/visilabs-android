package com.visilabs.capture

import android.app.UiModeManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.util.Locale
import java.util.TimeZone

/** Cihaz / uygulama bilgisinin kaynağı. Testlerde sahte bir implementasyon verilebiliyor. */
internal interface VisilabsCaptureEnvironment {
    val appVersion: String?
    val appBuild: Long?
    /** Uygulama debug build ise gönderilen istek gövdeleri logcat'e yazılır. */
    val isDebuggable: Boolean
    fun staticProperties(): Map<String, Any?>
    fun dynamicProperties(): Map<String, Any?>
}

/**
 * Her capture event'ine otomatik eklenen `$` property'leri. Anahtar isimleri posthog-android
 * `PostHogAndroidContext` ile aynı tutuldu; PostHog arayüzündeki OS / Device Type / Library
 * kolonları bu isimleri okuyor.
 */
internal class AndroidCaptureEnvironment(
    context: Context,
    private val libName: String,
    private val libVersion: String
) : VisilabsCaptureEnvironment {

    private val appContext: Context = context.applicationContext ?: context
    private val packageInfo: PackageInfo? = try {
        appContext.packageManager.getPackageInfo(appContext.packageName, 0)
    } catch (e: Exception) {
        null
    }

    override val appVersion: String? = packageInfo?.versionName

    override val appBuild: Long? = packageInfo?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            it.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            it.versionCode.toLong()
        }
    }

    override val isDebuggable: Boolean =
        (appContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    private val staticProperties: Map<String, Any?> by lazy {
        val properties = mutableMapOf<String, Any?>(
            "\$lib" to libName,
            "\$lib_version" to libVersion,
            "\$app_namespace" to appContext.packageName,
            "\$device_manufacturer" to Build.MANUFACTURER,
            "\$device_model" to Build.MODEL,
            "\$device_name" to Build.DEVICE,
            "\$is_emulator" to isEmulator,
            // Mobil SDK'lar `$os_name`, web SDK'sı `$os` yazıyor; PostHog'daki iki filtre de çalışsın diye ikisi de gönderiliyor.
            "\$os" to "Android",
            "\$os_name" to "Android",
            "\$os_version" to Build.VERSION.RELEASE
        )
        try {
            properties["\$app_name"] = appContext.applicationInfo.loadLabel(appContext.packageManager).toString()
        } catch (e: Exception) {
            // Etiket okunamazsa alan gönderilmez.
        }
        appVersion?.let { properties["\$app_version"] = it }
        appBuild?.let { properties["\$app_build"] = it }
        deviceType()?.let { properties["\$device_type"] = it }
        properties
    }

    override fun staticProperties(): Map<String, Any?> = staticProperties

    override fun dynamicProperties(): Map<String, Any?> {
        val properties = mutableMapOf<String, Any?>(
            "\$locale" to localeIdentifier(),
            "\$timezone" to TimeZone.getDefault().id
        )
        // Döndürme / pencere değişiminde değiştiği için her event'te okunuyor (posthog-android gibi piksel).
        val metrics = appContext.resources.displayMetrics
        properties["\$screen_width"] = metrics.widthPixels
        properties["\$screen_height"] = metrics.heightPixels
        properties["\$screen_density"] = metrics.density
        networkStatus()?.let { (wifi, cellular) ->
            properties["\$network_wifi"] = wifi
            properties["\$network_cellular"] = cellular
        }
        return properties
    }

    /** "tr_TR" biçiminde. */
    private fun localeIdentifier(): String {
        val locale = Locale.getDefault()
        return if (locale.country.isNullOrEmpty()) locale.language else "${locale.language}_${locale.country}"
    }

    private fun deviceType(): String? = try {
        val uiModeManager = appContext.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
        when {
            uiModeManager?.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION -> "TV"
            appContext.resources.configuration.smallestScreenWidthDp >= 600 -> "Tablet"
            else -> "Mobile"
        }
    } catch (e: Exception) {
        null
    }

    /** ACCESS_NETWORK_STATE izni yoksa veya okunamazsa null. */
    private fun networkStatus(): Pair<Boolean, Boolean>? = try {
        val manager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (manager == null) {
            null
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
            Pair(
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true,
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
            )
        } else {
            @Suppress("DEPRECATION")
            val info = manager.activeNetworkInfo
            @Suppress("DEPRECATION")
            Pair(
                info?.isConnected == true && info.type == ConnectivityManager.TYPE_WIFI,
                info?.isConnected == true && info.type == ConnectivityManager.TYPE_MOBILE
            )
        }
    } catch (e: Exception) {
        null
    }

    private val isEmulator: Boolean by lazy {
        Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for x86") ||
            Build.MANUFACTURER.contains("Genymotion") ||
            (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
            Build.PRODUCT == "google_sdk" ||
            Build.PRODUCT.contains("sdk_gphone") ||
            Build.HARDWARE.contains("goldfish") ||
            Build.HARDWARE.contains("ranchu")
    }
}
