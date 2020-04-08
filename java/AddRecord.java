package com.example.balloonsgame;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/*
    AddRecord class
    Generate when add button clicked in main activity
    Check input data's validation
    Valid new record saved when "check" button clicked
    Return to main activity when "back" button clicked
 */
public class AddRecord extends AppCompatActivity
{
    // A player information on the rank contains following item.
    private String playername;
    private float score;
    private String date;
    private String time;
    private String current_score = "0";


    // Create a new activity with tool bar
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            current_score = bundle.getString("current_round_score");
            EditText scoreET = findViewById(R.id.scoreInput);
            scoreET.setText(current_score);
        }
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    // Create a time picker, clock format and text format, record selected time
    public void showTimePickerDialog(View v)
    {
        TextView showTime = findViewById(R.id.showTime);
        DialogFragment timePickerFragment = new TimePicker(showTime);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
        //time = (String) showTime.getText();
    }

    // Create a date picker, calendar format, record selected date
    public void showDatePickerDialog(View v)
    {
        TextView showDate = findViewById(R.id.showDate);
        DialogFragment datePickerFragment = new DatePicker(showDate);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        //date = (String) showDate.getText();
    }

    // Get input player's name and score
    public void getPlayerNameScore(View v)
    {
        EditText playernameET = findViewById(R.id.playernameInput);
        playername = playernameET.getText().toString();
        score = Integer.parseInt(current_score);
    }

    // Check input data validation
    // Player's name: 0-30 characters
    // Score: larger than 0
    // No future date
    public boolean checkValidation(View v)
    {
        if(TextUtils.isEmpty(playername) || playername == null)
        {
            Toast.makeText(this, "Player's name cannot be empty.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(score <= 0)
        {
            Toast.makeText(this, "Score cannot less than 0.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(TextUtils.isEmpty(time)  || time.equals("Selected Time"))
        {
            Toast.makeText(this, "Wrong selected time, please check.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!TextUtils.isEmpty(date) && date.equals("Selected Date"))
        {
            Toast.makeText(this, "Wrong selected date, please check.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    // Generate a new record, read input data from screen
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getNewRecord(View v) throws IOException {
        // Check button
        ImageButton checkButton = findViewById(R.id.checkButton);
        // Get player's name and score
        getPlayerNameScore(v);

//        // Get record date
//        TextView showDate = findViewById(R.id.showDate);
//        date = (String) showDate.getText();
//        // Get record time
//        TextView showTime = findViewById(R.id.showTime);
//        time = (String) showTime.getText();

        // Get current date
        Date dNow = new Date( );
        SimpleDateFormat date_format = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat time_format = new SimpleDateFormat ("hh:mm");

        date = date_format.format(dNow);
        time = time_format.format(dNow);



        //If input data is valid, process the new record
        if(checkValidation(v))
        {
            checkButton.setEnabled(true);
            Record newRecord = new Record(playername, score, date, time);
            RankMainActivity.recordArrayList.add(newRecord);
            RankMainActivity.recordArrayList.sort(new ScoreSorter());

            while(RankMainActivity.recordArrayList.size() > 12)
            {
                RankMainActivity.recordArrayList.remove(RankMainActivity.recordArrayList.size() - 1);
            }
            FileIO file = new FileIO();
            file.writeFile(this);
            Toast.makeText(this, "New Record Saved", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // Back button, back to main activity
    public void backToMain(View v)
    {
        finish();
    }
}
