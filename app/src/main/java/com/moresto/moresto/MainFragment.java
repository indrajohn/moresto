package com.moresto.moresto;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Model.Dashboard;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Services.ServiceAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    TextView txtTotalTransaksiHariIni,txtTotalTransaksiBulanIni,txtTotalKomisiBulanIni;
    RecyclerView rc1;
    View rootView;
    Type mTypeDashBoard,mType;
    Gson mGson;
    ServiceAPI mServiceAPI;
    Login loginPreference;
    SharedPreferences sharedPreferences;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        getDataDashboard();
    }
    private void init(){
        txtTotalKomisiBulanIni = (TextView) rootView.findViewById(R.id.txtTotalKomisiBulanIni);
        txtTotalTransaksiBulanIni = (TextView) rootView.findViewById(R.id.txtTotalTransaksiBulanIni);
        txtTotalTransaksiHariIni = (TextView) rootView.findViewById(R.id.txtTotalTransaksiHariIni);

        rc1 = (RecyclerView) rootView.findViewById(R.id.rc1);

        mTypeDashBoard = new TypeToken<Dashboard>() {
        }.getType();
        mGson = new Gson();
        mServiceAPI = ServiceAPI.Factory.getInstance(getContext());
        sharedPreferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", "");
        loginPreference =  mGson.fromJson(login, mType);
    }
    private void getDataDashboard(){
        mServiceAPI.getDashboard(loginPreference.getTokenid()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i(TAG, "onResponse: "+response.body().toString());
                String mResponse = mGson.toJson(response.body());
                try {
                    JSONObject myObject = new JSONObject(mResponse);
                    if(myObject.get("hasil").toString().equals("true")){
                        Log.i(TAG, "onResponse: "+myObject.get("data").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.toString());
            }
        });
    }
}
