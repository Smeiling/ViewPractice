package com.demo.songmeiling.view.verticalviewslider;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.demo.songmeiling.view.R;

import java.util.List;

/**
 * Created by songmeiling on 2017/7/19.
 */

public class VerticalViewSlider extends ViewGroup {

    public static final String TAG = VerticalViewSlider.class.getSimpleName();

    private int curScreen;
    private int defaultScreen = 0;
    //跟踪触摸速度
    private VelocityTracker velocityTracker;
    //无事件状态
    private static final int TOUCH_STATE_REST = 0;
    //处于向上拖动的状态
    private static final int TOUCH_STATE_SCROLLING_UP = 1;
    //处于向下拖动的状态
    private static final int TOUCH_STATE_SCROLLING_DOWN = 2;
    //可以切换页面的最小滑动速度
    private static final int SNAP_VELOCITY = 500;
    //当前所处的状态
    private int touchState = TOUCH_STATE_REST;

    private float lastMotionY;
    private int lastDistance = 0;

    private int lastPadding = 0;
    private int slideDistance = 0;

    private Context mContext;

    private OnVerticalPageChangeListener verticalPageChangeListener;
    private int validDistance = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    public VerticalViewSlider(Context context) {
        super(context);
        mContext = context;
    }

    public VerticalViewSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public VerticalViewSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.d(TAG, "onLayout");

        //First child view's topY = 0

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            //leftX,topY,rightX,bottomY
            if (i == curScreen) {
                //childView.setVisibility(View.GONE);
                //当前页面显示位置
                childView.layout(0, slideDistance, childWidth, childView.getMeasuredHeight() + slideDistance);
            } else if (i == (curScreen + 1) && touchState == TOUCH_STATE_SCROLLING_UP) {
                //滑动页显示位置
                childView.layout(0, childView.getMeasuredHeight() - lastDistance, childWidth, childView.getMeasuredHeight() * 2 - lastDistance);
            } else {
                //其他页面显示位置
                childView.layout(0, childView.getMeasuredHeight(), childWidth, childView.getMeasuredHeight() * 2);
            }
        }
    }


    /**
     * 重写此方法用来计算高度和宽度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.d(TAG, "onMeasure");

        // 其中包含setMeasuredDimension方法，它是一个很关键的函数，它对View的成员变量mMeasuredWidth和mMeasuredHeight变量赋值
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    public void setViewList(List<View> viewList) {
        //设置时先清空原有的view
        if (getChildCount() > 0) {
            removeAllViews();
        }
        curScreen = defaultScreen;
        for (int i = 0; i < viewList.size(); i++) {
            addView(viewList.get(i));
        }
    }


    public interface OnVerticalPageChangeListener {
        void onVerticalPageSelected(int postion);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (velocityTracker == null) {
            // 使用obtain方法得到VelocityTracker的一个对象
            velocityTracker = VelocityTracker.obtain();
        }
        // 将当前的触摸事件传递给VelocityTracker对象
        velocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG, "touch DOWN");
                lastMotionY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "touch MOVE");
                final VelocityTracker velocityTracker = this.velocityTracker;
                //计算当前的速度
                velocityTracker.computeCurrentVelocity(1000);
                //垂直方向滑动速度
                int velocityY = (int) velocityTracker.getYVelocity();
                Log.w(TAG, "velocity Y = " + velocityY);
                Log.w(TAG, "dura = " + getAnimDuration(velocityY));
                int distance = (int) (lastMotionY - event.getY());
                if (Math.abs(distance) > validDistance) {
                    Log.w(TAG, "valid slide");
                    if (distance > 0) {
                        Log.w(TAG, "slide up");
                        touchState = TOUCH_STATE_SCROLLING_UP;
                        getChildAt(curScreen).setPadding(Math.abs(distance / 9), Math.abs(distance / 9), Math.abs(distance / 9), 0);
                        lastPadding = Math.abs(distance / 9);
                        lastDistance = Math.abs(distance);
                    } else {
                        Log.w(TAG, "slide down");
                    }
                } else {
                    touchState = TOUCH_STATE_REST;
                }

                if (this.velocityTracker != null) {
                    this.velocityTracker.recycle();
                    this.velocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touchState == TOUCH_STATE_SCROLLING_UP) {
                    performAnimate(true);
                    doAnimate();
                } else {

                }
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG, "intercept DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "intercept MOVE");
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    private void doAnimate() {
        Log.w(TAG, "doAnimate");
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                curScreen++;
                lastDistance = 0;
                touchState = TOUCH_STATE_REST;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getChildAt(curScreen).startAnimation(anim);
//        getChildAt(curScreen + 1).setVisibility(View.VISIBLE);
//        getChildAt(curScreen + 1).startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.show_from_bottom));
//        getChildAt(curScreen).setVisibility(View.GONE);
        //this.curScreen++;
        //verticalPageChangeListener.onVerticalPageSelected(curScreen);
    }


    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getTop() {
            return mTarget.getTop();
        }

        public void setTop(int top) {
            mTarget.setTop(top);
        }

        public int getBottom() {
            return mTarget.getBottom();
        }

        public void setBottom(int bottom) {
            mTarget.setBottom(bottom);
        }

        public int getPadding() {
            return mTarget.getPaddingTop();
        }

        public void setPadding(int padding) {
            mTarget.setPadding(padding, padding, padding, 0);
        }

    }

    /**
     * 控件属性渐变动画
     *
     * @param expand
     */
    private void performAnimate(boolean expand) {
        Log.w(TAG, "performAnimate");
        ViewWrapper wrapper = new ViewWrapper(getChildAt(curScreen + 1));
        ViewWrapper curView = new ViewWrapper(getChildAt(curScreen));
        //Log.w(TAG, "screenWidth = " + screenWidth);
        if (expand) {
            ObjectAnimator.ofInt(wrapper, "top", 0).setDuration(500).start();
            ObjectAnimator.ofInt(wrapper, "bottom", getChildAt(curScreen + 1).getMeasuredHeight()).setDuration(500).start();
            //ObjectAnimator.ofInt(curView, "padding", getChildAt(curScreen).getMeasuredWidth() / 2).setDuration(500).start();
        } else {
            ObjectAnimator.ofInt(wrapper, "top", getChildAt(curScreen + 1).getMeasuredHeight()).setDuration(500).start();
            ObjectAnimator.ofInt(wrapper, "bottom", getChildAt(curScreen + 1).getMeasuredHeight() * 2).setDuration(500).start();
            //ObjectAnimator.ofInt(curView, "padding", 0).setDuration(500).start();
        }
    }

    private int getAnimDuration(int velocity) {
        if (velocity == 0) {
            return 500;
        }
        int duration = ((getChildAt(curScreen + 1).getMeasuredHeight() - lastDistance) * 1000) / Math.abs(velocity);
        Log.w(TAG, "duration = " + duration);
        if (duration == 0) {
            return 100;
        }
        return duration;
    }
}
