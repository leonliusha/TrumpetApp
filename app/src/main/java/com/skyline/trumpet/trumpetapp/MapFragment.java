package com.skyline.trumpet.trumpetapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyline.trumpet.trumpetapp.common.UserLocalStore;
import com.skyline.trumpet.trumpetapp.model.Broadcast;
import com.skyline.trumpet.trumpetapp.model.MyCoordinate;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptor;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapFragment extends Fragment implements TencentMap.OnMarkerDraggedListener,View.OnClickListener {

    final static String TAG = "MainActivity";
    private View view;
    private MapView mapView;
    private TencentMap tencentMap;
    private MyCoordinate myCoordinate;
    //private double myLongitude;
    //private Marker markerFix;
    //private Marker markerLongPress;
    private Marker myMarker;
    //private Marker broadcastMarker;
    private Context context ;
    private TencentLocationListener locationListener;
    private TencentMap.OnInfoWindowClickListener infoWindowClickListener;
    private TencentLocationRequest locationRequest;
    private TencentLocationManager locationManager;
    private ProgressDialog progressDialog;
    private boolean destroyed = false;
    private Map<Marker,Broadcast> markerBroadcastMap;
    private Set<Broadcast> markedBroadcast;
    private Marker openedMarker;
   // private Toolbar toolbar;
    private LinearLayout clickedLayout, ll_menuHome, ll_menuFireBroadcast, ll_menuLocate, ll_menuRetrieveBroadcast, ll_menuFriends;
    private UserLocalStore userLocalStore;
    private Button btnRetrieveBroadcast;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        userLocalStore = new UserLocalStore(this.getActivity());
        btnRetrieveBroadcast = (Button)view.findViewById(R.id.btn_retrieveBroadcast);
        btnRetrieveBroadcast.setOnClickListener(this);
        //dbManager = new DBManager(this);
        //updateLocalTags();
        //initToolbarListener();
        //initMenuListener();
        initLongClickListener();
        mapView.onCreate(savedInstanceState);
        initOnMarkerClickListener();
        initInfoWindowLayout();
        positionServiceInit();
        //fireBroadcastButtonListener();
        //retrieveBroadcastButtonListener();
        inforWindowClickListener();

        return view;
    }


//    private void initToolbarListener(){
//        toolbar = (Toolbar)getView().findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//                switch (item.getItemId()) {
//                    case R.id.toolbar_myself:
//                        if(userLocalStore.getLoginStatus())
//                            startActivity(new Intent(getApplicationContext(), UserHomePageActivity.class));
//                        else
//                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
//                        break;
//                    case R.id.toolbar_search:
//
//                        break;
//                }
//                return true;
//            }
//        });
//
//    }


//    private void initMenuListener(){
//        ll_menuRetrieveBroadcast = (LinearLayout)findViewById(R.id.menu_retrieve_broadcast);
//        ll_menuRetrieveBroadcast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(clickedLayout != null)
//                    clickedLayout.setBackgroundColor(getResources().getColor(R.color.menu_normal));
//                clickedLayout = ll_menuRetrieveBroadcast;
//                //ll_menuRetrieveBroadcast.setBackgroundColor(getResources().getColor(R.color.menu_highlight));
//                retrieveBroadcastButtonListener();
//            }
//        });
//
//        ll_menuFireBroadcast = (LinearLayout)findViewById(R.id.menu_fire_broadcast);
//        ll_menuFireBroadcast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (clickedLayout != null)
//                    clickedLayout.setBackgroundColor(getResources().getColor(R.color.menu_normal));
//                clickedLayout = ll_menuFireBroadcast;
//                // ll_menuFireBroadcast.setBackgroundColor(getResources().getColor(R.color.menu_highlight));
//                fireBroadcastButtonListener();
//            }
//        });
//    }


    //only myMarker has LongClickListener ONLY!!
    private void initLongClickListener(){
        mapView = (MapView)view.findViewById(R.id.mapview);
        tencentMap = mapView.getMap();

        markerBroadcastMap = new HashMap<>();
        markedBroadcast = new HashSet<>();
        //tencentMap.setZoom(5);
        //tencentMap.setOnMarkerDraggedListener(this);
        TencentMap.OnMapLongClickListener mapLongClickListener = new TencentMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(myMarker == null){
                    myMarker = tencentMap.addMarker(new MarkerOptions().draggable(true)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker())
                            .position(latLng)
                            .title("myMarker")
                            .snippet(latLng.toString()));

                } else{
                    myMarker.setPosition(latLng);
                    myMarker.setSnippet(latLng.toString());
                }
                myMarker.showInfoWindow();
                myCoordinate.setMyLatitude(latLng.getLatitude());
                myCoordinate.setMyLongitude(latLng.getLongitude());
            }
        };
        tencentMap.setOnMapLongClickListener(mapLongClickListener);
    }

    private void initOnMarkerClickListener(){
        TencentMap.OnMarkerClickListener markerClickListener = new TencentMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(openedMarker == null){
                    openedMarker = marker;
                } else{
                    openedMarker.hideInfoWindow();
                    openedMarker = marker;
                }
                marker.showInfoWindow();
                return false;
            }
        };
        tencentMap.setOnMarkerClickListener(markerClickListener);
    }


    //initiate and register the Tencent Position service
    private void positionServiceInit(){
        myCoordinate = new MyCoordinate();
        context = this.getActivity().getApplicationContext();
        locationListener = new TencentLocationListener(){
            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int error, String reason){
                if(TencentLocation.ERROR_OK == error){
                    //get location success
                    Log.i(TAG, tencentLocation.getAddress());
                    //myLatitude = tencentLocation.getLatitude();
                    //myLongitude = tencentLocation.getLongitude();
                    myCoordinate.setMyLatitude(tencentLocation.getLatitude());
                    myCoordinate.setMyLongitude(tencentLocation.getLongitude());
                    //tencentMap.animateTo(new LatLng(latitude,longitude));
                    tencentMap.setCenter(new LatLng(myCoordinate.getMyLatitude(),myCoordinate.getMyLongitude()));
                    markSelf(myCoordinate.getMyLatitude(), myCoordinate.getMyLongitude());
                }else{
                    //failed
                }
                stopLocation();
            }

            @Override
            public void onStatusUpdate(String name, int status, String desc){

            }
        };

        locationRequest = TencentLocationRequest.create();
        //set request level to REQ_LEVEL_POI
        locationRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        locationManager = TencentLocationManager.getInstance(context);
        //registering the TencentLocationListener
        int error = locationManager.requestLocationUpdates(locationRequest,locationListener);
        if(error == 1){
            System.out.println("the device does not meet the uses permissions");
        }
    }

    private void stopLocation(){
        locationManager.removeUpdates(locationListener);
    }



//    private void fireBroadcastButtonListener(){
//        btn_fireBroadcast = (Button)findViewById(R.id.btn_fireBroadcast);
//        btn_fireBroadcast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), BroadcastActivity.class);
//                intent.putExtra("myCoordinate", myCoordinate);
//                startActivity(intent);
//            }
//        });
//        Intent intent = new Intent(this.getActivity().getApplicationContext(), BroadcastActivity.class);
//        intent.putExtra("myCoordinate", myCoordinate);
//        startActivity(intent);
//    }

    private void retrieveBroadcastButtonListener(){
        String url = getString(R.string.base_uri) + "/getBroadcastsInDefaultRange";
        new GetBroadcastsTask().execute();
//        btn_retrieveBroadcast = (Button)findViewById(R.id.btn_retrieveBroadcast);
//        btn_retrieveBroadcast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new GetBroadcastsTask().execute();
//            }
//        });

    }

    private void inforWindowClickListener(){
        infoWindowClickListener = new TencentMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
//                marker.setSnippet("Infor window clicked");
//                marker.showInfoWindow();
                Intent intent = new Intent(getActivity(),BroadcastInfoActivity.class);
                intent.putExtra("selectedBroadcast",markerBroadcastMap.get(marker));
                startActivity(intent);
            }
        };
        tencentMap.setOnInfoWindowClickListener(infoWindowClickListener);
    }

    private void initInfoWindowLayout(){
        tencentMap.setInfoWindowAdapter(new TencentMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                //LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View infoWindowView = getActivity().getLayoutInflater().inflate(R.layout.layout_info_window, null);
                //Broadcast broadcast = markerBroadcastMap.get(marker);
                TextView tv_infowin_briefContent = (TextView) infoWindowView.findViewById(R.id.tv_infowin_briefContent);
                TextView tv_infowin_descriptionContent = (TextView) infoWindowView.findViewById(R.id.tv_infowin_descriptionContent);
                TextView tv_infowin_userName = (TextView) infoWindowView.findViewById(R.id.tv_infowin_userName);
                TextView tv_createdDate = (TextView) infoWindowView.findViewById(R.id.tv_createdDate);
                TextView tv_tags = (TextView) infoWindowView.findViewById(R.id.tv_tags);
                Broadcast selectedBroadcast = markerBroadcastMap.get(marker);
                if (selectedBroadcast != null) {
                    tv_infowin_briefContent.setText(selectedBroadcast.getBrief());
                    tv_infowin_descriptionContent.setText(selectedBroadcast.getDescription());
                    //Marker's title has the value of User Name;
                    tv_infowin_userName.setText(selectedBroadcast.getAuthor());
                    tv_tags.setText(selectedBroadcast.getTags());
                    tv_createdDate.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(selectedBroadcast.getCreatedDate()));
                    return infoWindowView;
                } else
                    return null;
            }

            @Override
            public void onInfoWindowDettached(Marker marker, View view) {

            }
        });
    }

    private void markSelf(double latitude, double longitude){
        LatLng myLocation = new LatLng(latitude, longitude);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.pinkicon128);
        //MarkerOptions markerOpt = new MarkerOptions().icon(bitmap).draggable(false).position(new LatLng(31.215109,121.432968));
        //Marker marker2 = tencentMap.addMarker(markerOpt);
        if(myMarker == null) {
            myMarker = tencentMap.addMarker(new MarkerOptions().position(myLocation)
                    .title("上海")
                    .snippet("我在这里")
                    .anchor(0.5f, 0.5f)
                            //.icon(BitmapDescriptorFactory.defaultMarker())
                    .icon(bitmap)
                    .draggable(true));
        }
        //myMarker.showInfoWindow();
        //setting the animation effects
        myMarker.setInfoWindowShowAnimation(R.anim.show_infowindow_anim);
        myMarker.setInfoWindowHideAnimation(R.anim.hide_infowindow_anim);
        tencentMap.setZoom(17);
    }

    private void markBroadcast(Broadcast broadcast){
        Marker broadcastMarker = tencentMap.addMarker(new MarkerOptions().position(new LatLng(broadcast.getLatitude(), broadcast.getLongitude()))
                .title(broadcast.getBrief())
                .snippet(broadcast.getBrief())
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.chat_small128))
                .draggable(false));

        broadcastMarker.setInfoWindowShowAnimation(R.anim.show_infowindow_anim);
        broadcastMarker.setInfoWindowHideAnimation(R.anim.hide_infowindow_anim);
        markerBroadcastMap.put(broadcastMarker, broadcast);
        markedBroadcast.add(broadcast);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_retrieveBroadcast:
                new GetBroadcastsTask().execute();
                break;
        }
    }


//    public class MyLocationListener implements TencentLocationListener{
//        @Override
//        public void onLocationChanged(TencentLocation tencentLocation, int error, String reason){
//            if(TencentLocation.ERROR_OK == error){
//                //get location success
//                double latitude = tencentLocation.getLatitude();
//                double longitude = tencentLocation.getLongitude();
//                locateSelf(latitude,longitude);
//            }else{
//                //failed
//            }
//        }
//
//        @Override
//        public void onStatusUpdate(String name, int status, String desc){
//
//        }
//    }


    private class GetBroadcastsTask extends AsyncTask<Void, Void, Broadcast[]> {
        private Broadcast[] broadcasts;
        @Override
        protected void onPreExecute(){
            showLoadingProgressDialog();
        }

        //Post request with Broadcast entity
        @Override
        protected Broadcast[] doInBackground(Void... params){
            try {
                final String url = getString(R.string.base_uri) + "/getBroadcastsInDefaultRange?latitude="+myCoordinate.getMyLatitude()+"&longitude="+myCoordinate.getMyLongitude();

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                broadcasts = restTemplate.getForObject(url, Broadcast[].class);
                //broadcast = restTemplate.getForObject(url, (Class<List<Boradcast>>)(Class<?>)List.class);
                return broadcasts;
            }catch(Exception e){
                Log.e(TAG,e.getMessage(),e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Broadcast[] broadcasts){
            dismissProgressDialog();

            /********************LATER WILL DO************************************/
            //Using ArrayList<Broadcast> did not work, the Jackson somehow does not know  how to cast to Broadcast, and using default LinkedHashMap instead!
            //should figure out this issue later. Using Array  Broadcast[] instead currently.
//            for(Iterator it=broadcasts.iterator(); it.hasNext();){
//                Broadcast broadcast = (Broadcast)it.next();
//                markBroadcast(broadcast);
//            }
            if(broadcasts != null && broadcasts.length > 0) {
                for (int i = 0; i < broadcasts.length; i++){
                    if(!markedBroadcast.contains(broadcasts[i]))
                        markBroadcast(broadcasts[i]);
                }

            }
        }

    }


//    private class getTagsTask extends AsyncTask<Void, Void, Tag[]>{
//        private Tag[] tags;
//
//        @Override
//        protected Tag[] doInBackground(Void... params) {
//            try {
//                final String url = getString(R.string.base_uri) + "/getTags";
//
//                RestTemplate restTemplate = new RestTemplate();
//                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//                tags = restTemplate.getForObject(url, Tag[].class);
//                //broadcast = restTemplate.getForObject(url, (Class<List<Boradcast>>)(Class<?>)List.class);
//                return tags;
//            }catch(Exception e){
//                Log.e(TAG,e.getMessage(),e);
//            }
//            return null;
//        }
//
//        @Override
//        protected  void onPostExecute(Tag[] tags){
//            dbManager.addTags(Arrays.asList(tags));
//        }
//    }


    @Override
    public void onMarkerDrag(Marker arg0){
    }

    @Override
    public void onMarkerDragEnd(Marker arg0){
        arg0.setSnippet(arg0.getPosition().toString());
        arg0.showInfoWindow();
        myCoordinate.setMyLatitude(arg0.getPosition().getLatitude());
        myCoordinate.setMyLongitude(arg0.getPosition().getLongitude());
    }

    @Override
    public void onMarkerDragStart(Marker arg0){
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy(){
        if(mapView!=null) {
            this.destroyed = true;
            mapView.onDestroy();
            mapView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onResume(){
        if(mapView != null) {
            mapView.onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause(){
        if(mapView != null){
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop(){
        if(mapView != null){
            mapView.onStop();
        }
        super.onStop();
    }
    public void showLoadingProgressDialog() {
        this.showProgressDialog(getString(R.string.progress_dialog));
    }

    public void showProgressDialog(CharSequence message) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this.getActivity());
            this.progressDialog.setIndeterminate(true);
        }

        this.progressDialog.setMessage(message);
        this.progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (this.progressDialog != null && !this.destroyed) {
            this.progressDialog.dismiss();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
