/*
    This game is for the test of reaction.
    Measure player's reaction time by letting them click target balloon on the screen.
    Total balloon has two possible shapes: square and circle.
    Total balloon has seven possible colors: red, orange, yellow, blue, green, purple and white.
    They show up in a certain area of the screen, no overlap or out of boundary.
    They show for a random time on random position with random shape and color.
    Player's score calculated only according to the time they use on a correct balloon.
    No punishment for missing target or click wrong balloon.
    After the game, according to the score of this round, if it is top 12, show the "Add" button to let player to choose whether add this record or not;
    if it's not top 12 , only show the return button.
    When adding a new record, only player's name and date and time are changeable, score is not.
    Player can check the score rank without playing the game.
 */
package com.example.balloonsgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.RenderNode;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class MainActivity extends AppCompatActivity {

    public static RandomGenerator randomGenerator = new RandomGenerator();
    public static ArrayList<Record> recordArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        randomGenerator.instruction_generator(this);

        //Read records from data file to array list(fixed file name: rank.txt)
        FileIO file = new FileIO();
        recordArrayList = file.readFile(this, recordArrayList);

        Button mStartButton = (Button)findViewById(R.id.button_start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(v);
            }
        });

        Button mViewRank = (Button) findViewById(R.id.button_view_rank);
        mViewRank.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                viewRank(v);
            }
        });


}
    // start a new round when return from other activity
    @Override
    protected void onRestart() {
        super.onRestart();
        //RandomGenerator randomGenerator = new RandomGenerator();
        randomGenerator.instruction_generator(this);
    }

    // when clicked "Start" button, go to the game activity
    private void startGame(View view)
    {
        Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
        startActivity(gameActivity);
    }

    // when clicked "View Rank" button, go to rank activity
    private void viewRank(View view)
    {
        Intent rankMainActivity = new Intent(MainActivity.this, RankMainActivity.class);
        rankMainActivity.putExtra("current_round_score", "-1");
        startActivity(rankMainActivity);
    }

}
