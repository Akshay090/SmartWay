package com.idealtechcontrivance.ashish.examcracker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.idealtechcontrivance.ashish.examcracker.Model.FAQ;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.TextJustification;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> {

    Context context;

    private List<FAQ> faqList;
    private boolean visible;
    private String count, strQuestion, strAnswer;

    public FAQAdapter(List<FAQ> faqList, Context context){
        super();

        this.faqList = faqList;
        this.context = context;
    }

    @Override
    public FAQAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_faq, parent, false);
        FAQAdapter.ViewHolder viewHolder = new FAQAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FAQAdapter.ViewHolder holder, int position) {

        FAQ faq=  faqList.get(position);

        count = String.valueOf(position+1);

        strQuestion = faq.getQuestion();
        strAnswer = faq.getAnswer();

        holder.txtNumber.setText(count);
        holder.txtAnswer.setVisibility(View.GONE);

        if (strQuestion != null || holder.txtAnswer != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.txtQuestion.setText(Html.fromHtml(strQuestion, Html.FROM_HTML_MODE_LEGACY));
                holder.txtAnswer.setText(Html.fromHtml(strAnswer, Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.txtQuestion.setText(Html.fromHtml(strQuestion));
                holder.txtAnswer.setText(Html.fromHtml(strAnswer));
            }
            TextJustification.justify(holder.txtAnswer);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visible == true) {
                    holder.txtAnswer.setVisibility(View.VISIBLE);
                    visible=false;
                }
                else {
                    holder.txtAnswer.setVisibility(View.GONE);
                    visible=true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNumber;
        public TextView txtAnswer;
        public TextView txtQuestion;


        public ViewHolder(View itemView) {
            super(itemView);

            txtNumber = (TextView) itemView.findViewById(R.id.txtNumber);
            txtQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);
            txtAnswer = (TextView) itemView.findViewById(R.id.txtAnswer) ;

        }

    }
}
