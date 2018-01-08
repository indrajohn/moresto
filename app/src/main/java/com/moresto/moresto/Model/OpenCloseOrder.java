package com.moresto.moresto.Model;

/**
 * Created by root on 11/4/17.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class OpenCloseOrder {
    @SerializedName("koordinatx")
    @Expose
    private String koordinatx;
    @SerializedName("koordinaty")
    @Expose
    private String koordinaty;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("status_openclose")
    @Expose
    private String status_openclose;
    public OpenCloseOrder() {
    }
    public OpenCloseOrder(String koordinatx,String koordinaty,String status_openclose,String alamat) {
        this.koordinatx = koordinatx;
        this.koordinaty = koordinaty;
        this.status_openclose = status_openclose;
        this.alamat = alamat;
    }

    public String getKoordinatx() {
        return koordinatx;
    }

    public void setKoordinatx(String koordinatx) {
        this.koordinatx = koordinatx;
    }

    public String getKoordinaty() {
        return koordinaty;
    }

    public void setKoordinaty(String koordinaty) {
        this.koordinaty = koordinaty;
    }

    public String getStatus_openclose() {
        return status_openclose;
    }

    public void setStatus_openclose(String status_openclose) {
        this.status_openclose = status_openclose;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
