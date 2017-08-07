package com.demo.songmeiling.view.branchviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.songmeiling.view.R;

/**
 * Created by songmeiling on 2017/8/4.
 */

public class BranchRecommendFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.branch_recommend_view, null);
    }
}
