package com.skyline.trumpet.trumpetapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyline.trumpet.trumpetapp.R;
import com.skyline.trumpet.trumpetapp.model.Broadcast;
import com.skyline.trumpet.trumpetapp.task.AsyncGetImageTask;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2015/8/2.
 */
public class BroadcastAdapter extends ArrayAdapter<Broadcast>{

    private Activity mainActivity;
    private File cache;
    public BroadcastAdapter(Activity mainActivity,List<Broadcast> broadcasts,File cache){
        super(mainActivity,0,broadcasts);
        this.mainActivity = mainActivity;
        this.cache = cache;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent){
        if(converView == null)
            converView = mainActivity.getLayoutInflater().inflate(R.layout.broadcast_list,null);
        Broadcast broadcast = getItem(position);
        TextView author = (TextView)converView.findViewById(R.id.tv_author);
        author.setText(broadcast.getAuthor());
        TextView briefContent = (TextView)converView.findViewById(R.id.tv_briefContent);
        briefContent.setText(broadcast.getBrief());
        ImageView img_avatar = (ImageView)converView.findViewById(R.id.img_avatar);
        //get picture
        AsyncGetImageTask getImageTask = new AsyncGetImageTask(img_avatar,cache);
        getImageTask.execute(broadcast.getAvaterUrl());
        return converView;
    }


}
