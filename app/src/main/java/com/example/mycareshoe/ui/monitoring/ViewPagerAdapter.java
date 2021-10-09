package com.example.mycareshoe.ui.monitoring;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mycareshoe.model.SensorsReading;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends PagerAdapter {

    // Context object
    Context context;

    // Array of images
    int[] images;
    boolean isInflated = false;
    // Layout Inflater
    LayoutInflater mLayoutInflater;

    private TextView[] dots;
    private int[] layouts;
    private SensorsReading sensorsReading;


    public boolean isInflated() {
        return isInflated;
    }

    public void setInflated(boolean inflated) {
        isInflated = inflated;
    }

    // Viewpager Constructor
    public ViewPagerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public void finishUpdate(@NonNull @NotNull ViewGroup container) {
        super.finishUpdate(container);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        int resId = 0;

        switch (position) {
            case 0:
                resId = com.example.mycareshoe.R.layout.down_sensors;
                break;
            case 1:
                resId = com.example.mycareshoe.R.layout.upper_sensors;
                break;
        }

        View myView = inflater.inflate(resId, null);


        ((ViewPager) container).addView(myView, 0);
        isInflated = true;
        return myView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }


}
