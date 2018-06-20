package com.idealtechcontrivance.ashish.examcracker.Utility;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by ashish on 16-02-2017.
 */
public class DatePickerFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener onDateSet;
    private  int year,month,day;

    public void setArguments(Bundle args) {
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(),onDateSet,year,month,day);
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate){
        onDateSet = ondate;
    }

}
