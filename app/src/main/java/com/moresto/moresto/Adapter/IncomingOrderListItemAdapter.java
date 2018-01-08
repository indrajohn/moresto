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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomingOrderListItemAdapter extends RecyclerView.Adapter<IncomingOrderListItemAdapter.OrderListViewHolder> {
    private Context mContext;
    private ArrayList<Object> mOrderList;


    public IncomingOrderListItemAdapter(ArrayList<Object> objects) {
        mOrderList = objects;
    }

    @Override
    public IncomingOrderListItemAdapter.OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_incoming_order_list_item_adapter, parent, false);
        IncomingOrderListItemAdapter.OrderListViewHolder pvh = new IncomingOrderListItemAdapter.OrderListViewHolder(v);
        mContext = parent.getContext();
        return pvh;
    }

    @Override
    public void onBindViewHolder(final IncomingOrderListItemAdapter.OrderListViewHolder holder, final int position) {
       // holder.txtNamaItem.setText(mOrderList.get(position).getNmproduk());
       // holder.txtJumlahItem.setText(String.valueOf(mOrderList.get(position).getJumlah()));
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaItem;
        private TextView txtJumlahItem;

        OrderListViewHolder(View itemView) {
            super(itemView);
            txtNamaItem = (TextView) itemView.findViewById(R.id.txtNamaItemList);
            txtJumlahItem = (TextView) itemView.findViewById(R.id.txtJumlahItemList);
        }
    }
}
