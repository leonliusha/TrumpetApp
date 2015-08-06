package com.skyline.trumpet.trumpetapp.task;


import android.os.AsyncTask;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.skyline.trumpet.trumpetapp.MainActivity;
import com.skyline.trumpet.trumpetapp.adapter.BroadcastAdapter;
import com.skyline.trumpet.trumpetapp.common.Constant;
import com.skyline.trumpet.trumpetapp.model.Broadcast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/8/2.
 */
public class GetBroadcastsByDateTask extends AsyncTask<Void,Void,Broadcast[]> {
    final static String TAG = GetBroadcastsByDateTask.class.getSimpleName();
    private MainActivity mainActivity;
    private String url;
    private Broadcast[] broadcasts;

    public GetBroadcastsByDateTask(MainActivity activity,String url){
        this.mainActivity = activity;
        this.url = url;
    }
    @Override
    protected void onPreExecute(){

    }

    @Override
    protected Broadcast[] doInBackground(Void... params) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            broadcasts = restTemplate.getForObject(url, Broadcast[].class);
            //broadcast = restTemplate.getForObject(url, (Class<List<Boradcast>>)(Class<?>)List.class);
            return broadcasts;
        }catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Broadcast[] broadcasts){
        BroadcastAdapter adapter = new BroadcastAdapter(mainActivity,Arrays.asList(broadcasts),mainActivity.getCache());
        /*******  this call back function is not elegant, recoding following  */
        ListFragment homeFragment = (ListFragment)mainActivity.fragments.get(Constant.HOME_FRAGMENT);
        homeFragment.setListAdapter(adapter);
    }

}
