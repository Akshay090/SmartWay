package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.content.Context;
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

import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentEnquiryFragment extends Fragment {

    View view;
    ScrollView scrollView;

    EditText edtTo, edtSubject,edtDescription;
    String strTo, strSubject,strDescription;
    Button btnSend, btnReset;

    public StudentEnquiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_enquiry, container, false);
        getActivity().setTitle("Student Enquiry");

        initViews();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if (Common.isConnectedToInternet(getContext()))
                {
                    sendInquiry();
                }
                else {
                    Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyInputEditText();
            }
        });

        return view;
    }

    private void initObjects() {
    }

    private void initViews() {
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        edtTo = (EditText) view.findViewById(R.id.edtTo);
        edtSubject = (EditText) view.findViewById(R.id.edtSubject);
        edtDescription = (EditText) view.findViewById(R.id.edtDescription);

        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnReset = (Button) view.findViewById(R.id.btnReset);
    }

    private void sendInquiry() {
        strTo = edtTo.getText().toString().trim();
        strSubject = edtSubject.getText().toString().trim();
        strDescription = edtDescription.getText().toString().trim();

        if (strTo.isEmpty()){
            Snackbar.make(scrollView,"Please Fill In This Field", Snackbar.LENGTH_LONG).show();
        }
        else if (strSubject.isEmpty()){
            Snackbar.make(scrollView,"Please Fill In This Field", Snackbar.LENGTH_LONG).show();
        }
        else if (strDescription.isEmpty()){
            Snackbar.make(scrollView,"Please Fill In This Field", Snackbar.LENGTH_LONG).show();
        }
        else {

        }
    }

    private void emptyInputEditText(){
        edtTo.setText(null);
        edtSubject.setText(null);
        edtDescription.setText(null);
    }

}
