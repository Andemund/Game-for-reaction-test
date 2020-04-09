package com.example.balloonsgame;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScoreRecordListAdapter extends ArrayAdapter <Record>
{
    private static final String TAG = "ScoreRecordListAdapter";
    private Context mContext;
    private int mResource;
    @SuppressLint("SimpleDateFormat")
    //private DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    public ScoreRecordListAdapter(Context context, int resource, ArrayList<Record> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {

        float score = Objects.requireNonNull(getItem(position)).getScore();
        String player_name = Objects.requireNonNull(getItem(position)).getPlayer_name();
        String record_date = Objects.requireNonNull(getItem(position)).getRecord_date();
        String record_time = Objects.requireNonNull(getItem(position)).getRecord_time();
        //Record record = new Record(player_name, score,record_date, record_time);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);


        TextView textViewScore = convertView.findViewById(R.id.textViewRank);
        TextView textViewPlayerName = convertView.findViewById(R.id.textViewPlayerName);
        TextView textViewDateTime = convertView.findViewById(R.id.textViewRecordDateTime);
        textViewScore.setText(String.valueOf(score));
        textViewPlayerName.setText(player_name);
        textViewDateTime.setText(record_date + " " + record_time);

        return convertView;
    }
}