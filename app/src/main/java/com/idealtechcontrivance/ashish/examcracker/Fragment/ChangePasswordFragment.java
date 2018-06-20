package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.idealtechcontrivance.ashish.examcracker.Activity.DashboardActivity;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Helper.SessionManager;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {

    private View view;
    private ScrollView scrollView;

    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    String  name, profile_pic, username, email, password, oldpassword, newpassword, confirmpassword, iusername, iemail, ipassword;
    private Button btnChange;

    private SessionManager session;
    RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        getActivity().setTitle("Change Password");

        initViews();
        initListeners();
        initObjects();
        loadSession();

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getContext()))
                {
                    changePassword();
                }
                else {
                    Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void loadSession() {
        HashMap<String, String> user = session.getUserDetails();
        iusername = user.get(SessionManager.KEY_USERNAME);
        ipassword = user.get(SessionManager.KEY_PASSWORD);
    }

    private void initListeners() {

    }

    private void initObjects() {
        session = new SessionManager(getApplicationContext());
        requestQueue = Volley.newRequestQueue(getActivity());
    }

    private void initViews() {
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        edtOldPassword = (EditText) view.findViewById(R.id.edtOldPassword);
        edtNewPassword = (EditText) view.findViewById(R.id.edtNewPassword);
        edtConfirmPassword = (EditText) view.findViewById(R.id.edtConfirmPassword);
        btnChange= (Button) view.findViewById(R.id.btnChangePassword);
    }

    private void changePassword() {
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        oldpassword = edtOldPassword.getText().toString().trim();
        newpassword = edtNewPassword.getText().toString().trim();
        confirmpassword = edtConfirmPassword.getText().toString().trim();

        if (oldpassword.equalsIgnoreCase("") || newpassword.equalsIgnoreCase("") || confirmpassword.equalsIgnoreCase("") ) {
            Snackbar.make(scrollView,"Please Fill All The Field", Snackbar.LENGTH_LONG).show();
        }
        else if(!ipassword.equalsIgnoreCase(oldpassword)) {
            Snackbar.make(scrollView,"Incorrect Current Password" ,Snackbar.LENGTH_LONG).show();
        }
        else if (!newpassword.equalsIgnoreCase(confirmpassword)) {
            Snackbar.make(scrollView,"Password Not Match" ,Snackbar.LENGTH_LONG).show();
        }
        else if(newpassword.length() < 8 || newpassword.length() > 20) {
            Snackbar.make(scrollView,"Please Enter Valid Password", Snackbar.LENGTH_LONG).show();
        }
        else {
            showProgressDialog();

            StringRequest request = new StringRequest(Request.Method.POST, Common.change_password_tbl+"?username="+iusername, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String code =jsonObject.getString("code");
                        String message =jsonObject.getString("message");

                        if (code.equals("change_success")) {
                            Toast.makeText(getActivity(),message+"", Toast.LENGTH_LONG).show();

                            session.createLoginSession(name, profile_pic, iusername, email, newpassword);

                            Intent intent = new Intent(getActivity(),DashboardActivity.class);
                            startActivity(intent);
                        }
                        else if (code.equals("change_failed")){
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
                    progressDialog.dismiss();
                    Snackbar.make(scrollView,error+"", Snackbar.LENGTH_LONG).show();
                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> parameters = new HashMap<String, String>();

                    parameters.put("password",newpassword);

                    return parameters;
                }
            };
            MySingleton.getInstance(getContext()).addToRequestque(request);
        }
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
