package com.visilabs.view;

import android.content.Context;
import android.view.View;

import androidx.media3.common.Player;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;

/**
 * Every media3 reference of {@link InAppVideoView} is isolated here. Touching this class throws
 * {@link NoClassDefFoundError} when media3 is not on the classpath, which lets the caller fall back
 * to a message without video instead of failing to inflate the whole layout.
 */
final class Media3PlayerViewProvider {

    private Media3PlayerViewProvider() {
    }

    static View createPlayerView(Context context) {
        PlayerView playerView = new PlayerView(context);
        playerView.setUseController(false);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        return playerView;
    }

    static void setPlayer(View playerView, Object player) {
        ((PlayerView) playerView).setPlayer((Player) player);
    }
}
