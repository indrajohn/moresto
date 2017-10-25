package com.moresto.moresto;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Adapter.DashboardAdapter;
import com.moresto.moresto.Model.Dashboard;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Model.Order;
import com.moresto.moresto.Services.ServiceAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
    Dashboard mDashboard;


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
        mType = new TypeToken<Login>() {
        }.getType();
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
                String json = mGson.toJson(response.body());
                try {
                    JSONObject mObject = new JSONObject(json);
                    if(mObject.get("hasil").toString().equals("true")){
                        String data = mObject.get("data").toString();
                        mDashboard = mGson.fromJson(data, mTypeDashBoard);
                        txtTotalKomisiBulanIni.setText(mDashboard.getTotalkomisibulanini());
                        txtTotalTransaksiBulanIni.setText(mDashboard.getTotaltransaksibulanini());
                        txtTotalTransaksiHariIni.setText(mDashboard.getTotaltransaksihariini());

                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        rc1.setLayoutManager(llm);
                        rc1.setHasFixedSize(true);
                        DashboardAdapter adapter = new DashboardAdapter((ArrayList<Order>) mDashboard.get3transaksiterbaruhariini());
                        rc1.setAdapter(adapter);
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
