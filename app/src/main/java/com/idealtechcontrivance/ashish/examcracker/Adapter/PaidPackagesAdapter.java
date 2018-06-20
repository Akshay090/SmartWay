package com.idealtechcontrivance.ashish.examcracker.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idealtechcontrivance.ashish.examcracker.Fragment.PackageDetailsFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.TestSeriesFragment;
import com.idealtechcontrivance.ashish.examcracker.Model.Package;
import com.idealtechcontrivance.ashish.examcracker.R;

import java.util.List;

public class PaidPackagesAdapter extends RecyclerView.Adapter<PaidPackagesAdapter.ViewHolder> {

    private Context context;
    private List<Package> packageList;
    private String strId,strStrickCost,strCost,strCategory,strSeriesName,strTotalTest,strExaminationTest,strPracticeTest,strSectionalTest,strHighlights;

    public PaidPackagesAdapter(List<Package> packageList, Context context){
        super();

        this.packageList = packageList;
        this.context = context;
    }

    @Override
    public PaidPackagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_horizontal_packages, parent, false);
        PaidPackagesAdapter.ViewHolder viewHolder = new PaidPackagesAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PaidPackagesAdapter.ViewHolder holder, int position) {

        final Package aPackage =  packageList.get(position);

        strId= String.valueOf(aPackage.getId());
        strStrickCost= String.valueOf(aPackage.getStrick_cost());
        strCost= String.valueOf(aPackage.getCost());
        strCategory= aPackage.getCategory();
        strSeriesName= aPackage.getTest_series_name();
        strTotalTest= String.valueOf(aPackage.getTotal_test());
        strExaminationTest= String.valueOf(aPackage.getExamination_test());
        strPracticeTest= String.valueOf(aPackage.getPractice_test());
        strSectionalTest= String.valueOf(aPackage.getSectional_test());
        strHighlights= aPackage.getHighlights();

        holder.txt_strick_cost.setText(strStrickCost);
        holder.txt_cost.setText(strCost);
        holder.txt_test_series_name.setText(strSeriesName);
        holder.txt_examination_test.setText(strExaminationTest);
        holder.txt_practice_test.setText(strPracticeTest);
        holder.txt_sectional_test.setText(strSectionalTest);

        if (strHighlights != null){
            if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            {
                holder.txt_highlights.setText(Html.fromHtml(strHighlights,Html.FROM_HTML_MODE_LEGACY));
            }
            else {
                holder.txt_highlights.setText(Html.fromHtml(strHighlights));
            }
        }

        holder.txt_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPackageDetails(aPackage);
            }
        });
    }

    private void openPackageDetails(Package aPackage) {

        Bundle bundle = new Bundle();

        bundle.putString("ID_KEY", String.valueOf(aPackage.getId()));
        bundle.putString("CATEGORY_KEY", aPackage.getCategory());
        bundle.putString("SERIES_NAME_KEY", aPackage.getTest_series_name());
        bundle.putString("TOTAL_TEST_KEY", String.valueOf(aPackage.getTotal_test()));
        bundle.putString("EXAMINATION_TEST_KEY",  String.valueOf(aPackage.getExamination_test()));
        bundle.putString("PRACTICE_TEST_KEY",  String.valueOf(aPackage.getPractice_test()));
        bundle.putString("SECTIONAL_TEST_KEY",  String.valueOf(aPackage.getSectional_test()));
        bundle.putString("COST_KEY",  String.valueOf(aPackage.getCost()));
        bundle.putString("STRICK_COST_KEY",  String.valueOf(aPackage.getStrick_cost()));
        bundle.putString("HIGHLIGHT_KEY", aPackage.getHighlights());

        PackageDetailsFragment packageDetailsFragment = new PackageDetailsFragment();
        packageDetailsFragment.setArguments(bundle);

        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .add(new TestSeriesFragment(),"TestSeriesFragment")
                .addToBackStack("TestSeriesFragment")
                .replace(R.id.mainContainer, packageDetailsFragment)
                .commit();
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txt_strick_cost;
        public TextView txt_cost;
        public TextView txt_test_series_name;
        public TextView txt_examination_test;
        public TextView txt_practice_test;
        public TextView txt_sectional_test;
        public TextView txt_highlights;
        public TextView txt_buy_now;


        public ViewHolder(View itemView) {
            super(itemView);

            txt_strick_cost = (TextView) itemView.findViewById(R.id.txt_strick_cost);
            txt_strick_cost.setPaintFlags(txt_strick_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            txt_cost = (TextView) itemView.findViewById(R.id.txt_cost) ;
            txt_test_series_name = (TextView) itemView.findViewById(R.id.txt_test_series_name);
            txt_examination_test = (TextView) itemView.findViewById(R.id.txt_examination_test);
            txt_practice_test = (TextView) itemView.findViewById(R.id.txt_practice_test);
            txt_sectional_test = (TextView) itemView.findViewById(R.id.txt_sectional_test) ;
            txt_highlights = (TextView) itemView.findViewById(R.id.txt_highlights);
            txt_buy_now = (TextView) itemView.findViewById(R.id.txt_buy_now);
        }

    }
}