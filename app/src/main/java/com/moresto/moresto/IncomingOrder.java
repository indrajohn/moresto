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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Adapter.IncomingOrderItemAdapter;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Services.ServiceAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomingOrder extends Fragment {
    private static final String TAG = "Incoming Order";
    TextView txtOrder,txtDeliverTo,txtDeliverTime;
    Button btnTerimaOrder,btnTolakOrder;
    RecyclerView rcListView;
    Gson mGson;
    Type mType;
    ServiceAPI mServiceAPI;
    Login loginPreference;
    SharedPreferences sharedPreferences;
    public IncomingOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_incoming_order, container, false);
        init(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getDataIncomingOrder();
        Bundle b = getArguments();
        String data = b.getString("myData");
        JSONArray myData;
        try {
            myData = new JSONArray(data);
            Log.i(TAG, "init: "+myData);
            setData(myData);
        } catch (JSONException e) {
            Log.i(TAG, "init: "+e.toString());
        }
    }

    private void init(View v){
        txtOrder = (TextView) v.findViewById(R.id.txtOrder);
        txtDeliverTime = (TextView) v.findViewById(R.id.textView1231);
        txtDeliverTo = (TextView) v.findViewById(R.id.txtDeliverTo);
        btnTerimaOrder = (Button) v.findViewById(R.id.btnTerimaOrder);
        btnTolakOrder = (Button) v.findViewById(R.id.btnTolakOrder);
        rcListView = (RecyclerView) v.findViewById(R.id.rcListView);
        mGson = new Gson();
        mType = new TypeToken<Login>() {
        }.getType();
        mServiceAPI = ServiceAPI.Factory.getInstance(getContext());
        sharedPreferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", "");
        loginPreference =  mGson.fromJson(login, mType);
    }
    private void setData(JSONArray mData){
        try {
            final JSONObject dataDeskripsi = mData.getJSONObject(0);
            JSONArray dataItemDeskripsi = mData.getJSONArray(1);
            txtOrder.setText(dataDeskripsi.get("no_transaction").toString());
            txtDeliverTime.setText(dataDeskripsi.get("tgl_request_kirim_user").toString());
            txtDeliverTo.setText(dataDeskripsi.get("lokasi").toString());


            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            rcListView.setLayoutManager(llm);
            rcListView.setHasFixedSize(true);
            IncomingOrderItemAdapter adapter = new IncomingOrderItemAdapter(dataItemDeskripsi);
            rcListView.setAdapter(adapter);

            btnTerimaOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mServiceAPI.updateIncomingOrder(loginPreference.getTokenid(),"updatestatus",dataDeskripsi.get("id").toString(),"1").enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                String json = mGson.toJson(response.body());
                                try{
                                    JSONObject mObject = new JSONObject(json);
                                    Log.i(TAG, "onResponse: updateStatus: "+mObject);
                                    if(mObject.get("msg").equals("successupdate")){
                                        Toast.makeText(getContext(), "Success Menerima Order", Toast.LENGTH_SHORT).show();
                                        TransactionListFragment fragment = new TransactionListFragment();
                                        FragmentTransaction fragmentTransaction =
                                                getFragmentManager().beginTransaction();
                                        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit);
                                        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                                        fragmentTransaction.commit();
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
           btnTolakOrder.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   try {
                       mServiceAPI.updateIncomingOrder(loginPreference.getTokenid(),"updatestatus",dataDeskripsi.get("id").toString(),"7").enqueue(new Callback<Object>() {
                           @Override
                           public void onResponse(Call<Object> call, Response<Object> response) {
                               String json = mGson.toJson(response.body());
                               try{
                                   JSONObject mObject = new JSONObject(json);
                                   Log.i(TAG, "onResponse: updateStatus: "+mObject);
                                   if(mObject.get("msg").equals("successupdate")){
                                       Toast.makeText(getContext(), "Success Menolak Order", Toast.LENGTH_SHORT).show();
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
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
           });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
