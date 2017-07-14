package com.demo.songmeiling.view.searchview;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.demo.songmeiling.view.R;
import com.demo.songmeiling.view.utils.DensityUtil;

public class SearchViewActivity extends Activity implements View.OnTouchListener {

    public static final String TAG = SearchViewActivity.class.getSimpleName();

    private LinearLayout searchResultPanel;
    private EditText etSearch;
    private TextView tvCancel;

    private int screenWidth;
    private int screenHeight;

    private InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        getScreenParams();
        initView();
    }

    private void initView() {
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        searchResultPanel = (LinearLayout) findViewById(R.id.ll_search_result);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchResultPanel.getVisibility() == View.VISIBLE) {
                    hideSearchPannel();
                }
            }
        });
        etSearch = (EditText) findViewById(R.id.btn_anim);
        etSearch.getLayoutParams().width = screenWidth * 2 - 40;
        etSearch.setPadding((screenWidth * 2 - 40 - 80) / 2, 10, 10, 10);
        etSearch.setFocusable(false);
        etSearch.setOnTouchListener(this);
    }


    /**
     * 获取屏幕信息
     */
    private void getScreenParams() {
        WindowManager wm = this.getWindowManager();

        screenWidth = DensityUtil.px2dip(this, wm.getDefaultDisplay().getWidth());
        screenHeight = DensityUtil.px2dip(this, wm.getDefaultDisplay().getHeight());
        Log.w(TAG, "screenWidth = " + screenWidth);
    }

    /**
     * 显示搜索结果面板
     */
    private void showSearchPannel() {
        Log.w(TAG, "show");
        searchResultPanel.setVisibility(View.VISIBLE);
        searchResultPanel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_from_bottom));
        tvCancel.setVisibility(View.VISIBLE);
        tvCancel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_from_right));
        performAnimate(true);
        etSearch.setFocusable(true);
        etSearch.setFocusableInTouchMode(true);
        etSearch.requestFocus();
    }

    /**
     * 隐藏搜索结果面板
     */
    private void hideSearchPannel() {
        Log.w(TAG, "hide");
        searchResultPanel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hide_to_bottom));
        searchResultPanel.setVisibility(View.GONE);
        tvCancel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hide_to_right));
        tvCancel.setVisibility(View.GONE);
        hideKeyboard();
        etSearch.setFocusable(false);
        performAnimate(false);

    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        Log.w(TAG, "hideKeyboard1");
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            Log.w(TAG, "hideKeyboard2");
            if (getCurrentFocus() != null) {
                Log.w(TAG, "hideKeyboard3");
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            //Log.w(TAG, "setWidth = " + width);
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }

        public int getPadding() {
            return mTarget.getPaddingLeft();
        }

        public void setPadding(int padding) {
            //Log.w(TAG, "setPadding = " + padding);
            mTarget.setPadding(padding, 10, 10, 10);
        }

    }

    /**
     * 控件属性渐变动画
     *
     * @param expand
     */
    private void performAnimate(boolean expand) {
        ViewWrapper wrapper = new ViewWrapper(etSearch);
        //Log.w(TAG, "screenWidth = " + screenWidth);
        if (expand) {
            ObjectAnimator.ofInt(wrapper, "padding", (screenWidth * 2 - 40 - 80) / 2, 10).setDuration(500).start();
            ObjectAnimator.ofInt(wrapper, "width", screenWidth * 2 - 40 - 80).setDuration(500).start();
        } else {
            ObjectAnimator.ofInt(wrapper, "padding", (screenWidth * 2 - 40 - 80) / 2).setDuration(500).start();
            ObjectAnimator.ofInt(wrapper, "width", screenWidth * 2 - 40).setDuration(500).start();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.btn_anim && searchResultPanel.getVisibility() == View.GONE) {
            showSearchPannel();
        }
        return false;
    }
}
