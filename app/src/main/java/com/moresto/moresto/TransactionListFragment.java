package com.moresto.moresto;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Adapter.ItemListTransactionAdapter;
import com.moresto.moresto.Adapter.Transaction_List_Adapter;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Services.ServiceAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionListFragment extends Fragment{
    private static final String TAG = "TransactionListFragment";
    Gson mGson;
    Type mType;
    ServiceAPI mServiceAPI;
    Login loginPreference;
    SharedPreferences sharedPreferences;
    RecyclerView rcView;


    public TransactionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        init(rootView);
        return rootView;
    }
    private void init(View rootView)
    {
        rcView = (RecyclerView) rootView.findViewById(R.id.rcDeskripsiList);
        mGson = new Gson();
        mType = new TypeToken<Login>() {
        }.getType();
        mServiceAPI = ServiceAPI.Factory.getInstance(getContext());
        sharedPreferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String login = sharedPreferences.getString("login", "");
        loginPreference =  mGson.fromJson(login, mType);

        mServiceAPI.getListTransaksi(loginPreference.getTokenid(),"NULL","1").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                String json = mGson.toJson(response.body());
                try{

                    JSONObject mObject = new JSONObject(json);
                    Log.i(TAG, "onResponse: getListTransaksi"+mObject);
                    if(mObject.get("hasil").toString().equals("false") && mObject.get("msg").toString().equals("nodata"))
                    {
                        Toast.makeText(getContext(), "NO DATA", Toast.LENGTH_LONG).show();
                    }
                    else {
                        JSONArray myArray = mObject.getJSONArray("data");

                        //getListOrder
                        ArrayList<JSONArray> myData = new ArrayList<>();
                        for (int i = 0; i < myArray.length(); i++) {
                            JSONArray data = myArray.getJSONArray(i);
                            myData.add(data);
                        }
                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        rcView.setLayoutManager(llm);
                        rcView.setHasFixedSize(true);
                        FragmentTransaction fragmentTransaction =
                                getFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit);
                        fragmentTransaction.addToBackStack("backTest");
                        Transaction_List_Adapter adapter = new Transaction_List_Adapter(myData, fragmentTransaction);
                        rcView.setAdapter(adapter);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.toString());
            }
        });
    }
}
