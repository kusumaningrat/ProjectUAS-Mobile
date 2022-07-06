package com.kusumaningrat.projectuas;

public class ModelBarang {
    private String nama;
    private String harga;
    private String customer;
    private String tgl;
    private String key;

    public ModelBarang(){}
    public ModelBarang(String nama, String harga, String customer, String tgl) {
        this.nama = nama;
        this.harga = harga;
        this.customer = customer;
        this.tgl = tgl;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
