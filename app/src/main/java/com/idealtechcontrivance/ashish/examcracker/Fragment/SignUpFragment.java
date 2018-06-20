package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private View view;
    private ScrollView scrollView;

    private CheckBox chkAgreeTo;
    private TextView txtAgreeTo;
    private String name, profile_pic, username, email, password, mobile;
    private EditText edtUserName, edtEmailId, edtPassword, edtPhoneNo;
    private Button btnSignUp,btnFB,btnGoogle;

    private LoginButton btnSignInFB;
    private CallbackManager callbackManager;

    private SignInButton btnSignInGoogle;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE =9001;
    private static final String TAG = "SignInActivity";

    RequestQueue requestQueue;
    private SessionManager session;

    private ProgressDialog progressDialog;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        getActivity().setTitle("Sign Up");

        initViews();
        initListeners();
        initObjects();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */,1, (GoogleApiClient.OnConnectionFailedListener) this /* OnConnectionFailedListener */)
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
                parameters.putString("fields", "id,name,email,gender, birthday");
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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getContext()))
                {
                    studentSignUp();
                }
                else {
                    Toast.makeText(getActivity(), "Please check your connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        txtAgreeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getActivity()))
                {
                    addTermsConditionFragment();
                }
                else {
                    Toast.makeText(getActivity(), "Please check your connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void addTermsConditionFragment() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("TERMS & CONDITIONS");
        alertDialog.setMessage("Please read our terms & condition carefully");

        WebView wv = new WebView(getActivity());
        wv.loadUrl(Common.terms_condition);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        alertDialog.setView(wv);
        alertDialog.show();

    }

    private void initObjects() {
        session = new SessionManager(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        requestQueue = Volley.newRequestQueue(getActivity());
    }

    private void initListeners() {
        btnGoogle.setOnClickListener(this);
    }

    private void initViews() {
        edtUserName = (EditText) view.findViewById(R.id.edtUserName);
        edtEmailId = (EditText) view.findViewById(R.id.edtEmailId);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        edtPhoneNo = (EditText) view.findViewById(R.id.edtPhoneNo);
        chkAgreeTo = (CheckBox) view.findViewById(R.id.chkAgreeTo);
        txtAgreeTo = (TextView) view.findViewById(R.id.txtAgreeTo);
        btnSignUp  = (Button) view.findViewById(R.id.btnSignUp);
        btnSignInGoogle = (SignInButton) view.findViewById(R.id.btnSignInGoogle);
        btnSignInFB = (LoginButton) view.findViewById(R.id.btnSignInFB);
        btnFB = (Button) view.findViewById(R.id.btnFb);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        btnGoogle = (Button) view.findViewById(R.id.btnGoogle);
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage((getActivity()));
        googleApiClient.disconnect();
    }

    private void studentSignUp() {
        try {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        username = edtUserName.getText().toString().trim();
        email = edtEmailId.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        mobile = edtPhoneNo.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || mobile.isEmpty()){
            Snackbar.make(scrollView,"Please fill all the fields", Snackbar.LENGTH_LONG).show();
        }
        else if (!(Pattern.matches("^[a-zA-Z0-9._-]{3,}$", username))) {
            Snackbar.make(scrollView,"Please enter valid username", Snackbar.LENGTH_LONG).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Snackbar.make(scrollView,"Please enter valid email address", Snackbar.LENGTH_LONG).show();
        }
        else if(password.length() < 8 || password.length() > 20) {
            Snackbar.make(scrollView,"Please enter valid password", Snackbar.LENGTH_LONG).show();
        }
        else if(!Patterns.PHONE.matcher(mobile).matches() && mobile.length()!=10){
            Snackbar.make(scrollView,"Please enter valid mobile number", Snackbar.LENGTH_LONG).show();
        }
        else if (!chkAgreeTo.isChecked()){
            Snackbar.make(scrollView,"Please Checked Terms & Condition", Snackbar.LENGTH_LONG).show();
        }
        else {
            showProgressDialog();

            StringRequest request = new StringRequest(Request.Method.POST, Common.sign_up_tbl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");

                        if (code.equals("add_failed")) {
                            Snackbar.make(scrollView,message+"", Snackbar.LENGTH_LONG).show();
                        }
                        else  if (code.equals("add_success")) {
                            Toast.makeText(getActivity(),message+"", Toast.LENGTH_LONG).show();

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
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> parameters = new HashMap<String, String>();

                    parameters.put("username", username);
                    parameters.put("email", email);
                    parameters.put("password", password);
                    parameters.put("mobile", mobile);

                    return parameters;
                }
            };
            MySingleton.getInstance(getActivity()).addToRequestque(request);
        }
    }

    private void setProfileToView(JSONObject jsonObject) {
        try {
            session.createLoginSession(name, profile_pic, username, email, password);

            name = (jsonObject.getString("name"));
            email = (jsonObject.getString("email"));
            profile_pic = "https://graph.facebook.com/" + jsonObject.getString("id")+ "/picture?type=large";

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
            getActivity().finish();
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
                Toast.makeText(getActivity(), "Please check your connection", Toast.LENGTH_LONG).show();
            }
        }
    }
}
