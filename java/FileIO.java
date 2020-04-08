package com.example.balloonsgame;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
    Read and write file
    File contains 12 records at most, top 12 records
    When a new record added, if total amount is larger the 12, delete the 13th and after
 */
public class FileIO
{
    private String path = "rank.txt";

    // Read file
    public ArrayList<Record> readFile(Context context, ArrayList<Record> list) {
        FileInputStream fis = null;
        BufferedReader br = null;
        list.clear();
        try {
            fis = context.openFileInput(path);
            br = new BufferedReader(new InputStreamReader(fis));
            String lr;
            lr = br.readLine();
            int i = 0;
            while (lr != null && lr != "" && i < 12) {
                String[] data = lr.split("\t", -1);
                Record nr = new Record(data[0], Float.valueOf(data[1]), data[2], data[3]);
                if(nr.check_validation())
                {
                    list.add(nr);
                    i++;
                }
                lr = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    // Write file
    public void writeFile(Context context) throws IOException {
        FileOutputStream fos = null;
        fos = context.openFileOutput(path, context.MODE_PRIVATE);
        String text;
        RankMainActivity mActivity= new RankMainActivity();
        for(int i = 0; i < 12 && i < mActivity.recordArrayList.size(); i ++)
        {
            Record r = mActivity.recordArrayList.get(i);
            if(r.check_validation())
            {
                text = r.getPlayer_name() + "\t" + String.valueOf(r.getScore()) + "\t"
                        + r.getRecord_date() + "\t" + r.getRecord_time() + "\n";
                fos.write(text.getBytes());
            }

        }
    }


}