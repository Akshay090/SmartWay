package com.idealtechcontrivance.ashish.examcracker.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ProfilePagerAdapter  extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();

    public  void addFragments(Fragment fragments)
    {
        this.fragments.add(fragments);
    }

    public ProfilePagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
