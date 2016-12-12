package com.example.testing.customdemo.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.testing.customdemo.R;
import com.example.testing.customdemo.util.Point;

/**
 * 绕矩形转动的线段
 * Created by wangying on 2016/12/12.
 */

public class RotateRectView extends View {

    private static final String LOG_TAG = "RotateRectView";

    private Paint mPaint;
    private Path mDrawPath;
    private ValueAnimator mRotateValueAnimator;

    private Point mHeadPoint;
    private Point mMidPoint;
    private Point mTailPoint;
    private Point mBasePoint;

    public RotateRectView(Context context) {
        this(context, null);
    }

    public RotateRectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateRectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initBasePoint(context, attrs);
    }

    private void init() {

        initNecessaryPoints();
        initPaint();
        initPath();
        initRotateAnimator();

    }

    private void initNecessaryPoints() {

        mHeadPoint = new Point();
        mMidPoint = new Point();
        mTailPoint = new Point();
        mBasePoint = new Point();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(30.0f);
        mPaint.setColor(Color.WHITE);
    }

    private void initPath() {
        mDrawPath = new Path();
        mDrawPath.reset();
    }

    private void initRotateAnimator() {
        if (mRotateValueAnimator == null) {
            mRotateValueAnimator = ValueAnimator.ofFloat(0.0f, 4.0f);
            mRotateValueAnimator.setDuration(4000);
            mRotateValueAnimator.setRepeatCount(-1);
            mRotateValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Log.i(LOG_TAG, "Update...");
                    float t = (float) valueAnimator.getAnimatedValue();
                    updateHeaderPoint(t);
                    updateMidPoint();
                    updateTailPoint(t - 1.0f);
                    updateDrawPath();
                    invalidate();

                }
            });
        }
    }

    private void initBasePoint(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RotateRectView);
        mBasePoint.mX = typedArray.getFloat(R.styleable.RotateRectView_base_location_x, 0.0f);
        mBasePoint.mY = typedArray.getFloat(R.styleable.RotateRectView_base_location_y, 0.0f);
        typedArray.recycle();
    }

    private void updateHeaderPoint(float t) {
        mHeadPoint.mX = calHeadPointX(t);
        mHeadPoint.mY = calHeadPointY(t);
    }

    private void updateMidPoint() {
        if (mHeadPoint.mY <= 0) {
            mMidPoint.mX = 0;
            mMidPoint.mY = 0;
        } else if (mHeadPoint.mX <= 0) {
            mMidPoint.mX = 0;
            mMidPoint.mY = 400;
        } else if (mHeadPoint.mY >= 400) {
            mMidPoint.mX = 400;
            mMidPoint.mY = 400;
        } else if (mHeadPoint.mX >= 400) {
            mMidPoint.mX = 400;
            mMidPoint.mY = 0;
        }
    }

    private void updateTailPoint(float t) {
        mTailPoint.mX = calTailPointX(t);
        mTailPoint.mY = calTailPointY(t);
    }

    private void updateDrawPath() {
        mDrawPath.reset();
        mDrawPath.moveTo(mHeadPoint.mX + mBasePoint.mX, mHeadPoint.mY + mBasePoint.mY);
        mDrawPath.lineTo(mMidPoint.mX + mBasePoint.mX, mMidPoint.mY + mBasePoint.mY);
        mDrawPath.lineTo(mTailPoint.mX + mBasePoint.mX, mTailPoint.mY + mBasePoint.mY);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        mRotateValueAnimator.start();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            mRotateValueAnimator.start();
        } else {
            mRotateValueAnimator.cancel();
            mRotateValueAnimator.end();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mDrawPath, mPaint);
    }


    private float calHeadPointX(float t) {
        float x = 0;
        if (t >= 0 && t <= 1) {
            x = 400 * t;
        } else if (t > 1 && t <= 2) {
            x = 400;
        } else if (t > 2 && t <= 3) {
            x = 400 * (3 - t);
        } else if (t > 3 && t <= 4) {
            x = 0;
        }
        return x;
    }

    private float calHeadPointY(float t) {
        float y = 0;
        if (t >= 0 && t <= 1) {
            y = 0;
        } else if (t > 1 && t <= 2) {
            y = (t - 1) * 400;
        } else if (t > 2 && t <= 3) {
            y = 400;
        } else if (t > 3 && t <= 4) {
            y = 400 * (4 - t);
        }
        return y;
    }

    private float calTailPointX(float t) {
        float x = 0;
        if (t >= -1 && t < 0) {
            x = 0;
        } else if (t > 0 && t <= 1) {
            x = 400 * t;
        } else if (t > 1 && t <= 2) {
            x = 400;
        } else if (t > 2 && t <= 3) {
            x = 400 * (3 - t);
        }
        return x;
    }

    private float calTailPointY(float t) {
        float y = 0;
        if (t >= -1 && t <= 0) {
            y = -400 * t;
        } else if (t > 0 && t <= 1) {
            y = 0;
        } else if (t > 1 && t <= 2) {
            y = 400 * (t - 1);
        } else if (t > 2 && t <= 3) {
            y = 400;
        }
        return y;
    }

}
