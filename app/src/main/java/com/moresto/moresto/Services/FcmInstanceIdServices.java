package com.moresto.moresto.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.moresto.moresto.R;

/**
 * Created by John on 06/12/2016.
 */

public class FcmInstanceIdServices extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String recentToken = FirebaseInstanceId.getInstance().getToken();

        SharedPreferences mSharedPreferences = getApplicationContext().getSharedPreferences(
                getString(R.string.FCM_PREF), Context.MODE_PRIVATE
        );
        Log.i("MainActivity", "onTokenRefresh: "+recentToken);
        SharedPreferences.Editor editor= mSharedPreferences.edit();
        editor.putString(getString(R.string.FCM_TOKEN),recentToken);
        editor.apply();
    }
}
