package com.skyline.trumpet.trumpetapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skyline.trumpet.trumpetapp.common.UserLocalStore;


public class UserHomePageFragment extends Fragment implements View.OnClickListener{
    TextView tvLogout;
    UserLocalStore userLocalStore;
    Activity mainActivity;
    View view;

    public UserHomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
        userLocalStore = new UserLocalStore(mainActivity.getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_user_home_page, container, false);
        tvLogout = (TextView)view.findViewById(R.id.tv_logout);
        tvLogout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_logout:
                userLocalStore.clearLocalUserData();
                userLocalStore.setLoginStatus(false);
                //startActivity(new Intent(this,MainActivity.class));
                //finish();
                //startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
