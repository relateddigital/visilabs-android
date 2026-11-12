# Video support is optional: com.visilabs.view.InAppVideoView falls back to a message without video
# when media3 is not part of the application, so R8 must not fail on the missing references.
-dontwarn androidx.media3.**

-keep class androidx.media3.ui.PlayerView { *; }
-keep class com.visilabs.view.InAppVideoView { *; }

# Gson: SDK models are (de)serialized by field name, generic types are read from signatures.
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*

-keep class com.visilabs.model.** { *; }
-keep class com.visilabs.favs.** { *; }
-keep class com.visilabs.gps.model.** { *; }

-keepclassmembers class com.visilabs.** {
    @com.google.gson.annotations.SerializedName <fields>;
}
-if class com.visilabs.** { @com.google.gson.annotations.SerializedName <fields>; }
-keep class com.visilabs.<1> { <init>(); }

-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Retrofit 2.9 does not ship these rules for R8 full mode.
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep interface com.visilabs.api.VisilabsApiMethods { *; }
