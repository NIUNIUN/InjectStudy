package com.example.wincber.recyclerdialogfragment;


import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by wincber on 9/21/2016.
 */

public class MyRecyclerHolder extends RecyclerView.ViewHolder{
    ViewPager mViewPager;

    public MyRecyclerHolder(View itemView) {
        super(itemView);
        this.mViewPager = (ViewPager)itemView.findViewById(R.id.pager);


    }
}
