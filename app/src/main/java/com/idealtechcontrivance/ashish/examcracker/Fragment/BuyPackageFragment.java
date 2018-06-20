package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.google.android.gms.common.api.GoogleApiClient;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Helper.SessionManager;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyPackageFragment extends Fragment {

    public boolean codes = false;
    private View view;
    private Bundle bundle;

    EditText edtCouponCode;
    TextView txtSeriesName,txtTotal,txtDiscount,txtGrandTotal, btnCoupon,btnPayment;
    String code,strId,strCategory,strSeriesName,strTotal,strTotalTest,strExaminationTest,strPracticeTest,strSectionalTest,strDiscount,strGrandTotal;
    int grandTotal = 0,total = 0,discount = 0;

    InstapayListener listener;
    ScrollView scrollView;
    GoogleApiClient googleApiClient;

    SharedPreferences sharedPref;
    private String names, username, emails, mobiles, tot;
    private SessionManager session;

    public BuyPackageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buy_package, container, false);
        getActivity().setTitle("Confirm Your Order");

        initViews();
        initListeners();
        initObjects();
        loadBundle();
        loadSession();

        if (username != null){
            loadProfile();
        }
        else {
            loadSession();
        }

        btnCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showApplyCouponDialog();
            }
        });

        txtSeriesName.setText(strSeriesName);
        txtTotal.setText(strTotal);
        txtGrandTotal.setText(strTotal);

        sharedPref = getApplicationContext().getSharedPreferences("GrandTotal",0);
        tot = sharedPref.getString("grand_total",strTotal);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.isLoggedIn() || googleApiClient != null && googleApiClient.isConnected() || AccessToken.getCurrentAccessToken() != null){

                    if (names == null || emails == null || mobiles == null){
                        showBillingInfo();
                        Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (codes == true){
                            callInstamojoPay(emails, mobiles, tot, strSeriesName, names);
                            sharedPref = getActivity().getSharedPreferences("GrandTotal",0);
                            SharedPreferences.Editor prefEditor = sharedPref.edit();
                            prefEditor.clear();
                            prefEditor.apply();
                        }
                        else {
                            callInstamojoPay(emails, mobiles, strTotal, strSeriesName, names);
                            sharedPref = getActivity().getSharedPreferences("GrandTotal",0);
                            SharedPreferences.Editor prefEditor = sharedPref.edit();
                            prefEditor.clear();
                            prefEditor.apply();
                        }
                    }
                }
                else {
                    SignInFragment signInFragment = new SignInFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(new BuyPackageFragment(),"BuyPackageFragment")
                            .addToBackStack("BuyPackageFragment")
                            .replace(R.id.mainContainer, signInFragment)
                            .commit();
                }

            }
        });
        return view;
    }


    private void showBillingInfo() {
        try {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("ENTER BILLING INFORMATION");
        alertDialog.setMessage("Please enter your billing information");

        LayoutInflater inflater =LayoutInflater.from(getActivity());
        View layout_billing_info = inflater.inflate(R.layout.layout_billing_info,null);

        final EditText edtName = (EditText) layout_billing_info.findViewById(R.id.edtName);
        final EditText edtEmail = (EditText) layout_billing_info.findViewById(R.id.edtEmail);
        final EditText edtPhone = (EditText) layout_billing_info.findViewById(R.id.edtPhone);

        edtName.setText(names);
        edtEmail.setText(emails);
        edtPhone.setText(mobiles);

        alertDialog.setView(layout_billing_info);

        alertDialog.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                names = edtName.getText().toString().trim();
                emails = edtEmail.getText().toString().trim();
                mobiles = edtPhone.getText().toString().trim();

                if (names.isEmpty()){
                    Toast.makeText(getActivity(),"Please enter valid email address", Snackbar.LENGTH_LONG).show();
                }
                 else if(!Patterns.EMAIL_ADDRESS.matcher(emails).matches()){
                    Toast.makeText(getActivity(),"Please enter valid email address", Snackbar.LENGTH_LONG).show();
                }
                else if(!Patterns.PHONE.matcher(mobiles).matches() && mobiles.length()!=10){
                    Toast.makeText(getActivity(),"Please enter valid mobile number", Snackbar.LENGTH_LONG).show();
                } else {
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("StudentProfile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("NAME",names);
                    editor.putString("EMAIL",emails);
                    editor.putString("MOBILE",mobiles);
                    editor.apply();
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

    private void loadSession() {
        HashMap<String, String> user = session.getUserDetails();
        username = user.get(SessionManager.KEY_USERNAME);
        names = user.get(SessionManager.KEY_NAME);
        emails= user.get(SessionManager.KEY_EMAIL);
    }

    private void loadProfile() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("StudentProfile", Context.MODE_PRIVATE);
        names = (preferences.getString("NAME",null));
        emails = (preferences.getString("EMAIL",null));
        mobiles = (preferences.getString("MOBILE",null));
    }

    private void showApplyCouponDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("ENTER COUPON CODE");
        alertDialog.setMessage("Please enter valid coupon code");

        LayoutInflater inflater =LayoutInflater.from(getActivity());
        View coupon_code_layout = inflater.inflate(R.layout.layout_apply_coupon,null);

        edtCouponCode = (EditText) coupon_code_layout.findViewById(R.id.edtCouponCode);
        alertDialog.setView(coupon_code_layout);

        alertDialog.setPositiveButton("APPLY", new DialogInterface.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                try {
                    InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                code = edtCouponCode.getText().toString().trim();

                if (code.isEmpty()) {
                    Snackbar.make(scrollView, "Please enter valid coupon code", Snackbar.LENGTH_LONG).show();
                } else {
                   checkCouponCode();
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

    private void checkCouponCode() {
        //showProgressDialog();
        StringRequest request = new StringRequest(Request.Method.POST, Common.apply_coupon, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    strDiscount = jsonObject.getString("value");

                    if (code.equals("change_failed")) {
                        codes = false;
                        Snackbar.make(scrollView, message + "", Snackbar.LENGTH_SHORT).show();
                    } else if (code.equals("change_success")) {
                        codes = true;

                        total = Integer.parseInt(strTotal);
                        txtDiscount.setText(strDiscount);
                        discount = Integer.valueOf(strDiscount);
                        grandTotal = total-discount;
                        strGrandTotal = String.valueOf(grandTotal);
                        txtGrandTotal.setText(strGrandTotal);

                        sharedPref = getApplicationContext().getSharedPreferences("GrandTotal",0);
                        SharedPreferences.Editor prefEditor = sharedPref.edit();
                        prefEditor.putString("grand_total", strGrandTotal);
                        prefEditor.apply();

                        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
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
                parameters.put("coupon_code", code);
                return parameters;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestque(request);
    }

    private void initObjects() {
        session = new SessionManager(getApplicationContext());
    }

    private void loadBundle() {
        if (bundle != null){
            strId = bundle.getString("ID_KEY");
            strCategory = bundle.getString("CATEGORY_KEY");
            strSeriesName = bundle.getString("SERIES_NAME_KEY");
            strTotal = bundle.getString("COST_KEY");
        }
    }

    private void initListeners() {
        bundle = this.getArguments();
    }

    private void initViews() {
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        txtSeriesName = (TextView) view.findViewById(R.id.txtSeriesName);
        txtTotal = (TextView) view.findViewById(R.id.txtTotal);
        txtDiscount = (TextView) view.findViewById(R.id.txtDiscount);
        txtGrandTotal = (TextView) view.findViewById(R.id.txtGrandTotal);
        btnCoupon = (TextView) view.findViewById(R.id.btnCoupon);
        btnPayment = (TextView) view.findViewById(R.id.btnPayment);
    }

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = getActivity();
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        getApplicationContext().registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                response = "status=success:orderId=a089f02724ed4a8db6c069f6d30b3245:txnId=None:paymentId=MOJO7918005A76494611:token=qyFwLidQ0aBNNWlsmwHx1gHFhlt6A1";
                String resArray[] = response.split(":");
                String orderId = resArray[1].substring(resArray[1].indexOf("=")+1);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG).show();
            }
        };
    }

}
