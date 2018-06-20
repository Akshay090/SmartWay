package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.idealtechcontrivance.ashish.examcracker.Activity.MainActivity;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Helper.SessionManager;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestimonialFragment extends Fragment {

    View view;
    ScrollView  scrollView;

    EditText edtName, edtEmail, edtExamCleared, edtCourses, edtTestimonial;
    String profile_pic, name, email, examcleared, course, testimonial;
    Button btnSubmit;

    RequestQueue requestQueue;
    SessionManager session;

    private ProgressDialog progressDialog;

    public TestimonialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_testimonial, container, false);
        getActivity().setTitle("Testimonial");

        initViews();
        initObjects();

        HashMap<String, String> user = session.getUserDetails();
        //strUserName = user.get(SessionManager.KEY_NAME);
        //strUserEmail= user.get(SessionManager.KEY_EMAIL);
        profile_pic = user.get(SessionManager.KEY_PROFILE_PIC);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getContext()))
                {
                    sendTestimonial();
                }
                else {
                    Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void sendTestimonial() {
        name = edtName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        examcleared = edtExamCleared.getText().toString().trim();
        course = edtCourses.getText().toString().trim();
        testimonial = edtTestimonial.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || examcleared.isEmpty() || course.isEmpty() || testimonial.isEmpty()){
            Snackbar.make(scrollView,"Please enter valid name",Snackbar.LENGTH_LONG).show();
        }
        else if (!(Pattern.matches("^[\\p{L} .'-]+$", name))) {
            edtName.setError("Please Enter Valid Full Name");
            Snackbar.make(scrollView,"Please enter valid name",Snackbar.LENGTH_LONG).show();
        }
        else {
            StringRequest request = new StringRequest(Request.Method.POST, Common.testimonial_tbl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String code =jsonObject.getString("code");
                        String message =jsonObject.getString("message");

                        emptyInputEditText();

                        if (code.equals("add_failed")){
                            Snackbar.make(scrollView,message+"", Snackbar.LENGTH_LONG).show();
                        }
                        hideProgressDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgressDialog();
                    Snackbar.make(scrollView,error+"", Snackbar.LENGTH_LONG).show();
                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> parameters = new HashMap<String, String>();

                    parameters.put("name",name);
                    parameters.put("email",email);
                    parameters.put("examcleared",examcleared);
                    parameters.put("course",course);
                    parameters.put("testimonial",testimonial);
                    parameters.put("profile_pic",profile_pic);

                    return parameters;
                }
            };
            MySingleton.getInstance(getContext()).addToRequestque(request);

        }
    }

    private void initObjects() {
        requestQueue = Volley.newRequestQueue(getActivity());
        session = new SessionManager(getApplicationContext());
    }

    private void initViews() {
        scrollView = (ScrollView)view.findViewById(R.id.scrollView);
        edtName = (EditText) view.findViewById(R.id.edtName);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtExamCleared = (EditText) view.findViewById(R.id.edtExamCleared);
        edtCourses = (EditText) view.findViewById(R.id.edtCourses);
        edtTestimonial = (EditText) view.findViewById(R.id.edtTestimonial);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
    }

    private void emptyInputEditText(){
        edtName.setText(null);
        edtEmail.setText(null);
        edtExamCleared.setText(null);
        edtCourses.setText(null);
        edtTestimonial.setText(null);
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.app_name));
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

}
