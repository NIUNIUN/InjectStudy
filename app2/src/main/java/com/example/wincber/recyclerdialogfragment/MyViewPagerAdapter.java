package com.example.wincber.recyclerdialogfragment;

import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by wincber on 9/23/2016.
 */

public class MyViewPagerAdapter extends PagerAdapter {
    int []imagesId = {R.drawable.scenery1,R.drawable.scenery2,R.drawable.scenery3,R.drawable.scenery4,R.drawable.scenery5,R.drawable.scenery6,
            R.drawable.scenery7,R.drawable.scenery8};
    private int mViewPositon;
    public MyViewPagerAdapter(int position) {
        mViewPositon = position;
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view== object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       View v =  LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item,container,false);
        ImageView mImage = (ImageView)v.findViewById(R.id.pager_image);
        mImage.setImageDrawable(container.getResources().getDrawable(imagesId[2*mViewPositon+position]));
        container.addView(v);
        Log.e("image",""+position );
        return v;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
         container.removeView((View) object);
    }
}
