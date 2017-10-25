package com.moresto.moresto.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moresto.moresto.Model.ItemOrder;
import com.moresto.moresto.R;

import java.util.ArrayList;

public class ItemOrderAdapter extends RecyclerView.Adapter<ItemOrderAdapter.OrderListViewHolder> {
    private Context mContext;
    private ArrayList<ItemOrder> mOrderList;


    public ItemOrderAdapter(ArrayList<ItemOrder> objects) {
        mOrderList = objects;
    }

    @Override
    public ItemOrderAdapter.OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_order, parent, false);
        ItemOrderAdapter.OrderListViewHolder pvh = new ItemOrderAdapter.OrderListViewHolder(v);
        mContext = parent.getContext();
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ItemOrderAdapter.OrderListViewHolder holder, final int position) {
        holder.txtNamaItem.setText(mOrderList.get(position).getNmproduk());
        holder.txtJumlahItem.setText(String.valueOf(mOrderList.get(position).getJumlah()));
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
