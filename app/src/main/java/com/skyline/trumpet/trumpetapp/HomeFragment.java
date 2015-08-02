package com.skyline.trumpet.trumpetapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.skyline.trumpet.trumpetapp.model.Broadcast;
import com.skyline.trumpet.trumpetapp.task.GetBroadcastsByDateTask;


public class HomeFragment extends ListFragment {

    private Broadcast[] broadcasts;
    private String url;
    private MainActivity mainActivity;
    private com.skyline.trumpet.trumpetapp.adapter.BroadcastAdapter broadcastAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity)getActivity();
        url = getString(R.string.base_uri) + "/getBroadcastsByDate";

        //retrieving broadcasts from backend
        /***               TO DO
         * this will be called every time, trying to story the retrieved broadcasts in local database*/
        new GetBroadcastsByDateTask(mainActivity,url).execute();

        //BroadcastAdapter adapter = new BroadcastAdapter(Arrays.asList(broadcasts));
        //setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onListItemClick(ListView listView,View v,int position,long id){
        Broadcast selectedBroadcast = (Broadcast)getListAdapter().getItem(position);
        Intent intent = new Intent(mainActivity,BroadcastInfoActivity.class);
        intent.putExtra("selectedBroadcast",selectedBroadcast);
        startActivity(intent);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
