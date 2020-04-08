package com.example.balloonsgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class RankMainActivity extends AppCompatActivity
{
    //Current activity tag
    private static final String TAG = "MainActivity";
    //Floating button, click to add a new record
    private ImageButton addRecordImageButton;
    private ImageButton backImageButton;
    //Array list, used to record rank data, top 12 players' information
    public static ArrayList<Record> recordArrayList = new ArrayList<>();
    //Adapter, used to arrange list filed
    public ScoreRecordListAdapter adapter;
    public String current_score = "0";

    //Create main activity
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Create main activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_main);
        //Log.d(TAG, "onCreate: Started.");

        //Read records from data file to array list(fixed file name: rank.txt)
        FileIO file = new FileIO();
        recordArrayList = file.readFile(this, recordArrayList);
//        recordArrayList = (ArrayList<Record>) getIntent().getSerializableExtra("rank_list");
        //Sort current records according to score and date
        recordArrayList.sort(new ScoreSorter());

        //Show the rank information on the screen according to a certain format, format set in adapter
        ListView mListView = (ListView)findViewById(R.id.textViewRank);
        adapter = new ScoreRecordListAdapter(RankMainActivity.this, R.layout.adapter_view_layout, recordArrayList);
        mListView.setAdapter(adapter);

        //Create a new activity when the add button clicked
        addRecordImageButton = (ImageButton)findViewById(R.id.addRecordImageButton);
        addRecordImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { addNewRecord(v); }});

        //Create a new activity when the add button clicked
        backImageButton = (ImageButton)findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish();}});

        // current round's score
        //String current_score = "0";
        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            current_score = bundle.getString("current_round_score");
        }
        int l = recordArrayList.size();
        assert current_score != null;
        if(Integer.parseInt(current_score) < 0)
        {
            this.findViewById(R.id.addRecordImageButton).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.addRecordImageButton).setClickable(false);
        }
        else if(l < 12 || recordArrayList.get(l - 1).getScore() <= Integer.parseInt(current_score))
        {

        }
        else
        {
            this.findViewById(R.id.addRecordImageButton).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.addRecordImageButton).setClickable(false);
        }
    }

    protected void onRestart()
    {
        super.onRestart();
        this.findViewById(R.id.addRecordImageButton).setVisibility(View.INVISIBLE);
        this.findViewById(R.id.addRecordImageButton).setClickable(false);
    }


    //Create a new activity, called addRecordActivity
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addNewRecord(View view)
    {
        Intent addRecordActivity = new Intent(RankMainActivity.this, AddRecord.class);
        addRecordActivity.putExtra("current_round_score", current_score);
        startActivity(addRecordActivity);
    }

    //Recreate onResume function, sort record list after added a new record and show on screen
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        recordArrayList.sort(new ScoreSorter());
        ListView mListView = (ListView)findViewById(R.id.textViewRank);
        adapter = new ScoreRecordListAdapter(this, R.layout.adapter_view_layout, recordArrayList);
        //Log.d("myTag", "in adapter2 "+ String.valueOf(adapter.getCount()));
        mListView.setAdapter(adapter);
    }
}
