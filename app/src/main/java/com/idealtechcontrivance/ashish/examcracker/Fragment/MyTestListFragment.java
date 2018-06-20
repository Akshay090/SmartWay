package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.idealtechcontrivance.ashish.examcracker.Adapter.FreePackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.ReportsPagerAdapter;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Model.Package;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.CustomVolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTestListFragment extends Fragment {

    View view;
    TabLayout tabLayout;
    ViewPager viewPager;

    ReportsPagerAdapter pagerAdapter;

    Bundle bundle;
    String strId,strCategory,strSeriesName,strPrice,strTotalTest,strExaminationTest,strPracticeTest,strSectionalTest;


    public MyTestListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_test_list, container, false);
        getActivity().setTitle("My Test");

        initViews();
        initObjects();
        initListeners();
        loadBundle();

        MyTestFullFragment myTestFullFragment = new MyTestFullFragment();
        bundle.putString("ID_KEY",strId);
        myTestFullFragment.setArguments(bundle);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        pagerAdapter = new ReportsPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragments(myTestFullFragment);
        pagerAdapter.addFragments(new MyTestPracticeFragment());
        pagerAdapter.addFragments(new MyTestSectionalFragment());

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

    private void initListeners() {

    }

    private void initObjects() {
        bundle = new Bundle();
        bundle = this.getArguments();
    }

    private void initViews() {

    }

    private void loadBundle() {
        if (bundle != null){
            strId = bundle.getString("ID_KEY");
            strCategory = bundle.getString("CATEGORY_KEY");
            strSeriesName = bundle.getString("SERIES_NAME_KEY");
            strTotalTest = bundle.getString("TOTAL_TEST_KEY");
            strExaminationTest = bundle.getString("EXAMINATION_TEST_KEY");
            strPracticeTest = bundle.getString("PRACTICE_TEST_KEY");
            strSectionalTest = bundle.getString("SECTIONAL_TEST_KEY");
            strPrice = bundle.getString("COST_KEY");
            //str.putString("STRICK_COST_KEY", strCost);
            //bundle.putString("HIGHLIGHT_KEY", strCost);
        }
    }

}
