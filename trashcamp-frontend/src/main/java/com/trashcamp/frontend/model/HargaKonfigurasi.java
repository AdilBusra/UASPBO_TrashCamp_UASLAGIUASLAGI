package com.trashcamp.frontend.model;

/**
 * Model untuk konfigurasi harga/deposit yang dapat diubah oleh Petugas.
 * Menerapkan Encapsulation: semua field private, akses via getter/setter.
 */
public class HargaKonfigurasi {

    private int id;
    private String namaItem;
    private String kategori;
    private double depositPerItem;
    private double dendaPerItem;
    private boolean aktif;

    public HargaKonfigurasi() {}

    public HargaKonfigurasi(int id, String namaItem, String kategori,
                            double depositPerItem, double dendaPerItem, boolean aktif) {
        this.id = id;
        this.namaItem = namaItem;
        this.kategori = kategori;
        this.depositPerItem = depositPerItem;
        this.dendaPerItem = dendaPerItem;
        this.aktif = aktif;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getNamaItem() { return namaItem; }
    public String getKategori() { return kategori; }
    public double getDepositPerItem() { return depositPerItem; }
    public double getDendaPerItem() { return dendaPerItem; }
    public boolean isAktif() { return aktif; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setNamaItem(String namaItem) { this.namaItem = namaItem; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public void setDepositPerItem(double depositPerItem) { this.depositPerItem = depositPerItem; }
    public void setDendaPerItem(double dendaPerItem) { this.dendaPerItem = dendaPerItem; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
}
