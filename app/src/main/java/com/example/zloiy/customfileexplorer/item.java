package com.example.zloiy.customfileexplorer;

import android.content.ClipData;

/**
 * Created by ZloiY on 25-Jul-16.
 */
public class Item implements Comparable<Item> {
    private String name;
    private String data;
    private String date;
    private String path;
    private String image;
    private boolean check;

    public Item(String name, String data, String date, String path, String image){
        this.name = name;
        this.data = data;
        this.date = date;
        this.path = path;
        this.image = image;
    }

    public Item(String name, String data, String date, String path, String image, boolean check){
        this.name = name;
        this.data = data;
        this.date = date;
        this.path = path;
        this.image = image;
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    public String getImage() {
        return image;
    }

    public boolean isCheck(){
        if(check)
            return true;
        else return false;
    }

    public void setCheck(){
        if(check)
            check = false;
        else check = true;
    }

    public int compareTo(Item o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}
