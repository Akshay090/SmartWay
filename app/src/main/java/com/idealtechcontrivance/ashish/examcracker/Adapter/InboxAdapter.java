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
import com.idealtechcontrivance.ashish.examcracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    private Context context;
    public ArrayList<Inbox> inboxList;

    public InboxAdapter(Context context, ArrayList<Inbox> inboxList) {
        this.context = context;
        this.inboxList = inboxList;
    }

    @Override
    public InboxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_inbox, parent, false);
        InboxAdapter.ViewHolder viewHolder = new InboxAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InboxAdapter.ViewHolder holder, int position) {

        Inbox inbox =  inboxList.get(position);

        Picasso.with(context).load(inbox.getImage()).fit().into(holder.imgSenderProfile);
        holder.txtSenderName.setText(inbox.getName());
        holder.txtSenderSubject.setText(inbox.getSubject());
        holder.txtSenderTime.setText(inbox.getTime());

//        if (position %2 == 1){
//            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        }
//        else {
//            holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
//        }

    }

    @Override
    public int getItemCount() {
        return inboxList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imgSenderProfile;
        TextView txtSenderName;
        TextView txtSenderSubject;
        TextView txtSenderTime;
        LinearLayout itemSelected;

        ViewHolder(View itemView) {
            super(itemView);
            imgSenderProfile = (CircleImageView) itemView.findViewById(R.id.imgSenderProfile);
            txtSenderName = (TextView) itemView.findViewById(R.id.txtSenderName);
            txtSenderSubject = (TextView) itemView.findViewById(R.id.txtSenderSubject) ;
            txtSenderTime = (TextView) itemView.findViewById(R.id.txtSenderTime);
            itemSelected = (LinearLayout) itemView.findViewById(R.id.itemSelected);
        }

    }
}

