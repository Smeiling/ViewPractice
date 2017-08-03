package com.demo.songmeiling.view.bezierviewpager.vPage;


import android.support.v7.widget.CardView;

/**
 * Created by songmeiling on 2017/8/3.
 */

interface CardAdapter {

    CardView getCardViewAt(int position);

    int getCount();

    int getMaxElevationFactor();

    void setMaxElevationFactor(int MaxElevationFactor);

}
