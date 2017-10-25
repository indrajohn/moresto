package com.moresto.moresto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Model.Dashboard;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Services.ServiceAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private boolean pressTwice = false;
    ServiceAPI mServiceAPI;
    Gson mGson;
    Type mType;
    Type mTypeDashBoard;
    SharedPreferences sharedPreferences;
    Login mLogin;
    Login loginPreference;
    NavigationView navigationView;
    SharedPreferences.Editor editor;
    Dashboard mDashBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ChangeLayoutMain();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        mType = new TypeToken<Login>() {
        }.getType();

        mGson = new Gson();
        mServiceAPI = ServiceAPI.Factory.getInstance(getApplicationContext());
        String login = sharedPreferences.getString("login", "");
        loginPreference =  mGson.fromJson(login, mType);
        navigationView.getMenu().findItem(R.id.nav_dashboard).setChecked(true);
    }
    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
       // SendLoagcatMail();
    }*/
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


    private void ChangeLayout(Fragment fragment){
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
    private void ChangeLayoutProfile(){
        ProfileFragment profileFragment = new ProfileFragment();
        ChangeLayout(profileFragment);
    }
    private void ChangeLayoutOpenCloseOrder(){
        OpenCloseOrder openCloseOrder = new OpenCloseOrder();
        ChangeLayout(openCloseOrder);
    }
    private void ChangeLayoutMain(){
        MainFragment mMainFragment = new MainFragment();
        ChangeLayout(mMainFragment);
    }
    private void ChangeLayoutIncomingOrder(){
        IncomingOrder mIncomingOrderFragment= new IncomingOrder();
        ChangeLayout(mIncomingOrderFragment);
    }
    private void ChangeLayoutTransaksiList(){
        TransactionListFragment mTransactionListFragment = new TransactionListFragment();
        ChangeLayout(mTransactionListFragment);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else  {
            int jumlahBackStack = getSupportFragmentManager().getBackStackEntryCount();
            if(jumlahBackStack!=0){
                getSupportFragmentManager().popBackStack();
            }
            else {
                if (pressTwice) {
                    finish();
                    System.exit(0);
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
        }
    }
    private void LogOut(){
        mServiceAPI.logOut(loginPreference.getId(),loginPreference.getTokenid()).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                String json = mGson.toJson(response.body());
                mLogin = mGson.fromJson(json, mType);

                if(mLogin.isHasil()){
                    Toast.makeText(MainActivity.this, "Log Out Success", Toast.LENGTH_SHORT).show();
                    editor= sharedPreferences.edit();
                    editor.putString("login", "");
                    editor.putString("koordinatX","");
                    editor.putString("koordinatY","");
                    editor.apply();
                    MainActivity.super.onBackPressed();
                }
                else{
                    Toast.makeText(MainActivity.this, "Error..Please Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_open_close_order) {
            ChangeLayoutOpenCloseOrder();
        }
        else if (id == R.id.nav_profile) {
            ChangeLayoutProfile();
        }
        else if( id == R.id.nav_logout){
            LogOut();
        }
        else if( id == R.id.nav_dashboard){
            ChangeLayoutMain();
        }
        else if( id == R.id.nav_incoming_order){
            ChangeLayoutIncomingOrder();
        }
        else if( id == R.id.nav_transaksi_list){
            ChangeLayoutTransaksiList();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
