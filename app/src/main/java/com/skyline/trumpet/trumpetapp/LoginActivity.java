package com.skyline.trumpet.trumpetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.skyline.trumpet.trumpetapp.common.UserLocalStore;
import com.skyline.trumpet.trumpetapp.model.User;
import com.skyline.trumpet.trumpetapp.task.UserRegisterLoginTask;

import org.springframework.http.MediaType;


public class LoginActivity extends Activity implements View.OnClickListener{

    TextView tvRegister;
    Button btnBack, btnLogin;
    EditText etUserName, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvRegister = (TextView)findViewById(R.id.tv_register);
        etUserName = (EditText)findViewById(R.id.et_userName);
        etPassword = (EditText)findViewById(R.id.et_password);
        btnBack = (Button)findViewById(R.id.btn_back);
        btnLogin = (Button)findViewById(R.id.btn_login);

        tvRegister.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        switch (v.getId()){
            case R.id.tv_register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.btn_login:
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                User user = new User(userName,password);
                String url = getString(R.string.base_uri) + "/userLogin";
                UserLocalStore userLocalStore = new UserLocalStore(this);
                new UserRegisterLoginTask(user,url,userLocalStore).execute(MediaType.APPLICATION_JSON);
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.btn_back:
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }


    }
}
