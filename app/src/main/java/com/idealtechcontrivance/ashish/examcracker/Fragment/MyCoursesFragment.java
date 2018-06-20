package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idealtechcontrivance.ashish.examcracker.Adapter.ContactUsPagerAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.MyCoursePagerAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.ReportsPagerAdapter;
import com.idealtechcontrivance.ashish.examcracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCoursesFragment extends Fragment {


    View view;

    public MyCoursesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_courses, container, false);
        getActivity().setTitle("My Course");

        return view;
    }

}
