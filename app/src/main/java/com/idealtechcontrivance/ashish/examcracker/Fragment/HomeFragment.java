package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.idealtechcontrivance.ashish.examcracker.Adapter.FeaturesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.FreePackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.LatestPackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.PaidPackagesAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.SliderPagerAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.TestimonialsAdapter;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Model.Features;
import com.idealtechcontrivance.ashish.examcracker.Model.Package;
import com.idealtechcontrivance.ashish.examcracker.Model.Slider;
import com.idealtechcontrivance.ashish.examcracker.Model.Testimonials;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.CustomVolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private View view;

    private Timer timer;
    private int page = 0;
    private ViewPager sliderViewPager;
    private SliderPagerAdapter sliderPagerAdapter;
    private LinearLayout bannerDots;
    private int dotsCount;
    private ImageView[] dots;
    private List<Slider> sliderList;

    private List<Package> freePackageList;
    private List<Package> paidPackageList;
    private List<Package> latestPackageList;
    private List<Features> featuresList;
    private List<Testimonials> testimonialsList;

    TextView moreFree, morePaid, moreLatest, moreFeatures, moreTestimonials;
    private RecyclerView testimonialRecycler,featuresRecycler,freePackageRecycler,paidPackageRecycler,latestPackageRecycler;
    RecyclerView.LayoutManager testimonialLayoutManager,featuresLayoutManager,freeLayoutManager,paidLayoutManager,latestLayoutManager;
    RecyclerView.Adapter testimonialRecyclerAdapter,featuresRecyclerAdapter,freeRecyclerAdapter,paidRecyclerAdapter,latestRecyclerAdapter;

    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;

    private ProgressDialog progressDialog;
    ProgressBar sliderProgressBar, testimonialProgressBar,featuresProgressBar,freeProgressBar,paidProgressBar,latestProgressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Exam Cracker");

        initViews();
        initListeners();
        initObjects();

        if (Common.isConnectedToInternet(getActivity()))
        {
            loadSlider();
            loadTestimonials();
            loadFeatures();
            loadFreePackages();
            loadPaidPackages();
            loadLatestPackages();
        }
        else {
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void initViews() {
        sliderProgressBar = view.findViewById(R.id.sliderProgressBar);
        freeProgressBar = view.findViewById(R.id.freeProgressBar);
        paidProgressBar = view.findViewById(R.id.paidProgressBar);
        latestProgressBar = view.findViewById(R.id.latestProgressBar);
        featuresProgressBar = view.findViewById(R.id.featuresProgressBar);
        testimonialProgressBar = view.findViewById(R.id.testimonialsProgressBar);

        sliderViewPager= (ViewPager) view.findViewById(R.id.sliderViewPager);
        bannerDots = (LinearLayout) view.findViewById(R.id.bannerDots);
        freePackageRecycler = (RecyclerView) view.findViewById(R.id.freePackagesRecycler);
        paidPackageRecycler = (RecyclerView) view.findViewById(R.id.paidPackagesRecycler);
        latestPackageRecycler = (RecyclerView) view.findViewById(R.id.latestPackagesRecycler);
        testimonialRecycler = (RecyclerView) view.findViewById(R.id.testimonialsRecycler);
        featuresRecycler = (RecyclerView) view.findViewById(R.id.featuresRecycler);
        moreFree = (TextView) view.findViewById(R.id.moreFree);
        morePaid = (TextView) view.findViewById(R.id.morePaid);
        moreLatest = (TextView) view.findViewById(R.id.moreLatest);
        moreFeatures = (TextView) view.findViewById(R.id.moreFeatures);
        moreTestimonials = (TextView) view.findViewById(R.id.moreTestimonials);
    }

    private void initListeners() {
        moreFree.setOnClickListener(this);
        morePaid.setOnClickListener(this);
        moreLatest.setOnClickListener(this);
        moreFeatures.setOnClickListener(this);
        moreTestimonials.setOnClickListener(this);
    }

    private void initObjects() {
        sliderList = new ArrayList<>();
        testimonialsList = new ArrayList<>();
        featuresList = new ArrayList<>();
        freePackageList = new ArrayList<>();
        paidPackageList = new ArrayList<>();
        latestPackageList = new ArrayList<>();
        requestQueue = CustomVolleyRequest.getInstance(getActivity()).getRequestQueue();
    }

    private void loadSlider() {
        sliderProgressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Common.slider,  null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++){
                    Slider slider = new Slider();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String path = "https://www.examcrackerst.in/assets/sliderbanner/"+jsonObject.getString("image");
                        slider.setImage(path);

                        sliderProgressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sliderList.add(slider);
                }
                sliderPagerAdapter = new SliderPagerAdapter(sliderList,getActivity());
                sliderPagerAdapter.notifyDataSetChanged();
                sliderViewPager.setAdapter(sliderPagerAdapter);

                dotsCount = sliderPagerAdapter.getCount();
                dots = new ImageView[dotsCount];

                for (int i=0; i<dotsCount; i++)
                {
                    dots[i] = new ImageView(getContext());
                    dots[i].setImageDrawable(ActivityCompat.getDrawable(getActivity(),R.drawable.dot_nonactive));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8,0,8,0);
                    bannerDots.addView(dots[i],params);
                }

                dots[0].setImageDrawable(ActivityCompat.getDrawable(getActivity(),R.drawable.dot_active));

                sliderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        page = position;
                        for (int i=0; i<dotsCount; i++)
                        {
                            dots[i].setImageDrawable(ActivityCompat.getDrawable(getActivity(), R.drawable.dot_nonactive));
                        }
                        dots[position].setImageDrawable(ActivityCompat.getDrawable(getActivity(),R.drawable.dot_active));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                timer = new Timer();
                timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sliderProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error + "", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.moreFree){
            TestSeriesFragment testSeriesFragment = new TestSeriesFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(new HomeFragment(),"HomeFragment")
                    .addToBackStack("HomeFragment")
                    .replace(R.id.mainContainer, testSeriesFragment)
                    .commit();
        }
        else if (v.getId() == R.id.morePaid){
            TestSeriesFragment testSeriesFragment = new TestSeriesFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(new HomeFragment(),"HomeFragment")
                    .addToBackStack("HomeFragment")
                    .replace(R.id.mainContainer, testSeriesFragment)
                    .commit();
        }
        else if (v.getId() == R.id.moreLatest){
            TestSeriesFragment testSeriesFragment = new TestSeriesFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(new HomeFragment(),"HomeFragment")
                    .addToBackStack("HomeFragment")
                    .replace(R.id.mainContainer, testSeriesFragment)
                    .commit();
        }

    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if (getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sliderPagerAdapter.getCount() == page) {
                            page = 0;
                        } else {
                            page++;
                        }
                        sliderViewPager.setCurrentItem(page, true);
                    }
                });
            }
        }
    }

    private void loadFreePackages() {
        freeProgressBar.setVisibility(View.VISIBLE);
        jsonArrayRequest = new JsonArrayRequest(Common.free_packages,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        freeProgressBar.setVisibility(View.GONE);
                        JSON_PARSE_DATA_AFTER_WEBCALL_FREE_PACKAGES(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        freeProgressBar.setVisibility(View.GONE);
                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL_FREE_PACKAGES(JSONArray response) {
        freePackageList.clear();
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

                freeProgressBar.setVisibility(View.GONE);
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
        paidProgressBar.setVisibility(View.VISIBLE);
        jsonArrayRequest = new JsonArrayRequest(Common.paid_packages,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        paidProgressBar.setVisibility(View.GONE);
                        JSON_PARSE_DATA_AFTER_WEBCALL_PAID_PACKAGES(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        paidProgressBar.setVisibility(View.GONE);
                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL_PAID_PACKAGES(JSONArray response) {
        paidPackageList.clear();
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

                paidProgressBar.setVisibility(View.GONE);
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
        latestProgressBar.setVisibility(View.VISIBLE);
        jsonArrayRequest = new JsonArrayRequest(Common.latest_packages,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        latestProgressBar.setVisibility(View.GONE);
                        JSON_PARSE_DATA_AFTER_WEBCALL_LATEST_PACKAGES(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        latestProgressBar.setVisibility(View.GONE);
                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL_LATEST_PACKAGES(JSONArray array){
        latestPackageList.clear();
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

                latestProgressBar.setVisibility(View.GONE);
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

    private void loadTestimonials() {
        testimonialProgressBar.setVisibility(View.VISIBLE);
        jsonArrayRequest = new JsonArrayRequest(Common.testimonials,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        testimonialProgressBar.setVisibility(View.GONE);
                        JSON_PARSE_DATA_AFTER_WEBCALL_TESTIMONIALS(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        testimonialProgressBar.setVisibility(View.GONE);
                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL_TESTIMONIALS(JSONArray array){
        testimonialsList.clear();
        for(int i = 0; i<array.length(); i++) {
            Testimonials testimonials = new Testimonials();
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                String path = "https://www.examcrackerst.in/upload/"+json.getString("profile_pic");
                testimonials.setImage(path);
                testimonials.setName(json.getString("name"));
                testimonials.setTestimonial(json.getString("testimonial"));

                testimonialProgressBar.setVisibility(View.GONE);
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
        testimonialRecyclerAdapter.notifyDataSetChanged();
        testimonialRecycler.setAdapter(testimonialRecyclerAdapter);
    }

    private void loadFeatures() {
        featuresProgressBar.setVisibility(View.VISIBLE);
        jsonArrayRequest = new JsonArrayRequest(Common.features,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        featuresProgressBar.setVisibility(View.GONE);
                        JSON_PARSE_DATA_AFTER_WEBCALL_FEATURES(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        featuresProgressBar.setVisibility(View.GONE);
                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL_FEATURES(JSONArray array){
        featuresList.clear();
        for(int i = 0; i<array.length(); i++) {
            Features features = new Features();
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                String path = "https://www.examcrackerst.in/assets/features/"+json.getString("img1");
                features.setImage(path);
                features.setTitle(json.getString("list_title"));
                features.setListing(json.getString("Listing"));

                featuresProgressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            featuresList.add(features);
        }
        featuresLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        testimonialRecycler.setHasFixedSize(true);
        testimonialRecycler.setNestedScrollingEnabled(false);
        featuresRecycler.setLayoutManager(featuresLayoutManager);

        featuresRecyclerAdapter = new FeaturesAdapter(featuresList,getActivity());
        featuresRecyclerAdapter.notifyDataSetChanged();
        featuresRecycler.setAdapter(featuresRecyclerAdapter);
    }
}