package com.skyline.trumpet.trumpetapp.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2015/7/6.
 */
public class Broadcast implements Serializable{
    private long id;
    private int type;
    private String brief;
    private String description;
    private int amount;
    private int count;
    private Timestamp createdDate;
    private Timestamp expireDate;
    private double latitude;
    private double longitude;
    private String tags;
    private String tagsId;
    public String getTagsId() {

        return tagsId;
    }

    public void setTagsId(String tagsId) {
        this.tagsId = tagsId;
    }



    public Broadcast(){}

    public Broadcast(int type, String brief, String description, int amount, int count, Timestamp createdDate, double latitude, double longitude, String tags){
        this.type = type;
        this.brief = brief;
        this.description = description;
        this.amount = amount;
        this.count = count;
        this.createdDate = createdDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;
    }

    public String toString(){
        return getType() + getBrief() + getDescription() + getCreatedDate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCreatedDate(Timestamp createdDate){ this.createdDate = createdDate; }

    public Timestamp getCreatedDate(){
        return createdDate;
    }

    public Timestamp getExpireDate() {return expireDate;}

    public void setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double myLatitude) {
        this.latitude = myLatitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double myLongitude) {
        this.longitude = myLongitude;
    }
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

}
