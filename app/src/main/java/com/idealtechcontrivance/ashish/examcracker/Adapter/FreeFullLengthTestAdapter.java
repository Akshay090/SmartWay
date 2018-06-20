package com.idealtechcontrivance.ashish.examcracker.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.idealtechcontrivance.ashish.examcracker.Activity.StartTestActivity;
import com.idealtechcontrivance.ashish.examcracker.Model.FullLengthTest;
import com.idealtechcontrivance.ashish.examcracker.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.valueOf;

public class FreeFullLengthTestAdapter  extends RecyclerView.Adapter<FreeFullLengthTestAdapter.ViewHolder> {

    private int sum = 0;
    public TextView txtMinutes, txtTestType, txtTestName, txtTestTime, txtTestQuestion, txtTime, txtQuestion, txtTestSubject, txtTestQuestion1;
    private CheckBox chkChecked;
    private String strTestId, strTestType, strTestSubject, strTestName, strTestTime, strTestQuestion, strTime, strQuestion;
    private Button btnNext, btnPrevious, btnStartNow, btnCancel;
    Context context;
    private List<FullLengthTest> fullLengthTestList;

    public FreeFullLengthTestAdapter(Context context, List<FullLengthTest> fullLengthTestList){
        super();
        this.context = context;
        this.fullLengthTestList = fullLengthTestList;
    }

    @Override
    public FreeFullLengthTestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_full_length_test, parent, false);
        FreeFullLengthTestAdapter.ViewHolder viewHolder = new FreeFullLengthTestAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FreeFullLengthTestAdapter.ViewHolder holder, int position) {
        final FullLengthTest fullLengthTest =  fullLengthTestList.get(position);

        holder.txtTestDetail.setText(valueOf(fullLengthTest.getTestName()));
        holder.txtTestSubject.setText(valueOf(fullLengthTest.getTestSubject()));
        holder.txtPackageType.setText(valueOf(fullLengthTest.getPackageType()));
        holder.txtPublishedDate.setText( fullLengthTest.getPublishedDate());


        holder.txtTestStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTestInstruction(fullLengthTest);
            }
        });
    }

    private void openTestInstruction(FullLengthTest fullLengthTest) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setCancelable(true);

        LayoutInflater inflater =LayoutInflater.from(context);
        View instruction_layout = inflater.inflate(R.layout.layout_instruction,null);

        strTestId = String.valueOf(fullLengthTest.getTestId());
        strTestType = fullLengthTest.getTestType();
        strTestSubject = fullLengthTest.getTestSubject();
        strTestName = fullLengthTest.getTestName();
        strTestTime = String.valueOf(fullLengthTest.getDuration());
        strTestQuestion = fullLengthTest.getCnt();

        String[] arr = strTestQuestion.split("\n");
        int[] numArr = new int[arr.length];

        sum = 0;
        for(int i=0; i<arr.length; i++){
            numArr[i] = (int) Double.parseDouble(arr[i]);
            sum+=numArr[i];
        }

        strTestQuestion = String.valueOf(sum);
        strTime = String.valueOf(fullLengthTest.getDuration());
        strQuestion = fullLengthTest.getCnt();

        txtMinutes = (TextView) instruction_layout.findViewById(R.id.txtMinutes);
        txtMinutes.setText(String.valueOf(fullLengthTest.getDuration()));

        btnCancel = (Button) instruction_layout.findViewById(R.id.btnCancel);
        btnNext = (Button) instruction_layout.findViewById(R.id.btnNext);
        builder1.setView(instruction_layout);

        final AlertDialog alertDialog = builder1.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGeneralInstruction(strTestId, strTestType, strTestSubject, strTestName, strTestTime, strTestQuestion, strTime, strQuestion);
            }
        });

        alertDialog.show();
    }

    private void openGeneralInstruction(final String strTestId, final String strTestType, String strTestSubject, final String strTestName, final String strTestTime, final String strTestQuestion, String strTime, final String strQuestion) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setCancelable(true);

        LayoutInflater inflater =LayoutInflater.from(context);
        final View instruction_layout = inflater.inflate(R.layout.layout_general_instructions,null);

        txtTestType = (TextView) instruction_layout.findViewById(R.id.txtTestType);
        txtTestName = (TextView) instruction_layout.findViewById(R.id.txtTestName);
        txtTestTime = (TextView) instruction_layout.findViewById(R.id.txtTestTime);
        txtTestQuestion = (TextView) instruction_layout.findViewById(R.id.txtTestQuestion);

        txtTime = (TextView) instruction_layout.findViewById(R.id.txtTime);
        txtQuestion = (TextView) instruction_layout.findViewById(R.id.txtQuestion);

        txtTestSubject = (TextView) instruction_layout.findViewById(R.id.txtTestSubject);
        txtTestQuestion1 = (TextView) instruction_layout.findViewById(R.id.txtTestQuestion1);

        chkChecked = (CheckBox) instruction_layout.findViewById(R.id.chkChecked);
        btnPrevious = (Button) instruction_layout.findViewById(R.id.btnCancel);
        btnStartNow = (Button) instruction_layout.findViewById(R.id.btnStartNow);

        txtTestType.setText(strTestType);
        txtTestName.setText(strTestName);
        txtTestTime.setText(strTestTime);
        txtTestQuestion.setText(strTestQuestion);

        txtTime.setText(strTime);
        txtQuestion.setText(strTestQuestion);

        txtTestSubject.setText(strTestSubject);
        txtTestQuestion1.setText(strQuestion);

        builder1.setView(instruction_layout);

        final AlertDialog alertDialog = builder1.create();

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnStartNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chkChecked.isChecked()){
                    final ScrollView scrollView = (ScrollView) instruction_layout.findViewById(R.id.scrollView);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    Toast.makeText(context,"Please checked rules & instruction", Toast.LENGTH_LONG).show();
                }
                else {
                    SharedPreferences.Editor editor = context.getSharedPreferences("QuestionPalette",MODE_PRIVATE).edit();
                    editor.putInt("NoQuestion", Integer.parseInt(strTestQuestion));
                    editor.apply();

                    Intent i = new Intent(context, StartTestActivity.class);
                    i.putExtra("ID_KEY",strTestId);
                    i.putExtra("TYPE_KEY",strTestType);
                    i.putExtra("NAME_KEY",strTestName);
                    i.putExtra("DURATION_KEY",strTestTime);
                    context.startActivity(i);
                    ((Activity)context).finish();
                }
            }
        });

        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return fullLengthTestList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTestDetail;
        public TextView txtTestSubject;
        public TextView txtPackageType;
        public TextView txtTestStatus;
        public TextView txtPublishedDate;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTestDetail = (TextView) itemView.findViewById(R.id.txtTestDetail) ;
            txtTestSubject = (TextView) itemView.findViewById(R.id.txtTestSubject);
            txtPackageType = (TextView) itemView.findViewById(R.id.txtPackage);
            txtTestStatus = (TextView) itemView.findViewById(R.id.txtTestStatus);
            txtPublishedDate = (TextView) itemView.findViewById(R.id.txtPublishedDate) ;
        }
    }
}
