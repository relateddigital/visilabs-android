package com.visilabs.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.VideoView;

import androidx.annotation.Nullable;

/**
 * Plays in app videos with an androidx.media3 PlayerView when media3 is available and falls back to
 * the platform media player when it is not. The video view is created lazily instead of being
 * declared in the in app message layouts, so that the layouts stay inflatable on apps that do not
 * have media3 on their runtime classpath.
 */
public class InAppVideoView extends FrameLayout {

    private static final String LOG_TAG = "InAppVideoView";

    /**
     * VideoView reports a zero height until the video is prepared, and it is only prepared after it
     * gets a surface with a non zero size. This ratio bounds the height of the first measure pass to
     * break that cycle, afterwards VideoView measures itself with the real ratio of the video.
     */
    private static final float INITIAL_ASPECT_RATIO = 9f / 16f;

    private View playerView;
    private boolean playerViewUnavailable = false;
    private VideoView fallbackVideoView;

    public InAppVideoView(Context context) {
        super(context);
    }

    public InAppVideoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InAppVideoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * The player is passed as an Object so that this class can be loaded without media3 being
     * present. Does nothing when media3 is missing, use {@link #playWithPlatformPlayer(String)} for
     * that case.
     */
    public void setPlayer(@Nullable Object player) {
        View view = getOrCreatePlayerView();
        if (view == null) {
            return;
        }
        try {
            Media3PlayerViewProvider.setPlayer(view, player);
        } catch (Throwable throwable) {
            Log.e(LOG_TAG, "Could not attach the player to the video view.", throwable);
        }
    }

    /**
     * Plays the given url with the platform media player, which has no dependency on media3. Videos
     * keep working this way on apps that do not ship media3, only the streaming formats that media3
     * adds on top of the platform ones are unsupported.
     */
    public void playWithPlatformPlayer(@Nullable String url) {
        if (url == null || url.isEmpty()) {
            setVisibility(GONE);
            return;
        }

        releaseFallbackVideoView();
        removeAllViews();
        playerView = null;

        final VideoView videoView = new VideoView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        videoView.setOnPreparedListener(mediaPlayer -> videoView.start());
        videoView.setOnErrorListener((mediaPlayer, what, extra) -> {
            Log.e(LOG_TAG, "Could not play the in app video, what: " + what + ", extra: " + extra);
            setVisibility(GONE);
            return true;
        });
        addView(videoView, layoutParams);
        fallbackVideoView = videoView;
        videoView.setVideoURI(Uri.parse(url));
    }

    public boolean isVideoSupported() {
        return getOrCreatePlayerView() != null;
    }

    @Nullable
    private View getOrCreatePlayerView() {
        if (playerView == null && !playerViewUnavailable) {
            try {
                playerView = Media3PlayerViewProvider.createPlayerView(getContext());
                addView(playerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            } catch (Throwable throwable) {
                playerView = null;
                playerViewUnavailable = true;
                Log.w(LOG_TAG, "androidx.media3 could not be found, falling back to the platform " +
                        "media player for in app videos.", throwable);
            }
        }
        return playerView;
    }

    private void releaseFallbackVideoView() {
        if (fallbackVideoView != null) {
            fallbackVideoView.stopPlayback();
            fallbackVideoView = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (fallbackVideoView != null
                && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    Math.round(width * INITIAL_ASPECT_RATIO), MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDetachedFromWindow() {
        releaseFallbackVideoView();
        super.onDetachedFromWindow();
    }
}
