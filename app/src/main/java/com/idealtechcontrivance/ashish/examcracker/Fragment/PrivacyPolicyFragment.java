package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyPolicyFragment extends Fragment {

    WebView webView;

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        getActivity().setTitle("Privacy & Policy");

        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Common.privacy_policy);

        return webView;
    }

}
