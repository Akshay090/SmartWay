package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.idealtechcontrivance.ashish.examcracker.Adapter.FreeFullLengthTestAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.NewsAdapter;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Model.FullLengthTest;
import com.idealtechcontrivance.ashish.examcracker.Model.News;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTestFullFragment extends Fragment {

    View view;
    Bundle bundle;
    private ProgressDialog progressDialog;
    String strId,strCategory,strSeriesName,strPrice,strTotalTest,strExaminationTest,strPracticeTest,strSectionalTest;

    ArrayList<FullLengthTest> fullLengthTestList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter  adapter;

    RequestQueue requestQueue;
    JsonArrayRequest jsonArrayRequest ;

    public MyTestFullFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_test_full, container, false);

        initViews();
        initListeners();
        initObjects();
        loadBundle();

        if (Common.isConnectedToInternet(getActivity()))
        {
            loadFullLengthTest();
        }
        else {
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void initViews() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    private void initListeners() {

    }

    private void initObjects() {
        bundle = this.getArguments();
        fullLengthTestList = new ArrayList<>();
    }

    private void loadFullLengthTest() {
        jsonArrayRequest = new JsonArrayRequest(Common.free_full_length_test+ "?package_id="+strId,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //progressBar.setVisibility(View.GONE);

                        JSON_PARSE_DATA_AFTER_WEBCALL_FULL_LENGTH_TEST(response);
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

    private void JSON_PARSE_DATA_AFTER_WEBCALL_FULL_LENGTH_TEST(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
            FullLengthTest fullLengthTest = new FullLengthTest();
            JSONObject json = null;

            try {
                json = response.getJSONObject(i);

                fullLengthTest.setTestId(json.getInt("testId"));
                fullLengthTest.setDuration(json.getInt("duration"));
                fullLengthTest.setCnt(json.getString("cntQuestion"));
                fullLengthTest.setTestType(json.getString("test_type"));
                fullLengthTest.setTestName(json.getString("testName"));
                fullLengthTest.setTestSubject(json.getString("subject"));
                fullLengthTest.setPackageType(json.getString("test_series_name"));
                fullLengthTest.setPublishedDate(json.getString("published_date"));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            fullLengthTestList.add(fullLengthTest);
        }
        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        if (!fullLengthTestList.isEmpty()) {
            adapter = new FreeFullLengthTestAdapter(getActivity(),fullLengthTestList);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }

    }

    private void loadBundle() {
        strId = getArguments().getString("ID_KEY");
        strCategory = getArguments().getString("CATEGORY_KEY");
        strSeriesName = getArguments().getString("SERIES_NAME_KEY");
        strTotalTest = getArguments().getString("TOTAL_TEST_KEY");
        strExaminationTest = getArguments().getString("EXAMINATION_TEST_KEY");
        strPracticeTest = getArguments().getString("PRACTICE_TEST_KEY");
        strSectionalTest = getArguments().getString("SECTIONAL_TEST_KEY");
        strPrice = getArguments().getString("COST_KEY");
        //str.putString("STRICK_COST_KEY", strCost);
        //bundle.putString("HIGHLIGHT_KEY", strCost);
    }

}
