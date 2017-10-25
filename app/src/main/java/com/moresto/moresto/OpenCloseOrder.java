package com.moresto.moresto;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Services.ServiceAPI;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class OpenCloseOrder extends Fragment {
    private final String TAG = "OpenCloseOrder";
    ServiceAPI mServiceAPI;
    Gson mGson;
    Type mType;
    SharedPreferences sharedPreferences;
    Login loginPreference;
    String koordinatX,koordinatY;
    Switch mSwitch;
    View rootView;
    AlertDialog.Builder builder;
    TextView txtlocationOpenClose;
    AlertDialog dialog;
    Button btnChangeLocation;
    public OpenCloseOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_open_close_order, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        switchClick();
        changeLocation();
    }
    private void changeLocation(){
        btnChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),MapsActivity.class);
                startActivity(i);
            }
        });
    }
    private void switchClick(){
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    builder.setMessage("Apakah alamat beroperasi untuk menerima order sudah benar")
                            .setTitle(txtlocationOpenClose.getText().toString());

                    builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getDataOpenClose("Open");
                        }
                    });
                    builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();

                }
                else{
                    builder.setMessage("Apakah yakin ingin close order, tidak menerima pesanan lagi?")
                            .setTitle(txtlocationOpenClose.getText().toString());
                    builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getDataOpenClose("Close");
                        }
                    });
                    builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                }
            }
        });
    }

    private void init(){
        btnChangeLocation = (Button) rootView.findViewById(R.id.btnChangeLocation);
        builder = new AlertDialog.Builder(getActivity());
        sharedPreferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        mType = new TypeToken<Login>() {
        }.getType();
        mGson = new Gson();
        mServiceAPI = ServiceAPI.Factory.getInstance(getContext());
        String login = sharedPreferences.getString("login", "");
        koordinatX = sharedPreferences.getString("koordinatX", "");
        koordinatY = sharedPreferences.getString("koordinatY", "");
        loginPreference =  mGson.fromJson(login, mType);
        txtlocationOpenClose = (TextView) rootView.findViewById(R.id.txtlocationOpenClose);
        mSwitch = (Switch) rootView.findViewById(R.id.switchOpenClose);
    }
    private void getDataOpenClose(final String OpenClose){
       mServiceAPI.getOpenClose(loginPreference.getTokenid(),koordinatX,koordinatY,OpenClose).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                String json = mGson.toJson(response.body());
                Toast.makeText(getContext(), "Success "+OpenClose, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.toString());
            }
        });
    }

}
