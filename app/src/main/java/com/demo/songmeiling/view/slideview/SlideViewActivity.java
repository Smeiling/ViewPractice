package com.demo.songmeiling.view.slideview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.demo.songmeiling.view.R;
import com.demo.songmeiling.view.utils.LogUtil;
import com.demo.songmeiling.view.views.SlideLayout;

public class SlideViewActivity extends Activity {

    public static final String TAG = SlideViewActivity.class.getSimpleName();

    @BindView(R.id.sl_one)
    SlideLayout slOne;
    @BindView(R.id.sl_two)
    SlideLayout slTwo;
    @BindView(R.id.sl_three)
    SlideLayout slThree;
    @BindView(R.id.content_slide_view2)
    LinearLayout contentSlideView2;
    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_slide_view2);
        ButterKnife.bind(this);
        initView();
    }


    Scroller scroller;

    private void initView() {

        tvOne.setClickable(true);
        TextView tv = new TextView(this);
        tv.setText("smlsmlsml");
        tv.setClickable(true);
        tv.setBackgroundColor(Color.CYAN);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        param.addRule(RelativeLayout.RIGHT_OF, R.id.tv_one);//此控件在id为1的控件的下边
        slOne.addView(param, tv);

        slOne.setOnSlideListener(new SlideLayout.OnSlideListener() {
            @Override
            public void onSlide(int slideMode) {
                if (slideMode == SlideLayout.SLIDE_LEFT) {
                    //tvOne.setVisibility(View.GONE);

                    //tvOne.scrollTo(150, 0);
                } else if (slideMode == SlideLayout.SLIDE_RIGHT) {
                    //tvOne.scrollTo(-150, 0);
                }
            }

            @Override
            public void onMoving(int distance) {
                LogUtil.w(TAG, "distance = " + distance);
                tvOne.scrollTo(distance, 0);
            }
        });
        tvTwo.setClickable(true);
        slTwo.setOnSlideListener(new SlideLayout.OnSlideListener() {
            @Override
            public void onSlide(int slideMode) {

            }

            @Override
            public void onMoving(int distance) {

            }
        });
        slThree.setOnSlideListener(new SlideLayout.OnSlideListener() {
            @Override
            public void onSlide(int slideMode) {
            }

            @Override
            public void onMoving(int distance) {

            }
        });
        slThree.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

}
