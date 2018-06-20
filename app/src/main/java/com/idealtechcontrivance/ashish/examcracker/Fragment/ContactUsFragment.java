package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idealtechcontrivance.ashish.examcracker.Adapter.ContactUsPagerAdapter;
import com.idealtechcontrivance.ashish.examcracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    View view;
    TabLayout tabLayout;
    ViewPager viewPager;

    ContactUsPagerAdapter pagerAdapter;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        getActivity().setTitle("Contact Us");

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        pagerAdapter = new ContactUsPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragments(new GetInTouchFragment());
        pagerAdapter.addFragments(new OurOfficeFragment());

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++)
        {
            tabLayout.getTabAt(0).setText("GET IN TOUCH");
            tabLayout.getTabAt(1).setText("OUR OFFICE");
        }

        return view;
    }

}
