package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.SendMail;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetInTouchFragment extends Fragment {

    private View view;
    private ScrollView scrollView;

    private EditText edtFirstName, edtLastName, edtEmailId, edtPhoneNo, edtMessage;
    String strFirstName, strLastName, strEmailId, strPhoneNo, strMessage;
    private Button btnSend;

    private ProgressDialog progressDialog;


    public GetInTouchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_get_in_touch, container, false);

        initViews();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getActivity()))
                {
                    sendMail();
                }
                else {
                    Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }

    private void sendMail() {
        strFirstName = edtFirstName.getText().toString().trim();
        strLastName = edtLastName.getText().toString().trim();
        strEmailId = edtEmailId.getText().toString().trim();
        strPhoneNo = edtPhoneNo.getText().toString().trim();
        strMessage = edtMessage.getText().toString().trim();

        if (!(Pattern.matches("^[\\p{L} .'-]+$", strFirstName))) {
            Snackbar.make(scrollView,"Please Enter Valid First Name",Snackbar.LENGTH_LONG).show();
        }
        else if (!(Pattern.matches("^[\\p{L} .'-]+$", strLastName))){
            Snackbar.make(scrollView,"Please Enter Valid Last  Name",Snackbar.LENGTH_LONG).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(strEmailId).matches()){
            Snackbar.make(scrollView,"Please Enter Valid Email Address",Snackbar.LENGTH_LONG).show();
        }
        else if(!Patterns.PHONE.matcher(strPhoneNo).matches() || strPhoneNo.length()!=10){
            Snackbar.make(scrollView,"Please Enter Valid Phone Number",Snackbar.LENGTH_LONG).show();
        }
        else if (strMessage.isEmpty() || strMessage.length() < 20){
            Snackbar.make(scrollView,"Please Input At Least 20 Character In Message Field",Snackbar.LENGTH_LONG).show();
        }
        else {
            String subject = strFirstName+" "+strLastName;
            String message = strPhoneNo+"\n\n"+strMessage;
            //Creating SendMail object
            SendMail sm = new SendMail(getActivity(), strEmailId, subject, message);

            //Executing sendmail to send email
            sm.execute();

            emptyInputEditText();
        }
    }

    private void initViews() {
        edtFirstName = (EditText) view.findViewById(R.id.edtFirstName);
        edtLastName = (EditText) view.findViewById(R.id.edtLastName);
        edtEmailId = (EditText) view.findViewById(R.id.edtEmailId);
        edtPhoneNo = (EditText) view.findViewById(R.id.edtPhoneNo);
        edtMessage = (EditText) view.findViewById(R.id.edtMessage);
        btnSend = (Button) view.findViewById(R.id.btnSend);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
    }

    private void emptyInputEditText(){
        edtFirstName.setText(null);
        edtLastName.setText(null);
        edtEmailId.setText(null);
        edtPhoneNo.setText(null);
        edtMessage.setText(null);
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
