package com.idealtechcontrivance.ashish.examcracker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idealtechcontrivance.ashish.examcracker.Model.Features;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.ViewHolder> {

    Context context;
    List<Features> featuresList;

    public FeaturesAdapter(List<Features> featuresList, Context context){
        super();

        this.featuresList = featuresList;
        this.context = context;
    }

    @Override
    public FeaturesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_features, parent, false);
        FeaturesAdapter.ViewHolder viewHolder = new FeaturesAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Features features =  featuresList.get(position);

        Picasso.with(context).load(features.getImage()).fit().into(holder.imgProfile);
        holder.txtTitle.setText(features.getTitle());
        holder.txtListing.setText(features.getListing());

    }

    @Override
    public int getItemCount() {
        return featuresList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imgProfile;
        public TextView txtTitle;
        public TextView txtListing;


        public ViewHolder(View itemView) {
            super(itemView);

            imgProfile = (CircleImageView) itemView.findViewById(R.id.imgProfile);
            txtTitle = (TextView) itemView.findViewById(R.id.txtListTitle) ;
            txtListing = (TextView) itemView.findViewById(R.id.txtListing);

        }

    }
}