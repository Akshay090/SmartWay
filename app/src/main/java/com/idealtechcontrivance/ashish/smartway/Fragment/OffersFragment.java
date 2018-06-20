package com.idealtechcontrivance.ashish.smartway.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.idealtechcontrivance.ashish.smartway.Activity.OfferDetailActivity;
import com.idealtechcontrivance.ashish.smartway.Common.Common;
import com.idealtechcontrivance.ashish.smartway.Model.Offer;
import com.idealtechcontrivance.ashish.smartway.R;
import com.idealtechcontrivance.ashish.smartway.ViewHolder.OfferViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OffersFragment extends Fragment {

    SwipeRefreshLayout refreshLayout;
    FirebaseDatabase database;
    DatabaseReference offerReference;
    RecyclerView offerRecyclerView;
    private FirebaseRecyclerAdapter<Offer, OfferViewHolder>  offerAdapter;
    String categoryId = "";

    public OffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_offers, container, false);

        final Bundle bundle = this.getArguments();

        refreshLayout = rootView.findViewById(R.id.swipeToRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Get Intent here
                if (bundle != null) {
                    categoryId = bundle.getString("categoryId");

                    if (!categoryId.isEmpty() && categoryId!=null) {

                        if (Common.isConnectedToInternet(getContext()))
                            loadOffer(categoryId);

                        else {
                            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }
        });

        //Default load for first time
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //Get Intent here
                if (bundle != null) {
                    categoryId = bundle.getString("categoryId");

                    if (!categoryId.isEmpty() && categoryId != null) {
                        if (Common.isConnectedToInternet(getContext()))
                            loadOffer(categoryId);
                        else {
                            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }
        });

        //INITIALIZE FB
        database = FirebaseDatabase.getInstance();
        offerReference = database.getReference(Common.offer_tbl);

        //INITIALIZE RV
        offerRecyclerView = rootView.findViewById(R.id.offerRecyclerView);
        //offerRecyclerView.setHasFixedSize(true);
        offerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    private void loadOffer(String categoryId) {
        //Create query by name
        Query query = offerReference.orderByChild("categoryId").equalTo(categoryId);  //Compare Name

        //Create option with query
        FirebaseRecyclerOptions<Offer> options = new FirebaseRecyclerOptions.Builder<Offer>().setQuery(query, Offer.class).build();
        offerAdapter = new FirebaseRecyclerAdapter<Offer, OfferViewHolder>(options) {
            @Override
            protected void onBindViewHolder(OfferViewHolder holder, final int position, final Offer model) {
                holder.setShopName(model.getShopName());
                holder.setOfferDiscount(model.getOfferDiscount());
                holder.setOfferName(model.getOfferName());
                //holder.offerRating.setText(o.getOfferRatting());
                holder.setGotDiscount(model.getGotDiscount());
                holder.setShopImage(getContext(), model.getShopImage());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String url = offerAdapter.getRef(position).getKey();
                        Intent intent = new Intent(getContext(),OfferDetailActivity.class);
                        intent.putExtra("offerId",url);    //Send food id to new activity
                        startActivity(intent);
                    }
                });
            }

            @Override
            public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_offer, parent, false);
                return new OfferViewHolder(view);
            }
        };
        offerAdapter.notifyDataSetChanged();     //Refresh data if data have changed
        offerAdapter.startListening();
        offerRecyclerView.setAdapter(offerAdapter);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        offerAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (offerAdapter!=null)
            offerAdapter.startListening();
    }
}
