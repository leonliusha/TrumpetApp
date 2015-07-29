package com.skyline.trumpet.trumpetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.skyline.trumpet.trumpetapp.common.UserLocalStore;
import com.skyline.trumpet.trumpetapp.model.User;
import com.skyline.trumpet.trumpetapp.task.UserRegisterLoginTask;

import org.springframework.http.MediaType;


public class RegisterActivity extends Activity implements View.OnClickListener{
    Button btnRegister, btnBack;
    EditText etUserName, etPassword, etEmail, etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = (EditText)findViewById(R.id.et_userName);
        etPassword = (EditText)findViewById(R.id.et_password);
        etEmail = (EditText)findViewById(R.id.et_email);
        etPhone = (EditText)findViewById(R.id.et_phone);
        btnBack = (Button)findViewById(R.id.btn_back);
        btnRegister = (Button)findViewById(R.id.btn_register);

        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_register:
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                User user = new User(userName,password,email,phone);
                String url = getString(R.string.base_uri) + "/userRegister";
                UserLocalStore userLocalStore = new UserLocalStore(this);
                new UserRegisterLoginTask(user,url,userLocalStore).execute(MediaType.APPLICATION_JSON);
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }

    }
}
