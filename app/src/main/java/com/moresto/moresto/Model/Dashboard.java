package com.moresto.moresto.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Dashboard {
    @SerializedName("totaltransaksihariini")
    @Expose
    private String totaltransaksihariini;
    @SerializedName("totaltransaksibulanini")
    @Expose
    private String totaltransaksibulanini;
    @SerializedName("totalkomisibulanini")
    @Expose
    private String totalkomisibulanini;
    @SerializedName("3transaksiterbaruhariini")
    @Expose
    private List<Order> OrderList = null;

    public String getTotaltransaksihariini() {
        return totaltransaksihariini;
    }

    public void setTotaltransaksihariini(String totaltransaksihariini) {
        this.totaltransaksihariini = totaltransaksihariini;
    }

    public String getTotaltransaksibulanini() {
        return totaltransaksibulanini;
    }

    public void setTotaltransaksibulanini(String totaltransaksibulanini) {
        this.totaltransaksibulanini = totaltransaksibulanini;
    }

    public String getTotalkomisibulanini() {
        return totalkomisibulanini;
    }

    public void setTotalkomisibulanini(String totalkomisibulanini) {
        this.totalkomisibulanini = totalkomisibulanini;
    }

    public List<Order> get3transaksiterbaruhariini() {
        return OrderList;
    }

    public void set3transaksiterbaruhariini(List<Order> OrderList) {
        this.OrderList = OrderList;
    }
}
