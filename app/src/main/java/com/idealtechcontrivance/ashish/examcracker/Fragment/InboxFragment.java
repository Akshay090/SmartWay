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
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Helper.SessionManager;
import com.idealtechcontrivance.ashish.examcracker.Model.Inbox;
import com.idealtechcontrivance.ashish.examcracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment{

    private View view;
    private ProgressDialog progressDialog;

    ArrayList<Inbox> inboxList;

    RecyclerView inboxRecycler;
    RecyclerView.LayoutManager inboxLayoutManager;
    RecyclerView.Adapter  inboxRecyclerAdapter;

    FloatingActionButton fabComposeMessage;
    ProgressBar progressBar;

    RequestQueue requestQueue;
    JsonArrayRequest jsonArrayRequest ;

    SessionManager session;

    String email;

    public InboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_inbox, container, false);
        getActivity().setTitle("Inbox");

        initViews();
        initListeners();
        initObjects();

        HashMap<String, String> user = session.getUserDetails();
        email= user.get(SessionManager.KEY_EMAIL);

        if (Common.isConnectedToInternet(getContext()))
        {
            loadInbox();
        }
        else {
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
        }

        fabComposeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudentEnquiryFragment();
            }
        });

        return view;
    }

    private void addStudentEnquiryFragment() {
        StudentEnquiryFragment studentEnquiryFragment = new StudentEnquiryFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(new InboxFragment(),"InboxFragment")
                .addToBackStack("InboxFragment")
                .replace(R.id.mainContainer, studentEnquiryFragment)
                .commit();
    }

    private void initViews() {
        fabComposeMessage = (FloatingActionButton) view.findViewById(R.id.fabComposeMessage);
        inboxRecycler = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    private void initListeners() {

    }

    private void initObjects() {
        session = new SessionManager(getApplicationContext());
        inboxList = new ArrayList<>();
    }

    private void loadInbox() {
        jsonArrayRequest = new JsonArrayRequest(Common.tbl_composemail+ "?email=" + email,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //progressBar.setVisibility(View.GONE);

                        JSON_PARSE_DATA_AFTER_WEBCALL_INBOX(response);
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

    private void JSON_PARSE_DATA_AFTER_WEBCALL_INBOX(JSONArray response) {
        for(int i = 0; i<response.length(); i++) {
            Inbox inbox = new Inbox();
            JSONObject json = null;

            try {
                json = response.getJSONObject(i);

                inbox.setImage(json.getString("sender_image"));
                inbox.setName(json.getString("sender_name"));
                inbox.setSubject(json.getString("subject"));
                inbox.setTime(json.getString("created_by"));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            inboxList.add(inbox);
        }
        inboxLayoutManager = new LinearLayoutManager(getActivity());
        inboxRecycler.setHasFixedSize(true);
        inboxRecycler.setLayoutManager(inboxLayoutManager);


        if (!inboxList.isEmpty()){
            inboxRecyclerAdapter = new InboxAdapter(getActivity(),inboxList);
            inboxRecyclerAdapter.notifyDataSetChanged();
            inboxRecycler.setAdapter(inboxRecyclerAdapter);
        }
    }

}
