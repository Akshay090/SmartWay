package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.idealtechcontrivance.ashish.examcracker.Adapter.AllPackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.SimilarPackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.TestimonialsAdapter;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Model.Package;
import com.idealtechcontrivance.ashish.examcracker.Model.Testimonials;
import com.idealtechcontrivance.ashish.examcracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PackageDetailsFragment extends Fragment {

    private View view;
    Bundle bundle;

    TextView txtTotalTest, txtExaminationTest, txtPracticalTest, txtSectionalTest, txtPrice, txtSeriesName, txtBuyNow;
    String strId,strCategory,strSeriesName,strPrice,strTotalTest,strExaminationTest,strPracticeTest,strSectionalTest;

    List<Package> packageList;
    List<Testimonials> testimonialsList;

    RecyclerView testimonialRecycler,packagesRecycler;
    RecyclerView.LayoutManager testimonialLayoutManager,packagesLayoutManager;
    RecyclerView.Adapter testimonialRecyclerAdapter,packagesRecyclerAdapter;

    RequestQueue requestQueue;
    JsonArrayRequest jsonArrayRequest ;

    private ProgressDialog progressDialog;

    public PackageDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_package_details, container, false);
        getActivity().setTitle("Package Details");

        initViews();
        initListeners();
        initObjects();

        if (Common.isConnectedToInternet(getContext()))
        {
            loadBundle();
            loadTestimonials();
            loadPackages();
        }
        else {
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
        }

        txtSeriesName.setText(strSeriesName);
        txtTotalTest.setText(strTotalTest);
        txtExaminationTest.setText(strExaminationTest);
        txtPracticalTest.setText(strPracticeTest);
        txtSectionalTest.setText(strSectionalTest);
        txtPrice.setText(strPrice);

        txtBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBuyPackageFragment();
            }
        });

        return view;
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

    private void addBuyPackageFragment() {
        bundle.putString("ID_KEY", strId);
        bundle.putString("CATEGORY_KEY", strCategory);
        bundle.putString("SERIES_NAME_KEY", strSeriesName);
        bundle.putString("COST_KEY", strPrice);

        BuyPackageFragment buyPackageFragment = new BuyPackageFragment();

        buyPackageFragment.setArguments(bundle);

        ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                .add(new PackageDetailsFragment(),"packageDetailsFragment")
                .addToBackStack("packageDetailsFragment")
                .replace(R.id.mainContainer, buyPackageFragment)
                .commit();
    }

    private void initListeners() {

    }

    private void initObjects() {
        bundle = this.getArguments();
        testimonialsList = new ArrayList<>();
        packageList = new ArrayList<>();
    }

    private void initViews() {
        txtSeriesName = (TextView) view.findViewById(R.id.txtSeriesName);
        txtTotalTest = (TextView) view.findViewById(R.id.txtTotalTest);
        txtExaminationTest = (TextView) view.findViewById(R.id.txtExaminationTest);
        txtPracticalTest = (TextView) view.findViewById(R.id.txtPracticalTest);
        txtSectionalTest = (TextView) view.findViewById(R.id.txtSectionalTest) ;
        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        txtBuyNow = (TextView) view.findViewById(R.id.txtBuyNow);
        testimonialRecycler = (RecyclerView) view.findViewById(R.id.testimonialsRecycler);
        packagesRecycler = (RecyclerView) view.findViewById(R.id.packagesRecycler);
    }

    private void loadPackages() {
        jsonArrayRequest = new JsonArrayRequest(Common.similar_packages+"?category="+strCategory,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //progressBar.setVisibility(View.GONE);
                        JSON_PARSE_DATA_AFTER_WEBCALL_SIMILAR_PACKAGES(response);
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

    public void JSON_PARSE_DATA_AFTER_WEBCALL_SIMILAR_PACKAGES(JSONArray response){

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
            packageList.add(aPackage);
        }
        packagesLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        packagesRecycler.setHasFixedSize(true);
        packagesRecycler.setNestedScrollingEnabled(false);
        packagesRecycler.setLayoutManager(packagesLayoutManager);

        packagesRecyclerAdapter = new SimilarPackagesAdapter(packageList,getActivity());
        packagesRecycler.setAdapter(packagesRecyclerAdapter);
    }

    private void loadTestimonials() {
        jsonArrayRequest = new JsonArrayRequest(Common.testimonials,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {

                        //progressBar.setVisibility(View.GONE);
                        JSON_PARSE_DATA_AFTER_WEBCALL_TESTIMONIALS(array);
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

    public void JSON_PARSE_DATA_AFTER_WEBCALL_TESTIMONIALS(JSONArray array){

        for(int i = 0; i<array.length(); i++) {
            Testimonials testimonials = new Testimonials();
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                String path = "https://www.examcrackerst.in/upload/"+json.getString("profile_pic");
                testimonials.setImage(path);
                testimonials.setName(json.getString("name"));
                testimonials.setTestimonial(json.getString("testimonial"));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            testimonialsList.add(testimonials);
        }
        testimonialLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        testimonialRecycler.setHasFixedSize(true);
        testimonialRecycler.setNestedScrollingEnabled(false);
        testimonialRecycler.setLayoutManager(testimonialLayoutManager);

        testimonialRecyclerAdapter = new TestimonialsAdapter(testimonialsList,getActivity());
        testimonialRecycler.setAdapter(testimonialRecyclerAdapter);
    }

}
