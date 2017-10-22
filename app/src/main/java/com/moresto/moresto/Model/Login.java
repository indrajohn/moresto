package com.moresto.moresto.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("hasil")
    @Expose
    private boolean hasil;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("nama_usaha")
    @Expose
    private String namaUsaha;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("last_logout")
    @Expose
    private String lastLogout;
    @SerializedName("tokenid")
    @Expose
    private String tokenid;
    @SerializedName("msg")
    @Expose
    private List<Object> msg = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Login() {
    }

    /**
     *
     * @param id
     * @param lastLogin
     * @param username
     * @param namaUsaha
     * @param email
     * @param lastLogout
     * @param tokenid
     * @param hasil
     * @param nama
     * @param msg
     */
    public Login(boolean hasil, String id, String nama, String namaUsaha, String username, String email, String lastLogin, String lastLogout, String tokenid, List<Object> msg) {
        super();
        this.hasil = hasil;
        this.id = id;
        this.nama = nama;
        this.namaUsaha = namaUsaha;
        this.username = username;
        this.email = email;
        this.lastLogin = lastLogin;
        this.lastLogout = lastLogout;
        this.tokenid = tokenid;
        this.msg = msg;
    }

    public boolean isHasil() {
        return hasil;
    }

    public void setHasil(boolean hasil) {
        this.hasil = hasil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNamaUsaha() {
        return namaUsaha;
    }

    public void setNamaUsaha(String namaUsaha) {
        this.namaUsaha = namaUsaha;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastLogout() {
        return lastLogout;
    }

    public void setLastLogout(String lastLogout) {
        this.lastLogout = lastLogout;
    }

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public List<Object> getMsg() {
        return msg;
    }

    public void setMsg(List<Object> msg) {
        this.msg = msg;
    }
}
