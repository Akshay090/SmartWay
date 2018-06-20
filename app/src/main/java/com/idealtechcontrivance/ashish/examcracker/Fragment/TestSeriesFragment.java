package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.idealtechcontrivance.ashish.examcracker.Activity.DashboardActivity;
import com.idealtechcontrivance.ashish.examcracker.Adapter.AllPackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.SelectedPackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Model.Package;
import com.idealtechcontrivance.ashish.examcracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestSeriesFragment extends Fragment implements View.OnClickListener{

    private LinearLayout linearLayout;

    View view;
    Spinner spinner;
    TextView txtMessage;
    TextView allPackages;
    String category;
    ImageView sortPackages;
    private boolean defaults = true;

    ArrayList<String> categoryName;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Package> allPackageList;
    ArrayList<Package> selectedPackageList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    RecyclerView.Adapter recyclerViewAdapter;

    JSONArray jsonArray;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;

    SharedPreferences preferences;
    private ProgressDialog progressDialog;

    public TestSeriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_test_series, container, false);
        getActivity().setTitle("Test Series");

        initViews();
        initListeners();
        initObjects();



        spinner.setPrompt("Categories");
        loadSpinner(Common.package_category);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                category = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                int selected_item = spinner.getSelectedItemPosition();

                SharedPreferences sharedPref = getActivity().getSharedPreferences("Position",0);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("spinner_item", selected_item);
                prefEditor.apply();

                if (position == 0 ){
                    allPackageList.clear();
                    selectedPackageList.clear();

                    if (recyclerViewAdapter != null){
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    if (Common.isConnectedToInternet(getActivity()))
                    {
                        loadAllPackages(Common.all_packages);
                    }
                    else {
                        Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
                    }
                }
                else if (position >= 1  && position < jsonArray.length()){
                    allPackageList.clear();
                    selectedPackageList.clear();

                    if (recyclerViewAdapter != null){
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    if (Common.isConnectedToInternet(getActivity()))
                    {
                        loadSelectedPackages(Common.selected_packages);
                    }
                    else {
                        Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Category not found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        allPackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allPackageList.clear();
                selectedPackageList.clear();
                spinner.setSelection(0);

                if (recyclerViewAdapter != null){
                    recyclerViewAdapter.notifyDataSetChanged();
                }

                if (Common.isConnectedToInternet(getActivity()))
                {
                    loadAllPackages(Common.all_packages);
                }
                else {
                    Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    private void initViews() {
        txtMessage = view.findViewById(R.id.txtMessage);
        allPackages = (TextView) view.findViewById(R.id.allPackages);
        sortPackages = (ImageView) view.findViewById(R.id.sortPackages);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
    }

    private void initListeners() {
        sortPackages.setOnClickListener(this);
    }

    private void initObjects() {
        categoryName =  new ArrayList<>();
        allPackageList = new ArrayList<>();
        selectedPackageList = new ArrayList<>();
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        requestQueue=Volley.newRequestQueue(getApplicationContext());
    }

    private void loadSpinner(String package_category) {
        //showProgressDialog();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, package_category, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    jsonArray=jsonObject.getJSONArray("CATEGORY");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        categoryName.add(jsonObject1.getString("category"));
                    }

                    //hideProgressDialog();
                    arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,categoryName);
                    arrayAdapter.notifyDataSetChanged();
                    spinner.setAdapter(arrayAdapter);

                    SharedPreferences sharedPref = getActivity().getSharedPreferences("Position", Context.MODE_PRIVATE);
                    int spinnerValue = sharedPref.getInt("spinner_item",-1);
                    if(spinnerValue != -1) {
                        // set the value of the spinner
                        spinner.setSelection(spinnerValue,true);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //hideProgressDialog();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void loadAllPackages(String all_packages) {
        //showProgressDialog();

        jsonArrayRequest = new JsonArrayRequest( all_packages,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSON_PARSE_DATA_AFTER_WEBCALL_ALL_PACKAGES(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //hideProgressDialog();
                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);

    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL_ALL_PACKAGES(JSONArray response) {
        allPackageList.clear();
        for(int i = 0; i<response.length(); i++) {
            Package aPackage = new Package();
            JSONObject json = null;

            try {
                json = response.getJSONObject(i);
                aPackage.setId(json.getInt("id"));
                aPackage.setCategory(json.getString("category"));
                aPackage.setTest_series_name(json.getString("test_series_name"));
                aPackage.setTotal_test(json.getInt("total_test"));
                aPackage.setExamination_test(json.getInt("examination_test"));
                aPackage.setPractice_test(json.getInt("practice_test"));
                aPackage.setSectional_test(json.getInt("sectional_test"));
                aPackage.setCost(json.getInt("cost"));
                aPackage.setStrick_cost(json.getInt("strick_cost"));
                aPackage.setHighlights(json.getString("highlights"));

                //hideProgressDialog();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            allPackageList.add(aPackage);
            int cost = aPackage.getCost();
            if (cost <= 0){
                selectedPackageList.remove(aPackage);
            }
        }

        recyclerViewAdapter = new AllPackagesAdapter(allPackageList,getActivity());
        recyclerViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void loadSelectedPackages(String selected_packages) {
        //showProgressDialog();

        jsonArrayRequest = new JsonArrayRequest( selected_packages+ "?category="+category,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        JSON_PARSE_DATA_AFTER_WEBCALL_SELECTED_PACKAGES(array);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //hideProgressDialog();
                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL_SELECTED_PACKAGES(JSONArray array){
        selectedPackageList.clear();
        for(int i = 0; i<array.length(); i++) {
            Package aPackage = new Package();
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                aPackage.setId(json.getInt("id"));
                aPackage.setCategory(json.getString("category"));
                aPackage.setTest_series_name(json.getString("test_series_name"));
                aPackage.setTotal_test(json.getInt("total_test"));
                aPackage.setExamination_test(json.getInt("examination_test"));
                aPackage.setPractice_test(json.getInt("practice_test"));
                aPackage.setSectional_test(json.getInt("sectional_test"));
                aPackage.setCost(json.getInt("cost"));
                aPackage.setStrick_cost(json.getInt("strick_cost"));
                aPackage.setHighlights(json.getString("highlights"));

                //hideProgressDialog();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            selectedPackageList.add(aPackage);
            int cost = aPackage.getCost();
            if (cost <= 0){
                selectedPackageList.remove(aPackage);
            }
        }
        recyclerViewAdapter = new SelectedPackagesAdapter(selectedPackageList, getActivity());
        recyclerViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.sortPackages){
            showSortPackagesDialog(defaults);
            defaults = !defaults;
        }
    }

    private void showSortPackagesDialog(final boolean defaul) {
        final String[] listItems ={"None","Latest","Ascending","Descending","Low To High","High To Low"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort By");
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    recyclerViewAdapter.notifyDataSetChanged();
                }
                else if (which == 1){
                    Collections.reverse(allPackageList);
                    Collections.reverse(selectedPackageList);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
                else if (which == 2){
                    Collections.sort(allPackageList, new Comparator<Package>() {
                        @Override
                        public int compare(Package o1, Package o2) {
                            return o1.getTest_series_name().compareTo(o2.getTest_series_name());
                        }
                    });
                    recyclerViewAdapter.notifyDataSetChanged();

                    Collections.sort(selectedPackageList, new Comparator<Package>() {
                        @Override
                        public int compare(Package o1, Package o2) {
                            return o1.getTest_series_name().compareTo(o2.getTest_series_name());
                        }
                    });
                    recyclerViewAdapter.notifyDataSetChanged();
                }
                else if (which == 3){
                    Collections.sort(allPackageList, new Comparator<Package>() {
                        @Override
                        public int compare(Package o1, Package o2) {
                            return o2.getTest_series_name().compareTo(o1.getTest_series_name());
                        }
                    });
                    recyclerViewAdapter.notifyDataSetChanged();

                    Collections.sort(selectedPackageList, new Comparator<Package>() {
                        @Override
                        public int compare(Package o1, Package o2) {
                            return o2.getTest_series_name().compareTo(o1.getTest_series_name());
                        }
                    });
                    recyclerViewAdapter.notifyDataSetChanged();
                }
                else if (which == 4){
                    Collections.sort(allPackageList, new Comparator<Package>() {
                        @Override
                        public int compare(Package o1, Package o2) {
                            return Integer.compare(o1.getCost(),o2.getCost());
                        }
                    });
                    recyclerViewAdapter.notifyDataSetChanged();

                    Collections.sort(selectedPackageList, new Comparator<Package>() {
                        @Override
                        public int compare(Package o1, Package o2) {
                            return Integer.compare(o1.getCost(),o2.getCost());
                        }
                    });
                    recyclerViewAdapter.notifyDataSetChanged();
                }
                else if (which == 5){
                    Collections.sort(allPackageList, new Comparator<Package>() {
                        @Override
                        public int compare(Package o1, Package o2) {
                            return Integer.compare(o2.getCost(),o1.getCost());
                        }
                    });
                    recyclerViewAdapter.notifyDataSetChanged();

                    Collections.sort(selectedPackageList, new Comparator<Package>() {
                        @Override
                        public int compare(Package o1, Package o2) {
                            return Integer.compare(o2.getCost(),o1.getCost());
                        }
                    });
                    recyclerViewAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}