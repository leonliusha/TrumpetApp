package com.skyline.trumpet.trumpetapp.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2015/7/6.
 */
public class Broadcast implements Serializable{
    private long id;
    private long userId;
    private String author;
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
    private String avatarUrl;
    private String photoUrl;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }




    public void setTagsId(String tagsId) {
        this.tagsId = tagsId;
    }

    public Broadcast(){}

    public Broadcast(long userId,String author,int type, String brief, String description, int amount, int count, Timestamp createdDate, double latitude, double longitude, String tags){
        this.userId = userId;
        this.author = author;
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

    @Override
    public int hashCode(){
        long latiLong = Double.doubleToLongBits(latitude);
        long longiLong = Double.doubleToLongBits(longitude);
        long dateLong = createdDate.getTime();
        int result = 17;
        result = 37 * result + (int) (id ^ (id >>> 32));
        result = 37 * result + (int) (latiLong ^ (latiLong >>> 32));
        result = 37 * result + (int) (longiLong ^ (longiLong >>> 32));
        result = 37 * result + (int) (dateLong ^ (dateLong >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Broadcast))
            return false;
        Broadcast broadcast = (Broadcast)o;
        return broadcast.id == id && broadcast.createdDate.equals(createdDate)
                                  && broadcast.latitude == latitude
                                  && broadcast.longitude == longitude;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTagsId() {

        return tagsId;
    }

}
