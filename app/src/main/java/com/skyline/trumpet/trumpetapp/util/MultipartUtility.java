package com.skyline.trumpet.trumpetapp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/6.
 */
public class MultipartUtility {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpURLConnection;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter printWriter;

    public MultipartUtility(String requestURL, String charset) throws IOException{
        this.charset = charset;
        boundary = "===" + System.currentTimeMillis() + "===";
        URL url = new URL(requestURL);
        httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        httpURLConnection.setRequestProperty("User-Agent", "Android HTTP Client");
        outputStream = httpURLConnection.getOutputStream();
        printWriter = new PrintWriter(new OutputStreamWriter(outputStream, charset),true);
    }

    /********Add a form field to the request************/
    public void addFormField(String name, String value){
        printWriter.append("--"+boundary).append(LINE_FEED);
        printWriter.append("Content-Disposition:form-data;name=\""+name+"\"").append(LINE_FEED);
        printWriter.append("Content-Type:text/plain; charset="+charset).append(LINE_FEED);
        printWriter.append(LINE_FEED);
        printWriter.append(value).append(LINE_FEED);
        printWriter.flush();
    }


    /********Add a upload file section to request*********/
    public void addFilePart(String fieldName,File uploadFile)throws IOException{
        String fileName = uploadFile.getName();
        printWriter.append("--"+boundary).append(LINE_FEED);
        printWriter.append("Content-Disposition: form-data; name=\""+fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
        printWriter.append("Content-Type: "+ URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
        printWriter.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        printWriter.append(LINE_FEED);
        printWriter.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while((bytesRead = inputStream.read(buffer)) != -1){
            outputStream.write(buffer,0,bytesRead);
        }
        outputStream.flush();
        inputStream.close();
        printWriter.append(LINE_FEED);
        printWriter.flush();
    }

    /********add a header field to the request**************/
    public void addHeaderField(String name, String value){
        printWriter.append(name + ": "+value).append(LINE_FEED);
        printWriter.flush();
    }


    /*******complete the request and receives response from server*
     * this method must be invoked lastly to complete the request and
     * recieve the response from server***/
    public List<String> finish() throws IOException{
        List<String> response = new ArrayList<>();
        printWriter.append(LINE_FEED).flush();
        printWriter.append("--" + boundary + "--").append(LINE_FEED);
        printWriter.close();

        int status = httpURLConnection.getResponseCode();
        if(status == HttpURLConnection.HTTP_OK){
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = null;
            while((line = reader.readLine()) != null){
                response.add(line);
            }
            reader.close();
            httpURLConnection.disconnect();
        }else{
            throw new IOException("Server returned non-OK status: "+status);
        }
        return response;

    }
}


