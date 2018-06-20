package com.idealtechcontrivance.ashish.smartway.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.idealtechcontrivance.ashish.smartway.R;
import com.squareup.picasso.Picasso;

public class OfferViewHolder extends RecyclerView.ViewHolder {

    public View mView;

    public OfferViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setShopName(String shopName){
        TextView txtShopName = (TextView) mView.findViewById(R.id.offerShopName);
        txtShopName.setText(shopName);
    }

    public void setOfferDiscount(String offerDiscount){
        TextView txtOfferDiscount = (TextView) mView.findViewById(R.id.offerDiscount);
        txtOfferDiscount.setText(offerDiscount);
    }

    public void setOfferName(String offerName){
        TextView txtOfferName = (TextView) mView.findViewById(R.id.offerName);
        txtOfferName.setText(offerName);
    }

    public void setGotDiscount(String gotDiscount){
        TextView txtGotDiscount = (TextView) mView.findViewById(R.id.offerGotUsers);
        txtGotDiscount.setText(gotDiscount);
    }

    public void setShopImage(Context context, String shopImage){
        ImageView imgShopImage = (ImageView) mView.findViewById(R.id.offerShopLogo);
        Picasso.with(context).load(shopImage).into(imgShopImage);
    }
}
