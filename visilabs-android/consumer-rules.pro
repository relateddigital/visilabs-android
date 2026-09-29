# Video support is optional: com.visilabs.view.InAppVideoView falls back to a message without video
# when media3 is not part of the application, so R8 must not fail on the missing references.
-dontwarn androidx.media3.**

-keep class androidx.media3.ui.PlayerView { *; }
-keep class com.visilabs.view.InAppVideoView { *; }
