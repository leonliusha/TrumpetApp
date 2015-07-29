package com.skyline.trumpet.trumpetapp.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.skyline.trumpet.trumpetapp.model.User;

/**
 * Created by Administrator on 2015/7/29.
 */
public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocal;

    public UserLocalStore(Context context){
        userLocal = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocal.edit();
        spEditor.putLong("id",user.getId());
        spEditor.putString("userName",user.getUserName());
        spEditor.putString("password",user.getPassword());
        spEditor.putString("email",user.getEmail());
        spEditor.putString("phone",user.getPhone());
        spEditor.commit();
    }

    public User getUserLoggedIn(){
        long id = userLocal.getLong("id", -1);
        String userName = userLocal.getString("userName", "");
        String password = userLocal.getString("password", "");
        String email = userLocal.getString("email","");
        String phone = userLocal.getString("phone","");
        User storedUser = new User(id,userName,password,email,phone);
        return storedUser;
    }

    public void setLoginStatus(boolean status){
        SharedPreferences.Editor spEditor = userLocal.edit();
        spEditor.putBoolean("loggedIn",status);
        spEditor.commit();
    }

    public boolean getLoginStatus(){
        boolean loggedIn = userLocal.getBoolean("loggedIn",false);
        return loggedIn;
    }

    public void clearLocalUserData(){
        SharedPreferences.Editor spEditor = userLocal.edit();
        spEditor.clear();
        spEditor.commit();
    }

}
