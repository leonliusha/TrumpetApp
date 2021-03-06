package com.skyline.trumpet.trumpetapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skyline.trumpet.trumpetapp.model.Broadcast;


public class BroadcastInfoActivity extends Activity {
    private Button btn_back;
    private TextView tv_briefContent;
    private TextView tv_descriptionContent;
    private Broadcast selectedBroadcast;
    private TextView tv_expireDate;
    private TextView tv_selectedTags;
    private TextView tv_quantity;
    private TextView tv_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_info);

        loadSelectedBroadcast();
        goBackButtonListener();
    }

    private void loadSelectedBroadcast() {
        selectedBroadcast = (Broadcast)getIntent().getSerializableExtra("selectedBroadcast");
        tv_briefContent = (TextView)findViewById(R.id.tv_briefContent);
        tv_descriptionContent = (TextView)findViewById(R.id.tv_descriptionContent);
        tv_expireDate = (TextView)findViewById(R.id.tv_expire_date);
        tv_selectedTags = (TextView)findViewById(R.id.tv_selected_tags);
        tv_quantity = (TextView)findViewById(R.id.tv_quantity);
        tv_count = (TextView)findViewById(R.id.tv_count);
        if(selectedBroadcast!=null) {
            tv_briefContent.setText(selectedBroadcast.getBrief());
            tv_descriptionContent.setText(selectedBroadcast.getDescription());
            tv_expireDate.setText(selectedBroadcast.getExpireDate().toString());
            tv_selectedTags.setText(selectedBroadcast.getTags());
            tv_quantity.setText(String.valueOf(selectedBroadcast.getAmount()));
            tv_count.setText(String.valueOf(selectedBroadcast.getCount()));
        }
    }

    private void goBackButtonListener() {
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_broadcast_info, menu);
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
