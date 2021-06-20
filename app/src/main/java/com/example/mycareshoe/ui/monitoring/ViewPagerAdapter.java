package com.example.mycareshoe.ui.monitoring;
import com.example.mycareshoe.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;

import static android.os.Build.VERSION_CODES.R;

class ViewPagerAdapter extends PagerAdapter {

    // Context object
    Context context;

    // Array of images
    int[] images;

    // Layout Inflater
    LayoutInflater mLayoutInflater;

    private TextView[] dots;
    private int[] layouts;


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

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        int resId=0;

        switch (position) {
            case 0:
                resId = com.example.mycareshoe.R.layout.feet_sensors;
                break;
            case 1:
                resId = com.example.mycareshoe.R.layout.upper_sensors;
                break;
        }

        View myView = inflater.inflate(resId, null);


        ((ViewPager) container).addView(myView, 0);
        //Access yout textView, ImageView, Button like below
        //TextView myText = (TextView)myView.findViewById(R.id.tv_myText);

        return myView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}
