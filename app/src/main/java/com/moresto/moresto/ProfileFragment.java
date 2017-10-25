package com.moresto.moresto;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Model.Profile;
import com.moresto.moresto.Services.ServiceAPI;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    ServiceAPI mServiceAPI;
    Gson mGson;
    Type mType;
    Type mTypeProfile;
    Profile mProfile;
    ImageView mImage;
    TextView txtNamaUserProfile,txtEmailUserProfile,txtTelpUserProfile,txtBirthdayUserProfile,txtLocationUserProfile;
    private final String TAG = "OpenCloseOrder";
    SharedPreferences sharedPreferences;
    Login loginPreference;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        init(rootView);
        return  rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        txtLocationUserProfile = (TextView) rootView.findViewById(R.id.txtLocationUserProfile);
        mImage = (ImageView) rootView.findViewById(R.id.imgProfile);
    }
    private void getDataProfile(){
        String login = sharedPreferences.getString("login", "");
        loginPreference =  mGson.fromJson(login, mType);
        mServiceAPI.getProfile(loginPreference.getTokenid()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                String json = mGson.toJson(response.body());
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
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.toString());
            }
        });
    }
}
