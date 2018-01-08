package com.moresto.moresto;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Adapter.DashboardAdapter;
import com.moresto.moresto.Model.Dashboard;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Model.Order;
import com.moresto.moresto.Services.ServiceAPI;
import com.wang.avi.AVLoadingIndicatorView;

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
public class MainFragment extends Fragment{
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
    AVLoadingIndicatorView avLoadingIndicatorView;
    TextView txtError;
    RelativeLayout relativeLayoutLoading;
    Button btnCodeOk;
    EditText edCode;
    LayoutInflater myInflater;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        avLoadingIndicatorView = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        txtError = (TextView) rootView.findViewById(R.id.txtError);
        relativeLayoutLoading = (RelativeLayout) rootView.findViewById(R.id.relativeLoading);
        myInflater = inflater;

        return rootView;
    }
    private void checkParentCustomDialog(View customDialog){
        if(customDialog.getParent()!=null)
            ((ViewGroup)customDialog.getParent()).removeView(customDialog);
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
        edCode = (EditText) rootView.findViewById(R.id.edCode);
        btnCodeOk = (Button) rootView.findViewById(R.id.btnCodeOk);

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
        btnCodeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtCode = edCode.getText().toString();
                if(!txtCode.isEmpty()) {
                    mServiceAPI.getTokenDashboard(loginPreference.getTokenid(),txtCode).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            String json = mGson.toJson(response.body());

                            Log.i(TAG, "onResponse: "+json);

                            try {
                                JSONObject mObject = new JSONObject(json);
                                String message = mObject.get("msg").toString();
                                String hasil = mObject.get("hasil").toString();
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                                if(hasil.equals("true")) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    final View customDialog = myInflater.inflate(R.layout.custom_dialog_success_code_dashboard, null);
                                    if (customDialog.getParent() != null)
                                        ((ViewGroup) customDialog.getParent()).removeView(customDialog);
                                    else {
                                        builder.setView(customDialog);
                                    }
                                    Button btnCloseDialog = (Button) customDialog.findViewById(R.id.btnCloseCodeDashboard);
                                    final AlertDialog dialog = builder.create();
                                    btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            checkParentCustomDialog(customDialog);
                                            dialog.dismiss();
                                        }
                                    });
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Toast.makeText(getContext(), "Kesalahan : "+t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });


    }
    private void getDataDashboard(){
        showLoadingIndicatorView();
        mServiceAPI.getDashboard(loginPreference.getTokenid()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                String json = mGson.toJson(response.body());
                hideLoadingIndicatorView();
                try {
                    JSONObject mObject = new JSONObject(json);
                    if(mObject.get("hasil").toString().equals("true")){
                        String data = mObject.get("data").toString();
                        mDashboard = mGson.fromJson(data, mTypeDashBoard);
                        txtTotalKomisiBulanIni.setText(mDashboard.getTotalkomisibulanini());
                        txtTotalTransaksiBulanIni.setText(mDashboard.getTotaltransaksibulanini());
                        txtTotalTransaksiHariIni.setText(mDashboard.getTotaltransaksihariini());
                        if(mDashboard.get3transaksiterbaruhariini().isEmpty()){
                            mDashboard.get3transaksiterbaruhariini().add(new Order("","nodata","",null));
                        }
                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        rc1.setLayoutManager(llm);
                        rc1.setHasFixedSize(true);
                        DashboardAdapter adapter = new DashboardAdapter((ArrayList<Order>) mDashboard.get3transaksiterbaruhariini());
                        rc1.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    errorLoadingIndicatorView();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.toString());
            }
        });
    }


}
