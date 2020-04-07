/*
    Generator attributes' value of the game
    Including color, shape, position, size and lifetime of a balloon
    Including total balloon number in one round of game and color and shape of target balloon
 */
package com.example.balloonsgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenerator
{
    private static final String TAG = "mytag";
    private String[] shape_range = {"circle", "square"};
    private String[] color_range = {"red", "orange", "yellow", "green", "blue", "purple", "white"};
    private String target_shape; // target shape
    private String target_color; // target color
    private String shape; // current shape
    private String color; // current color
    private int size; // current size

    public List<Balloon> balloons = new ArrayList<>();

    private int balloon_number = 0; // total balloon number on the screen
    private int maximum_balloon_size = 64;

    private float x0 = 0;
    private float y0 = 0;
    private float height;
    private float width;
    private float scale;

    // range of random lifetime
    private int shortest_lifetime = 3000;
    private int longest_lifetime = 7000;

    private Activity activity;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    RandomGenerator()
    {
        //get_empty_game_board();
        Log.d(TAG,"total balloon number is:" + balloon_number);
    }

    public String getTarget_shape() {
        return target_shape;
    }

    public String getTarget_color() {
        return target_color;
    }

    // generate a random shape in the range, as "shape"
    public void shape_generator()
    {
        int shape_l = shape_range.length;
        Random rand = new Random();
        int shape_id = rand.nextInt(shape_l);
        shape = shape_range[shape_id];
    }

    // generate a random color in the range, as "color"
    public void color_generator()
    {
        int color_l = color_range.length;
        Random rand = new Random();
        int color_id = rand.nextInt(color_l);
        color = color_range[color_id];
    }

    // generate a random size in the range, as "size"
    public void size_generator()
    {
        Random rand = new Random();
        int minimum_balloon_size = 32;
        int rand_size = rand.nextInt(maximum_balloon_size - minimum_balloon_size + 1);
        size = minimum_balloon_size + rand_size;
    }

    // generate a random balloon lifetime in the range, return the time
    public int random_lifetime_generator()
    {
        Random random = new Random();
        int time = random.nextInt(longest_lifetime - shortest_lifetime + 1) + shortest_lifetime;
        return time;
    }

    // generate target balloon's shape and color, and a instruction showed on the start screen
    public void instruction_generator(Activity activity)
    {
        this.activity = activity;
        shape_generator();
        color_generator();
        target_color = color;
        target_shape = shape;
        String s1 = "Please select all ";
        String instruction = s1 + target_color + " " + target_shape + " on the screen.";
        instruction = instruction + "\n" + "Touch the right one and be as quick as possible. ";
        instruction = instruction + "\n" + "10 is your target.";

        SpannableStringBuilder str = new SpannableStringBuilder(instruction);
        str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), s1.length() - 1, target_color.length() + 1 + target_shape.length() + s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        @SuppressLint("ResourceType") TextView text_view_instruction = activity.findViewById(R.id.text_view_game_instruction);
        Log.d(TAG, instruction);
        text_view_instruction.setText(instruction);
    }

    // check whether the new balloon gonna overlap any other balloon already existed or out of boundary
    public boolean no_overlap(int temp_x, int temp_y, int temp_s)
    {
        int l = balloons.size();
        if(l > 0)
        {
            Balloon b;
            for(int i = 0; i < l; i++)
            {
                b = balloons.get(i);
                int bx = b.getPosition_x();
                int by = b.getPosition_y();
                int bs = b.getSize();
                double d = Math.sqrt(Math.pow((temp_x - bx), 2) + Math.pow((temp_y - by), 2));
                if(d <= 64 * 2 )
                {
                    return false;
                }
            }
        }
        return true;
    }

    // generate a new balloon using random values and add it to Balloon list
    // if cannot find a proper position for a new balloon, return null to keep program keep working
    public Balloon new_balloon_generator()
    {
        Random random = new Random();
        int x, y;
        int count = 1000;
        //Log.d(TAG, "x0 = " + x0 + " y0 = " + y0 + " x1 = " + (x0 + width) + " y1 = " + (y0 + height));
        while(count > 0)
        {
            x = (int) (random.nextInt((int) ((int)width - maximum_balloon_size * scale - 10)) + x0 + 5);
            y = (int) (random.nextInt((int) ((int)height - maximum_balloon_size * scale - 10)) + y0 + 5);
            size_generator();
            color_generator();
            shape_generator();
            if(no_overlap(x,y,size))
            {
                Balloon new_balloon = new Balloon(shape, color, x, y, size, random_lifetime_generator());
                balloons.add(new_balloon);
                return new_balloon;
            }
            count--;
        }
        Log.d(TAG, "cannot generate a new balloon now, maybe too crowded");
        return null;
    }

    // generate a board, get all possible center position of balloons
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void get_empty_game_board(float x0, float y0, float h, float w, float scale)
    {
        this.x0 = x0;
        this.y0 = y0;
        height = h;
        width = w;
        this.scale = scale;
    }

    // get new balloon number, to decide whether needs to generate new balloon and how many
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public int balloon_number_generator()
    {
        Random random = new Random();
        int minimum_balloon_number = 6;
        int maximum_balloon_number = 12;
        int n = random.nextInt(maximum_balloon_number - minimum_balloon_number + 1) + minimum_balloon_number; // 6 to 12
        return n;
    }

    public void set_balloon_number(int n) { balloon_number = n; };

    public int get_balloon_number() { return balloon_number; } // get current balloon number


    // after a balloon was clicked, make it disappear on the screen and delete it from balloon list
    public void balloon_disappear(final Balloon b)
    {
        balloons.remove(b);
        balloon_number--;
    }
}
