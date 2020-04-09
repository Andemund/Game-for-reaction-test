package com.example.balloonsgame;

public class Record
{
    private String player_name;
    private float score;
    private String record_date;
    private String record_time;
    //private int rank; //calculate

    public boolean check_validation()
    {
        if(this.player_name.isEmpty() || this.player_name.length() > 30 ||
                this.score < 0 ||
                this.record_date.isEmpty() || this.record_date.equals("Selected Date") ||
                this.record_time.isEmpty() || this.record_time.equals("Selected Time"))
        {
            return false;
        }
        return true;
    }

    public Record(String pn, float s, String d, String t)
    {
        this.player_name = pn;
        this.score = s;
        this.record_date = d;
        this.record_time = t;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public String getRecord_time() {
        return record_time;
    }

    public void setRecord_time(String record_time) {
        this.record_time = record_time;
    }

    /*
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

     */
}
