package com.moresto.moresto.Adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moresto.moresto.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListTransactionAdapter  extends RecyclerView.Adapter<ItemListTransactionAdapter.OrderListViewHolder> {
    private Context mContext;
    private JSONArray mOrderList;


    public ItemListTransactionAdapter(JSONArray objects) {
        mOrderList = objects;
    }

    @Override
    public ItemListTransactionAdapter.OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_list_transaction_adapter, parent, false);
        ItemListTransactionAdapter.OrderListViewHolder pvh = new ItemListTransactionAdapter.OrderListViewHolder(v);
        mContext = parent.getContext();
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ItemListTransactionAdapter.OrderListViewHolder holder, final int position) {

        try {
            JSONObject dataItem = (JSONObject) mOrderList.get(position);
            holder.txtNamaItem.setText(dataItem.get("nama_food").toString());
            holder.txtJumlahItem.setText(dataItem.get("jumlah").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mOrderList.length();
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaItem;
        private TextView txtJumlahItem;

        OrderListViewHolder(View itemView) {
            super(itemView);
            txtNamaItem = (TextView) itemView.findViewById(R.id.txtNamaItemDetailTransaction);
            txtJumlahItem = (TextView) itemView.findViewById(R.id.txtJumlahDetailTransaction);
        }
    }

}
