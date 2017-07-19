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
import android.view.animation.AnimationUtils;

import com.demo.songmeiling.view.R;
import com.demo.songmeiling.view.searchview.SearchViewActivity;

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
    //处于拖动的状态
    private static final int TOUCH_STATE_SCROLLING = 1;
    //可以切换页面的最小滑动速度
    private static final int SNAP_VELOCITY = 500;
    //当前所处的状态
    private int touchState = TOUCH_STATE_REST;

    private float lastMotionY;
    private int lastDistance = 0;

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
                childView.layout(0, 0, childWidth, childView.getMeasuredHeight());
            } else if (i == (curScreen + 1) && touchState == TOUCH_STATE_SCROLLING) {
                childView.layout(0, childView.getMeasuredHeight() - lastDistance, childWidth, childView.getMeasuredHeight() * 2 - lastDistance);
            } else {
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
        Log.d(TAG, "setViewList");

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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG, "touch DOWN");
                lastMotionY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "touch MOVE");
                int distance = (int) (lastMotionY - event.getY());
                if (Math.abs(distance) > validDistance) {
                    Log.w(TAG, "valid slide");
                    if (distance > 0) {
                        Log.w(TAG, "slide up");
                        touchState = TOUCH_STATE_SCROLLING;
                        getChildAt(curScreen).setPadding(Math.abs(distance / 8), Math.abs(distance / 8), Math.abs(distance / 8), 0);
                        lastDistance = Math.abs(distance);
                    } else {
                        Log.w(TAG, "slide down");
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touchState == TOUCH_STATE_SCROLLING) {
                    doAnimate();
                    performAnimate(true);

                    curScreen++;
                    lastDistance = 0;
                    touchState = TOUCH_STATE_REST;
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
        getChildAt(curScreen).startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.zoom_in));
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
}
