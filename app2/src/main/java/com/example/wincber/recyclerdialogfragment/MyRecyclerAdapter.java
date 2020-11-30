package com.example.wincber.recyclerdialogfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by wincber on 9/21/2016.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerHolder> {
    public int currentItem;
    @Override
    public MyRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyRecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyRecyclerHolder holder, int position) {
        currentItem = holder.getPosition();
        MyViewPagerAdapter mPagerAdapter = new MyViewPagerAdapter(position);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,400);
        holder.mViewPager.setLayoutParams(params);
        holder.mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}
