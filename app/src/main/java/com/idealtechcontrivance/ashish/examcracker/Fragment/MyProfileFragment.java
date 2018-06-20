package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.idealtechcontrivance.ashish.examcracker.Activity.DashboardActivity;
import com.idealtechcontrivance.ashish.examcracker.Adapter.ContactUsPagerAdapter;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Helper.SessionManager;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    private View view;
    private ProgressDialog progressDialog;

    TabLayout tabLayout;
    ViewPager viewPager;
    ContactUsPagerAdapter pagerAdapter;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("My Profile");

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        pagerAdapter = new ContactUsPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragments(new AccountOverviewFragment());
        pagerAdapter.addFragments(new TransactionDetailFragment());

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++)
        {
            tabLayout.getTabAt(0).setText("Account Overview");
            tabLayout.getTabAt(1).setText("Transaction Detail");
        }

        return view;
    }

}
