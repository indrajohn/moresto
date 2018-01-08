package com.moresto.moresto;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Adapter.ItemListTransactionAdapter;
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
public class TransactionListDetailKirimFragment extends Fragment {
    private final static String TAG = "TransactionDetailKirim";
    TextView txtOrder,txtDeliverTo,txtDeliverTime,txtStatus;
    Button btnKirim;
    RecyclerView rcListView;
    Gson mGson;
    Type mType;
    ServiceAPI mServiceAPI;
    Login loginPreference;
    SharedPreferences sharedPreferences;
    JSONArray myData;
    public TransactionListDetailKirimFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =inflater.inflate(R.layout.fragment_transaction_list_detail_kirim, container, false);
        init(rootView);
        return rootView;
    }
    private void init(View v){
        txtOrder = (TextView) v.findViewById(R.id.txtOrder);
        txtStatus = (TextView) v.findViewById(R.id.txtStatusDetail);
        txtDeliverTime = (TextView) v.findViewById(R.id.textView1231);
        txtDeliverTo = (TextView) v.findViewById(R.id.txtDeliverTo);
        btnKirim = (Button) v.findViewById(R.id.btnOkTransactionList);
        rcListView = (RecyclerView) v.findViewById(R.id.rcListView);
        mGson = new Gson();
        mType = new TypeToken<Login>() {
        }.getType();
        mServiceAPI = ServiceAPI.Factory.getInstance(getContext());
        sharedPreferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", "");
        loginPreference =  mGson.fromJson(login, mType);
        Bundle b = getArguments();
        String data = b.getString("myData");
        try {
            myData = new JSONArray(data);
            setData(myData);
            Log.i(TAG, "init: "+myData);
        } catch (JSONException e) {
            Log.i(TAG, "init: "+e.toString());
        }
    }
    private void setData(JSONArray mData){
        try {
            final JSONObject dataDeskripsi = mData.getJSONObject(0);
            JSONArray dataItemDeskripsi = mData.getJSONArray(1);
            final String id = dataDeskripsi.get("id").toString();
            txtOrder.setText(dataDeskripsi.get("no_transaction").toString());
            txtDeliverTime.setText(dataDeskripsi.get("waktu_order").toString());
            txtDeliverTo.setText(dataDeskripsi.get("alamat").toString());

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            rcListView.setLayoutManager(llm);
            rcListView.setHasFixedSize(true);
            ItemListTransactionAdapter adapter = new ItemListTransactionAdapter(dataItemDeskripsi);
            rcListView.setAdapter(adapter);

            btnKirim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mServiceAPI.changeStatusTransaksi(loginPreference.getTokenid(),id,"3","updatestatus").enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            String json = mGson.toJson(response.body());
                            Log.i(TAG, "onResponse: "+json);
                            for(int i=getFragmentManager().getBackStackEntryCount();i>0;i--) {
                                getFragmentManager().popBackStack();
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
