package com.example.madproject2;


import java.util.ArrayList;


public class LifeEntry {
    public ArrayList<Integer> icons;
    public String note;
    public String date;
    public String time;


    public LifeEntry(ArrayList<Integer> icons, String note, String date, String time) {
        this.icons = icons;
        this.note = note;
        this.date = date;
        this.time = time;
    }
}