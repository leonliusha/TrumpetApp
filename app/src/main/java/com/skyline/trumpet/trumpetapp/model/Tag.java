package com.skyline.trumpet.trumpetapp.model;

/**
 * Created by Administrator on 2015/7/17.
 */
public class Tag {
    private long tag_id;
    private String tag_name;
    private int tag_count;

    public Tag(){}

    public Tag(String tag_name, int tag_count){
        this.tag_name = tag_name;
        this.tag_count = tag_count;
    }

    public long getTag_id() {
        return tag_id;
    }

    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public int getTag_count() {
        return tag_count;
    }

    public void setTag_count(int tag_count) {
        this.tag_count = tag_count;
    }


}
