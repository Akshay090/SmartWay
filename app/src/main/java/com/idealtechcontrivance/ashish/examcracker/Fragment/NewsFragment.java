package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.idealtechcontrivance.ashish.examcracker.Adapter.InboxAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.NewsAdapter;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Model.Inbox;
import com.idealtechcontrivance.ashish.examcracker.Model.News;
import com.idealtechcontrivance.ashish.examcracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private View view;
    private ProgressDialog progressDialog;

    ArrayList<News> newsList;

    RecyclerView newsRecycler;
    RecyclerView.LayoutManager newsLayoutManager;
    RecyclerView.Adapter  newsRecyclerAdapter;

    RequestQueue requestQueue;
    JsonArrayRequest jsonArrayRequest ;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);
        getActivity().setTitle("News & Updates");

        initViews();
        initListeners();
        initObjects();

        if (Common.isConnectedToInternet(getContext()))
        {
            loadNews();
        }
        else {
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void initViews() {
        newsRecycler = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    private void initListeners() {

    }

    private void initObjects() {
        newsList = new ArrayList<>();
    }

    private void loadNews() {
        jsonArrayRequest = new JsonArrayRequest(Common.news_updates,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //progressBar.setVisibility(View.GONE);

                        JSON_PARSE_DATA_AFTER_WEBCALL_NEWS(response);
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

    private void JSON_PARSE_DATA_AFTER_WEBCALL_NEWS(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
            News news = new News();
            JSONObject json = null;

            try {
                json = response.getJSONObject(i);

                news.setNewsName(json.getString("news_name"));
                news.setNewsDescription(json.getString("news_description"));
                news.setStartDate(json.getString("start_date"));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            newsList.add(news);
        }
        newsLayoutManager = new LinearLayoutManager(getActivity());
        newsRecycler.setHasFixedSize(true);
        newsRecycler.setLayoutManager(newsLayoutManager);

        if (!newsList.isEmpty()) {
            newsRecyclerAdapter = new NewsAdapter(getActivity(),newsList);
            newsRecyclerAdapter.notifyDataSetChanged();
            newsRecycler.setAdapter(newsRecyclerAdapter);
        }

    }

}
