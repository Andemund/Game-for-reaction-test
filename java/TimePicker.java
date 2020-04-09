package com.example.balloonsgame;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

/*
    Time picker
    Create a new time picker
    Set time format
    Show result on screen
 */

public class TimePicker extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener
{
    private TextView mShowTime;
    public String time;

    public TimePicker(TextView textView)
    {
        mShowTime = textView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        @SuppressLint("DefaultLocale") String hh = String.format("%02d", hourOfDay);
        @SuppressLint("DefaultLocale") String mm = String.format("%02d", minute);

        String formattedTime = hh + ":" + mm;
        Toast.makeText(getActivity(), formattedTime, Toast.LENGTH_LONG).show();
        mShowTime.setText( formattedTime );
        time = formattedTime;
    }
}