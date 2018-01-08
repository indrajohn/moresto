package com.moresto.moresto;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Model.OpenCloseOrder;
import com.moresto.moresto.Services.ServiceAPI;
import com.moresto.moresto.locationService.LocationHelper;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class OpenCloseOrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = "OpenCloseOrderFragment";
    ServiceAPI mServiceAPI;
    Gson mGson;
    Type mType,mTypeOpenClose;
    SharedPreferences sharedPreferences;
    Login loginPreference;
    String koordinatX,koordinatY;
    Switch mSwitch;
    View rootView;
    LocationHelper mLocationHelper;
    AlertDialog.Builder builder;
    public static TextView txtlocationOpenClose;
    Button btnChangeLocation;
    AVLoadingIndicatorView avLoadingIndicatorView;
    TextView txtError;
    SwipeRefreshLayout mLayoutRefresh;
    RelativeLayout relativeLayoutLoading;
    public OpenCloseOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_open_close_order, container, false);
        avLoadingIndicatorView = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        txtError = (TextView) rootView.findViewById(R.id.txtError);
        relativeLayoutLoading = (RelativeLayout) rootView.findViewById(R.id.relativeLoading);
        //mLayoutRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
/*        mLayoutRefresh.setOnRefreshListener(this);
        mLayoutRefresh.setColorSchemeColors(Color.GRAY,Color.GREEN,Color.BLUE,Color.RED, Color.CYAN);
        mLayoutRefresh.setDistanceToTriggerSync(20);
        mLayoutRefresh.setSize(SwipeRefreshLayout.DEFAULT);*/
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View customDialog = inflater.inflate(R.layout.custom_dialog_open_close_order,null);
        init();
        switchClick(customDialog);
        changeLocation();
        getDataOpenClose();
    }
    private void changeLocation(){
        btnChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),MapsActivity.class);
                i.putExtra("koordinatX",koordinatX);
                i.putExtra("koordinatY",koordinatY);
                getActivity().startActivityForResult(i, 2404);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    private void checkParentCustomDialog(View customDialog){
        if(customDialog.getParent()!=null)
            ((ViewGroup)customDialog.getParent()).removeView(customDialog);
    }
    private void switchClick(final View customDialog){

        final TextView txtAlamat = (TextView) customDialog.findViewById(R.id.txtAlamatOpenCloseOrderDialog);
        final TextView txtDeskripsi = (TextView) customDialog.findViewById(R.id.txtDeskripsiOpenCloseOrderDialog);
        final Button btnYa = (Button) customDialog.findViewById(R.id.btnYa);
        final Button btnTidak = (Button) customDialog.findViewById(R.id.btnTidak);
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSwitch.isChecked()){
                    Toast.makeText(getContext(), "im checked", Toast.LENGTH_SHORT).show();
                    if(customDialog.getParent()!=null)
                        ((ViewGroup)customDialog.getParent()).removeView(customDialog);
                    else {
                        builder.setView(customDialog);
                    }
                    txtDeskripsi.setText("Apakah alamat beroperasi untuk menerima order sudah benar?");
                    txtAlamat.setText(txtlocationOpenClose.getText().toString());
                    final AlertDialog dialog =builder.create();
                    dialog.show();
                    btnYa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateDataOpenClose("Open");
                            checkParentCustomDialog(customDialog);
                            dialog.dismiss();
                        }
                    });
                    btnTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkParentCustomDialog(customDialog);
                            mSwitch.setChecked(false);
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    if(customDialog.getParent()!=null)
                        ((ViewGroup)customDialog.getParent()).removeView(customDialog);
                    else {
                        builder.setView(customDialog);
                    }
                    txtDeskripsi.setText("Apakah yakin ingin close order, tidak menerima pesanan lagi?");
                    txtAlamat.setText(txtlocationOpenClose.getText().toString());
                    builder.create();
                    final AlertDialog dialog = builder.show();
                    btnYa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateDataOpenClose("Close");
                            checkParentCustomDialog(customDialog);
                            dialog.dismiss();
                        }
                    });
                    btnTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkParentCustomDialog(customDialog);
                            mSwitch.setChecked(true);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });/*
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if(isChecked && !isFirst){
                    if(customDialog.getParent()!=null)
                        ((ViewGroup)customDialog.getParent()).removeView(customDialog);
                    else {
                        builder.setView(customDialog);
                    }
                    txtDeskripsi.setText("Apakah alamat beroperasi untuk menerima order sudah benar?");
                    txtAlamat.setText(txtlocationOpenClose.getText().toString());
                    final AlertDialog dialog =builder.create();
                     dialog.show();
                    btnYa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateDataOpenClose("Open");
                        }
                    });
                    btnTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkParentCustomDialog(customDialog);
                            mSwitch.setChecked(false);
                            dialog.dismiss();
                        }
                    });
                    /*
                    builder.setMessage("Apakah alamat beroperasi untuk menerima order sudah benar")
                            .setTitle(txtlocationOpenClose.getText().toString());

                    builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateDataOpenClose("Open");
                        }
                    });
                    builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });*/

/*
                }
                else if (!isChecked && !isFirst){
                    if(customDialog.getParent()!=null)
                        ((ViewGroup)customDialog.getParent()).removeView(customDialog);
                    else {
                        builder.setView(customDialog);
                    }
                    txtDeskripsi.setText("Apakah yakin ingin close order, tidak menerima pesanan lagi?");
                    txtAlamat.setText(txtlocationOpenClose.getText().toString());
                    builder.create();
                    final AlertDialog dialog = builder.show();
                    btnYa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateDataOpenClose("Open");
                        }
                    });
                    btnTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkParentCustomDialog(customDialog);
                            mSwitch.setChecked(true);
                            dialog.dismiss();
                        }
                    });
                    /*
                    builder.setMessage("Apakah yakin ingin close order, tidak menerima pesanan lagi?")
                            .setTitle(txtlocationOpenClose.getText().toString());
                    builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateDataOpenClose("Close");
                        }
                    });
                    builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });*/
    }

    private void init(){
        btnChangeLocation = (Button) rootView.findViewById(R.id.btnChangeLocation);
        builder = new AlertDialog.Builder(getActivity());
        sharedPreferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        mType = new TypeToken<Login>() {
        }.getType();
        mTypeOpenClose = new TypeToken<OpenCloseOrder>() {
        }.getType();
        mGson = new Gson();
        mServiceAPI = ServiceAPI.Factory.getInstance(getContext());
        String login = sharedPreferences.getString("login", "");
        koordinatX = sharedPreferences.getString("koordinatX", "");
        koordinatY = sharedPreferences.getString("koordinatY", "");
        loginPreference =  mGson.fromJson(login, mType);
        txtlocationOpenClose = (TextView) rootView.findViewById(R.id.txtlocationOpenClose);
        mSwitch = (Switch) rootView.findViewById(R.id.switchOpenClose);
        mLocationHelper=new LocationHelper(getContext());
    }
    public void showLoadingIndicatorView(){
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        txtError.setVisibility(View.VISIBLE);
        relativeLayoutLoading.setVisibility(View.VISIBLE);
        txtError.setText("Memuat Data...");
    }
    public  void hideLoadingIndicatorView(){
        avLoadingIndicatorView.setVisibility(View.GONE);
        txtError.setVisibility(View.GONE);
        relativeLayoutLoading.setVisibility(View.GONE);
    }
    public void errorLoadingIndicatorView(){
        avLoadingIndicatorView.hide();
        txtError.setText("Terjadi Kesalahan.. Silahkan Muat Ulang");
    }
    private void getDataOpenClose(){
//        showLoadingIndicatorView();
        mServiceAPI.getOpenClose(loginPreference.getTokenid()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                String json = mGson.toJson(response.body());
                //hideLoadingIndicatorView();
                try {
                    JSONObject mObject = new JSONObject(json);
                    if(mObject.get("hasil").toString().equals("true")) {

                        String tmpData = mObject.get("data").toString();
                        Log.i(TAG, "onResponse: getdata "+tmpData);
                        OpenCloseOrder myObject = mGson.fromJson(tmpData, mTypeOpenClose);
                        //Log.i(TAG, "onResponse: status:"+myObject.getStatus_openclose().equals("1"));

                        mSwitch.setChecked(myObject.getStatus_openclose().equals("1"));
                            Address targetAddress = mLocationHelper.getAddress(
                                    Double.valueOf(myObject.getKoordinatx()),
                                    Double.valueOf(myObject.getKoordinaty()));
                            if(targetAddress!=null) {
                                txtlocationOpenClose.setText(targetAddress.getAddressLine(0));
                            }
                            else {
                                Toast.makeText(getContext(), "Cannot find the address", Toast.LENGTH_SHORT).show();
                            }
                        Log.i(TAG, "onResponse: " + myObject);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                   // errorLoadingIndicatorView();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.toString());
               //errorLoadingIndicatorView();
            }
        });
    }
    private void updateDataOpenClose(final String OpenClose){
        String value = OpenClose.contains("Close") ? "0" : "1";
        String alamat = txtlocationOpenClose.getText().toString();
        Log.i(TAG, "onResponse: "+value+","+alamat);
        final ProgressDialog mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Loading..");
        mProgressDialog.show();
       mServiceAPI.updateOpenClose(loginPreference.getTokenid(),value)
               .enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                String json = mGson.toJson(response.body());
                Log.i(TAG, "onResponse update: "+json);
                Toast.makeText(getContext(), "Success "+OpenClose, Toast.LENGTH_SHORT).show();
                if(mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.toString());
                if(mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onRefresh() {
        getDataOpenClose();
        mLayoutRefresh.setRefreshing(false);
    }
}
