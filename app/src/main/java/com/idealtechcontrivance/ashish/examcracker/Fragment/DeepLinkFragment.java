package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteReferral;
import com.idealtechcontrivance.ashish.examcracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeepLinkFragment extends Fragment implements
        View.OnClickListener {

    View view;
    private static final String TAG = DeepLinkFragment.class.getSimpleName();


    public DeepLinkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_deep_link, container, false);

        // Button click listener
        view.findViewById(R.id.button_ok).setOnClickListener(this);

        return view;
    }

    // [START deep_link_on_start]
    @Override
    public void onStart() {
        super.onStart();

        // Check if the intent contains an AppInvite and then process the referral information.
        Intent intent = getActivity().getIntent();
        if (AppInviteReferral.hasReferral(intent)) {
            processReferralIntent(intent);
        }
    }
    // [END deep_link_on_start]

    // [START process_referral_intent]
    private void processReferralIntent(Intent intent) {
        // Extract referral information from the intent
        String invitationId = AppInviteReferral.getInvitationId(intent);
        String deepLink = AppInviteReferral.getDeepLink(intent);

        // Display referral information
        // [START_EXCLUDE]
        Log.d(TAG, "Found Referral: " + invitationId + ":" + deepLink);
        ((TextView) view.findViewById(R.id.deep_link_text))
                .setText(getString(R.string.deep_link_fmt, deepLink));
        ((TextView) view.findViewById(R.id.invitation_id_text))
                .setText(getString(R.string.invitation_id_fmt, invitationId));
        // [END_EXCLUDE]
    }
    // [END process_referral_intent]


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                getActivity().finish();
                break;
        }
    }

}
