package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class OurOfficeFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    TextView txtCityName, txtPhoneNo, txtEmailId;

    GoogleMap mMap;
    SupportMapFragment mapFragment;

    private ProgressDialog progressDialog;

    public OurOfficeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_our_office, container, false);

        initViews();

        if (Common.isConnectedToInternet(getContext()))
        {
            if (mapFragment == null){
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                mapFragment = SupportMapFragment.newInstance();
                ft.replace(R.id.map,mapFragment).commit();
            }
            mapFragment.getMapAsync(this);

            loadContactInfo();
        }
        else {
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void initViews() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        txtCityName = (TextView) view.findViewById(R.id.txtCityName);
        txtPhoneNo = (TextView) view.findViewById(R.id.txtPhoneNo);
        txtEmailId = (TextView) view.findViewById(R.id.txtEmailId);
    }

    private void loadContactInfo() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Common.tbl_our_office,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    txtCityName.setText(response.getString("address"));
                    txtPhoneNo.setText(response.getString("contactno"));
                    txtEmailId.setText(response.getString("contactemail"));

                    //PD.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //PD.dismiss();
                Toast.makeText(getActivity(),"Something went wrong...", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(getActivity()).addToRequestque(jsonObjectRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng allahabad = new LatLng(25.4358, 81.8463);
        mMap.addMarker(new MarkerOptions().position(allahabad).title("Allahabad"));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(allahabad, zoomLevel));
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
