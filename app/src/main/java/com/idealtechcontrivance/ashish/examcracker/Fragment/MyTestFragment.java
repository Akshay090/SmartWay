package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.idealtechcontrivance.ashish.examcracker.Adapter.FreePackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.MyTestAdapter;
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
public class MyTestFragment extends Fragment {

    View view;

    private List<Package> freePackageList;

    private RecyclerView freePackageRecycler;
    RecyclerView.LayoutManager freeLayoutManager;
    RecyclerView.Adapter freeRecyclerAdapter;

    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;

    private ProgressDialog progressDialog;

    public MyTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_test, container, false);
        getActivity().setTitle("My Test");

        initViews();
        initListeners();
        initObjects();

        if (Common.isConnectedToInternet(getActivity()))
        {
            loadFreePackages();
        }
        else {
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void initViews() {
        freePackageRecycler = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    private void initListeners() {
    }

    private void initObjects() {
        freePackageList = new ArrayList<>();
        requestQueue = CustomVolleyRequest.getInstance(getActivity()).getRequestQueue();
    }

    private void loadFreePackages() {
        jsonArrayRequest = new JsonArrayRequest(Common.free_packages,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL_FREE_PACKAGES(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL_FREE_PACKAGES(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
            Package aPackage = new Package();
            JSONObject json = null;

            try {
                json = response.getJSONObject(i);

                aPackage.setId(json.getInt("id"));
                aPackage.setCategory(json.getString("category"));
                aPackage.setTest_series_name(json.getString("test_series_name"));
                //aPackage.setCost(json.getInt("cost"));
                aPackage.setStrick_cost(json.getInt("strick_cost"));
                aPackage.setTotal_test(json.getInt("total_test"));
                aPackage.setExamination_test(json.getInt("examination_test"));
                aPackage.setPractice_test(json.getInt("practice_test"));
                aPackage.setSectional_test(json.getInt("sectional_test"));
                aPackage.setHighlights(json.getString("highlights"));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            freePackageList.add(aPackage);
        }
        freeLayoutManager = new LinearLayoutManager(getActivity());
        freePackageRecycler.setHasFixedSize(true);
        freePackageRecycler.setLayoutManager(freeLayoutManager);

        freeRecyclerAdapter = new MyTestAdapter(freePackageList,getActivity());
        freeRecyclerAdapter.notifyDataSetChanged();
        freePackageRecycler.setAdapter(freeRecyclerAdapter);
    }

}
