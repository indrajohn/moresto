package com.moresto.moresto;


import android.app.ProgressDialog;
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
public class TransactionListDetailProsesFragment extends Fragment {

    private final static String TAG = "TransactionDetailProses";
    TextView txtOrder,txtDeliverTo,txtDeliverTime,txtStatus;
    Button btnKirim;
    RecyclerView rcListView;
    Gson mGson;
    Type mType;
    ServiceAPI mServiceAPI;
    Login loginPreference;
    SharedPreferences sharedPreferences;
    JSONArray myData;
    public TransactionListDetailProsesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_transaction_list_detail_proses, container, false);
        init(rootView);
        return rootView;
    }
    private void init(View v){
        txtOrder = (TextView) v.findViewById(R.id.txtOrder);
        txtStatus = (TextView) v.findViewById(R.id.txtStatusDetail);
        txtDeliverTime = (TextView) v.findViewById(R.id.textView1231);
        txtDeliverTo = (TextView) v.findViewById(R.id.txtDeliverTo);
        btnKirim = (Button) v.findViewById(R.id.btnKirimTransactionList);
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
            if(dataDeskripsi.get("status").toString().equals("1"))
                txtStatus.setText("Diproses");
            else{
                txtStatus.setText("Unknown");
            }
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            rcListView.setLayoutManager(llm);
            rcListView.setHasFixedSize(true);
            ItemListTransactionAdapter adapter = new ItemListTransactionAdapter(dataItemDeskripsi);
            rcListView.setAdapter(adapter);

           btnKirim.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {
                   final ProgressDialog myProgress = new ProgressDialog(getContext());
                   myProgress.setMessage("Loading");
                   myProgress.show();
                   mServiceAPI.changeStatusTransaksi(loginPreference.getTokenid(),id,"2","updatestatus").enqueue(new Callback<Object>() {
                       @Override
                       public void onResponse(Call<Object> call, Response<Object> response) {
                           String json = mGson.toJson(response.body());
                           Log.i(TAG, "onResponse: "+json);
                           if(myProgress.isShowing())
                               myProgress.dismiss();
                           JSONArray myTemp = changeStatusManual();
                           TransactionListDetailKirimFragment fragment = new TransactionListDetailKirimFragment();
                           Bundle b = new Bundle();
                           b.putString("myData",myTemp.toString());
                           fragment.setArguments(b);
                           FragmentTransaction fragmentTransaction =
                                   getFragmentManager().beginTransaction();
                           fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                           fragmentTransaction.commit();
                       }

                       @Override
                       public void onFailure(Call<Object> call, Throwable t) {
                           if(myProgress.isShowing())
                               myProgress.dismiss();
                       }
                   });
               }
           });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private JSONArray changeStatusManual(){
        JSONArray myTemp = myData;
        JSONObject dataDeskripsi = null;
        try {
            dataDeskripsi = myTemp.getJSONObject(0);
            JSONArray dataItemDeskripsi = myTemp.getJSONArray(1);
            dataDeskripsi.put("status","2");
            return myTemp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myTemp;
    }

}
