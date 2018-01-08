package com.moresto.moresto;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Services.ServiceAPI;
import com.moresto.moresto.locationService.LocationHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        AdapterView.OnItemClickListener {
    private static final String TAG = "MapsActivity";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBncjuThF0fF-xWmldHIXSNzmXKSS_P8bE";
    private GoogleMap mMap;
    private String koordinatX, koordinatY;
    protected boolean mMapIsTouched = false;
    LocationHelper mLocationHelper;
    Location mLocation;
    Address targetAddress;
    //SearchView edSearch;
    AutoCompleteTextView edSearch;
    Button btnSetAddress;
    ServiceAPI mServiceApi;
    Gson mGson;
    Type mType;
    Login loginPreference;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        koordinatX = getIntent().getStringExtra("koordinatX");
        koordinatY = getIntent().getStringExtra("koordinatY");
        mLocationHelper = new LocationHelper(MapsActivity.this);
        edSearch = (AutoCompleteTextView) findViewById(R.id.edSearchLocation);
        mServiceApi = ServiceAPI.Factory.getInstance(MapsActivity.this);
        sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        mType = new TypeToken<Login>() {
        }.getType();
        mGson = new Gson();
        String login = sharedPreferences.getString("login", "");
        loginPreference = mGson.fromJson(login, mType);
        edSearch.setAdapter(new GooglePlacesAutocompleteAdapter(MapsActivity.this, android.R.layout.simple_list_item_2));

        edSearch.setOnItemClickListener(this);
       /* edSearch = (SearchView) findViewById(R.id.edSearchLocation);
        edSearch.setIconified(false);
        edSearch.onActionViewExpanded();
        edSearch.clearFocus();
        try {
            Field mDrawable = SearchView.class.getDeclaredField("mSearchHintIcon");
            mDrawable.setAccessible(true);
            Drawable drawable =  (Drawable)mDrawable.get(edSearch);
            drawable.setAlpha(0);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        btnSetAddress = (Button) findViewById(R.id.btnSetAddress);
        mLocationHelper.checkpermission();
        // check availability of play services
        if (mLocationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            mLocationHelper.buildGoogleApiClient();
        }
        Log.i(TAG, "onCreate: " + mLocationHelper.getLocation());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnSetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (targetAddress != null) {
                    Log.i(TAG, "onClick: targetaddress tidak null");
                    mServiceApi.updateKoordinat(loginPreference.getTokenid(),
                            String.valueOf(targetAddress.getLatitude()),
                            String.valueOf(targetAddress.getLongitude()),
                            targetAddress.getAddressLine(0)).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            String json = mGson.toJson(response.body());
                            try {
                                JSONObject mObject = new JSONObject(json);
                                if (mObject.get("hasil").toString().equals("true")) {
                                    Toast.makeText(MapsActivity.this, "Success save", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("address", targetAddress.getAddressLine(0));
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    Toast.makeText(MapsActivity.this, "Failed to save \n Please Try Again", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Log.i(TAG, "onFailure: " + t.toString());
                        }
                    });
                } else {
                    Log.i(TAG, "onClick: targetAddress null");
                }

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng myLatlng = new LatLng(Double.valueOf(koordinatX), Double.valueOf(koordinatY));
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myLatlng)
                .zoom(17)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationHelper.checkPlayServices();
        mLocationHelper=new LocationHelper(MapsActivity.this);
        mLocationHelper.checkpermission();

        // check availability of play services
        if (mLocationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            mLocationHelper.buildGoogleApiClient();
        }
    }

    @Override
    public void onCameraIdle() {
       try{
           LatLng targetLatlng = mMap.getCameraPosition().target;
           targetAddress = mLocationHelper.getAddress(targetLatlng.latitude,targetLatlng.longitude);
           edSearch.setText(targetAddress.getAddressLine(0));
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
        Log.i(TAG, "onCameraIdle: "+targetAddress);
    }

    @Override
    public void onCameraMoveCanceled() {
    }

    @Override
    public void onCameraMove() {
    }
    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            Log.i(TAG, "The user gestured on the map.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
            Log.i(TAG, "The user tapped something on the map. ");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
            Log.i(TAG, "The app moved the camera.");
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationHelper.connectApiClient();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mLocationHelper.connectApiClient();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mLocationHelper.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Connection failed:", " ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        mLocationHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Address targetAddress = mLocationHelper.getLocationByAddress(str);
        LatLng targetLatlng = new LatLng(targetAddress.getLatitude(),targetAddress.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(targetLatlng)
                .zoom(17)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public static ArrayList autocomplete(String input){
        ArrayList resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try{
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:id");
            sb.append("&input=").append(URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Log.i(TAG, "autocomplete: "+jsonResults);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);
           // return resultList;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Places API", e);
            //return resultList;
        }finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try{
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        }catch (JSONException e) {
            Log.e("MapsActivity", "Cannot process JSON results", e);
        }
        return resultList;
    }
    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;
        private LayoutInflater inflater;
        TextView txtAddress;
       /* public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }*/
       public GooglePlacesAutocompleteAdapter(Context context,int textViewResourceId) {
           super(context, textViewResourceId);
           inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
           View rootView = inflater.inflate(R.layout.list_item, null);

           txtAddress = (TextView) rootView.findViewById(R.id.txtAlamat);
           txtAddress.setText(resultList.get(position).toString());

           return rootView;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return resultList.get(position).toString();
        }

        @NonNull
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if(constraint!=null&&constraint.length()>3){
                        Log.i(TAG, "performFiltering: "+constraint.toString());
                        resultList = autocomplete(constraint.toString());
                        Log.i(TAG, "performFiltering: "+resultList);
                        filterResults.values= resultList;
                        filterResults.count=resultList.size();
                        Log.i(TAG, "performFiltering: "+filterResults.values+","+filterResults.count);
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if(results!=null && results.count>0){
                        notifyDataSetChanged();
                    }else{
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}