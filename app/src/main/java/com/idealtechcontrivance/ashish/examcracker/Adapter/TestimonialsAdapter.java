package com.idealtechcontrivance.ashish.examcracker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.idealtechcontrivance.ashish.examcracker.Model.Testimonials;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.TextJustification;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class TestimonialsAdapter extends RecyclerView.Adapter<TestimonialsAdapter.ViewHolder> {

    private Context context;
    private List<Testimonials> testimonialsList;

    public TestimonialsAdapter(List<Testimonials> testimonialsList, Context context){
        super();

        this.testimonialsList = testimonialsList;
        this.context = context;
    }

    @Override
    public TestimonialsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_testimonials, parent, false);
        TestimonialsAdapter.ViewHolder viewHolder = new TestimonialsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TestimonialsAdapter.ViewHolder holder, int position) {

        Testimonials testimonials =  testimonialsList.get(position);

        Picasso.with(context).load(testimonials.getImage()).fit().into(holder.imgProfile);
        holder.txtName.setText(testimonials.getName());
        holder.txtTestimonial.setText(testimonials.getTestimonial());

        TextJustification.justify(holder.txtTestimonial);

    }

    @Override
    public int getItemCount() {
        return testimonialsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imgProfile;
        public TextView txtName;
        public TextView txtTestimonial;


        public ViewHolder(View itemView) {
            super(itemView);

            imgProfile = (CircleImageView) itemView.findViewById(R.id.imgProfile);
            txtName = (TextView) itemView.findViewById(R.id.txtName) ;
            txtTestimonial = (TextView) itemView.findViewById(R.id.txtTestimonial);

        }

    }
}
