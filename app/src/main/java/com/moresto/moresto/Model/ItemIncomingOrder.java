package com.moresto.moresto.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by JOHN on 11/24/2017.
 */

public class ItemIncomingOrder {
    @SerializedName("food_id")
    @Expose
    private String id;
    @SerializedName("nama_food")
    @Expose
    private String nama_food;
    @SerializedName("jumlah")
    @Expose
    private String jumlah;
    @SerializedName("rupiah")
    @Expose
    private String harga;

    public ItemIncomingOrder(String id, String nama_food, String jumlah, String harga) {
        this.id = id;
        this.nama_food = nama_food;
        this.jumlah = jumlah;
        this.harga = harga;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getNama_food() {
        return nama_food;
    }

    public void setNama_food(String nama_food) {
        this.nama_food = nama_food;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
