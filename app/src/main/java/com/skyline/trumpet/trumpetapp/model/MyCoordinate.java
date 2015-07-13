package com.skyline.trumpet.trumpetapp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/6.
 */
public class MyCoordinate implements Serializable{
    private double myLatitude;
    private double myLongitude;

    public double getMyLatitude() {
        return myLatitude;
    }

    public void setMyLatitude(double myLatitude) {
        this.myLatitude = myLatitude;
    }

    public double getMyLongitude() {
        return myLongitude;
    }

    public void setMyLongitude(double myLongitude) {
        this.myLongitude = myLongitude;
    }



}
