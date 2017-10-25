package com.moresto.moresto.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ItemOrder {
    @SerializedName("nmproduk")
    @Expose
    private String nmproduk;
    @SerializedName("no")
    @Expose
    private String no;
    @SerializedName("jumlah")
    @Expose
    private String jumlah;

    /**
     * No args constructor for use in serialization
     *
     */
    public ItemOrder() {
    }

    /**
     *
     * @param nmproduk
     * @param no
     * @param jumlah
     */
    public ItemOrder(String nmproduk, String no, String jumlah) {
        super();
        this.nmproduk = nmproduk;
        this.no = no;
        this.jumlah = jumlah;
    }

    public String getNmproduk() {
        return nmproduk;
    }

    public void setNmproduk(String nmproduk) {
        this.nmproduk = nmproduk;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
