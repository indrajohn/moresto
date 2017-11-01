package com.moresto.moresto.Adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.moresto.moresto.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListTableTransaksiAdapter extends ArrayAdapter<String> {
    int vg;
    String[] item_list;
    Context ctx;

    public ListTableTransaksiAdapter(@NonNull Context context, int vg,int id,@NonNull String[] item_list) {
        super(context, vg,id,item_list);
        this.ctx = context;
        this.item_list=item_list;
        this.vg = vg;
    }
    static class ViewHolder{
        public TextView no_transaksi,tanggal,lokasi,nama_pemesan,status;
    }
    public View getView(int position,View convertView,ViewGroup parent)
    {
        View rowView = convertView;
        {
            if(rowView == null){
                LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(vg,parent,false);
                ViewHolder holder = new ViewHolder();
                holder.no_transaksi = (TextView) rowView.findViewById(R.id.no_transaksi);
                holder.tanggal = (TextView) rowView.findViewById(R.id.tanggal);
                holder.lokasi = (TextView) rowView.findViewById(R.id.lokasi);
                holder.nama_pemesan = (TextView) rowView.findViewById(R.id.nama_pemesan);
                holder.status = (Button) rowView.findViewById(R.id.status);
                rowView.setTag(holder);
            }
            String[] items = item_list[position].split("__");
            Log.i("ListTableAdapter", "getView: "+items.length);
            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.no_transaksi.setText(items[0]);
            holder.tanggal.setText(items[1]);
            holder.lokasi.setText(items[2]);
            holder.nama_pemesan.setText(items[3]);
           // holder.status.setText(items[4]);
            return rowView;
        }
    }
}
