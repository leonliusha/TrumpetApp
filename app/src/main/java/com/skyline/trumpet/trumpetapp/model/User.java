package com.skyline.trumpet.trumpetapp.model;

/**
 * Created by Administrator on 2015/7/28.
 */
public class User {
    private long id;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public User(){}

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public User(String userName, String password, String email, String phone){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public User(String userName, String password, String email, String phone, String avatarUrl){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
    }

    public User(long id,String userName, String password, String email, String phone){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }


}
