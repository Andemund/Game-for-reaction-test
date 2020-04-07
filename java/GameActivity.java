/*
    The game activity
    Shows timer, time per clicking one correct balloon, total click number, total missed number, total correct click number and score
    shows balloons in s certain area and make sure they are not out of boundary or overlap
    Shows a dark color background and a light color boundary
    Game will finish after 10 correct balloons clicked
 */
package com.example.balloonsgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity
{
    public RandomGenerator randomGenerator;
    public int SCALE = 3;
    private static final String TAG = "mytag";
    public ConstraintLayout layout;
    public int correct_touch = 0;
    public int total_touch = 0;
    public int missed_touch = 0;
    public int score = 0;

    public float x0, y0, h ,w;// left top point of canvas and height and width of canvas

    public long startTime =  System.currentTimeMillis();
    public long millis;
    public long last_click = 0;
    public int seconds;
    public int decis;
    public long correct_balloon_time = 0;
    public float average_correct_time = 0;
    public Runnable timerRunnable;
    public boolean game_on = true;
    public TextView score_tv;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        randomGenerator = MainActivity.randomGenerator;
        game_begin();
    }

    // start game
    // set the very first scene of one round of game
    // init time, score and balloons
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void game_begin()
    {
        game_on = true;
        float factor = this.getResources().getDisplayMetrics().density;
        this.SCALE = (int)factor;
        layout = this.findViewById(R.id.game_layout);

        set_background();

        final Handler timerHandler = new Handler();
        final TextView finalTimerBox = (TextView) findViewById(R.id.text_view_timer);
        final TextView missed_touch_tv = (TextView) findViewById(R.id.text_view_missed_touch_count);
        final TextView correct_touch_tv = (TextView) findViewById(R.id.text_view_correct_touch_count);
        final TextView total_touch_tv = (TextView) findViewById(R.id.text_view_total_touch_count);

        // timer, as a clock of whole game
        // run as a new handler
        final long finalStartTime = startTime;
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                millis = System.currentTimeMillis() - finalStartTime;
                seconds =  (int) (millis / 1000);
                decis = (int) ((millis % 1000)/10);

                finalTimerBox.setText(String.format("Time: %d.%02d", seconds, decis));
                timerHandler.postDelayed(this, 10);
                missed_touch_tv.setText(String.valueOf(missed_touch));
                correct_touch_tv.setText(String.valueOf(correct_touch));
                total_touch_tv.setText(String.valueOf(total_touch));
            }
        };
        timerHandler.postDelayed(timerRunnable, 0);

        // generate a certain amount of balloon at the beginning according to the random total number
        randomGenerator.set_balloon_number(randomGenerator.balloon_number_generator());
        for(int i = 0; i < randomGenerator.get_balloon_number(); i++)
        {
            new_balloon_text_view();
        }
    }

    // when finished one round of game, go to a new activity
    // show the current player the score rank of this game
    // "Add" button is clickable if current score is in top 12
    // otherwise, only "Back" button is clickable
    public void show_rank_list()
    {
        layout.removeAllViews();
        Intent rankMainActivity = new Intent(this, RankMainActivity.class);
        String score_string = String.valueOf(score);
        rankMainActivity.putExtra("current_round_score", score_string);
        startActivity(rankMainActivity);
        finish();
    }

    // generate a new balloon
    // set the show up time, color, shape, size, position and lifetime
    // set function after it's lifetime and when it was clicked
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void new_balloon_text_view()
    {
        // create a new Balloon object
        // show it on screen as text view
        final TextView average_time_tv = (TextView) findViewById(R.id.text_view_average_correct_time);
        final Balloon b = randomGenerator.new_balloon_generator();

        if(b == null)
        {
            return;
        }
        final TextView btv = new TextView(this);
        final ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(b.getSize() * SCALE, b.getSize() * SCALE);
        // draw the balloon according to the random shape
        // choose different drawable objects
        if(b.getShape().equals("circle"))
        {
            btv.setBackgroundResource(R.drawable.circle);
        }
        else if (b.getShape().equals("square"))
        {
            btv.setBackgroundResource(R.drawable.square);
        }
        else
        {
            Log.d(TAG, "unknown shape");
            return;
        }

        // draw the balloon according to the random color
        // set drawable object's color
        GradientDrawable gradientDrawable = (GradientDrawable)btv.getBackground().getCurrent();
        switch (b.getColor())
        {
            case "red":
                gradientDrawable.setColor((getResources().getColor(R.color.red)));
                break;
            case "orange":
                gradientDrawable.setColor((getResources().getColor(R.color.orange)));
                break;
            case "yellow":
                gradientDrawable.setColor((getResources().getColor(R.color.yellow)));
                break;
            case "green":
                gradientDrawable.setColor((getResources().getColor(R.color.green)));
                break;
            case "blue":
                gradientDrawable.setColor((getResources().getColor(R.color.blue)));
                break;
            case "purple":
                gradientDrawable.setColor((getResources().getColor(R.color.purple)));
                break;
            case "white":
                gradientDrawable.setColor((getResources().getColor(R.color.white)));
                break;
            default:
                Log.d(TAG,"unknown color");
                break;
        }
        btv.setX(b.getPosition_x());
        btv.setY(b.getPosition_y());
        Log.d(TAG,"current balloon position: " + b.getPosition_x() + "," + b.getPosition_y());

        layout.addView(btv, lp);
        b.setShow_up_time(millis);

        // after it's lifetime
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(game_on == false)
                {
                    return;
                }
                if(b.getColor().equals(randomGenerator.getTarget_color()) && b.getShape().equals(randomGenerator.getTarget_shape()))
                {
                    missed_touch ++;
                }
                btv.setVisibility(View.GONE);
                layout.removeView(btv);
                new_balloon_text_view();
                randomGenerator.balloon_disappear(b);
            }
        };
        btv.postDelayed(runnable, (long) b.getLife_time());
        // when it was clicked
        btv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(game_on == false)
                {
                    return;
                }
                b.setOn_click_time(millis);
                btv.removeCallbacks(runnable);
                // clicked the correct balloon
                if(b.getColor().equals(randomGenerator.getTarget_color()) && b.getShape().equals(randomGenerator.getTarget_shape()))
                {
                    //Log.d(TAG, b.getColor() + " " + b.getShape() + "  You are right");
                    correct_touch ++;
                    total_touch ++;
                    correct_balloon_time = (long) (correct_balloon_time + b.getOn_click_time() - b.getShow_up_time());

                    Log.d(TAG,"this balloon used: " + correct_balloon_time);
                    average_correct_time = correct_balloon_time / correct_touch;
                    average_time_tv.setText(String.format("%d.%02d", (int)(average_correct_time/1000), (int) (average_correct_time % 1000)/10));
                    calculate_score(correct_balloon_time - last_click);
                    last_click = correct_balloon_time;
                }
                else
                {
                    total_touch ++;
                }
                if(correct_touch >= 10) // to do: change to 10
                {
                    Log.d(TAG, "Finished!");
                    game_on = false;
                    show_rank_list();
                    // finish();
                }

                layout.removeView(btv);
                new_balloon_text_view();
                randomGenerator.balloon_disappear(b);
            }
        });


    }

    // set background of the area used to show balloons
    // dark background color and light boundary color
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void set_background() // (Canvas canvas)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screen_height = displayMetrics.heightPixels;
        int screen_width = displayMetrics.widthPixels;

        TextView background_text_view = new TextView(getApplicationContext());
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        background_text_view.setLayoutParams(lp);
        background_text_view.setWidth(screen_width - 60);
        background_text_view.setHeight(screen_height * 2 / 3);
        background_text_view.setY((float)(screen_height / 3 - 250));
        background_text_view.setX((float)30);
        background_text_view.setBackgroundResource(R.drawable.boundary);
        layout.addView(background_text_view);
        x0 = 30;
        y0 = (float)(screen_height / 3 - 250);
        h = screen_height * 2 /3;
        w = screen_width - 60;
        randomGenerator.get_empty_game_board(x0 , y0, h, w, SCALE);
    }


    // add score after a correct balloon was clicked
    // calculate the score according to used time on this balloon
    public void calculate_score(float t)
    {
        if(t <= 300)
        {
            score = score + 10;
        }
        else if(t <= 600)
        {
            score = score + 9;
        }
        else if(t <= 1000)
        {
            score = score + 8;
        }
        else if(t <= 1500)
        {
            score = score + 7;
        }
        else if(t <= 2000)
        {
            score = score + 6;
        }
        else if(t <= 2500)
        {
            score = score + 5;
        }
        else if(t <= 3000)
        {
            score = score + 4;
        }
        else if(t <= 4000)
        {
            score = score + 3;
        }
        else if(t <= 5000)
        {
            score = score + 2;
        }
        else if(t <= 6000)
        {
            score = score + 1;
        }
        else
        {
            score = score + 0;
        }
        score_tv = this.findViewById(R.id.text_view_score);
        score_tv.setText(String.valueOf(score));
    }
}
