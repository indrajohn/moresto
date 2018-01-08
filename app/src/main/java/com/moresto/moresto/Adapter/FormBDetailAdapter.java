package com.moresto.moresto.Adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moresto.moresto.Model.ItemFormB;
import com.moresto.moresto.Model.ItemFormBTes;
import com.moresto.moresto.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormBDetailAdapter extends RecyclerView.Adapter<FormBDetailAdapter.OrderListViewHolder> {
    private Context mContext;
    private ArrayList<ItemFormBTes> mOrderList;


    public FormBDetailAdapter(ArrayList<ItemFormBTes> objects) {
        mOrderList = objects;
    }

    @Override
    public FormBDetailAdapter.OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_form_bdetail_adapter, parent, false);
        FormBDetailAdapter.OrderListViewHolder pvh = new FormBDetailAdapter.OrderListViewHolder(v);
        mContext = parent.getContext();
        return pvh;
    }

    @Override
    public void onBindViewHolder(final FormBDetailAdapter.OrderListViewHolder holder, final int position) {
       holder.txtNamaOrder.setText(mOrderList.get(position).getName());
       holder.txtJumlah.setText(mOrderList.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder {
        private TextView txtJumlah,txtNamaOrder;

        OrderListViewHolder(View itemView) {
            super(itemView);
            txtJumlah = (TextView) itemView.findViewById(R.id.txtJumlahPesanan);
            txtNamaOrder = (TextView) itemView.findViewById(R.id.txtNamaPesanan);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
