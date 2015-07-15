package com.skyline.trumpet.trumpetapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.skyline.trumpet.trumpetapp.common.DateTimePickerDialog;
import com.skyline.trumpet.trumpetapp.model.Broadcast;
import com.skyline.trumpet.trumpetapp.model.MyCoordinate;

import org.joda.time.DateTime;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class BroadcastActivity extends AppCompatActivity {
    final static String TAG = BroadcastActivity.class.getSimpleName();
    private DateFormat dtfOut = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private Button btn_sendBroadcast;
    private MyCoordinate myCoordinate;
    private EditText etBriefContent;
    private Toolbar toolbar;
    private TextView tv_expireDate;
    private LinearLayout layout_changeTime;

    private String defaultExpireDateTime_chinese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        btn_sendBroadcast = (Button)findViewById(R.id.btn_sendBroadcast);
        btn_sendBroadcast.setEnabled(false);
        initToolbar();
        initMyCoordinate();
        sendBroadcastButtonListener();
        initBriefContentListener();
        initDateTimePicker();

        //dataSettingForTest();
    }

    private void initToolbar(){
        toolbar = (Toolbar)findViewById(R.id.broadcastToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//                switch (item.getItemId()) {
//                    case R.id.menu_fire_broadcast:
//                        fireBroadcastButtonListener();
//                        return true;
//                    case R.id.menu_retrieve_broadcast:
//                        retrieveBroadcastButtonListener();
//                        return true;
//                }
                return false;
            }
        });
    }

    private void dataSettingForTest(){
        TextView tvCoordinate = (TextView) findViewById(R.id.tv_brief);
        tvCoordinate.setText(myCoordinate.getMyLatitude() + " " + myCoordinate.getMyLongitude());
    }

    private void initMyCoordinate(){
        myCoordinate = (MyCoordinate)getIntent().getSerializableExtra("myCoordinate");
    }

    private void sendBroadcastButtonListener(){
        //btn_sendBroadcast = (Button)findViewById(R.id.btn_sendBroadcast);
        btn_sendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(BroadcastActivity.this,R.string.send_toast,Toast.LENGTH_SHORT).show();
                //send Broadcast message to backend
                new PostBroadcastTask().execute(MediaType.APPLICATION_JSON);
                finish();
            }
        });
    }

    private void initBriefContentListener(){
        etBriefContent = (EditText)findViewById(R.id.et_briefContent);
        etBriefContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(""))
                    btn_sendBroadcast.setEnabled(false);
                else
                    btn_sendBroadcast.setEnabled(true);
            }
        });
    }

    private void initDateTimePicker(){
        DateTime expireDate = new DateTime().plusDays(1);
        defaultExpireDateTime_chinese = expireDate.toString("yyyy年MM月dd日 HH:mm");
        layout_changeTime = (LinearLayout) findViewById(R.id.ll_change_time);
        tv_expireDate = (TextView) findViewById(R.id.tv_expire_date);
        tv_expireDate.setText(defaultExpireDateTime_chinese);
        //endDateTime.setText(initEndDateTime);
        layout_changeTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateTimePickerDialog dateTimePicKerDialog = new DateTimePickerDialog(
                        BroadcastActivity.this, tv_expireDate.getText().toString());
                dateTimePicKerDialog.dateTimePicKerDialog(tv_expireDate);
            }
        });
    }

    public void showTimePickerDialog(View view){
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getFragmentManager(),"timePicker");

    }

    private class PostBroadcastTask extends AsyncTask<MediaType, Void, Broadcast> {
        private Broadcast broadcast = new Broadcast();

        @Override
        //preparing the Broadcast entity
        protected void onPreExecute(){
            //etBriefContent = (EditText)findViewById(R.id.et_briefContent);
            EditText etDescriptionContent = (EditText)findViewById(R.id.et_descriptionContent);
            broadcast.setLatitude(myCoordinate.getMyLatitude());
            broadcast.setLongitude(myCoordinate.getMyLongitude());
            broadcast.setBrief(etBriefContent.getText().toString());
            broadcast.setDescription(etDescriptionContent.getText().toString());
            Date expireDate = new Date();
            try {
                expireDate = dtfOut.parse(tv_expireDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            broadcast.setExpireDate((new Timestamp(expireDate.getTime())));

        }

        //POST request with Broadcast entity
        @Override
        protected Broadcast doInBackground(MediaType... params){
            try {
                if (params.length == 0) {
                    return null;
                }
                MediaType mediaType = params[0];
                final String url = getString(R.string.base_uri) + "/newBroadcast";
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(mediaType);
                RestTemplate restTemplate = new RestTemplate();
                //marshalling the Broadcast object to JSON
                List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
                //MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter)converters.get(5);
                //mappingJackson2HttpMessageConverter(jacksonConverter);
                /***DO NOT need to Add a new MappingJackson2HttpMessageConverter to converters, its already added to the converter list. New Feature from Spring  **/
                //converters.add(jacksonConverter);
                //Broadcast response = restTemplate.postForObject(url,broadcast,Broadcast.class);
                //return response;

                /**********The following code using the HttpEntity and causing the dumplicate HttpEntity error
                 *          If add Spring-Web jar to class path
                 * *************/
                HttpEntity<Broadcast> requestEntity = new HttpEntity<>(broadcast, requestHeaders);
                ResponseEntity<Broadcast> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Broadcast.class);
                return responseEntity.getBody();
            }catch(Exception e){
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Broadcast broadcast){
            System.out.println("my first broadcast brief is: " + broadcast.getBrief());
            System.out.println("my first broadcast description is: " + broadcast.getDescription());
            System.out.println("the created date is : " + broadcast.getExpireDate());
        }

        /*****Creating a new objectMapper for serialise Joda Datetime to JSON.*/
        private ObjectMapper objectMapper(){
            //there is no need to create an Objectmapper from an MapperFactoryBean()

            //Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
            //ObjectMapper objectMapper = bean.getObject();

            ObjectMapper objectMapper = new ObjectMapper();
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(df);
            objectMapper.registerModule(new JodaModule());
           return objectMapper;
        }

        private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(MappingJackson2HttpMessageConverter converter){
            //MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setObjectMapper(objectMapper());
            return converter;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_broadcast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
