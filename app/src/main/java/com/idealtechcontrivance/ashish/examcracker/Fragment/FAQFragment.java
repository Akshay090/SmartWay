package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.idealtechcontrivance.ashish.examcracker.Adapter.FAQAdapter;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Model.FAQ;
import com.idealtechcontrivance.ashish.examcracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFragment extends Fragment {

    private View view;
    private ProgressDialog progressDialog;

    private List<FAQ> faqList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter viewadapter;

    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;


    public FAQFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_faq, container, false);
        getActivity().setTitle("FAQ");

        initViews();
        initListeners();
        initObjects();

        if (Common.isConnectedToInternet(getContext()))
        {
            loadFAQ();
        }
        else {
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void initListeners() {
    }

    private void initObjects() {
        faqList = new ArrayList<>();
    }

    private void initViews() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    private void loadFAQ() {
        jsonArrayRequest = new JsonArrayRequest(Common.faq_question_tbl,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //progressBar.setVisibility(View.GONE);

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
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

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {
            FAQ faq = new FAQ();
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                faq.setQuestion(json.getString("question"));
                faq.setAnswer(json.getString("answer"));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            faqList.add(faq);
        }
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        viewadapter = new FAQAdapter(faqList,getActivity());
        recyclerView.setAdapter(viewadapter);
    }

}
