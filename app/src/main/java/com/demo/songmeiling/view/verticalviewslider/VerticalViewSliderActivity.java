package com.demo.songmeiling.view.verticalviewslider;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.demo.songmeiling.view.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerticalViewSliderActivity extends Activity {


    @BindView(R.id.ll_one)
    LinearLayout llOne;
    @BindView(R.id.ll_two)
    LinearLayout llTwo;
    @BindView(R.id.ll_three)
    LinearLayout llThree;
    @BindView(R.id.ll_four)
    LinearLayout llFour;
    @BindView(R.id.ll_five)
    LinearLayout llFive;
    @BindView(R.id.btn_animate)
    Button btnAnimate;
    @BindView(R.id.slider)
    VerticalViewSlider2 slider;

    private int curScreen = 0;
    private List<View> viewList = new ArrayList<View>();
    private List<View> list = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_view_slider);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        viewList.add(llOne);
        viewList.add(llTwo);
        viewList.add(llThree);
        viewList.add(llFour);
        viewList.add(llFive);

        btnAnimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAnimate(curScreen, curScreen + 1);
            }
        });

        for (int i = 0; i < 5; i++) {
            View view;
            if (i % 2 == 0) {
                view = getLayoutInflater().inflate(R.layout.layout_0, null);
            } else {
                view = getLayoutInflater().inflate(R.layout.layout_1, null);
            }
            list.add(view);
        }

        slider.setViewList(list);

    }


    private void doAnimate(int curScreen, int nextScreen) {
        viewList.get(curScreen).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in));
        viewList.get(nextScreen).setVisibility(View.VISIBLE);
        viewList.get(nextScreen).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_from_bottom));
        viewList.get(curScreen).setVisibility(View.GONE);
        this.curScreen++;
    }
}
