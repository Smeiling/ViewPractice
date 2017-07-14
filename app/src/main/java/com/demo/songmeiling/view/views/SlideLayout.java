package com.demo.songmeiling.view.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import com.demo.songmeiling.view.utils.LogUtil;

/**
 * Created by songmeiling on 2017/7/7.
 */
public class SlideLayout extends LinearLayout {

    public static final String TAG = SlideLayout.class.getSimpleName();

    public static final int SLIDE_INIT = -1;
    public static final int SLIDE_UP = 0;
    public static final int SLIDE_DOWN = 1;
    public static final int SLIDE_LEFT = 2;
    public static final int SLIDE_RIGHT = 3;

    private int slideMode = SLIDE_INIT;

    private float downX;
    private float downY;
    private float upX;
    private float upY;
    private int validDistance = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private OnSlideListener onSlideListener;

    public SlideLayout(Context context) {
        super(context);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void addView(ViewGroup.LayoutParams layoutParams, View view) {
        this.addView(view, layoutParams);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                LogUtil.w(TAG, "ACTION_DOWN " + ev.getX() + ", " + ev.getY());

                slideMode = SLIDE_INIT;
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.w(TAG, "ACTION_MOVE");
                onSlideListener.onMoving((int) (downX - ev.getX()));
                break;
            case MotionEvent.ACTION_UP:

                LogUtil.w(TAG, "ACTION_UP " + ev.getX() + ", " + ev.getY());

                upX = ev.getX();
                upY = ev.getY();
                checkDirection(downX, downY, upX, upY);
                if (slideMode >= 0 && onSlideListener != null) {
                    LogUtil.w(TAG, "SLIDE MODE");
                    onSlideListener.onSlide(slideMode);
                    return true;
                }
                break;
        }
        return false;
    }

    public void checkDirection(float dX, float dY, float uX, float uY) {

        float horizontal = Math.abs(dX - uX);
        float vertical = Math.abs(dY - uY);

        if (horizontal > validDistance && vertical > validDistance) {
            if (horizontal - vertical > 0) {
                slideMode = dX - uX > 0 ? SLIDE_LEFT : SLIDE_RIGHT;
            } else {
                slideMode = dY - uY > 0 ? SLIDE_UP : SLIDE_DOWN;
            }
        } else if (horizontal > validDistance) {
            slideMode = dX - uX > 0 ? SLIDE_LEFT : SLIDE_RIGHT;
        } else if (vertical > validDistance) {
            slideMode = dY - uY > 0 ? SLIDE_UP : SLIDE_DOWN;
        } else {
            slideMode = SLIDE_INIT;
        }

    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }

    public interface OnSlideListener {
        void onSlide(int slideMode);

        void onMoving(int distance);
    }


}
