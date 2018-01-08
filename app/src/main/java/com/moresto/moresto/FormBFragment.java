package com.moresto.moresto;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Adapter.DashboardAdapter;
import com.moresto.moresto.Adapter.FormBGridAdapter;
import com.moresto.moresto.Model.ItemFormB;
import com.moresto.moresto.Model.ItemFormBTes;
import com.moresto.moresto.Model.ItemFormBTesParent;
import com.moresto.moresto.Model.Login;
import com.moresto.moresto.Model.Order;
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
public class FormBFragment extends Fragment {
    private static final String TAG = "FormBActivity";
    RecyclerView recyclerView;
    Gson mGson;
    Type mType,mTypeItem;
    ServiceAPI mServiceAPI;
    Login loginPreference;
    SharedPreferences sharedPreferences;
    Button mBtnPesan,mBtnDetail;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    RecyclerView.Adapter recyclerView_Adapter;
    public static ArrayList<ItemFormB> myItem;


    public FormBFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_form_b, container, false);
        init(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDataGrid();
        setOnClickFormB();
    }
    private void init(View rootView){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rcListGrid);
        mGson = new Gson();
        mType = new TypeToken<Login>() {
        }.getType();
        mTypeItem = new TypeToken<ArrayList<ItemFormB>>() {
        }.getType();
        myItem = new ArrayList<>();
        mServiceAPI = ServiceAPI.Factory.getInstance(getContext());
        sharedPreferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", "");
        loginPreference =  mGson.fromJson(login, mType);
        mBtnPesan = (Button) rootView.findViewById(R.id.btnPesanFormB);
        mBtnDetail = (Button) rootView.findViewById(R.id.btnGoToDetailFormB);
    }
    private void getDataGrid(){/*
        int numberOfColumns = 3;
        ArrayList<ItemFormB> mItem = new ArrayList<>();
        for(int i=0;i<30;i++) {
            mItem.add(new ItemFormB(String.valueOf(i), "bihun", "http://via.placeholder.com/100x100"));
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        FormBGridAdapter adapter = new FormBGridAdapter(mItem);
        recyclerView.setAdapter(adapter);
*/
        mServiceAPI.getFormB(loginPreference.getTokenid(),"listbahan","1").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                String json = mGson.toJson(response.body());
                Log.i(TAG, "onResponse: "+json);
                try {
                    JSONObject mObject = new JSONObject(json);
                    if(mObject.get("hasil").toString().equals("true")){
                        String data = mObject.get("data").toString();
                        ArrayList<ItemFormB> mItem = mGson.fromJson(data, mTypeItem);
                        /*
                        int numberOfColumns = 3;
                       recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setItemViewCacheSize(20);
                        recyclerView.setDrawingCacheEnabled(true);
                        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                       FormBGridAdapter adapter = new FormBGridAdapter(mItem);
                        recyclerView.setAdapter(adapter);*/


                        recyclerViewLayoutManager = new GridLayoutManager(getContext(), 3);
                        recyclerView.setLayoutManager(recyclerViewLayoutManager);
                        recyclerView_Adapter  = new FormBGridAdapter(mItem);
                        recyclerView.setAdapter(recyclerView_Adapter);
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
    private void setOnClickFormB() {
        mBtnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (myItem.size() != 0) {
                        ArrayList<ItemFormBTes> myData = new ArrayList<>();
                        for (int i = 0; i < myItem.size(); i++) {
                            myData.add(new ItemFormBTes(myItem.get(i).getId(), myItem.get(i).getNama_bahan(), myItem.get(i).getJumlah()));
                        }
                        String json = mGson.toJson(myData);
                        FormBDetailFragment mFragment = new FormBDetailFragment();
                        Bundle b = new Bundle();
                        b.putString("dataArray", json);
                        mFragment.setArguments(b);
                        FragmentTransaction fragmentTransaction =
                                getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainer, mFragment);
                        fragmentTransaction.commit();
                    } else {
                        Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }


            }
        });
        mBtnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (myItem.size() != 0) {
                        JSONObject mData = new JSONObject();
                        JSONArray mDataArray = new JSONArray();
                        for (int i = 0; i < myItem.size(); i++) {
                            try {
                                mData.put("food_id", myItem.get(i).getId());
                                mData.put("food_name", myItem.get(i).getNama_bahan());
                                mData.put("food_quantity", myItem.get(i).getJumlah());
                                mDataArray.put(mData);
                                mData = new JSONObject();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayList<ItemFormBTes> myData = new ArrayList<>();
                        for (int i = 0; i < myItem.size(); i++) {
                            myData.add(new ItemFormBTes(myItem.get(i).getId(), myItem.get(i).getNama_bahan(), myItem.get(i).getJumlah()));
                        }
/*
                        String json = mGson.toJson(myData);
                        Log.i(TAG, "onClick: "+json);
                        mServiceAPI.setFormBRequest(loginPreference.getTokenid(),"saverequest",json).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                String json = mGson.toJson(response.body());
                                Log.i(TAG, "onResponse: " + json);
                                JSONObject mObject = null;
                                try {
                                    mObject = new JSONObject(json);
                                    if (mObject.get("hasil").toString().equals("true")) {
                                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), mObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }

                                Log.i(TAG, "onResponse: " + json);
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                Log.i(TAG, "onFailure: " + t.toString());
                            }
                        });
*/
                        ItemFormBTesParent myParent = new ItemFormBTesParent(loginPreference.getTokenid(), "saverequest", myData);

                        mServiceAPI.setFormBRequestTest(myParent).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                String json = mGson.toJson(response.body());
                                Log.i(TAG, "onResponse: " + json);
                                JSONObject mObject = null;
                                try {
                                    mObject = new JSONObject(json);
                                    if (mObject.get("hasil").toString().equals("true")) {
                                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), mObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }

                                Log.i(TAG, "onResponse: " + json);
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                Log.i(TAG, "onFailure: " + t.toString());
                            }
                        });
                        myItem.clear();
                        getDataGrid();
                        /*

                        //----
                        String json11 = mGson.toJson(myData);
                        try {
                            JSONArray obj = new JSONArray(json11);
                            Log.d("My App", obj.toString());


                            //--
                            mServiceAPI.setFormBRequest(loginPreference.getTokenid(), "saverequest", obj.toString()).enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    String json = mGson.toJson(response.body());
                                    Log.i(TAG, "onResponse: " + json);
                                    JSONObject mObject = null;
                                    try {
                                        mObject = new JSONObject(json);
                                        if (mObject.get("hasil").toString().equals("true")) {
                                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), mObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                                    }

                                    Log.i(TAG, "onResponse: " + json);
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    Log.i(TAG, "onFailure: " + t.toString());
                                }
                            });
                        }
                        catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" +t.toString()+ json11 + "\"");
                            }
                    }
                    else {
                        Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                }

                catch (Exception ex) {
                    Toast.makeText(getContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }


                    }

            });

               /* FormBDetailFragment mFragment = new FormBDetailFragment();
                Bundle b = new Bundle();
                b.putString("dataArray",data);
                mFragment.setArguments(b);
                FragmentTransaction fragmentTransaction =
                        getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack("Detail");
                fragmentTransaction.replace(R.id.fragmentContainer, mFragment);
                fragmentTransaction.commit();
                Log.i(TAG, "onClick: " + data);
                        FormBDetailFragment mFragment = new FormBDetailFragment();
                        Bundle b = new Bundle();
                        b.putString("dataArray",data);
                        mFragment.setArguments(b);
                        FragmentTransaction fragmentTransaction =
                                getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainer, mFragment);
                        fragmentTransaction.commit();


                */
              /*  */

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
