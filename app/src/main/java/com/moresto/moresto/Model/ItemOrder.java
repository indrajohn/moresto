package com.moresto.moresto.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ItemOrder {
    @SerializedName("0")
    @Expose
    private String id;
    @SerializedName("1")
    @Expose
    private String namaItem;
    @SerializedName("2")
    @Expose
    private String jumlahItem;

    public ItemOrder(String id,String namaItem,String jumlahItem) {
        super();
        this.id = id;
        this.namaItem = namaItem;
        this.jumlahItem = jumlahItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public String getJumlahItem() {
        return jumlahItem;
    }

    public void setJumlahItem(String jumlahItem) {
        this.jumlahItem = jumlahItem;
    }
}
