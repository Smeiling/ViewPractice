package com.demo.songmeiling.view.verticalviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.List;

/**
 * Created by songmeiling on 2017/7/19.
 */

public class VerticalViewPager extends ViewGroup {

    public static final String TAG = VerticalViewPager.class.getSimpleName();

    private Scroller scroller;
    //跟踪触摸速度
    private VelocityTracker velocityTracker;
    private int curScreen;
    private int defaultScreen = 0;
    //无事件状态
    private static final int TOUCH_STATE_REST = 0;
    //处于拖动的状态
    private static final int TOUCH_STATE_SCROLLING = 1;
    //可以切换页面的最小滑动速度
    private static final int SNAP_VELOCITY = 500;
    //当前所处的状态
    private int touchState = TOUCH_STATE_REST;

    private float lastMotionY;

    private OnVerticalPageChangeListener verticalPageChangeListener;


    public VerticalViewPager(Context context) {
        super(context);
        init(context);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public VerticalViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        scroller = new Scroller(context);
        curScreen = defaultScreen;
    }


    public interface OnVerticalPageChangeListener {
        void onVerticalPageSelected(int postion);
    }

    /**
     * 页面滑动事件监听
     *
     * @param onVerticalPageChangeListener
     */
    public void setOnVerticalPageChangeListener(OnVerticalPageChangeListener onVerticalPageChangeListener) {
        verticalPageChangeListener = onVerticalPageChangeListener;
    }

    /**
     * 设置页面视图
     *
     * @param viewList
     */
    public void setViewList(List<View> viewList) {
        //设置时先清空原有的view
        if (getChildCount() > 0) {
            removeAllViews();
        }
        curScreen = defaultScreen;
        for (int i = 0; i < viewList.size(); i++) {
            addView(viewList.get(i));
        }
        scrollTo(0, 0);
    }

    /**
     * 为子View进行布局
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //First child view's topY = 0
        int childHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                int childWidth = childView.getMeasuredWidth();
                //leftX,topY,rightX,bottomY
                childView.layout(0, childHeight, childWidth, childView.getMeasuredHeight() + childHeight);
                //Next child view's topY start from the bottom of precede child
                childHeight += childView.getMeasuredHeight();
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
        // 其中包含setMeasuredDimension方法，它是一个很关键的函数，它对View的成员变量mMeasuredWidth和mMeasuredHeight变量赋值
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);

        }
    }

    /**
     * 根据目前的位置滚动到下一个视图的位置
     */
    public void snapToDestination() {
        // 这个高度是View可见部分的高度
        int screenHeight = getHeight();
        // 根据View的高度以及滑动的值来判断是哪个View
        int destScreen = (getScrollY() + screenHeight / 2) / screenHeight;
        snapToScreen(destScreen);
    }

    public void snapToScreen(int whichScreen) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        if (getScrollY() != (whichScreen * getHeight())) {
            final int delta = whichScreen * getHeight() - getScrollY();
            //开始滑动
            scroller.startScroll(0, getScrollY(), 0, delta, Math.abs(delta) / 4);
            curScreen = whichScreen;
            //重新布局
            invalidate();
            if (verticalPageChangeListener != null) {
                verticalPageChangeListener.onVerticalPageSelected(whichScreen);
            }
        }
    }

    /*public void setToScreen(int whichScreen) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        curScreen = whichScreen;
        scrollTo(0, whichScreen * getHeight());
        if (verticalPageChangeListener != null) {
            verticalPageChangeListener.onVerticalPageSelected(whichScreen);
        }
    }*/

    /**
     * 获取当前页面
     *
     * @return curScreen
     */
    public int getCurScreen() {
        return curScreen;
    }

    /**
     * 获取当前视图
     *
     * @return
     */
    public View getCurView() {
        return getChildAt(curScreen);
    }

    /**
     * 获取指定位置视图
     *
     * @param position
     * @return
     */
    public View getView(int position) {
        return getChildAt(position);
    }

    /**
     * 在父容器重绘自己的孩子时，它会调用孩子的computScroll方法
     */
    @Override
    public void computeScroll() {
        //super.computeScroll();
        //判断滑动是否结束，结束返回true
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            //需要调用此方法才能看到滚动效果
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            // 使用obtain方法得到VelocityTracker的一个对象
            velocityTracker = VelocityTracker.obtain();
        }
        // 将当前的触摸事件传递给VelocityTracker对象
        velocityTracker.addMovement(event);

        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果上一次滚动未结束，直接中断
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                //起始坐标
                lastMotionY = y;
                Log.w(TAG, "onTouchEvent = ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                //滑动差值
                int deltaY = (int) (lastMotionY - y);
                lastMotionY = y;

                if ((curScreen == getChildCount() - 1 && deltaY > 0) || (curScreen == 0 && deltaY < 0)) {
                    Log.w(TAG, "向上滑动最后一页 or 向下滑动第一页");
                } else {
                    scrollBy(0, deltaY);
                }
                Log.w(TAG, "onTouchEvent = ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = this.velocityTracker;
                //计算当前的速度
                velocityTracker.computeCurrentVelocity(1000);
                //垂直方向滑动速度
                int velocityY = (int) velocityTracker.getYVelocity();

                if (velocityY > SNAP_VELOCITY && curScreen > 0) {
                    //向下翻页
                    snapToScreen(curScreen - 1);
                } else if (velocityY < -SNAP_VELOCITY && curScreen < getChildCount() - 1) {
                    //向上翻页
                    snapToScreen(curScreen + 1);
                } else {
                    snapToDestination();
                }

                if (this.velocityTracker != null) {
                    this.velocityTracker.recycle();
                    this.velocityTracker = null;
                }
                touchState = TOUCH_STATE_REST;
                break;

            case MotionEvent.ACTION_CANCEL:
                touchState = TOUCH_STATE_REST;
                break;
            default:
                break;
        }
        //当前View处理touch事件，不传递到父View
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                touchState = scroller.isFinished() ? TOUCH_STATE_REST
                        : TOUCH_STATE_SCROLLING;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                touchState = TOUCH_STATE_REST;
                break;
        }
        //滑动状态下拦截，非滑动状态下继续传递至子View
        return touchState != TOUCH_STATE_REST;
    }
}
