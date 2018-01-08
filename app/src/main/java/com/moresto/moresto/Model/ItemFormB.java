package com.moresto.moresto.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by JOHN on 11/17/2017.
 */

public class ItemFormB {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama_bahan")
    @Expose
    private String nama_bahan;
    @SerializedName("img_url")
    @Expose
    private String img_url;
    private String jumlah;

    /**
     * No args constructor for use in serialization
     *
     */
    public ItemFormB() {
    }

    public ItemFormB(String id, String nama_bahan, String img_url) {
        super();
        this.id = id;
        this.nama_bahan = nama_bahan;
        this.img_url = img_url;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_bahan() {
        return nama_bahan;
    }

    public void setNama_bahan(String nama_bahan) {
        this.nama_bahan = nama_bahan;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
