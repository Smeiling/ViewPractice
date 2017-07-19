package com.demo.songmeiling.view.verticalviewpager;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.demo.songmeiling.view.R;

import java.util.ArrayList;
import java.util.List;

public class VerticalViewPagerActivity extends Activity {

    public static final String TAG = VerticalViewPagerActivity.class.getSimpleName();

    private VerticalViewPager verticalViewPager;

    private List<View> viewList = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_view_pager);

        verticalViewPager = (VerticalViewPager) findViewById(R.id.vp_vertical);
        for (int i = 0; i < 5; i++) {
            View view;
            if (i % 2 == 0) {
                view = getLayoutInflater().inflate(R.layout.layout_0, null);
            } else {
                view = getLayoutInflater().inflate(R.layout.layout_1, null);
            }
            viewList.add(view);
        }

        verticalViewPager.setViewList(viewList);
        verticalViewPager.setOnVerticalPageChangeListener(new VerticalViewPager.OnVerticalPageChangeListener() {
            @Override
            public void onVerticalPageSelected(int postion) {
                Log.w(TAG, "pager = " + postion);
            }
        });
    }
}
