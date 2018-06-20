package com.idealtechcontrivance.ashish.examcracker.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idealtechcontrivance.ashish.examcracker.R;

public class BookmarkFragment extends Fragment {

    private View view;
    private ProgressDialog progressDialog;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        getActivity().setTitle("Bookmark");

        return view;
    }
}
