package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.idealtechcontrivance.ashish.examcracker.Activity.DashboardActivity;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Helper.SessionManager;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.MySingleton;
import com.idealtechcontrivance.ashish.examcracker.Utility.SendMail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    View view;
    ScrollView scrollView;

    EditText edtUserName, edtPassword, edtEmail;
    Button btnSignIn,btnFB,btnGoogle;
    TextView txtForgotPassword, txtLoginRegister;
    String name, profile_pic, username, email, password, mobile;

    private LoginButton btnSignInFB;
    private CallbackManager callbackManager;

    private SignInButton btnSignInGoogle;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE =9001;
    private static final String TAG = "SignInActivity";

    RequestQueue requestQueue;
    private SessionManager session;
    private ProgressDialog progressDialog;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        getActivity().setTitle("Sign In");

        initViews();
        initListeners();
        initObjects();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */,0, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnFb){
                    if (Common.isConnectedToInternet(getActivity()))
                    {
                        btnSignInFB.performClick();
                    }
                    else {
                        Toast.makeText(getActivity(), "Please check your connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnSignInFB.setReadPermissions(Arrays.asList("email","user_photos","public_profile","user_birthday","user_friends"));
        btnSignInFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                setProfileToView(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,birthday,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "error to Login Facebook", Toast.LENGTH_SHORT).show();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getActivity()))
                {
                    studentSignIn();
                }
                else {
                    Toast.makeText(getActivity(), "Please check your connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getApplicationContext()))
                {
                    showDialogForgotPwd();
                }
                else {
                    Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        txtLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSignUpFragment();
            }
        });

        return view;
    }

    private void addSignUpFragment() {
        SignUpFragment signUpFragment = new SignUpFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(new SignInFragment(),"SignInFragment")
                .addToBackStack("HomeFragment")
                .replace(R.id.mainContainer, signUpFragment)
                .commit();
    }

    private void initObjects() {
        session = new SessionManager(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    private void initListeners() {
        btnGoogle.setOnClickListener(this);
    }

    private void initViews() {
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        edtUserName = (EditText) view.findViewById(R.id.edtUserName);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        txtForgotPassword = (TextView) view.findViewById(R.id.txtForgotPassword);
        txtLoginRegister = (TextView) view.findViewById(R.id.txtLoginRegister);
        btnSignIn = (Button) view.findViewById(R.id.btnSignIn);
        btnSignInFB = (LoginButton) view.findViewById(R.id.btnSignInFB);
        btnFB = (Button) view.findViewById(R.id.btnFb);
        btnSignInGoogle = (SignInButton) view.findViewById(R.id.btnSignInGoogle);
        btnGoogle = (Button) view.findViewById(R.id.btnGoogle);
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage((getActivity()));
        googleApiClient.disconnect();
    }

    private void showDialogForgotPwd() {
        try {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("FORGOT PASSWORD");
        alertDialog.setMessage("Please enter your email address");

        LayoutInflater inflater =LayoutInflater.from(getActivity());
        View forgot_pwd_layout = inflater.inflate(R.layout.layout_forgot_password,null);

        edtEmail = (EditText) forgot_pwd_layout.findViewById(R.id.edtEmailAddress);
        alertDialog.setView(forgot_pwd_layout);

        alertDialog.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                final String emails = edtEmail.getText().toString().trim();

                if (emails.isEmpty()) {
                    Snackbar.make(scrollView, "Please enter your email", Snackbar.LENGTH_LONG).show();
                } else {
                    //showProgressDialog();
                    StringRequest request = new StringRequest(Request.Method.POST, Common.forgot_password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");

                                if (code.equals("login_failed")) {
                                    Snackbar.make(scrollView, message + "", Snackbar.LENGTH_LONG).show();
                                } else if (code.equals("login_success")) {
                                    Snackbar.make(scrollView, message + "", Snackbar.LENGTH_LONG).show();
                                }
                               // hideProgressDialog();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //hideProgressDialog();
                            Snackbar.make(scrollView, error + "", Snackbar.LENGTH_LONG).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("email", emails);
                            return parameters;
                        }
                    };
                    MySingleton.getInstance(getActivity()).addToRequestque(request);
                }
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void studentSignIn() {
        try {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        username = edtUserName.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()){
            Snackbar.make(scrollView,"Please enter your username and password", Snackbar.LENGTH_LONG).show();
        }
        else if (!(Pattern.matches("^[a-zA-Z0-9._-]{3,}$", username))) {
            Snackbar.make(scrollView,"Please enter valid username", Snackbar.LENGTH_LONG).show();
        }
        else if(password.length() < 8 || password.length() > 20) {
            Snackbar.make(scrollView,"Please enter valid password", Snackbar.LENGTH_LONG).show();
        }
        else {
            showProgressDialog();

            StringRequest request = new StringRequest(Request.Method.POST, Common.sign_in_tbl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String code =jsonObject.getString("code");
                        String message = jsonObject.getString("message");

                        if (code.equals("login_failed"))
                        {
                            Snackbar.make(scrollView,message+"", Snackbar.LENGTH_LONG).show();
                        }
                        else if (code.equals("login_success")) {
                            session.createLoginSession(name, profile_pic, username, email, password);

                            Intent intent = new Intent(getActivity(),DashboardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);
                            getActivity().finish();
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

                    Map<String,String> parameters = new HashMap<String, String>();

                    parameters.put("username",username);
                    parameters.put("password",password);
                    return parameters;
                }
            };
            MySingleton.getInstance(getActivity()).addToRequestque(request);
        }
    }

    private void setProfileToView(JSONObject jsonObject) {
        try {
            name = (jsonObject.getString("name"));
            email = (jsonObject.getString("email"));
            profile_pic = "https://graph.facebook.com/" + jsonObject.getString("id")+ "/picture?type=large";

            session.createLoginSession(name, profile_pic, username, email, password);

            Intent intent = new Intent(getActivity(),DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            getActivity().finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

    public void disconnectFromGoogle() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        disconnectFromFacebook();
        //disconnectFromGoogle();
    }

    public void handleResult(GoogleSignInResult result){

        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();

            assert account != null;
            name = account.getDisplayName();
            email = account.getEmail();
            profile_pic = account.getPhotoUrl().toString();

            session.createLoginSession(name, profile_pic, username, email, password);
            updateUI(true);
        }
        else {
            updateUI(false);
        }
    }

    public void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    public void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
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

    public void updateUI(boolean isLogin){
        if (isLogin){
            Intent intent = new Intent(getActivity(),DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnGoogle){
            if (Common.isConnectedToInternet(getApplicationContext()))
            {
                if (googleApiClient.isConnected()){
                    googleApiClient.disconnect();
                    googleApiClient.connect();
                }
                signIn();
            }
            else {
                Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);

        if (requestCode == REQ_CODE){
            GoogleSignInResult result  = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleResult(result);
        }
    }
}
