package com.skyline.trumpet.trumpetapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.TencentMap.OnInfoWindowClickListener;
import com.tencent.tencentmap.mapsdk.map.TencentMap.OnMapLongClickListener;
import com.tencent.tencentmap.mapsdk.map.TencentMap.OnMarkerDraggedListener;


import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends MapActivity implements OnMarkerDraggedListener {
    final static String TAG = "MainActivity";
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
    private OnInfoWindowClickListener infoWindowClickListener;
    private TencentLocationRequest locationRequest;
    private TencentLocationManager locationManager;
    private Button btn_fireBroadcast;
    private Button btn_retrieveBroadcast;
    private ProgressDialog progressDialog;
    private boolean destroyed = false;
    private Map<Marker,Broadcast> markerBroadcastMap;
    private Marker openedMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        mapView.onCreate(savedInstanceState);
        initOnMarkerClickListener();
        initInfoWindowLayout();
        positionServiceInit();
        fireBroadcastButtonListener();
        retrieveBroadcastButtonListener();
        inforWindowClickListener();


    }

    //only myMarker has LongClickListener ONLY!!
    private void initialization(){
        mapView = (MapView)findViewById(R.id.mapview);
        tencentMap = mapView.getMap();
        markerBroadcastMap = new HashMap<>();
        //tencentMap.setZoom(5);
        //tencentMap.setOnMarkerDraggedListener(this);
        OnMapLongClickListener mapLongClickListener = new OnMapLongClickListener() {
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
        context = getApplicationContext();
        locationListener = new TencentLocationListener(){
            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int error, String reason){
                if(TencentLocation.ERROR_OK == error){
                    //get location success
                    Log.i(TAG,tencentLocation.getAddress());
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

    private void fireBroadcastButtonListener(){
        btn_fireBroadcast = (Button)findViewById(R.id.btn_fireBroadcast);
        btn_fireBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BroadcastActivity.class);
                intent.putExtra("myCoordinate", myCoordinate);
                startActivity(intent);
            }
        });
    }

    private void retrieveBroadcastButtonListener(){
        String url = getString(R.string.base_uri) + "/getBroadcastsInDefaultRange";
        btn_retrieveBroadcast = (Button)findViewById(R.id.btn_retrieveBroadcast);
        btn_retrieveBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetBroadcastsTask().execute();
            }
        });
    }

    private void inforWindowClickListener(){
        infoWindowClickListener = new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
//                marker.setSnippet("Infor window clicked");
//                marker.showInfoWindow();
                Intent intent = new Intent(getApplicationContext(),BroadcastInfoActivity.class);
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
                View infoWindowView = getLayoutInflater().inflate(R.layout.layout_info_window, null);
                //Broadcast broadcast = markerBroadcastMap.get(marker);
                TextView tv_infowin_briefContent = (TextView)infoWindowView.findViewById(R.id.tv_infowin_briefContent);
                TextView tv_infowin_userName = (TextView)infoWindowView.findViewById(R.id.tv_infowin_userName);
                TextView tv_createdDate = (TextView)infoWindowView.findViewById(R.id.tv_createdDate);
                Broadcast selectedBroadcast = markerBroadcastMap.get(marker);
                if(selectedBroadcast != null) {
                    tv_infowin_briefContent.setText(selectedBroadcast.getBrief());
                    //Marker's title has the value of User Name;
                    tv_infowin_userName.setText("路人用户");
                    tv_createdDate.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(selectedBroadcast.getCreatedDate()));
                    return infoWindowView;
                }
                else
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


    private class GetBroadcastsTask extends AsyncTask<Void, Void, Broadcast[]>{
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
            //Using ArrayList<Broadcast> did not work, the Jackson somehow doesnot know  how to cast to Broadcast, and using default LinkedHashMap instead!
            //should figure out this issue later. Using Array  Broadcast[] instead currently.
//            for(Iterator it=broadcasts.iterator(); it.hasNext();){
//                Broadcast broadcast = (Broadcast)it.next();
//                markBroadcast(broadcast);
//            }

            for(int i=0;i<broadcasts.length;i++)
                markBroadcast(broadcasts[i]);
        }

    }
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

//    @Override
//    public void onInfoWindowClick(Marker marker){
//        marker.setSnippet("Infor window clicked");
//        marker.showInfoWindow();
//        tencentMap.clearAllOverlays();
//    }


    @Override
    protected void onDestroy(){
        if(mapView!=null) {
            this.destroyed = true;
            mapView.onDestroy();
            mapView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        if(mapView != null) {
            mapView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause(){
        if(mapView != null){
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onStop(){
        if(mapView != null){
            mapView.onStop();
        }
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void showLoadingProgressDialog() {
        this.showProgressDialog(getString(R.string.progress_dialog));
    }

    public void showProgressDialog(CharSequence message) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this);
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
}