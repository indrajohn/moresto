package com.moresto.moresto;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.moresto.moresto.Adapter.ListTableTransaksiAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TableTransaksiFragment extends Fragment {
    ListView lv;
    ViewGroup headerView;

    AdaptiveTableLayout mTableLayout;

    public TableTransaksiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_table_transaksi, container, false);

        lv = (ListView) rootView.findViewById(R.id.lvTable);
        headerView = (ViewGroup) inflater.inflate(R.layout.fragment_table_header_transaksi,lv,false);
        lv.addHeaderView(headerView);
        String[] items = getResources().getStringArray(R.array.list_item);
        ListAdapter adapter = new ListTableTransaksiAdapter(getContext(),R.layout.fragment_list_table_transaksi_adapter, R.id.no_transaksi,items);
        lv.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


/*
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_list_table_transaksi_adapter, container, false);
        }*/

    }
}
