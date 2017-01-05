package co.roomapp.room.activity;

import android.app.Activity;

import co.roomapp.room.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.LangUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.google.android.gms.internal.it;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Join;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.EditText;
/**
* Created by a on 2/7/15.
*/
public class RAMapViewActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, OnClickListener, LocationListener {

    enum BUTTON_TAGS
    {
        btn_done_tag,
        btn_back_tag,
        btn_user_location_tag,
        btn_search_nearby_tag,
        edt_nearby_tag
    };
    ImageButton btnBack;
    ImageButton btnUserLocation;
    ImageButton btnSearchNearby;
    ImageButton btnDone;

    EditText edtNearby;

    private Button mBtn_Map_Zoom;
    private LinearLayout mView_Map_Linear;
    private ListView mLv_Customer_info;
    private FrameLayout mMf_Map_view;

    private Boolean screen_flag;
    private Boolean server_failed_flag;

    private LocationManager mLocationManager;
    private Location mCurrentLocation;
    private Marker mUserMarker;
    private String mUserAddress;
    private CameraUpdate mCameraUpdate;

    private double x;
    private double y;

    private Geocoder geocoder;
    private List<Address> addresses;
    private Location loc;
    private GoogleMap gMap;

    private Marker current_marker;
    double cur_latitude;
    double cur_longitude;
//    private ArrayList<NewItem> image_details;
//    private LinearLayout mParentLinearofLst;
//
//
//    private ArrayList<NewItem> shop_info_arry = new ArrayList<NewItem>();
//    private ProgressDialog mProgressDialog;
//    private AsyncTask<Void, Void, ResultCode> mgetShopInfo;

    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_ramapview);

        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnDone = (ImageButton)findViewById(R.id.btnDone);
        btnUserLocation = (ImageButton)findViewById(R.id.btnUserLocation);
        btnSearchNearby = (ImageButton)findViewById(R.id.btnSearchNearby);
        edtNearby = (EditText)findViewById(R.id.edtNearby);

        btnDone.setTag(BUTTON_TAGS.btn_done_tag);
        btnBack.setTag(BUTTON_TAGS.btn_back_tag);
        btnUserLocation.setTag(BUTTON_TAGS.btn_user_location_tag);
        btnSearchNearby.setTag(BUTTON_TAGS.btn_search_nearby_tag);
        edtNearby.setTag(BUTTON_TAGS.edt_nearby_tag);

        btnDone.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnUserLocation.setOnClickListener(this);
        btnSearchNearby.setOnClickListener(this);
        edtNearby.setOnClickListener(this);

//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.mapView);
//        mapFragment.getMapAsync(this);

        gMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setAllGesturesEnabled(true);
        gMap.setOnMarkerClickListener(this);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setTiltGesturesEnabled(true);
        gMap.getUiSettings().setRotateGesturesEnabled(true);
        gMap.getUiSettings().setScrollGesturesEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setZoomGesturesEnabled(true);
        gMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                getCurrenctLocation();
                return true;
            }
        });
        if(gMap != null)
            setupMap();
    }

    public void setupMap()
    {
        mUserMarker = gMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Marker"));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(-33.867, 151.206);


        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }

    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        if(!marker.equals(mUserMarker))
        {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long duration = 1500;
            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
               @Override
            public void run() {
                   long elapsed = SystemClock.uptimeMillis() - start;
                   float t = Math.max(1 - interpolator.getInterpolation((float)elapsed / duration), 0);
                   marker.setAnchor(0.5f, 1.0f + 2 * t);
                   if(t > 0.0)
                   {
                       handler.postDelayed(this, 16);
                   }
               }
            });
        }
        return false;
    }

    private LatLng getLatitudeFromAddr(String address) {
        // TODO Auto-generated method stub
        double lat = 0;
        double lng = 0;
        String nonSpaceAddress = null;
        String[] items = address.split(" ");
        for (String item : items)
        {
            nonSpaceAddress = ""+item;
        }
        address = nonSpaceAddress;
        Log.d("split character" , address);

        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                address + "&sensor=false";
        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            Log.d("-------1-------", "--------1-------");
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            Log.d("-------1-------", "--------1-------");
            e.printStackTrace();

        } catch (IOException e) {
            Log.d("---------2-----", "-------2--------");
            e.printStackTrace();
        }
        Log.d("-------4-------", "--------4-------");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("latitude", String.valueOf(lat));
            Log.d("longitude", String.valueOf(lng));
        } catch (JSONException e) {
            Log.d("--------3------", "------3---------");
            e.printStackTrace();

        }

        return new LatLng(lat, lng);
    }

    private float setCalculateDistance(double setLatitude, double setLongitude) {
        // TODO Auto-generated method stub
        double shop_latitude, shop_longtitude, cur_latitude, cur_longtitude;

        shop_latitude = setLatitude;
        shop_longtitude = setLongitude;
        cur_latitude = this.cur_latitude;
        cur_longtitude = this.cur_longitude;

        Location loc1 = new Location("shop position");
        loc1.setLatitude(shop_latitude);
        loc1.setLongitude(shop_longtitude);

        Location loc2 = new Location("shop position");
        loc2.setLatitude(cur_latitude);
        loc2.setLongitude(cur_longtitude);

        float distanceInMeters = loc1.distanceTo(loc2);

        Log.d("Distance",String.valueOf(distanceInMeters));
        Log.d("cur",String.valueOf(shop_latitude));
        Log.d("cur",String.valueOf(shop_longtitude));
        Log.d("cur",String.valueOf(cur_latitude));
        Log.d("cur",String.valueOf(cur_longtitude));

        return distanceInMeters/1000;
    }

    private void getCurrenctLocation() {
        // TODO Auto-generated method stub
        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = (LocationManager) RAMapViewActivity.this.getSystemService(Context.LOCATION_SERVICE);

        boolean enabledGPS = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean enabledWiFi = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        // Check if enabled and if not send user to the GPS settings
        if (!enabledGPS && !enabledWiFi) {
            Toast.makeText(RAMapViewActivity.this, "GPS signal not found",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = mLocationManager.getBestProvider(criteria, true);

        // Getting Current Location From GPS
        Location oLc = mLocationManager.getLastKnownLocation(provider);

        Log.d("Location provider : ", provider);
        Log.d("Location value", String.valueOf(oLc));


        if (oLc != null) {
            onLocationChanged(oLc);
            moveCamera(new LatLng(oLc.getLatitude(), oLc.getLongitude()));
        }
        mLocationManager.requestLocationUpdates(provider, 10000, 1 , (LocationListener) this);
    }

    private void moveCamera(LatLng center) {
        // TODO Auto-generated method stub
        mCameraUpdate = CameraUpdateFactory.newLatLngZoom(center, 15);
        gMap.animateCamera(mCameraUpdate);
//  mMap.moveCamera(mCameraUpdate);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {

            default:
                return super.onContextItemSelected(item);
        }

    }

    private String getStreetName(LatLng location ){
        String street ="";
        if(Geocoder.isPresent()){
            Geocoder geocoder = new Geocoder(RAMapViewActivity.this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                Log.d("Current position", mUserAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                street = address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "";
                Log.d("Current position", mUserAddress);
            }
        }
        return street;
    }

    private void displayUserAddress(final LatLng location, final Marker marker)
    {
        //indicator.setVisibility(View.VISIBLE);
        AsyncTask<Void, Void, String> mUpdateTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                return getStreetName(location);
            }

            @Override
            protected void onPostExecute(String address) {

                mUserAddress = address;
                marker.showInfoWindow();
                Log.d("Current position", mUserAddress);
                super.onPostExecute(address);
            }

        };

        // execute AsyncTask
        mUpdateTask.execute(null, null, null);

    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        mCurrentLocation  = location;
        LatLng currentPostion = new LatLng(location.getLatitude(), location.getLongitude());
        if(mUserMarker!=null){
            mUserMarker.setPosition(currentPostion);
            mUserMarker.setTitle("Current Position");
            current_marker = mUserMarker;
            cur_latitude = location.getLatitude();
            cur_longitude = location.getLongitude();
            System.out.println("Current Latitude: " + cur_latitude);
            System.out.println("Current Longitude: " + cur_longitude);
        }
        mUserAddress = null;
//           displayUserAddress(currentPostion, mUserMarker);



        x = location.getLatitude();
        y = location.getLongitude();
        try {
             geocoder = new Geocoder(RAMapViewActivity.this, Locale.ENGLISH);
             addresses = geocoder.getFromLocation(x, y, 1);
             StringBuilder str = new StringBuilder();
             if (geocoder.isPresent()) {
                 Toast.makeText(getApplicationContext(),
                 "geocoder present", Toast.LENGTH_SHORT).show();
                 Address returnAddress = addresses.get(0);

                 String localityString = returnAddress.getLocality();
                 String city = returnAddress.getCountryName();
                 String region_code = returnAddress.getCountryCode();
                 String zipcode = returnAddress.getPostalCode();

                 str.append(localityString + " ");
                 str.append(city + "" + region_code + " ");
                 str.append(zipcode + " ");


                  Toast.makeText(getApplicationContext(), str,
                  Toast.LENGTH_SHORT).show();

                  Log.d("Location information", String.valueOf(str));

             } else {
                 Toast.makeText(getApplicationContext(),
                 "geocoder not present", Toast.LENGTH_SHORT).show();
             }

         } catch (IOException e) {
         // TODO Auto-generated catch block

            Log.e("tag", e.getMessage());
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public void onClick(View v)
    {
        if(v.getTag() == BUTTON_TAGS.btn_done_tag)
        {
            Bundle bundle = new Bundle();
            bundle.putString("status", "Done");
            bundle.putString("locationTitle", mUserMarker.getTitle());
            bundle.putDouble("latitude", mUserMarker.getPosition().latitude);
            bundle.putDouble("longitude", mUserMarker.getPosition().longitude);
            Intent mIntent = new Intent();
            mIntent.putExtras(bundle);
            setResult(Activity.RESULT_OK, mIntent);
            finish();
        }
        else if(v.getTag() == BUTTON_TAGS.btn_back_tag)
        {
            Bundle bundle = new Bundle();
            bundle.putString("status", "Canceled");
            Intent mIntent = new Intent();
            mIntent.putExtras(bundle);
            setResult(Activity.RESULT_CANCELED, mIntent);
            finish();
        }
        else if(v.getTag() == BUTTON_TAGS.btn_user_location_tag)
        {
            this.getCurrenctLocation();
        }
        else if(v.getTag() == BUTTON_TAGS.btn_search_nearby_tag)
        {

        }
        else if(v.getTag() == BUTTON_TAGS.edt_nearby_tag)
        {

        }

    }
}
