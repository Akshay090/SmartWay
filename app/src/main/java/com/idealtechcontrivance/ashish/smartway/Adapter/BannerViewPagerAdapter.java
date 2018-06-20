package com.idealtechcontrivance.ashish.smartway.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.idealtechcontrivance.ashish.smartway.Model.Banner;
import com.idealtechcontrivance.ashish.smartway.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerViewPagerAdapter extends PagerAdapter {

    private List<Banner> bannerList;
    private Context context;

    public BannerViewPagerAdapter(List<Banner> bannerList, Context context) {
        this.bannerList = bannerList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_banner,null);
        final ImageView bannerImage = (ImageView) view.findViewById(R.id.bannerImage);
        Picasso.with(context).load(bannerList.get(position).getImage()).fit().into(bannerImage);
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
