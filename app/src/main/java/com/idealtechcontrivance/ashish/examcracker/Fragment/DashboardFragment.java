package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.idealtechcontrivance.ashish.examcracker.Adapter.FreePackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.LatestPackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.PaidPackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Helper.SessionManager;
import com.idealtechcontrivance.ashish.examcracker.Model.Package;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.provider.MediaStore.Video.Thumbnails.VIDEO_ID;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener{

    private View view;

    private List<Package> freePackageList;
    private List<Package> paidPackageList;
    private List<Package> latestPackageList;

    TextView moreFree, morePaid, moreLatest;
    RecyclerView freePackageRecycler,paidPackageRecycler,latestPackageRecycler;
    RecyclerView.LayoutManager freeLayoutManager,paidLayoutManager,latestLayoutManager;
    RecyclerView.Adapter freeRecyclerAdapter,paidRecyclerAdapter,latestRecyclerAdapter;

    private SessionManager session;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest ;
    private ProgressDialog progressDialog;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        getActivity().setTitle("Dashboard");

        initViews();
        initListeners();
        initObjects();

        if (Common.isConnectedToInternet(getActivity()))
        {
            loadFreePackages();
            loadPaidPackages();
            loadLatestPackages();
        }
        else {
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void initObjects() {
        freePackageList = new ArrayList<>();
        paidPackageList = new ArrayList<>();
        latestPackageList = new ArrayList<>();
        session = new SessionManager(getActivity());
    }

    private void initListeners() {
        moreFree.setOnClickListener(this);
        morePaid.setOnClickListener(this);
        moreLatest.setOnClickListener(this);
    }

    private void initViews() {
        moreFree = (TextView) view.findViewById(R.id.moreFree);
        morePaid = (TextView) view.findViewById(R.id.morePaid);
        moreLatest = (TextView) view.findViewById(R.id.moreLatest);
        freePackageRecycler = (RecyclerView) view.findViewById(R.id.freePackagesRecycler);
        paidPackageRecycler = (RecyclerView) view.findViewById(R.id.paidPackagesRecycler);
        latestPackageRecycler = (RecyclerView) view.findViewById(R.id.latestPackagesRecycler);
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
        freeLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        freePackageRecycler.setHasFixedSize(true);
        freePackageRecycler.setNestedScrollingEnabled(false);
        freePackageRecycler.setLayoutManager(freeLayoutManager);

        freeRecyclerAdapter = new FreePackagesAdapter(freePackageList,getActivity());
        freeRecyclerAdapter.notifyDataSetChanged();
        freePackageRecycler.setAdapter(freeRecyclerAdapter);
    }

    private void loadPaidPackages() {
        jsonArrayRequest = new JsonArrayRequest(Common.paid_packages,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //progressBar.setVisibility(View.GONE);

                        JSON_PARSE_DATA_AFTER_WEBCALL_PAID_PACKAGES(response);
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

    private void JSON_PARSE_DATA_AFTER_WEBCALL_PAID_PACKAGES(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
            Package aPackage = new Package();
            JSONObject json = null;

            try {
                json = response.getJSONObject(i);

                aPackage.setId(json.getInt("id"));
                aPackage.setCategory(json.getString("category"));
                aPackage.setTest_series_name(json.getString("test_series_name"));
                aPackage.setCost(json.getInt("cost"));
                aPackage.setStrick_cost(json.getInt("strick_cost"));
                aPackage.setTotal_test(json.getInt("total_test"));
                aPackage.setExamination_test(json.getInt("examination_test"));
                aPackage.setPractice_test(json.getInt("practice_test"));
                aPackage.setSectional_test(json.getInt("sectional_test"));
                aPackage.setHighlights(json.getString("highlights"));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            paidPackageList.add(aPackage);
        }
        paidLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        paidPackageRecycler.setHasFixedSize(true);
        paidPackageRecycler.setNestedScrollingEnabled(false);
        paidPackageRecycler.setLayoutManager(paidLayoutManager);

        paidRecyclerAdapter = new PaidPackagesAdapter(paidPackageList,getActivity());
        paidRecyclerAdapter.notifyDataSetChanged();
        paidPackageRecycler.setAdapter(paidRecyclerAdapter);
    }

    private void loadLatestPackages() {
        jsonArrayRequest = new JsonArrayRequest(Common.latest_packages,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //progressBar.setVisibility(View.GONE);

                        JSON_PARSE_DATA_AFTER_WEBCALL_LATEST_PACKAGES(response);
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

    public void JSON_PARSE_DATA_AFTER_WEBCALL_LATEST_PACKAGES(JSONArray array){

        for(int i = 0; i<array.length(); i++) {
            Package aPackage = new Package();
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                aPackage.setId(json.getInt("id"));
                aPackage.setCategory(json.getString("category"));
                aPackage.setTest_series_name(json.getString("test_series_name"));
                aPackage.setCost(json.getInt("cost"));
                aPackage.setStrick_cost(json.getInt("strick_cost"));
                aPackage.setTotal_test(json.getInt("total_test"));
                aPackage.setExamination_test(json.getInt("examination_test"));
                aPackage.setPractice_test(json.getInt("practice_test"));
                aPackage.setSectional_test(json.getInt("sectional_test"));
                aPackage.setHighlights(json.getString("highlights"));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            latestPackageList.add(aPackage);
        }
        latestLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        latestPackageRecycler.setHasFixedSize(true);
        latestPackageRecycler.setNestedScrollingEnabled(false);
        latestPackageRecycler.setLayoutManager(latestLayoutManager);

        latestRecyclerAdapter = new LatestPackagesAdapter(latestPackageList,getActivity());
        latestRecyclerAdapter.notifyDataSetChanged();
        latestPackageRecycler.setAdapter(latestRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.moreFree) {
            TestSeriesFragment testSeriesFragment = new TestSeriesFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(new HomeFragment(), "HomeFragment")
                    .addToBackStack("HomeFragment")
                    .replace(R.id.mainContainer, testSeriesFragment)
                    .commit();
        } else if (v.getId() == R.id.morePaid) {
            TestSeriesFragment testSeriesFragment = new TestSeriesFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(new HomeFragment(), "HomeFragment")
                    .addToBackStack("HomeFragment")
                    .replace(R.id.mainContainer, testSeriesFragment)
                    .commit();
        } else if (v.getId() == R.id.moreLatest) {
            TestSeriesFragment testSeriesFragment = new TestSeriesFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(new HomeFragment(), "HomeFragment")
                    .addToBackStack("HomeFragment")
                    .replace(R.id.mainContainer, testSeriesFragment)
                    .commit();
        }
    }
}
