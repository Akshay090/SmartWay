package com.idealtechcontrivance.ashish.examcracker.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.idealtechcontrivance.ashish.examcracker.Fragment.PackageDetailsFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.TestSeriesFragment;
import com.idealtechcontrivance.ashish.examcracker.Model.Package;
import com.idealtechcontrivance.ashish.examcracker.R;

import java.util.List;

import static java.lang.String.valueOf;

public class AllPackagesAdapter extends RecyclerView.Adapter<AllPackagesAdapter.ViewHolder> {

    private Context context;
    private List<Package> packageList;

    public AllPackagesAdapter(List<Package> packageList, Context context){
        super();

        this.packageList = packageList;
        this.context = context;
    }

    @Override
    public AllPackagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_vertical_packages, parent, false);
        AllPackagesAdapter.ViewHolder viewHolder = new AllPackagesAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AllPackagesAdapter.ViewHolder holder, int position) {

        final Package aPackage =  packageList.get(position);

        holder.txt_strick_cost.setText(valueOf(aPackage.getStrick_cost()));
        holder.txt_cost.setText(valueOf(aPackage.getCost()));
        holder.txt_test_series_name.setText( aPackage.getTest_series_name());
        holder.txt_examination_test.setText(valueOf(aPackage.getExamination_test()));
        holder.txt_practice_test.setText(valueOf(aPackage.getPractice_test()));
        holder.txt_sectional_test.setText(valueOf(aPackage.getSectional_test()));

        if (aPackage.getHighlights() != null){
            if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            {
                holder.txt_highlights.setText(Html.fromHtml(aPackage.getHighlights() ,Html.FROM_HTML_MODE_LEGACY));
            }
            else {
                holder.txt_highlights.setText(Html.fromHtml(aPackage.getHighlights()));
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