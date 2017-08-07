package com.demo.songmeiling.view.branchviewpager;

import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.demo.songmeiling.view.R;
import com.demo.songmeiling.view.scrolllist.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class BranchViewPagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_view_pager);

        Button button = (Button) findViewById(R.id.buttonPanel);
        final BranchViewPager viewPager = (BranchViewPager) findViewById(R.id.viewpager);
        assert viewPager != null;
        viewPager.setPageMargin(80);
        viewPager.setOffscreenPageLimit(3);
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.a);
        list.add(R.drawable.b);
        list.add(R.drawable.c);
        list.add(R.drawable.d);
        list.add(R.drawable.e);
        list.add(R.drawable.f);

        final BranchViewPagerAdapter adapter = new BranchViewPagerAdapter(this, list);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new BranchPickedFragment());
        fragments.add(new BranchRecommendFragment());
        fragments.add(new BranchPickedFragment());
        fragments.add(new RankListFragment());
        FragmentAdapter adapter1 = new FragmentAdapter(getSupportFragmentManager(), fragments);

        viewPager.setAdapter(adapter1);
        viewPager.addOnPageChangeListener(adapter);

        adapter.setOnTouchListener(new BranchViewPagerAdapter.OnTouchListener() {
            @Override
            public void onVerticalFling(int offsetPosition) {
                viewPager.setVertical(true);
                viewPager.setCurrentItem(adapter.getPosition() + offsetPosition);
                Log.d("sml", "onVerticalFling position = " + offsetPosition);
            }

            @Override
            public void onHorizontalFling(int offsetPosition) {
                viewPager.setVertical(false);
                viewPager.setCurrentItem(adapter.getPosition() + offsetPosition);
                Log.d("sml", "onHorizontalFling position = " + offsetPosition);
            }
        });

        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setVertical(!viewPager.isVertical());
                adapter.notifyDataSetChanged();
            }
        });
    }

}
