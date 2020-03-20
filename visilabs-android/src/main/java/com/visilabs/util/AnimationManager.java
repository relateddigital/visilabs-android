package com.visilabs.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.visilabs.android.R;

public class AnimationManager {

    static GradientDrawable gradientDrawable;

    public static Animation getScaleAnimation() {
        ScaleAnimation scale = new ScaleAnimation(
                .95f, 1.0f, .95f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        scale.setDuration(200);
        return scale;
    }


    public static TranslateAnimation getMiniTranslateAnimation(Context context) {

        float heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, context.getResources().getDisplayMetrics());
        TranslateAnimation translate = new TranslateAnimation(0, 0, heightPx, 0);
        translate.setInterpolator(new DecelerateInterpolator());
        translate.setDuration(200);

        return translate;
    }

    public static TranslateAnimation getTranslateAnimation() {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );
        translate.setInterpolator(new DecelerateInterpolator());
        translate.setDuration(200);
        return translate;
    }

    public static Animation getFadeInAnimation(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.anim_fade_in);
    }


    public static GradientDrawable getGradient(LinearLayout closeButtonWrapper, Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) closeButtonWrapper.getLayoutParams();
            params.setMargins(0, 0, 0, (int) (size.y * 0.06f)); // make bottom margin 6% of screen height
            closeButtonWrapper.setLayoutParams(params);
        }

        final GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, // Ignored in radial gradients
                new int[]{0xE560607C, 0xE548485D, 0xE518181F, 0xE518181F}
        );
        gd.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gd.setGradientCenter(0.25f, 0.5f);
            gd.setGradientRadius(Math.min(size.x, size.y) * 0.8f);
        } else {
            gd.setGradientCenter(0.5f, 0.33f);
            gd.setGradientRadius(Math.min(size.x, size.y) * 0.7f);
        }


        return gd;
    }

    public static GradientDrawable getGradientDrawable() {
        return gradientDrawable;
    }

    public static void setNoDropShadowBackgroundToView(View inAppImageView, Bitmap inAppImage) {

        int h = inAppImage.getHeight() / 100;
        int w = inAppImage.getWidth() / 100;
        final Bitmap scaledImage = Bitmap.createScaledBitmap(inAppImage, w, h, false);
        int averageColor;
        int averageAlpha;
        outerloop:
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                averageColor = scaledImage.getPixel(x, y);
                averageAlpha = Color.alpha(averageColor);
                if (averageAlpha < 0xFF) {
                    inAppImageView.setBackgroundResource(R.drawable.bg_square_nodropshadow);
                    break outerloop;
                }
            }

        }
    }

    public static void setBackgroundGradient(View v, Drawable d) {
        if (Build.VERSION.SDK_INT < 16) {
            v.setBackgroundDrawable(d);
        } else {
            v.setBackground(d);
        }
    }

    public static Animation getMiniScaleAnimation(Context context) {

        float heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, context.getResources().getDisplayMetrics());

        ScaleAnimation scale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, heightPx / 2, heightPx / 2);
        scale.setInterpolator(new SineBounceInterpolator());
        scale.setDuration(400);
        scale.setStartOffset(200);

        return scale;
    }

    private static class SineBounceInterpolator implements Interpolator {
        public SineBounceInterpolator() {
        }

        public float getInterpolation(float t) {
            return (float) -(Math.pow(Math.E, -8 * t) * Math.cos(12 * t)) + 1;
        }
    }
}
