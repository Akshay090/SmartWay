package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.idealtechcontrivance.ashish.examcracker.Adapter.ReportsPagerAdapter;
import com.idealtechcontrivance.ashish.examcracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment {

    View view;
    TabLayout tabLayout;
    ViewPager viewPager;

    ReportsPagerAdapter pagerAdapter;

    public ReportsFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reports, container, false);
        getActivity().setTitle("Report");
        
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        pagerAdapter = new ReportsPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragments(new ReportFullFragment());
        pagerAdapter.addFragments(new ReportPracticeFragment());
        pagerAdapter.addFragments(new ReportSectionalFragment());

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++)
        {
            tabLayout.getTabAt(0).setText("EXAMINATION");
            tabLayout.getTabAt(1).setText("PRACTICE");
            tabLayout.getTabAt(2).setText("SECTIONAL");
        }

        return view;
    }

}
