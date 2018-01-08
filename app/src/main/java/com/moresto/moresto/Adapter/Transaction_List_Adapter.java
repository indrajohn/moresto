package com.moresto.moresto.Adapter;


import android.content.Context;
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

import com.moresto.moresto.R;
import com.moresto.moresto.TransactionListDetailFragment;
import com.moresto.moresto.TransactionListDetailProsesFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Transaction_List_Adapter extends RecyclerView.Adapter<Transaction_List_Adapter.OrderListViewHolder> {
    private Context mContext;
    public static ArrayList<JSONArray> mJsonArray;
    private static FragmentTransaction fragmentTransaction;

    public Transaction_List_Adapter(ArrayList<JSONArray> objects,FragmentTransaction fragmentTransaction) {
        mJsonArray = objects;
        Transaction_List_Adapter.fragmentTransaction = fragmentTransaction;
    }

    @Override
    public Transaction_List_Adapter.OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_transaction__list__adapter, parent, false);
        Transaction_List_Adapter.OrderListViewHolder pvh = new Transaction_List_Adapter.OrderListViewHolder(v);
       mContext = parent.getContext();
        return pvh;
    }


    @Override
    public void onBindViewHolder(final Transaction_List_Adapter.OrderListViewHolder holder, final int position) {
        try{
            JSONObject dataDeskripsi = mJsonArray.get(position).getJSONObject(0);
            JSONArray dataItemDeskripsi = mJsonArray.get(position).getJSONArray(1);
            holder.txtNoOrder.setText("Order "+dataDeskripsi.get("no_transaction"));
            holder.txtWaktuOrder.setText(dataDeskripsi.get("waktu_order").toString());
            holder.txtTujuanOrder.setText(dataDeskripsi.get("alamat").toString());
            String status = getStatus(dataDeskripsi.get("status").toString());
            holder.txtStatus.setText(status);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            holder.rcItem.setLayoutManager(llm);
            holder.rcItem.setHasFixedSize(true);
            ItemListTransactionAdapter adapter = new ItemListTransactionAdapter(dataItemDeskripsi);
            holder.rcItem.setAdapter(adapter);
            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.txtStatus.getText().toString().equals("Diproses")){
                        TransactionListDetailProsesFragment fragment = new TransactionListDetailProsesFragment();
                        Bundle b = new Bundle();
                        b.putString("myData", mJsonArray.get(position).toString());
                        fragment.setArguments(b);
                        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                        fragmentTransaction.commit();
                    }
                    else {
                        TransactionListDetailFragment fragment = new TransactionListDetailFragment();
                        Bundle b = new Bundle();
                        b.putString("myData", mJsonArray.get(position).toString());
                        fragment.setArguments(b);
                        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                        fragmentTransaction.commit();
                    }
                }
            });


        } catch (JSONException e) {
           e.printStackTrace();
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

    @Override
    public int getItemCount() {
        return mJsonArray.size();
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView rcItem;
        private TextView txtTujuanOrder,txtNoOrder,txtWaktuOrder,txtStatus;
        private Button btnDetail;

        OrderListViewHolder(View itemView) {
            super(itemView);
            rcItem = (RecyclerView) itemView.findViewById(R.id.rcItemList);
            txtTujuanOrder = (TextView) itemView.findViewById(R.id.txtTujuanOrder);
            txtNoOrder = (TextView) itemView.findViewById(R.id.noOrder);
            txtWaktuOrder = (TextView) itemView.findViewById(R.id.waktuOrder);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            btnDetail = (Button) itemView.findViewById(R.id.btnGoToDetailTransactionList);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
