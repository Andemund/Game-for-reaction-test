package com.example.balloonsgame;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

/*
    Date picker
    Create a new date picker
    Set date format
    Show result on screen
 */

public class DatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener
{
    private TextView mShowDate;
    private String date;

    public DatePicker(TextView textView)
    {
        mShowDate = textView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        return datePickerDialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        @SuppressLint("DefaultLocale") String dd = String.format("%02d", dayOfMonth);
        @SuppressLint("DefaultLocale") String mm = String.format("%02d", month + 1);
        @SuppressLint("DefaultLocale") String yyyy = String.format("%04d", year);
        String formattedDate = yyyy + "-" + mm + "-" + dd;

        Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_LONG).show();
        mShowDate.setText( formattedDate );
        date = formattedDate;
    }
}