package com.moresto.moresto;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moresto.moresto.Adapter.FormBDetailAdapter;
import com.moresto.moresto.Model.ItemFormB;
import com.moresto.moresto.Model.ItemFormBTes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FormBDetailFragment extends Fragment {
    private final static String TAG = "FormBDetail";
    ArrayList<ItemFormBTes> myItem;
    RecyclerView rcView;
    Button btnEdit,btnCancel;
    Gson mGson;
    Type mType;
    public FormBDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_form_bdetail, container, false);
        init(rootView);
        return rootView;
    }
    private void init(View rootView){
        rcView = (RecyclerView) rootView.findViewById(R.id.rcView);
        btnEdit = (Button) rootView.findViewById(R.id.btnEditPesanan);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancelPesanan);
        Bundle b = getArguments();
        String dataArray = b.getString("dataArray");
        Log.i(TAG, "init: "+dataArray);
        mGson = new Gson();
        mType = new TypeToken<ArrayList<ItemFormBTes>>() {
        }.getType();
        myItem = mGson.fromJson(dataArray, mType);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rcView.setLayoutManager(llm);
        rcView.setHasFixedSize(true);
        FormBDetailAdapter adapter = new FormBDetailAdapter(myItem);
        rcView.setAdapter(adapter);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }
}
