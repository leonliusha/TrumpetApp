package com.skyline.trumpet.trumpetapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.skyline.trumpet.trumpetapp.model.User;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2015/8/6.
 */
public class GetUserByIdTask extends AsyncTask<Void,Void,User> {
    User user;
    String url;
    public static final String TAG = GetUserByIdTask.class.getSimpleName();
    @Override
    protected User doInBackground(Void... params) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            user = restTemplate.getForObject(url, User.class);
            //broadcast = restTemplate.getForObject(url, (Class<List<Boradcast>>)(Class<?>)List.class);
            return user;
        }catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
