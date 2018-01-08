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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Adapter.IncomingOrderItemAdapter;
import com.moresto.moresto.Adapter.IncomingOrderListAdapter;
import com.moresto.moresto.Model.ItemOrder;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Services.ServiceAPI;

import org.json.JSONArray;
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
public class IncomingOrderListFragment extends Fragment {
    private static final String TAG="IncomingOrderList";
    RecyclerView rcListView;
    Gson mGson;
    Type mType;
    ServiceAPI mServiceAPI;
    Login loginPreference;
    SharedPreferences sharedPreferences;
    public IncomingOrderListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_incoming_order_list, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView){
        rcListView = (RecyclerView) rootView.findViewById(R.id.rcView);
        mGson = new Gson();
        mType = new TypeToken<Login>() {
        }.getType();
        mServiceAPI = ServiceAPI.Factory.getInstance(getContext());
        sharedPreferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", "");
        loginPreference =  mGson.fromJson(login, mType);
    }
    private void getDataIncomingOrder(){
        mServiceAPI.getIncomingOrder(loginPreference.getTokenid()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                String json = mGson.toJson(response.body());
                Log.i(TAG, "onResponse: "+json);
                try {
                    JSONObject mObject = new JSONObject(json);
                    if(mObject.get("hasil").toString().equals("true")){
                        JSONArray myArray = mObject.getJSONArray("data");
                        ArrayList<JSONArray> myData = new ArrayList<>();
                        for(int i=0;i<myArray.length();i++){
                            JSONArray data = myArray.getJSONArray(i);
                            myData.add(data);
                        }
                        FragmentTransaction fragmentTransaction =
                                getFragmentManager().beginTransaction();
                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        rcListView.setLayoutManager(llm);
                        rcListView.setHasFixedSize(true);
                        IncomingOrderListAdapter adapter = new IncomingOrderListAdapter(myData,fragmentTransaction);
                        rcListView.setAdapter(adapter);
                    }
                    else if(mObject.get("hasil").toString().equals("false") && mObject.get("msg").toString().equals("nodata"))
                    {
                        Toast.makeText(getContext(), "NO DATA", Toast.LENGTH_LONG).show();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDataIncomingOrder();
    }
}


