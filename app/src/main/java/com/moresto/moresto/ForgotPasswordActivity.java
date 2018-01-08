package com.moresto.moresto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Services.ServiceAPI;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private final static String TAG = "ForgotPassword";
    EditText edEmail;
    Button btnKirim,btnCancel;
    ServiceAPI mServiceAPI;
    Type mType;
    Gson mGson;
    Login mLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mServiceAPI = ServiceAPI.Factory.getInstance(getApplicationContext());
        mGson = new Gson();
        mType = new TypeToken<Login>() {
        }.getType();
        edEmail = (EditText) findViewById(R.id.forgotPswd);
        btnKirim = (Button) findViewById(R.id.btnKirim);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServiceAPI.lupaPassword(edEmail.getText().toString()).enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        String json = mGson.toJson(response.body());
                        mLogin = mGson.fromJson(json, mType);

                        if(mLogin.isHasil())
                        {
                            edEmail.setText("");
                            Toast.makeText(ForgotPasswordActivity.this, "Email Terkirim /n Silahkan Cek Email Anda.", Toast.LENGTH_LONG).show();
                           // ForgotPasswordActivity.super.onBackPressed();
                        }
                        else
                        {
                            Toast.makeText(ForgotPasswordActivity.this, "Email Invalid", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        Log.i(TAG, "onFailure: "+t.toString());
                    }
                });
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edEmail.setText("");
                ForgotPasswordActivity.super.onBackPressed();
            }
        });

    }
}
