package com.moresto.moresto.Adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moresto.moresto.Model.ItemOrder;
import com.moresto.moresto.Model.Order;
import com.moresto.moresto.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.OrderListViewHolder> {
    private Context mContext;
    private ArrayList<Order> mOrderList;


    public DashboardAdapter(ArrayList<Order> objects) {
        mOrderList = objects;
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

        if(mOrderList.get(position).getNoOrder().equals("nodata")){
            holder.txtNoDataDashboard.setVisibility(View.VISIBLE);
            holder.linearLayout.setVisibility(View.GONE);
        }
        else {
            Log.i("myData", "onBindViewHolder: "+mOrderList.get(position).getNoOrder());
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.txtNoDataDashboard.setVisibility(View.GONE);
            holder.txtTujuanOrder.setText(mOrderList.get(position).getDelivery());
            holder.txtNoOrder.setText("Order "+mOrderList.get(position).getNoOrder());
            holder.txtWaktuOrder.setText(mOrderList.get(position).getTime());
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            holder.rcItem.setLayoutManager(llm);
            holder.rcItem.setHasFixedSize(true);
            ItemOrderAdapter adapter = new ItemOrderAdapter((ArrayList<ItemOrder>) mOrderList.get(position).getOrder());
            holder.rcItem.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rcItem;
        private TextView txtTujuanOrder,txtNoOrder,txtWaktuOrder,txtNoDataDashboard;
        private LinearLayout linearLayout;

        OrderListViewHolder(View itemView) {
            super(itemView);
            rcItem = (RecyclerView) itemView.findViewById(R.id.rcItemList);
            txtTujuanOrder = (TextView) itemView.findViewById(R.id.txtTujuanOrder);
            txtNoOrder = (TextView) itemView.findViewById(R.id.noOrder);
            txtWaktuOrder = (TextView) itemView.findViewById(R.id.waktuOrder);
            txtNoDataDashboard = (TextView) itemView.findViewById(R.id.txtNoDataDashboard);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linlay1);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
