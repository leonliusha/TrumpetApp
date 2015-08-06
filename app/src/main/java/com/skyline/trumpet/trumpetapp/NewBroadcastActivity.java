package com.skyline.trumpet.trumpetapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.skyline.trumpet.trumpetapp.common.Constant;
import com.skyline.trumpet.trumpetapp.common.DateTimePickerDialog;
import com.skyline.trumpet.trumpetapp.common.TagsSelectorDialog;
import com.skyline.trumpet.trumpetapp.common.UserLocalStore;
import com.skyline.trumpet.trumpetapp.database.DBManager;
import com.skyline.trumpet.trumpetapp.model.Broadcast;
import com.skyline.trumpet.trumpetapp.model.BroadcastTags;
import com.skyline.trumpet.trumpetapp.model.MyCoordinate;
import com.skyline.trumpet.trumpetapp.model.Tag;
import com.skyline.trumpet.trumpetapp.model.User;
import com.skyline.trumpet.trumpetapp.util.Bimp;
import com.skyline.trumpet.trumpetapp.util.FileUtils;
import com.skyline.trumpet.trumpetapp.util.ImageItem;
import com.skyline.trumpet.trumpetapp.util.MultipartUtility;
import com.skyline.trumpet.trumpetapp.util.PublicWay;
import com.skyline.trumpet.trumpetapp.util.Res;

import org.joda.time.DateTime;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NewBroadcastActivity extends Activity {
    final static String TAG = BroadcastActivity.class.getSimpleName();
    private DateFormat dtfOut = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private Button btn_sendBroadcast;
    private MyCoordinate myCoordinate;
    //private EditText etBriefContent;
    EditText etDescriptionContent;
    private Toolbar toolbar;
    private TextView tv_expireDate, tv_sendBroadcast;
    private LinearLayout layout_changeTime;
    private LinearLayout ll_menuFireBroadcast;
    private String defaultExpireDateTime_chinese;
    private DBManager dbManager;
    private Tag[] tags;
    private Integer[] selectedTagId;
    private BroadcastTags broadcastTags;
    private UserLocalStore userLocalStore;
    private Broadcast broadcast;
    private User user;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_broadcast);
        parentView = getLayoutInflater().inflate(R.layout.activity_new_broadcast, null);
        setContentView(parentView);

        Res.init(this);
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
        PublicWay.activityList.add(this);
        //the Main view, should replace activity_new_broadcast.xml with activity_selectimg
        broadcast = new Broadcast();
        userLocalStore = new UserLocalStore(this);
        //dbManager = new DBManager(this);
        //btn_sendBroadcast = (Button)findViewById(R.id.btn_sendBroadcast);
        //btn_sendBroadcast.setEnabled(false);
        tv_sendBroadcast = (TextView)findViewById(R.id.tv_sendBroadcast);
        //initToolbar();
        //initMenuListener();
        Init();
        updateLocalTags();
        initMyCoordinate();
        //sendBroadcastButtonListener();
        initSendBroadcastListener();
        initDescriptionContentListener();
        initDateTimePicker();
        initTagsSelectListener();
    }

    public void Init() {
        pop = new PopupWindow(NewBroadcastActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button btn_camera = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button btn_photo = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button btn_cancel = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        btn_photo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NewBroadcastActivity.this,
                        AlbumActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        btn_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    Log.i("ddddddd", "----------");
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(NewBroadcastActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(NewBroadcastActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });

    }

    private void updateLocalTags(){
        //String url = getString(R.string.base_uri) + "/getTags";
        new getTagsTask().execute();
    }

    private void initMyCoordinate(){
        myCoordinate = (MyCoordinate)getIntent().getSerializableExtra("myCoordinate");
    }

    private void initSendBroadcastListener(){
        tv_sendBroadcast.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String charset = "UTF-8";
                String requestURL = getString(R.string.base_uri) + "/newBroadcastWithPhoto";
//        broadcast.setUserId(user.getId());
//        broadcast.setAuthor(user.getUserName());
//        broadcast.setType(Constant.BROADCAST_GALLERY);
//        broadcast.setDescription(etDescriptionContent.getText().toString());
//        broadcast.setLatitude(myCoordinate.getMyLatitude());
//        broadcast.setLongitude(myCoordinate.getMyLongitude());
//        broadcast.setAvatarUrl(user.getAvatarUrl());
                List<File> uploadPhotos = new ArrayList<>();
                for(ImageItem imageItem : Bimp.tempSelectBitmap)
                    uploadPhotos.add(new File(imageItem.imagePath));
                try{
                    MultipartUtility multipartUtility = new MultipartUtility(requestURL,charset);
                    multipartUtility.addHeaderField("User-Agent", "Android App Client");
                    multipartUtility.addHeaderField("Test-Header", "Header-value");

                    multipartUtility.addFormField("userId",String.valueOf(user.getId()));
                    multipartUtility.addFormField("author",user.getUserName());
                    multipartUtility.addFormField("type",String.valueOf(Constant.BROADCAST_GALLERY));
                    if(etDescriptionContent.getText() != null && !etDescriptionContent.getText().toString().trim().equals(""))
                        multipartUtility.addFormField("description",etDescriptionContent.getText().toString());
                    else
                        multipartUtility.addFormField("description","");
                    multipartUtility.addFormField("latitude",String.valueOf(myCoordinate.getMyLatitude()));
                    multipartUtility.addFormField("longitude",String.valueOf(myCoordinate.getMyLongitude()));
                    multipartUtility.addFormField("avatarUrl",user.getAvatarUrl());

                    for(File file:uploadPhotos)
                        multipartUtility.addFilePart("fileUpload",file);

                    List<String> response = multipartUtility.finish();

                    for(String line:response)
                        System.out.println(line);

                    finish();

                }catch(IOException e){
                    System.err.println(e);
                }
            }
        });


    }

    private void sendBroadcastButtonListener(){
        //btn_sendBroadcast = (Button)findViewById(R.id.btn_sendBroadcast);
        tv_sendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewBroadcastActivity.this, R.string.send_toast, Toast.LENGTH_SHORT).show();
                //send Broadcast message to backend
                new PostBroadcastTask().execute(MediaType.APPLICATION_JSON);
                finish();
            }
        });
    }

    private void initDescriptionContentListener(){
        etDescriptionContent = (EditText)findViewById(R.id.et_descriptionContent);
        etDescriptionContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    //btn_sendBroadcast.setEnabled(false);
                    tv_sendBroadcast.setTextColor(getResources().getColor(R.color.grey));
                    tv_sendBroadcast.setEnabled(false);
                }
                else {
                    tv_sendBroadcast.setTextColor(getResources().getColor(R.color.white));
                    tv_sendBroadcast.setEnabled(true);
                    //btn_sendBroadcast.setEnabled(true);
                }
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
                        NewBroadcastActivity.this, tv_expireDate.getText().toString());
                dateTimePicKerDialog.dateTimePicKerDialog(tv_expireDate);
            }
        });
    }

    private void initTagsSelectListener(){
        final int tagsDialogFontColor = getResources().getColor(R.color.highlightColor);
        LinearLayout layout_selectTags = (LinearLayout)findViewById(R.id.ll_adding_tag);
        final TextView tv_selectedTags = (TextView)findViewById(R.id.tv_selected_tags);
        final String title = getResources().getString(R.string.tags_selector_title);
        layout_selectTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // List<Tag> tagList = dbManager.selectAll();
                TagsSelectorDialog tagsDialog = new TagsSelectorDialog(NewBroadcastActivity.this, title, Arrays.asList(tags), tagsDialogFontColor, broadcast);
                tagsDialog.initDialog(tv_selectedTags, getApplicationContext());
            }
        });

    }


    private class PostBroadcastTask extends AsyncTask<MediaType, Void, Broadcast> {
        @Override
        //preparing the Broadcast entity
        protected void onPreExecute(){
            etDescriptionContent = (EditText)findViewById(R.id.et_descriptionContent);
            //etBriefContent = (EditText)findViewById(R.id.et_briefContent);
            //TextView tvSelectedTags = (TextView)findViewById(R.id.tv_selected_tags);
            //EditText et_quantity = (EditText)findViewById(R.id.et_quantity);
            //broadcast.setBrief(etBriefContent.getText().toString());
            //broadcast.setTags(tvSelectedTags.getText().toString());
            //broadcast.setAmount(new Integer(et_quantity.getText().toString()));
//            Date expireDate = new Date();
//            try {
//                expireDate = dtfOut.parse(tv_expireDate.getText().toString());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
           // broadcast.setExpireDate((new Timestamp(expireDate.getTime())));
            broadcast.setUserId(user.getId());
            broadcast.setAuthor(user.getUserName());
            broadcast.setType(Constant.BROADCAST_GALLERY);
            broadcast.setDescription(etDescriptionContent.getText().toString());
            broadcast.setLatitude(myCoordinate.getMyLatitude());
            broadcast.setLongitude(myCoordinate.getMyLongitude());
            broadcast.setAvatarUrl(user.getAvatarUrl());


            //broadcastTags = new BroadcastTags(broadcast,selectedTagId);

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
                //List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
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
//            System.out.println("my first broadcast brief is: " + broadcast.getBrief());
//            System.out.println("my first broadcast description is: " + broadcast.getDescription());
//            System.out.println("the created date is : " + broadcast.getExpireDate());
        }

        /*****Creating a new objectMapper for serialize Joda Datetime to JSON.*/
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

    private class getTagsTask extends AsyncTask<Void, Void, Tag[]>{
        @Override
        protected Tag[] doInBackground(Void... params) {
            try {
                final String url = getString(R.string.base_uri) + "/getTags";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                tags = restTemplate.getForObject(url, Tag[].class);
                //broadcast = restTemplate.getForObject(url, (Class<List<Boradcast>>)(Class<?>)List.class);
                return tags;
            }catch(Exception e){
                Log.e(TAG,e.getMessage(),e);
            }
            return null;
        }

        @Override
        protected  void onPostExecute(Tag[] tags){
            //dbManager.removeAll();
            //dbManager.addTags(Arrays.asList(tags));
        }
    }



    //the adapter for getting the layout view for each selected pic
    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if(Bimp.tempSelectBitmap.size() == Constant.MAX_PICTURE){
                return Constant.MAX_PICTURE;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                if (position == Constant.MAX_PICTURE) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    private static final int TAKE_PICTURE = 0x000001;

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    //call back method for taking photo
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < Constant.MAX_PICTURE && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);

                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for(int i=0;i<PublicWay.activityList.size();i++){
                if (null != PublicWay.activityList.get(i)) {
                    PublicWay.activityList.get(i).finish();
                }
            }
            System.exit(0);
        }
        return true;
    }

//    @Override
//    protected void onStart(){
//        super.onStart();
//
//        if(userLocalStore.getLoginStatus()){
//            user = userLocalStore.getUserLoggedIn();
//        }
//        else{
//            startActivity(new Intent(this,LoginActivity.class));
//            finish();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_broadcast, menu);
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
