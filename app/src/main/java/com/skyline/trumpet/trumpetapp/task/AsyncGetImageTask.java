package com.skyline.trumpet.trumpetapp.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.skyline.trumpet.trumpetapp.common.MD5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/8/5.
 */
public class AsyncGetImageTask extends AsyncTask<String, Void, Uri> {
    private ImageView imageView;
    private File cache;
    public AsyncGetImageTask(ImageView imageView, File cache){
        this.imageView = imageView;
        this.cache = cache;
    }

    @Override
    protected Uri doInBackground(String... params) {
        URL url;
        String path = params[0];
        String name = MD5.getMD5(path) + path.substring(path.lastIndexOf("."));
        File file = new File(cache, name);
        //if file already exists in cache
        if(file.exists())
            return Uri.fromFile(file);
        else{
            //get pictures from backend
            try {
                url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                if(connection.getResponseCode() == 200){
                    InputStream inputStream = connection.getInputStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len =0;
                    while((len = inputStream.read(buffer))!=1)
                        fileOutputStream.write(buffer,0,len);
                    inputStream.close();
                    fileOutputStream.close();
                    return Uri.fromFile(file);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(Uri result){
        super.onPostExecute(result);
        if(imageView != null && result != null)
            imageView.setImageURI(result);
    }
}
