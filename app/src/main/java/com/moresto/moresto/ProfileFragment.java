package com.moresto.moresto;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Model.Profile;
import com.moresto.moresto.Services.ServiceAPI;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
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
public class ProfileFragment extends Fragment{
    ServiceAPI mServiceAPI;
    Gson mGson;
    Type mType;
    Type mTypeProfile;
    Profile mProfile;
    ImageView mImage;
    TextView txtNamaUserProfile,txtEmailUserProfile,txtTelpUserProfile,txtBirthdayUserProfile,txtLocationUserProfile;
    private final String TAG = "OpenCloseOrderFragment";
    SharedPreferences sharedPreferences;
    Login loginPreference;
    AVLoadingIndicatorView avLoadingIndicatorView;
    TextView txtError;
    SwipeRefreshLayout mLayoutRefresh;
    RelativeLayout relativeLayoutLoading;
    ImageView btnChangePassword;
    LayoutInflater myInflater;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        init(rootView);
        myInflater = inflater;
        return  rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View customDialog = myInflater.inflate(R.layout.custom_dialog_password,null);
                final EditText edOldPassword = (EditText) customDialog.findViewById(R.id.edOldPassword);
                EditText edNewPassword = (EditText) customDialog.findViewById(R.id.edNewPassword);
                EditText edConfirmPassword = (EditText) customDialog.findViewById(R.id.edConfirmPassword);
                Button btnYa = (Button) customDialog.findViewById(R.id.btnYa);
                Button btnTidak = (Button) customDialog.findViewById(R.id.btnTidak);
                if(customDialog.getParent()!=null)
                    ((ViewGroup)customDialog.getParent()).removeView(customDialog);
                else {
                    builder.setView(customDialog);
                }
                 builder.create();
                final AlertDialog dialog = builder.show();
                btnYa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Success Change Password", Toast.LENGTH_SHORT).show();
                        if (customDialog.getParent() != null) {
                            ((ViewGroup) customDialog.getParent()).removeView(customDialog);
                        }
                        dialog.dismiss();
                    }
                });
                btnTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customDialog.getParent() != null) {
                            ((ViewGroup) customDialog.getParent()).removeView(customDialog);
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
        getDataProfile();
    }
    private void init(View rootView){
        mServiceAPI = ServiceAPI.Factory.getInstance(getContext());
        mGson = new Gson();
        mType = new TypeToken<Login>() {
        }.getType();
        mTypeProfile = new TypeToken<Profile>() {
        }.getType();
        sharedPreferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);

        txtNamaUserProfile = (TextView) rootView.findViewById(R.id.txtNamaUserProfile);
        txtEmailUserProfile = (TextView) rootView.findViewById(R.id.txtEmailUserProfile);
        txtTelpUserProfile = (TextView) rootView.findViewById(R.id.txtTelpUserProfile);
        txtBirthdayUserProfile = (TextView) rootView.findViewById(R.id.txtBirthdayUserProfile);
        btnChangePassword = (ImageView) rootView.findViewById(R.id.imgChangePassword);
        txtLocationUserProfile = (TextView) rootView.findViewById(R.id.txtLocationUserProfile);
        mImage = (ImageView) rootView.findViewById(R.id.imgProfile);
        avLoadingIndicatorView = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        txtError = (TextView) rootView.findViewById(R.id.txtError);
        relativeLayoutLoading = (RelativeLayout) rootView.findViewById(R.id.relativeLoading);
       /* mLayoutRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mLayoutRefresh.setOnRefreshListener(this);
        mLayoutRefresh.setColorSchemeColors(Color.GRAY,Color.GREEN,Color.BLUE,Color.RED, Color.CYAN);
        mLayoutRefresh.setDistanceToTriggerSync(20);
        mLayoutRefresh.setSize(SwipeRefreshLayout.DEFAULT);*/
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
    private void getDataProfile(){
        showLoadingIndicatorView();
        String login = sharedPreferences.getString("login", "");
        loginPreference =  mGson.fromJson(login, mType);
        mServiceAPI.getProfile(loginPreference.getTokenid()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                String json = mGson.toJson(response.body());
                hideLoadingIndicatorView();
                try{
                    Log.i(TAG, "onResponse: data: "+json);
                    JSONObject mObject = new JSONObject(json);
                    if(mObject.get("hasil").toString().equals("true")){
                        String data = mObject.get("data").toString();
                        mProfile = mGson.fromJson(data, mTypeProfile);

                        txtNamaUserProfile.setText(mProfile.getNama());
                        txtEmailUserProfile.setText(mProfile.getEmail());
                        txtTelpUserProfile.setText(mProfile.getNoHp());
                        txtBirthdayUserProfile.setText(mProfile.getBergabungsejak());
                        txtLocationUserProfile.setText(mProfile.getLokasi());
                        if (!ImageLoader.getInstance().isInited()) {
                            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                                    .cacheInMemory(true)
                                    .cacheOnDisk(true)
                                    .build();
                            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                                    .defaultDisplayImageOptions(defaultOptions)
                                    .build();
                            ImageLoader.getInstance().init(config);
                        }
                        ImageLoader.getInstance().displayImage(mProfile.getGambar(), mImage);
                    }
                }
                catch (Exception e)
                {
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
/*
    @Override
    public void onRefresh() {
        getDataProfile();
        mLayoutRefresh.setRefreshing(false);
    }*/
}
