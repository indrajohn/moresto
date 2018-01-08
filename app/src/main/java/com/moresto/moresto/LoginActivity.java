package com.moresto.moresto;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Services.ServiceAPI;
import com.moresto.moresto.locationService.LocationHelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class LoginActivity extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private final static String TAG = "LoginActivity";
    private boolean pressTwice = false;
    Location mLocation;
    TextView txtLupaPassword;
    EditText txtUsername,txtPassword;
    Button btnLogin;
    Login myLogin;
    Type mType;
    Gson mGson;
    ProgressDialog mProgressDialog;
    ServiceAPI mServiceAPI;
    LocationHelper mLocationHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       init();
        LoginClicked();
        lupaPassword();
        checkLogin();
        mLocationHelper=new LocationHelper(LoginActivity.this);
        mLocationHelper.checkpermission();
        // check availability of play services
        if (mLocationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            mLocationHelper.buildGoogleApiClient();
        }
        //ChangeStatusBarColor();
    }
    private void ChangeStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this
                    ,R.color.colorPrimary));
        }
    }

    private void lupaPassword(){
        txtLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
    }
    private void checkLogin(){
        String login = sharedPreferences.getString("login", "");
        if(!login.isEmpty()){
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        if (pressTwice) {
            finishAffinity();
        }
        pressTwice = true;
        Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pressTwice = false;
            }
        }, 2000);
    }
    public void SendLoagcatMail(){

        // save logcat in file
        File outputFile = new File(Environment.getExternalStorageDirectory(),
                "logcat.txt");
        try {
            Runtime.getRuntime().exec(
                    "logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //send file using email
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // Set type to "email"
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"indrajohn2@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, outputFile.getAbsolutePath());
        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }
/*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SendLoagcatMail();
    }*/

    private void LoginClicked(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mLocation=mLocationHelper.getLocation();
                //    Log.i(TAG, "onCreate: "+mLocationHelper.getLocation());
                  //  Log.i(TAG, "onCreate:1 "+mLocation.getLongitude());
                    final String username, password, koordinatX, koordinatY;
                    username = txtUsername.getText().toString();
                    password = txtPassword.getText().toString();
                    if(mLocationHelper.isInternetAvailable()||mLocationHelper.isNetworkConnected(LoginActivity.this)) {
                        if (!username.isEmpty() && !password.isEmpty()) {
                            mProgressDialog.show();
                            //final String myKoordinatX = "-6.1231511";
                            //final String myKoordinatY = "10.00123131";
                            koordinatX = String.valueOf(mLocation.getLatitude());
                            koordinatY = String.valueOf(mLocation.getLongitude());
                            mServiceAPI.getAuth(username, password, koordinatX, koordinatY).enqueue(new Callback<Login>() {
                            //mServiceAPI.getAuth(username, password, myKoordinatX, myKoordinatY).enqueue(new Callback<Login>() {
                                @Override
                                public void onResponse(Call<Login> call, Response<Login> response) {
                                    String json = mGson.toJson(response.body());
                                    myLogin = mGson.fromJson(json, mType);
                                    if (mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                    if (myLogin.isHasil()) {
                                        editor = sharedPreferences.edit();
                                        editor.putString("login", json);
                                      //  editor.putString("koordinatX", String.valueOf(mLocation.getLatitude()));
                                        editor.putString("koordinatX",koordinatX);
                                        //editor.putString("koordinatX",myKoordinatX);

                                        // editor.putString("koordinatY", String.valueOf(mLocation.getLongitude()));
                                        editor.putString("koordinatY", koordinatY);
                                        //editor.putString("koordinatY", myKoordinatY);


                                        editor.apply();
                                        txtUsername.setText("");
                                        txtPassword.setText("");
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Login> call, Throwable t) {
                                    Log.i(TAG, "onFailure: " + t.toString());
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Koneksi Tidak Tersedia", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    if(e.toString().contains("longitude"))
                    {
                        Toast.makeText(LoginActivity.this, "Tidak dapat menemukan lokasi anda..\n Coba lagi", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        e.printStackTrace();
                    }
                }
            }

        });


    }
    private void init(){
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtLupaPassword = (TextView) findViewById(R.id.txtLupaPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        mProgressDialog =new ProgressDialog(LoginActivity.this);
        mProgressDialog.setMessage("Please Wait..");


        mGson = new Gson();
        mType = new TypeToken<Login>() {
        }.getType();
        mServiceAPI = ServiceAPI.Factory.getInstance(getApplicationContext());
        sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationHelper.checkPlayServices();
        mLocationHelper=new LocationHelper(LoginActivity.this);
        mLocationHelper.checkpermission();
        // check availability of play services
        if (mLocationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            mLocationHelper.buildGoogleApiClient();
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocation=mLocationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mLocationHelper.connectApiClient();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Connection failed:", " ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mLocationHelper.onActivityResult(requestCode,resultCode,data);
    }
    // Permission check functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        mLocationHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }
}
