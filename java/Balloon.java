/*
    Balloon class: to store all the information needed for onr balloon
    Including shape, color, size, position, lifetime, show up time and on  click time
    Only has setter and getter, no more functions
 */
package com.example.balloonsgame;

public class Balloon
{
    private String shape;
    private String color;
    private int position_x;
    private int position_y;

    private int size;
    private float life_time; // set each balloon's life time as a certain time. (larger size one with shorter time?)
    private float show_up_time;
    private float on_click_time;

    Balloon(String s, String c, int x, int y, int ss, float lt)
    {
        setShape(s);
        setColor(c);
        setPosition_x(x);
        setPosition_y(y);
        setSize(ss);
        setLife_time(lt);
    }

    public String getShape()
    {
        return shape;
    }

    public void setShape(String shape)
    {
        this.shape = shape;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public int getPosition_x()
    {
        return position_x;
    }

    public void setPosition_x(int position_x)
    {
        this.position_x = position_x;
    }

    public int getPosition_y()
    {
        return position_y;
    }

    public void setPosition_y(int position_y)
    {
        this.position_y = position_y;
    }

    public int getSize() {  return size; }

    public void setSize(int size) { this.size = size; }

    public float getLife_time() { return life_time; }

    public void setLife_time(float life_time) {
        this.life_time = life_time;
    }

    public float getShow_up_time() { return show_up_time; }

    public void setShow_up_time(float show_up_time) { this.show_up_time = show_up_time; }

    public float getOn_click_time() { return on_click_time; }

    public void setOn_click_time(float on_click_time) { this.on_click_time = on_click_time; }
}
