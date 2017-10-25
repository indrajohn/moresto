package com.moresto.moresto.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Profile {
    @SerializedName("lokasi")
    @Expose
    private String lokasi;
    @SerializedName("no_hp")
    @Expose
    private String noHp;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("bergabungsejak")
    @Expose
    private String bergabungsejak;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("nama_usaha")
    @Expose
    private String namaUsaha;

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBergabungsejak() {
        return bergabungsejak;
    }

    public void setBergabungsejak(String bergabungsejak) {
        this.bergabungsejak = bergabungsejak;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNamaUsaha() {
        return namaUsaha;
    }

    public void setNamaUsaha(String namaUsaha) {
        this.namaUsaha = namaUsaha;
    }
}
