package com.moresto.moresto.Services;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moresto.moresto.BuildConfig;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Model.Profile;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Gangsterz on 10/17/2017.
 */

public interface ServiceAPI {
    static String BASE_URL = "https://apimob.moresto.id/partner/";

    @FormUrlEncoded
    @POST("dologin.php")
    Call<Login> getAuth(@Field("txt_email") String email, @Field("txt_password") String password,
                               @Field("txt_x") String koordinatX, @Field("txt_y") String koordinatY);
    @FormUrlEncoded
    @POST("dologout.php")
    Call<Login> logOut(@Field("idp") String idp,@Field("tokenid") String tokenid);

    @FormUrlEncoded
    @POST("lupapassword.php")
    Call<Login> lupaPassword(@Field("emailuser") String email);

    @FormUrlEncoded
    @POST("home_dashboard.php")
    Call<Object> getDashboard(@Field("tokenid") String tokenid);

    @FormUrlEncoded
    @POST("openclose.php")
    Call<Object> getOpenClose(@Field("tokenid") String tokenid,@Field("koord_x") String koordinatX,
                                @Field("koord_y") String koordinatY,@Field("openclose") String openclose);

    @FormUrlEncoded
    @POST("profile.php")
    Call<Object> getProfile(@Field("tokenid") String tokenid);


    class Factory {
        private static ServiceAPI service;
        public static final String Path = BASE_URL;
        public static final String FotoPath = BASE_URL + "images/wisata/";
        public static final String FotoUserPath = BASE_URL + "images/user/";

        public static ServiceAPI getInstance(Context context) {
            if (service == null) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.readTimeout(70, TimeUnit.SECONDS);
                builder.connectTimeout(70, TimeUnit.SECONDS);
                builder.writeTimeout(70, TimeUnit.SECONDS);

                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    builder.addInterceptor(interceptor);
                }
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                int cacheSize = 10 * 1024 * 1024;
                Cache cache = new Cache(context.getCacheDir(), cacheSize);
                builder.cache(cache);
                Retrofit retrofit = new Retrofit.Builder().client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(BASE_URL).build();
                service = retrofit.create(ServiceAPI.class);
                return service;
            } else {
                return service;
            }
        }
    }
}
