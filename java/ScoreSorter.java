package com.example.balloonsgame;

import java.util.Comparator;
/*
    Sort record
    According to score
    If scores are same, the most recent onr shows on the top, first compare date, then compare time
 */

public class ScoreSorter implements Comparator<Record>
{

    @Override
    public int compare(Record o1, Record o2)
    {
        int r = (int) (o2.getScore() - o1.getScore());
        if(r != 0)
        {
            return r;
        }
        else
        {
            int r1 = o2.getRecord_date().compareTo(o1.getRecord_date());
            if(r1 > 0)
            {
                return 1;
            }
            if(r1 < 0)
            {
                return -1;
            }
            else
            {
                int r2 = o2.getRecord_time().compareTo(o1.getRecord_time());
                if(r1 > 0)
                {
                    return 1;
                }
                else
                {
                    return -1;
                }

            }
        }
    }
}