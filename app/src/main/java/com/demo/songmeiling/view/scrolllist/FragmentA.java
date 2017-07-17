package com.demo.songmeiling.view.scrolllist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.songmeiling.view.R;

/**
 * Created by songmeiling on 2017/7/17.
 */

public class FragmentA extends Fragment {

    private TextView tvView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.w("SML", "fragmentA");
        return inflater.inflate(R.layout.layout_0, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvView = (TextView) getView().findViewById(R.id.tv_id);
        tvView.setText("FragmentA");
    }
}
