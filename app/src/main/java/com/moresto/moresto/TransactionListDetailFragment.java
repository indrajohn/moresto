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
import android.widget.Toast;

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
public class TransactionListDetailFragment extends Fragment {
    private final static String TAG = "TransactionDetail";
    TextView txtOrder,txtDeliverTo,txtDeliverTime,txtStatus;
    Button btnChange,btnProses,btnCancel;
    RecyclerView rcListView;
    Gson mGson;
    Type mType;
    ServiceAPI mServiceAPI;
    Login loginPreference;
    SharedPreferences sharedPreferences;
    JSONArray myData;
    public TransactionListDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_transaction_list_detail, container, false);
        init(rootView);
        return rootView;
    }
    private void init(View v){
        txtOrder = (TextView) v.findViewById(R.id.txtOrder);
        txtStatus = (TextView) v.findViewById(R.id.txtStatusDetail);
        txtDeliverTime = (TextView) v.findViewById(R.id.textView1231);
        txtDeliverTo = (TextView) v.findViewById(R.id.txtDeliverTo);
        btnProses = (Button) v.findViewById(R.id.btnProsesTransactionList);
        btnCancel = (Button) v.findViewById(R.id.btnCancelTransactionList);
        btnChange = (Button) v.findViewById(R.id.btnChangeTransactionList);
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
    private String getStatus(String myCode){
        String status = "";
        switch (myCode){
            case "11": status = "Diterima";
                break;
            case "1": status = "Diproses";
                break;
            case "2": status = "Batal";
                break;
            case "3": status = "Dikirim";
                break;
            default:
                status = "Unknown";
                break;
        }
        return status;
    }
    private void setData(JSONArray mData){
        try {
            final JSONObject dataDeskripsi = mData.getJSONObject(0);
            JSONArray dataItemDeskripsi = mData.getJSONArray(1);
            Log.i(TAG, "setData: "+dataDeskripsi);
            final String id = dataDeskripsi.get("id").toString();
            txtOrder.setText(dataDeskripsi.get("no_transaction").toString());
            txtDeliverTime.setText(dataDeskripsi.get("waktu_order").toString());
            txtDeliverTo.setText(dataDeskripsi.get("alamat").toString());
            txtStatus.setText(getStatus(dataDeskripsi.get("status").toString()));

            if(txtStatus.getText().toString().equals("Batal")){
                btnChange.setVisibility(View.GONE);
                btnProses.setVisibility(View.GONE);
            }
            else if(txtStatus.getText().toString().equals("Diterima")) {
                btnProses.setVisibility(View.VISIBLE);
                btnChange.setVisibility(View.VISIBLE);
            }
            else{
                btnChange.setVisibility(View.GONE);
                btnProses.setVisibility(View.GONE);
            }
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            rcListView.setLayoutManager(llm);
            ItemListTransactionAdapter adapter = new ItemListTransactionAdapter(dataItemDeskripsi);
            rcListView.setAdapter(adapter);

            btnProses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog myProgress = new ProgressDialog(getContext());
                    myProgress.setMessage("Loading");
                    myProgress.show();

                    mServiceAPI.changeStatusTransaksi(loginPreference.getTokenid(),id,"1","updatestatus").enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            String json = mGson.toJson(response.body());
                            Log.i(TAG, "onResponse: "+json);
                            JSONArray myTemp = changeStatusManual();
                            if(myProgress.isShowing())
                                myProgress.dismiss();
                            TransactionListDetailProsesFragment fragment = new TransactionListDetailProsesFragment();
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
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStackImmediate();
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
            dataDeskripsi.put("status","1");
            return myTemp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myTemp;

    }

}
