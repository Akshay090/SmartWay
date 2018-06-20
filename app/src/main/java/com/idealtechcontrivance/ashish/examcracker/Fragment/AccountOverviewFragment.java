package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.google.android.gms.common.api.GoogleApiClient;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Helper.SessionManager;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.DatePickerFragment;
import com.idealtechcontrivance.ashish.examcracker.Utility.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountOverviewFragment extends Fragment {

    private View view;
    private ScrollView scrollView;

    private CircleImageView userImage;
    private EditText edtName,edtEmail,edtBirthday,edtPhone,edtQualification,edtAddress,edtCity,edtZipCode;
    private String states, genders, name, profile_pic, username, email, password, mobile, birthday, qualification, address, city, zipcode, gender, state;
    private Spinner spinnerState = null, spinnerGender = null;
    private Button btnSave;

    private ArrayAdapter<String> genderAdapter;
    private ArrayAdapter<String> stateAdapter;
    private ArrayList<String> stateName;
    String[] strGender;
    private List<String> genderList;

    private SessionManager session;
    private ProgressDialog progressDialog;

    private RequestQueue requestQueue;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    public AccountOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account_overview, container, false);

        initViews();
        initListeners();
        initObjects();
        loadGender();
        loadSession();

        if (username != null){
            loadProfile();
        }
        else {
            loadSession();
        }

        if (Common.isConnectedToInternet(getApplicationContext()))
        {
            loadSpinner(Common.state_tbl);
        }
        else {
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
        }

        if (genders != null){
            spinnerGender.setSelection(genderAdapter.getPosition(genders));
        }

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // First item will be gray
                if (position == 0) {
                    ((TextView) view).setTextColor(ActivityCompat.getColor(getApplicationContext(), R.color.colorEditTextHint));
                } else {
                    ((TextView) view).setTextColor(ActivityCompat.getColor(getApplicationContext(), R.color.colorBlack));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String state = spinnerState.getItemAtPosition(spinnerState.getSelectedItemPosition()).toString();
                // First item will be gray
                if (position == 0) {
                    ((TextView) view).setTextColor(ActivityCompat.getColor(getApplicationContext(), R.color.colorEditTextHint));
                } else {
                    ((TextView) view).setTextColor(ActivityCompat.getColor(getApplicationContext(), R.color.colorBlack));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getApplicationContext()))
                {
                    updateProfile();
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
        username = user.get(SessionManager.KEY_USERNAME);
        name = user.get(SessionManager.KEY_NAME);
        email= user.get(SessionManager.KEY_EMAIL);
        profile_pic = user.get(SessionManager.KEY_PROFILE_PIC);

        edtName.setText(name);
        edtEmail.setText(email);
        Picasso.with(getApplicationContext()).load(profile_pic).into(userImage);
    }

    private void loadProfile() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("StudentProfile", Context.MODE_PRIVATE);

        Picasso.with(getActivity()).load(preferences.getString("PROFILE_PIC",null)).fit().into(userImage);
        edtName.setText(preferences.getString("NAME",null));
        edtEmail.setText(preferences.getString("EMAIL",null));
        edtPhone.setText(preferences.getString("MOBILE",null));
        genders = preferences.getString("GENDER",null);
        edtQualification.setText(preferences.getString("QUALIFICATION",null));
        edtAddress.setText(preferences.getString("ADDRESS",null));
        states = preferences.getString("STATE",null);
        edtCity.setText(preferences.getString("CITY",null));
        edtZipCode.setText(preferences.getString("ZIPCODE",null));
    }

    private void loadGender() {
        genderAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, genderList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);
    }

    private void initViews() {
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        userImage = (CircleImageView) view.findViewById(R.id.imgImage);
        edtName = (EditText) view.findViewById(R.id.edtName);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtBirthday = (EditText) view.findViewById(R.id.edtBirthday);
        edtPhone = (EditText) view.findViewById(R.id.edtPhoneNo);
        spinnerGender = (Spinner) view.findViewById(R.id.spinnerGender);
        edtQualification = (EditText) view.findViewById(R.id.edtQualification);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);
        spinnerState = (Spinner) view.findViewById(R.id.spinnerState);
        edtCity = (EditText) view.findViewById(R.id.edtCity);
        edtZipCode = (EditText) view.findViewById(R.id.edtZipCode);
        btnSave = (Button) view.findViewById(R.id.btnSave);
    }

    private void initListeners() {

    }

    private void initObjects() {
        session = new SessionManager(getApplicationContext());
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        strGender = new String[]{"Gender","Male","Female"};
        genderList = new ArrayList<>(Arrays.asList(strGender));
        stateName = new ArrayList<>();
    }

    private void loadSpinner(String state_tbl) {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, state_tbl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("State");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String state=jsonObject1.getString("state");
                        stateName.add(state);
                    }
                    stateAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_profile_spinner, stateName);
                    spinnerState.setAdapter(stateAdapter);

                    if (states != null){
                        spinnerState.setSelection(stateAdapter.getPosition(states));
                    }
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bmp != null){
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void updateProfile() {
        profile_pic = getStringImage(bitmap);
        name = edtName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        birthday = edtBirthday.getText().toString().trim();
        mobile = edtPhone.getText().toString().trim();
        gender = spinnerGender.getSelectedItem().toString();
        qualification = edtQualification.getText().toString().trim();
        address = edtAddress.getText().toString().trim();
        state = spinnerState.getSelectedItem().toString();
        city = edtCity.getText().toString().trim();
        zipcode = edtZipCode.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || birthday.isEmpty() || mobile.isEmpty() || gender.isEmpty() || qualification.isEmpty() || address.isEmpty() || state.isEmpty() || city.isEmpty() || zipcode.isEmpty()){
            Snackbar.make(scrollView,"Please fill all the fields",Snackbar.LENGTH_LONG).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Snackbar.make(scrollView,"Please enter valid email",Snackbar.LENGTH_LONG).show();
        }
        else if(!Patterns.PHONE.matcher(mobile).matches() && mobile.length()!=10){
            Snackbar.make(scrollView,"Please enter valid mobile number",Snackbar.LENGTH_LONG).show();
        }
        else if (!(Pattern.matches("^[1-9][0-9]{5}$", zipcode)) && zipcode.length()!=6) {
            edtZipCode.setError("Please Enter Valid Zip Code");
            Snackbar.make(scrollView,"Please enter valid name",Snackbar.LENGTH_LONG).show();
        }
        else {
            //Showing the progress dialog
            final ProgressDialog loading = ProgressDialog.show(getActivity(),"Uploading...","Please wait...",false,false);

            StringRequest request = new StringRequest(Request.Method.POST, Common.change_profile+"?username=" + username, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");

                        if (code.equals("change_success")) {
                            //Disimissing the progress dialog
                            loading.dismiss();

                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("StudentProfile", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("PROFILE_PIC",profile_pic);
                            editor.putString("NAME",name);
                            editor.putString("EMAIL",email);
                            editor.putString("MOBILE",mobile);
                            editor.putString("GENDER",gender);
                            editor.putString("QUALIFICATION",qualification);
                            editor.putString("ADDRESS",address);
                            editor.putString("STATE",state);
                            editor.putString("CITY",city);
                            editor.putString("ZIPCODE",zipcode);
                            editor.apply();

                            Toast.makeText(getActivity(), message + "", Toast.LENGTH_LONG).show();
                        } else if (code.equals("change_failed")) {
                            //Disimissing the progress dialog
                            loading.dismiss();
                            Snackbar.make(scrollView, message + "", Snackbar.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Disimissing the progress dialog
                    loading.dismiss();
                    Snackbar.make(scrollView, error + "", Snackbar.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> parameters = new HashMap<String, String>();

                    parameters.put("profile_pic", profile_pic);
                    parameters.put("name", name);
                    parameters.put("email", email);
                    parameters.put("mobile", mobile);
                    parameters.put("gender", gender);
                    parameters.put("qualification", qualification);
                    parameters.put("address", address);
                    parameters.put("state", state);
                    parameters.put("city", city);
                    parameters.put("zipcode", zipcode);

                    return parameters;
                }
            };
            MySingleton.getInstance(getActivity()).addToRequestque(request);
        }
    }

    public void onViewCreated(View view, Bundle savesInstanceState){
        super.onViewCreated(view, savesInstanceState);
        edtBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    edtBirthday.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    showDatePicker();
                }
            }
        });
    }

    private void showDatePicker(){

        DatePickerFragment date = new DatePickerFragment();
        Calendar c = Calendar.getInstance();

        Bundle args = new Bundle();
        args.putInt("year",c.get(Calendar.YEAR));
        args.putInt("month",c.get(Calendar.MONTH)+1);
        args.putInt("day",c.get(Calendar.DAY_OF_MONTH));

        date.setArguments(args);

        date.setCallBack(ondate);
        date.show(getChildFragmentManager(),"Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String dob = String.valueOf(dayOfMonth) + "-" + String.valueOf(month) + "-" + String.valueOf(year);
            edtBirthday.setText(dob);
        }
    };

    /**
     * Start pick image activity with chooser.
     */
    public void onSelectImageClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
