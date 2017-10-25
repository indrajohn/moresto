package com.moresto.moresto.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Dashboard {
    @SerializedName("totalkomisibulanini")
    @Expose
    private String totalkomisibulanini;
    @SerializedName("3transaksiterbaruhariini")
    @Expose
    private List<Order> _3transaksiterbaruhariini = null;
    @SerializedName("totaltransaksibulanini")
    @Expose
    private String totaltransaksibulanini;
    @SerializedName("totaltransaksihariini")
    @Expose
    private String totaltransaksihariini;

    /**
     * No args constructor for use in serialization
     *
     */
    public Dashboard() {
    }

    /**
     *
     * @param _3transaksiterbaruhariini
     * @param totalkomisibulanini
     * @param totaltransaksihariini
     * @param totaltransaksibulanini
     */
    public Dashboard(String totalkomisibulanini, List<Order> _3transaksiterbaruhariini, String totaltransaksibulanini, String totaltransaksihariini) {
        super();
        this.totalkomisibulanini = totalkomisibulanini;
        this._3transaksiterbaruhariini = _3transaksiterbaruhariini;
        this.totaltransaksibulanini = totaltransaksibulanini;
        this.totaltransaksihariini = totaltransaksihariini;
    }

    public String getTotalkomisibulanini() {
        return totalkomisibulanini;
    }

    public void setTotalkomisibulanini(String totalkomisibulanini) {
        this.totalkomisibulanini = totalkomisibulanini;
    }

    public List<Order> get3transaksiterbaruhariini() {
        return _3transaksiterbaruhariini;
    }

    public void set3transaksiterbaruhariini(List<Order> _3transaksiterbaruhariini) {
        this._3transaksiterbaruhariini = _3transaksiterbaruhariini;
    }

    public String getTotaltransaksibulanini() {
        return totaltransaksibulanini;
    }

    public void setTotaltransaksibulanini(String totaltransaksibulanini) {
        this.totaltransaksibulanini = totaltransaksibulanini;
    }

    public String getTotaltransaksihariini() {
        return totaltransaksihariini;
    }

    public void setTotaltransaksihariini(String totaltransaksihariini) {
        this.totaltransaksihariini = totaltransaksihariini;
    }
}
