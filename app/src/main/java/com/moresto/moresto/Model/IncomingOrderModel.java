package com.moresto.moresto.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by JOHN on 11/24/2017.
 */

public class IncomingOrderModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("no_transaction")
    @Expose
    private String no_transaction;
    @SerializedName("total_biaya")
    @Expose
    private String total_biaya;
    @SerializedName("tgl_request_kirim_user")
    @Expose
    private String tgl_request_kirim_user;
    @SerializedName("lokasi")
    @Expose
    private String lokasi;

    public IncomingOrderModel(String id, String no_transaction, String total_biaya, String tgl_request_kirim_user, String lokasi) {
        this.id = id;
        this.no_transaction = no_transaction;
        this.total_biaya = total_biaya;
        this.tgl_request_kirim_user = tgl_request_kirim_user;
        this.lokasi = lokasi;
    }

    public String getTgl_request_kirim_user() {
        return tgl_request_kirim_user;
    }

    public void setTgl_request_kirim_user(String tgl_request_kirim_user) {
        this.tgl_request_kirim_user = tgl_request_kirim_user;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getTotal_biaya() {
        return total_biaya;
    }

    public void setTotal_biaya(String total_biaya) {
        this.total_biaya = total_biaya;
    }

    public String getNo_transaction() {
        return no_transaction;
    }

    public void setNo_transaction(String no_transaction) {
        this.no_transaction = no_transaction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
