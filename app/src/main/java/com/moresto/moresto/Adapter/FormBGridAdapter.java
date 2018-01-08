package com.moresto.moresto.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.moresto.moresto.FormBFragment;
import com.moresto.moresto.Model.ItemFormB;
import com.moresto.moresto.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormBGridAdapter extends RecyclerView.Adapter<FormBGridAdapter.ItemFormBListViewHolder> {
    private Context mContext;
    private ArrayList<ItemFormB> mItemFormB;
    private static final String TAG = "FormBGridAdapter";
    private ImageLoader imageLoader = ImageLoader.getInstance();
    public FormBGridAdapter(ArrayList<ItemFormB> objects) {
        mItemFormB= objects;
    }

    @Override
    public FormBGridAdapter.ItemFormBListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_form_bgrid_adapter, parent, false);
        FormBGridAdapter.ItemFormBListViewHolder pvh = new FormBGridAdapter.ItemFormBListViewHolder(v);
        mContext = parent.getContext();
       /* if (!imageLoader.isInited()) {
            Log.i(TAG, "onCreateViewHolder: masuk sini");
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                    .defaultDisplayImageOptions(defaultOptions)
                    .build();*/
          /*  DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new WeakMemoryCache())
                    .diskCacheSize(100 * 1024 * 1024)
                    .build();
            imageLoader.init(config);
        }*/
        return pvh;
    }

    @Override
    public void onBindViewHolder(final FormBGridAdapter.ItemFormBListViewHolder holder, final int position) {
        holder.txtNamaItem.setText(mItemFormB.get(position).getNama_bahan());
        if (!ImageLoader.getInstance().isInited()) {
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                    .defaultDisplayImageOptions(defaultOptions)
                    .build();
            ImageLoader.getInstance().init(config);
        }
      //  ImageLoader.getInstance().displayImage(mItemFormB.get(position).getImg_url(),
        //        holder.imgView);
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(holder.txtCount.getText().toString());
                if(count>=99) {
                    count=99;
                }
                else{
                    count++;
                    int tes = 0;
                    boolean ada = false;
                    if(FormBFragment.myItem.size()!=0) {
                        for (int i = 0; i < FormBFragment.myItem.size(); i++) {
                            if (FormBFragment.myItem.get(i).getId().equals(mItemFormB.get(position).getId())) {
                                tes = i;
                                ada = true;
                            }
                        }
                        Log.i(TAG, "onClick: "+ada);
                        if(!ada){
                            ItemFormB myItem = mItemFormB.get(position);
                            myItem.setJumlah(String.valueOf(count));
                            FormBFragment.myItem.add(myItem);
                        }
                        else{
                            FormBFragment.myItem.get(tes).setJumlah(String.valueOf(count));
                        }
                    }
                    else{
                        ItemFormB myItem = mItemFormB.get(position);
                        myItem.setJumlah(String.valueOf(count));
                        FormBFragment.myItem.add(myItem);
                    }

                    holder.txtCount.setText(String.valueOf(count));
                    Log.i(TAG, "onClick:=> "+count);
                }
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.valueOf(holder.txtCount.getText().toString());
                if(count<=0){
                    count=0;
                }
                else{
                    count--;
                    int tes = 0;
                    boolean ada = false;
                    if(FormBFragment.myItem.size()!=0) {
                        for (int i = 0; i < FormBFragment.myItem.size(); i++) {
                            if (FormBFragment.myItem.get(i).getId().equals(mItemFormB.get(position).getId())) {
                                tes = i;
                                ada = true;
                            }
                        }
                        if(!ada){
                            ItemFormB myItem = mItemFormB.get(position);
                            myItem.setJumlah(String.valueOf(count));
                            FormBFragment.myItem.add(myItem);
                        }
                        else{
                            FormBFragment.myItem.get(tes).setJumlah(String.valueOf(count));
                        }
                    }
                    else{
                        ItemFormB myItem = mItemFormB.get(position);
                        myItem.setJumlah(String.valueOf(count));
                        FormBFragment.myItem.add(myItem);
                    }

                    holder.txtCount.setText(String.valueOf(count));
                    Log.i(TAG, "onClick:=> "+count);
                }
            }
        });
        holder.imgView.setImageBitmap(null);

        if(mItemFormB.get(position).getImg_url() !=null && !mItemFormB.get(position).getImg_url().equals("")){
            File image = DiskCacheUtils.findInCache(mItemFormB.get(position).getImg_url(), imageLoader.getDiskCache());
            if(image !=null && image.exists()){
                Picasso.with(mContext).load(image).fit().into(holder.imgView);
            }else{
                imageLoader.loadImage(mItemFormB.get(position).getImg_url(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        holder.imgView.setImageBitmap(null);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Picasso.with(mContext).load(imageUri).fit().into(holder.imgView);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
        }else{
           holder.imgView.setImageBitmap(null);
        }
        /*
        Blurry.with(mContext).radius(2)
                .sampling(3)
                .async()
                .animate(500).from(loadedImage).into(holder.imgView);*/

    }

    @Override
    public int getItemCount() {
        return mItemFormB.size();
    }

    public static class ItemFormBListViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaItem;
        private ImageView imgView;
        private Button btnPlus;
        private Button btnMinus;
        private EditText txtCount;


        ItemFormBListViewHolder(View itemView) {
            super(itemView);
            txtNamaItem = (TextView) itemView.findViewById(R.id.txtNamaItemFormB);
            imgView = (ImageView) itemView.findViewById(R.id.imgItemFormB);
            btnMinus= (Button) itemView.findViewById(R.id.btnMinus);
            btnPlus= (Button) itemView.findViewById(R.id.btnPlus);
            txtCount = (EditText) itemView.findViewById(R.id.edCount);

        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}




  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_bgrid_adapter, container, false);
    }

}
*/