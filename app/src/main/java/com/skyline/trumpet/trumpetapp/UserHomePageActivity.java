package com.skyline.trumpet.trumpetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.skyline.trumpet.trumpetapp.common.UserLocalStore;


public class UserHomePageActivity extends Activity implements View.OnClickListener{

    TextView tvLogout;
    UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        userLocalStore = new UserLocalStore(this);
        tvLogout = (TextView)findViewById(R.id.tv_logout);
        tvLogout.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_home_page, menu);
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_logout:
                userLocalStore.clearLocalUserData();
                userLocalStore.setLoginStatus(false);
                //startActivity(new Intent(this,MainActivity.class));
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }
}
