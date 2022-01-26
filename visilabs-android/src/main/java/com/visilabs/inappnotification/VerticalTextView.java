package com.visilabs.inappnotification;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

public class VerticalTextView extends androidx.appcompat.widget.AppCompatTextView
{
    boolean topDown;
    boolean isRight;
    boolean isCircle;

    public VerticalTextView( Context context,
                             AttributeSet attrs )
    {
        super( context, attrs );
        final int gravity = getGravity();
        if ( Gravity.isVertical( gravity )
                && ( gravity & Gravity.VERTICAL_GRAVITY_MASK )
                == Gravity.BOTTOM )
        {
            setGravity(
                    ( gravity & Gravity.HORIZONTAL_GRAVITY_MASK )
                            | Gravity.TOP );
            topDown = false;
        }
        else
        {
            topDown = true;
        }
    }

    @Override
    protected void onMeasure( int widthMeasureSpec,
                              int heightMeasureSpec )
    {
        super.onMeasure( heightMeasureSpec,
                widthMeasureSpec );
        setMeasuredDimension( getMeasuredHeight(),
                getMeasuredWidth() );
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        TextPaint textPaint = getPaint();
        textPaint.setColor( getCurrentTextColor() );
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if ( topDown )
        {
            canvas.translate( getWidth(), 0 );
            canvas.rotate( 90 );
        }
        else
        {
            canvas.translate( 0, getHeight() );
            canvas.rotate( -90 );
        }

        if(isCircle) {
            if(topDown) {
                if(isRight) {
                    canvas.translate(getCompoundPaddingLeft(),
                            getExtendedPaddingTop() + 28);
                } else {
                    canvas.translate(getCompoundPaddingLeft(),
                            getExtendedPaddingTop() + 80);
                }
            } else {
                if(isRight) {
                    canvas.translate(getCompoundPaddingLeft(),
                            getExtendedPaddingTop() + 80);
                } else {
                    canvas.translate(getCompoundPaddingLeft(),
                            getExtendedPaddingTop() + 28);
                }
            }
        } else {
            if(topDown) {
                canvas.translate( getCompoundPaddingLeft(),
                        getExtendedPaddingTop() + 20);
            } else {
                canvas.translate( getCompoundPaddingLeft(),
                        getExtendedPaddingTop() + 20);
            }
        }

        getLayout().draw( canvas );
        canvas.restore();
    }
}
