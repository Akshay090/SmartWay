package com.idealtechcontrivance.ashish.examcracker.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idealtechcontrivance.ashish.examcracker.Model.Inbox;
import com.idealtechcontrivance.ashish.examcracker.Model.News;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<News> newsList;

    public NewsAdapter(Context context, ArrayList<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news, parent, false);
        NewsAdapter.ViewHolder viewHolder = new NewsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {

        News news =  newsList.get(position);

        holder.txtNewsName.setText(news.getNewsName());
        holder.txtNewsDescription.setText(news.getNewsDescription());
        holder.txtStartDate.setText(news.getStartDate());

        if (position %2 == 1){
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtNewsName;
        TextView txtNewsDescription;
        TextView txtStartDate;

        ViewHolder(View itemView) {
            super(itemView);
            txtNewsName = (TextView) itemView.findViewById(R.id.txtNewsName);
            txtNewsDescription = (TextView) itemView.findViewById(R.id.txtNewsDescription) ;
            txtStartDate = (TextView) itemView.findViewById(R.id.txtStartDate);
        }

    }
}

