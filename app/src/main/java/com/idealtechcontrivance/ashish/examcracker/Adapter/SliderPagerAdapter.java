package com.idealtechcontrivance.ashish.examcracker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.idealtechcontrivance.ashish.examcracker.Model.Slider;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.CustomVolleyRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    private List<Slider> sliderList;
    private Context context;
    private ImageLoader imageLoader;
    private LayoutInflater layoutInflater;

    public SliderPagerAdapter(List<Slider>sliderList, Context context) {
        this.sliderList = sliderList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_slider,null);

        Slider slider= sliderList.get(position);
        ImageView sliderImage = (ImageView) view.findViewById(R.id.sliderImage);
        Picasso.with(context).load(slider.getImage()).fit().into(sliderImage);

        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
