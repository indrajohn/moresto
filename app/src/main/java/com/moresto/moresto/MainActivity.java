package com.moresto.moresto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Adapter.MyExpandableListAdapter;
import com.moresto.moresto.Model.Dashboard;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Services.ServiceAPI;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moresto.moresto.OpenCloseOrderFragment.txtlocationOpenClose;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private boolean pressTwice = false;
    public static String address = "";
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
        getSupportActionBar().setTitle("");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            Log.i(TAG, "onCreate: "+b.get("tes"));
            IncomingOrderListFragment incomingOrderListFragment = new IncomingOrderListFragment();
            ChangeLayout(incomingOrderListFragment);
        }
        else{
            ChangeLayoutMain();
        }
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
        FirebaseTokensControl();
        //enableExpandableList();
    }
    private void FirebaseTokensControl() {
        SharedPreferences mSharedPreferences = getApplicationContext().getSharedPreferences(
                getString(R.string.FCM_PREF), Context.MODE_PRIVATE
        );

        String token = mSharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
        Log.i(TAG, "FirebaseTokensControl: "+token);

        if(!token.isEmpty()){
            mServiceAPI.insertFirebaseToken(loginPreference.getTokenid(),token,"updatetoken").enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    String json = mGson.toJson(response.body());
                    Log.i(TAG, "onResponse: "+json);
                    Toast.makeText(MainActivity.this, "Success Created Firebase Token", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.i(TAG, "onFailure: "+t.toString());
                }
            });
        }
        else{
            Toast.makeText(this, "Failed to get Token Firebase..\nPlease Try Again", Toast.LENGTH_SHORT).show();
        }
    }
    private void prepareListData(List<String> listDataHeader, Map<String,
                List<String>> listDataChild) {


        // Adding child data
        listDataHeader.add("Product1");
        listDataHeader.add("product2");
        listDataHeader.add("Product3");

        // Adding child data
        List<String> top = new ArrayList<String>();
       // top.add("x1");
        //top.add("x2");
        //top.add("x3");
        //top.add("x4");
        //top.add("x5");


        List<String> mid = new ArrayList<String>();
     //   mid.add("y1");
       // mid.add("y2");
        //mid.add("y3");

        List<String> bottom = new ArrayList<String>();
        bottom.add("z1");
        bottom.add("z2");
        bottom.add("z3");



        listDataChild.put(listDataHeader.get(0), top); // Header, Child data
        listDataChild.put(listDataHeader.get(1), mid);
        listDataChild.put(listDataHeader.get(2), bottom);
    }
   /* private void enableExpandableList() {
        final ArrayList<String> listDataHeader = new ArrayList<String>();
        final HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.left_drawer);
        prepareListData(listDataHeader, listDataChild);
        MyExpandableListAdapter listAdapter = new MyExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                 Toast.makeText(getApplicationContext(),
                 "Group Clicked " + listDataHeader.get(groupPosition),
                 Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                // Temporary code:

                // till here
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }*/
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
        OpenCloseOrderFragment openCloseOrderFragment = new OpenCloseOrderFragment();
        ChangeLayout(openCloseOrderFragment);
    }
    private void ChangeLayoutMain(){
        MainFragment mMainFragment = new MainFragment();
        ChangeLayout(mMainFragment);
    }
    private void ChangeLayoutIncomingOrder(){
        IncomingOrderListFragment mIncomingOrderFragment= new IncomingOrderListFragment();
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
                    //finish();
                    //System.exit(0);
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
        else if(id == R.id.nav_formb){
            FormBFragment fragment= new FormBFragment();
            ChangeLayout(fragment);
        }
        else if(id==R.id.nav_help){
            HelpFragment fragment = new HelpFragment();
            ChangeLayout(fragment);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: "+resultCode+","+requestCode+","+data);

        if(resultCode == RESULT_OK && requestCode==2404) {
            if(data != null) {
                try {
                    address = data.getStringExtra("address");
                    txtlocationOpenClose.setText(address);
                }catch (Exception e)
                {
                    Toast.makeText(this, "onActivityResulterror:"+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
