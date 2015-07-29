package com.skyline.trumpet.trumpetapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.skyline.trumpet.trumpetapp.common.UserLocalStore;
import com.skyline.trumpet.trumpetapp.model.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2015/7/29.
 */
public class UserRegisterLoginTask extends AsyncTask<MediaType,Void,User>{
    final static String TAG = UserRegisterLoginTask.class.getSimpleName();
    private User user;
    private String url;
    private UserLocalStore userLocalStore;

    public UserRegisterLoginTask(User user,String url, UserLocalStore userLocalStore){
        this.user = user;
        this.url = url;
        this.userLocalStore = userLocalStore;
    }
    @Override
    protected void onPreExecute(){

    }

    @Override
    protected User doInBackground(MediaType... params) {
        try{
            if(params.length== 0)
                return null;
            MediaType mediaType = params[0];
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(mediaType);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<User> requestEntity = new HttpEntity<>(user, requestHeaders);
            ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class);
            return responseEntity.getBody();
        }catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
            return null;
    }

    @Override
    protected void onPostExecute(User user){
        if(user != null && user.getId() > 0){
            userLocalStore.storeUserData(user);
            userLocalStore.setLoginStatus(true);
        }
    }
}
