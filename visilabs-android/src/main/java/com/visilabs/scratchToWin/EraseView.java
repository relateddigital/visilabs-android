package com.visilabs.scratchToWin;

import com.visilabs.inApp.InAppSafeUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.visilabs.android.R;

public class EraseView extends View {
    private static final int SCALE = 100;
    private static final double PERCENTAGE_THRESHOLD = 0.6;
    private Bitmap mSourceBitmap;
    private final Canvas mSourceCanvas = new Canvas();
    private final Paint mDestPaint = new Paint();
    private final Path mDestPath = new Path();
    private int mColor;
    private boolean isEnabled;
    private boolean isEmailEntered;
    private boolean isConsent1Entered;
    private boolean isConsent2Entered;
    private String invalidEmailMessage;
    private String missingConsentMessage;
    private ContainerScrollView mContainer;
    private ScratchToWinInterface mListener;
    private int mCodeWidth;
    private int mCodeHeight;
    private boolean isCompleted;
    private Context mContext;

    public EraseView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        isEnabled = false;
        isCompleted = false;
        mColor = InAppSafeUtils.parseColor("#000000", android.graphics.Color.BLACK); //Default black

        mDestPaint.setAlpha(0);
        mDestPaint.setAntiAlias(true);
        mDestPaint.setStyle(Paint.Style.STROKE);
        mDestPaint.setStrokeJoin(Paint.Join.ROUND);
        mDestPaint.setStrokeCap(Paint.Cap.ROUND);
        mDestPaint.setStrokeWidth(50);
        mDestPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mSourceBitmap == null) {
            super.onDraw(canvas);
            return;
        }
        //Draw path
        mSourceCanvas.drawPath(mDestPath, mDestPaint);

        //Draw bitmap
        canvas.drawBitmap(mSourceBitmap, 0, 0, null);

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled) {
            if (!isCompleted) {
                if (calculatePercentageErased() >= PERCENTAGE_THRESHOLD) {
                    isCompleted = true;
                    if (mListener != null) {
                        mListener.onScratchingComplete();
                    }
                }
            }
            if (mContainer != null) {
                mContainer.setScrollingState(false);
            }

            float xPos = event.getX();
            float yPos = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDestPath.moveTo(xPos, yPos);
                    break;

                case MotionEvent.ACTION_MOVE:
                    mDestPath.lineTo(xPos, yPos);
                    break;

                default:
                    return false;
            }

            invalidate();
            return true;
        } else {
            if(!isEmailEntered) {
                Toast.makeText(mContext, invalidEmailMessage, Toast.LENGTH_SHORT).show();
            } else if(!isConsent1Entered || !isConsent2Entered){
                Toast.makeText(mContext, missingConsentMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.missing_subs_email), Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCodeWidth = w;
        mCodeHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
        if (w <= 0 || h <= 0) {
            return;
        }
        Bitmap oldBitmap = mSourceBitmap;
        mSourceBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mSourceCanvas.setBitmap(mSourceBitmap);
        mSourceCanvas.drawColor(mColor);
        if (oldBitmap != null) {
            oldBitmap.recycle();
        }
    }

    public void setColor(int color) {
        mColor = color;
        if (mSourceBitmap != null) {
            mSourceCanvas.drawColor(mColor);
        }
    }

    public void setEmailStatus(boolean value) {
        isEmailEntered = value;
    }

    public void setConsent1Status(boolean value) {
        isConsent1Entered = value;
    }

    public void setConsent2Status(boolean value) {
        isConsent2Entered = value;
    }

    public void setInvalidEmailMessage (String message) {
        invalidEmailMessage = message;
    }

    public void setMissingConsentMessage (String message) {
        missingConsentMessage = message;
    }

    public void setContainer(ContainerScrollView container) {
        mContainer = container;
    }

    public void setListener(ScratchToWinInterface listener) {
        mListener = listener;
    }

    public void enableScratching() {
        isEnabled = true;
    }

    private double calculatePercentageErased() {
        if (mSourceBitmap == null) {
            return 0;
        }
        final int width = Math.min(mCodeWidth, mSourceBitmap.getWidth());
        final int height = Math.min(mCodeHeight, mSourceBitmap.getHeight());
        if (width <= 0 || height <= 0) {
            return 0;
        }

        // size of sample rectangles
        final int xStep = Math.max(1, width / SCALE);
        final int yStep = Math.max(1, height / SCALE);

        // center of the first rectangle
        final int xInit = xStep / 2;
        final int yInit = yStep / 2;

        // center of the last rectangle
        final int xEnd = Math.min(width - xStep / 2, width - 1);
        final int yEnd = Math.min(height - yStep / 2, height - 1);

        int totalTransparent = 0;

        for (int x = xInit; x <= xEnd; x += xStep) {
            for (int y = yInit; y <= yEnd; y += yStep) {
                if (mSourceBitmap.getPixel(x, y) == Color.TRANSPARENT) {
                    totalTransparent++;
                }
            }
        }
        return ((float) totalTransparent) / ((float) (width * height) / (float) (xStep * yStep));
    }
}
