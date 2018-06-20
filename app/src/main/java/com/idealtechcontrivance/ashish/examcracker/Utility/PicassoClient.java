package com.idealtechcontrivance.ashish.examcracker.Utility;

import android.content.Context;
import android.widget.ImageView;

import com.idealtechcontrivance.ashish.examcracker.R;
import com.squareup.picasso.Picasso;

/**
 * Created by ashish on 20-02-2017.
 */

public class PicassoClient
{
    public static void downloadImage(Context context, String imageUrl, ImageView imageView)
    {
        if (imageUrl.length()>0 && imageUrl!=null){
           Picasso.with(context).load(imageUrl).placeholder(R.drawable.logo).into(imageView);
        }
        else
        {
            Picasso.with(context).load(R.drawable.logo).into(imageView);
        }
    }
}
