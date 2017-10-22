package com.moresto.moresto.Adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moresto.moresto.Model.Dashboard;
import com.moresto.moresto.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.OrderListViewHolder> {
    private Context mContext;
    private ArrayList<Dashboard> mKoridorList;


    public DashboardAdapter(ArrayList<Dashboard> objects) {
        mKoridorList = objects;
    }

    @Override
    public DashboardAdapter.OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dashboard_adapter, parent, false);
        DashboardAdapter.OrderListViewHolder pvh = new DashboardAdapter.OrderListViewHolder(v);
        mContext = parent.getContext();
        return pvh;
    }

    @Override
    public void onBindViewHolder(final DashboardAdapter.OrderListViewHolder holder, final int position) {
        // holder.mNamaKoridor.setText(mKoridorList.get(position).getNamaKoridor());
        holder.rcItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       /* Intent intent = new Intent(mContext,HalteMapsActivity.class);
        intent.putExtra("idKoridor",mKoridorList.get(position).getId());
        mContext.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return mKoridorList.size();
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rcItem;
        private TextView txtTujuanOrder;

        OrderListViewHolder(View itemView) {
            super(itemView);
            rcItem = (RecyclerView) itemView.findViewById(R.id.rcItem);
            txtTujuanOrder = (TextView) itemView.findViewById(R.id.txtTujuanOrder);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
