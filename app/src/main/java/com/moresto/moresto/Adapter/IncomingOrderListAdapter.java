package com.moresto.moresto.Adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.moresto.moresto.IncomingOrder;
import com.moresto.moresto.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomingOrderListAdapter extends RecyclerView.Adapter<IncomingOrderListAdapter.OrderListViewHolder> {
    private Context mContext;
    private static ArrayList<JSONArray> mJsonArray;
    private static FragmentTransaction fragmentTransaction;


    public IncomingOrderListAdapter( ArrayList<JSONArray> objects,FragmentTransaction fragmentTransaction) {
        mJsonArray = objects;
        IncomingOrderListAdapter.fragmentTransaction = fragmentTransaction;
    }

    @Override
    public IncomingOrderListAdapter.OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_incoming_order_list_adapter, parent, false);
        IncomingOrderListAdapter.OrderListViewHolder pvh = new IncomingOrderListAdapter.OrderListViewHolder(v);
        mContext = parent.getContext();
        return pvh;
    }

    @Override
    public void onBindViewHolder(final IncomingOrderListAdapter.OrderListViewHolder holder, final int position) {
        try{
            JSONObject dataDeskripsi = mJsonArray.get(position).getJSONObject(0);
            JSONArray dataItemDeskripsi = mJsonArray.get(position).getJSONArray(1);
            holder.txtNoOrder.setText("Order "+dataDeskripsi.get("no_transaction"));
            holder.txtWaktuOrder.setText(dataDeskripsi.get("tgl_request_kirim_user").toString());
            holder.txtTujuanOrder.setText(dataDeskripsi.get("lokasi").toString());
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            holder.rcItem.setLayoutManager(llm);
            holder.rcItem.setHasFixedSize(true);
            ItemListTransactionAdapter adapter = new ItemListTransactionAdapter(dataItemDeskripsi);
            holder.rcItem.setAdapter(adapter);
            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IncomingOrder fragment = new IncomingOrder();
                    Bundle b = new Bundle();
                    b.putString("myData",mJsonArray.get(position).toString());
                    fragment.setArguments(b);
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                    fragmentTransaction.commit();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Ada Kesalahan..Silahkan Muat Ulang..", Toast.LENGTH_SHORT).show();
        }
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        holder.rcItem.setLayoutManager(llm);
        holder.rcItem.setHasFixedSize(true);
    }

    @Override
    public int getItemCount() {
        return mJsonArray.size();
    }


    public static class OrderListViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView rcItem;
        private TextView txtTujuanOrder,txtNoOrder,txtWaktuOrder;
        private Button btnDetail;

        OrderListViewHolder(View itemView) {
            super(itemView);
            rcItem = (RecyclerView) itemView.findViewById(R.id.rcItemList);
            txtTujuanOrder = (TextView) itemView.findViewById(R.id.txtTujuanOrder);
            txtNoOrder = (TextView) itemView.findViewById(R.id.noOrder);
            txtWaktuOrder = (TextView) itemView.findViewById(R.id.waktuOrder);
            btnDetail = (Button) itemView.findViewById(R.id.btnGoToDetailIncomingOrder);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
