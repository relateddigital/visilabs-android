package com.visilabs.shakeToWin;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.squareup.picasso.Picasso;
import com.visilabs.android.R;
import com.visilabs.android.databinding.ActivityShakeToWinStep1Binding;
import com.visilabs.android.databinding.ActivityShakeToWinStep2Binding;
import com.visilabs.android.databinding.ActivityShakeToWinStep3Binding;
import com.visilabs.util.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

public class ShakeToWinActivity extends Activity implements SensorEventListener {

    private static final String LOG_TAG = "ShakeToWinActivity";

    private ActivityShakeToWinStep1Binding bindingStep1;
    private ActivityShakeToWinStep2Binding bindingStep2;
    private ActivityShakeToWinStep3Binding bindingStep3;
    private SensorManager mSensorManager;
    private float mAccelerometer;
    private float mAccelerometerCurrent;
    private float mAccelerometerLast;
    private Timer mTimerWithoutShaking;
    private Timer mTimerAfterShaking;
    private boolean isShaken = false;
    private boolean isStep3 = false;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingStep1 = ActivityShakeToWinStep1Binding.inflate(getLayoutInflater());
        bindingStep2 = ActivityShakeToWinStep2Binding.inflate(getLayoutInflater());
        bindingStep3 = ActivityShakeToWinStep3Binding.inflate(getLayoutInflater());
        View view = bindingStep1.getRoot();
        setContentView(view);

        cacheResources();

        //mShakeToWinMessage = getShakeToWinMessage();

        setupStep1View();
    }

    @Override
    protected void onDestroy() {
        if (mTimerWithoutShaking != null) {
            mTimerWithoutShaking.cancel();
        }
        if (mTimerAfterShaking != null) {
            mTimerAfterShaking.cancel();
        }
        if(mSensorManager != null) {
            mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        }
        releasePlayer();
        super.onDestroy();
    }

    private void setupStep1View() {
        //TODO : replace this dummy data with the real one later
        //TODO : check and set the visibilities. Only the button is mandatory
        setupCloseButtonStep1();
        bindingStep1.container.setBackgroundColor(Color.parseColor("#ff99de"));
        Picasso.get().load("https://imgvisilabsnet.azureedge.net/in-app-message/uploaded_images/163_1100_490_20210319175823217.jpg")
                .into(bindingStep1.imageView);
        bindingStep1.titleView.setText("Title".replace("\\n", "\n"));
        bindingStep1.titleView.setTextColor(Color.parseColor("#92008c"));
        bindingStep1.titleView.setTextSize(32);
        bindingStep1.bodyTextView.setText("Text".replace("\\n", "\n"));
        bindingStep1.bodyTextView.setTextColor(Color.parseColor("#4060ff"));
        bindingStep1.bodyTextView.setTextSize(24);
        bindingStep1.buttonView.setText("Button");
        bindingStep1.buttonView.setBackgroundColor(Color.parseColor("#79e7ff"));
        bindingStep1.buttonView.setTextColor(Color.parseColor("#000000"));
        bindingStep1.buttonView.setTextSize(24);

        bindingStep1.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(bindingStep2.getRoot());
                setupStep2View();
            }
        });
    }

    private void setupStep2View() {
        startPlayer();
        initAccelerometer();
    }

    private void setupCloseButtonStep1() {
        bindingStep1.closeButton.setBackgroundResource(getCloseIconStep1());
        bindingStep1.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int getCloseIconStep1() {

        return R.drawable.ic_close_black_24dp;
        //TODO when real data comes:
       /* switch (mInAppMessage.getActionData().getCloseButtonColor()) {

            case "white":
                return R.drawable.ic_close_white_24dp;

            case "black":
                return R.drawable.ic_close_black_24dp;
        }
        return R.drawable.ic_close_black_24dp;*/
    }

    private void initAccelerometer() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccelerometer = 10f;
        mAccelerometerCurrent = SensorManager.GRAVITY_EARTH;
        mAccelerometerLast = SensorManager.GRAVITY_EARTH;
        mTimerWithoutShaking = new Timer("ShakeToWinTimerWithoutShaking", false);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!isShaken) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setupStep3View();
                        }
                    });
                }
            }
        };
        mTimerWithoutShaking.schedule(task, 5000);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!isStep3) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelerometerLast = mAccelerometerCurrent;
            mAccelerometerCurrent = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = mAccelerometerCurrent - mAccelerometerLast;
            mAccelerometer = mAccelerometer * 0.9f + delta;
            if (mAccelerometer > 12) {
                isShaken = true;
                mTimerAfterShaking = new Timer("ShakeToWinTimerAfterShaking", false);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupStep3View();
                            }
                        });
                    }
                };
                mTimerAfterShaking.schedule(task, 0); //TODO: real data here
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void setupStep3View() {
        releasePlayer();
        if (mTimerWithoutShaking != null) {
            mTimerWithoutShaking.cancel();
        }
        if (mTimerAfterShaking != null) {
            mTimerAfterShaking.cancel();
        }
        isStep3 = true;
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        setContentView(bindingStep3.getRoot());

        //TODO : replace this dummy data with the real one later
        //TODO : check and set the visibilities.
        setupCloseButtonStep3();
        bindingStep3.container.setBackgroundColor(Color.parseColor("#ff99de"));
        Picasso.get().load("https://imgvisilabsnet.azureedge.net/in-app-message/uploaded_images/163_1100_490_20210319175823217.jpg")
                .into(bindingStep3.imageView);
        bindingStep3.titleView.setText("Title".replace("\\n", "\n"));
        bindingStep3.titleView.setTextColor(Color.parseColor("#92008c"));
        bindingStep3.titleView.setTextSize(32);
        bindingStep3.bodyTextView.setText("Text".replace("\\n", "\n"));
        bindingStep3.bodyTextView.setTextColor(Color.parseColor("#4060ff"));
        bindingStep3.bodyTextView.setTextSize(24);
        bindingStep3.couponView.setBackgroundColor(Color.parseColor("#00ffab"));
        bindingStep3.couponCodeView.setText("SDFJSDKFMSASDAKASD");
        bindingStep3.couponCodeView.setTextColor(Color.parseColor("#400080"));
        bindingStep3.couponCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.coupon_code), "SDFJSDKFMSASDAKASD"); //TODO : real promo code here
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_LONG).show();
            }
        });

        bindingStep3.buttonView.setText("Button");
        bindingStep3.buttonView.setBackgroundColor(Color.parseColor("#79e7ff"));
        bindingStep3.buttonView.setTextColor(Color.parseColor("#000000"));
        bindingStep3.buttonView.setTextSize(24);

        bindingStep3.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, StringUtils.
                            getURIfromUrlString("https://www.relateddigital.com")); // TODO : real data here
                    startActivity(viewIntent);

                } catch (Exception e) {
                    Log.i(LOG_TAG, "Error : Could not direct to the URI given");
                }
            }
        });

    }

    private void setupCloseButtonStep3() {
        bindingStep3.closeButton.setBackgroundResource(getCloseIconStep3());
        bindingStep3.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int getCloseIconStep3() {

        return R.drawable.ic_close_black_24dp;
        //TODO when real data comes:
       /* switch (mInAppMessage.getActionData().getCloseButtonColor()) {

            case "white":
                return R.drawable.ic_close_white_24dp;

            case "black":
                return R.drawable.ic_close_black_24dp;
        }
        return R.drawable.ic_close_black_24dp;*/
    }

    private void startPlayer() {
        player.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void cacheResources() {
        //TODO : cache video in step 2 and picture in step 3 here
        Picasso.get().load("https://imgvisilabsnet.azureedge.net/in-app-message/uploaded_images/163_1100_490_20210319175823217.jpg")
                .fetch();
        initializePlayer();
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this).build();
        bindingStep2.videoView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"); //TODO : real url here
        player.setMediaItem(mediaItem);
        player.prepare();
    }
}
