package com.demo.songmeiling.view.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by songmeiling on 2017/7/7.
 */
public class ZoomLayout extends LinearLayout {

    public static final String TAG = ZoomLayout.class.getSimpleName();

    /**
     * 初始化状态常量
     */
    public static final int STATUS_INIT = 1;

    /**
     * 图片放大状态常量
     */
    public static final int STATUS_ZOOM_OUT = 2;

    /**
     * 图片缩小状态常量
     */
    public static final int STATUS_ZOOM_IN = 3;

    /**
     * 记录上次两指之间的距离
     */
    private double lastFingerDis;

    /**
     * 记录当前操作的状态，可选值为STATUS_INIT、STATUS_ZOOM_OUT、STATUS_ZOOM_IN和STATUS_MOVE
     */
    private int currentStatus = STATUS_INIT;

    /**
     * 记录上次手指移动时的横坐标
     */
    private float lastXMove = -1;

    /**
     * 记录上次手指移动时的纵坐标
     */
    private float lastYMove = -1;


    public ZoomLayout(Context context) {
        super(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG, "ACTION_DOWN");
                currentStatus = STATUS_INIT;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.w(TAG, "ACTION_POINTER_DOWN");
                if (motionEvent.getPointerCount() == 2) {
                    // 当有两个手指按在屏幕上时，计算两指之间的距离
                    lastFingerDis = distanceBetweenFingers(motionEvent);

                } else {
                    currentStatus = STATUS_INIT;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (motionEvent.getPointerCount() == 2) {
                    // 有两个手指按在屏幕上移动时，为缩放状态
                    double fingerDis = distanceBetweenFingers(motionEvent);
                    Log.w(TAG, "lastFingerDis = " + lastFingerDis);
                    Log.w(TAG, "fingerDis = " + fingerDis);
                    if (fingerDis > lastFingerDis) {
                        currentStatus = STATUS_ZOOM_OUT;
                        Log.w(TAG, "currentStatus = ZOOM_OUT");
                    } else {
                        currentStatus = STATUS_ZOOM_IN;
                        Log.w(TAG, "currentStatus = ZOOM_IN");
                    }
                } else {
                    currentStatus = STATUS_INIT;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.w(TAG, "currentStatus = " + currentStatus);
            default:
                break;
        }
        return currentStatus == STATUS_INIT ? false : true;

    }

    /**
     * 计算两个手指之间的距离。
     *
     * @param event
     * @return 两个手指之间的距离
     */
    private double distanceBetweenFingers(MotionEvent event) {
        float disX = Math.abs(event.getX(0) - event.getX(1));
        float disY = Math.abs(event.getY(0) - event.getY(1));
        return Math.sqrt(disX * disX + disY * disY);
    }

}
