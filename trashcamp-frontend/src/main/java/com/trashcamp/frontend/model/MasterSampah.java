package com.trashcamp.frontend.model;

/**
 * Model untuk jenis sampah beserta tarif deposit dan denda.
 * Menerapkan Encapsulation: semua field private, akses via getter/setter.
 */
public class MasterSampah {

    private int id;
    private String namaItem;
    private String kategori; // Plastik, Organik, Metal, Kaca, Lainnya
    private double depositPerItem;
    private double dendaPerItem;

    public MasterSampah() {}

    public MasterSampah(int id, String namaItem, String kategori,
                        double depositPerItem, double dendaPerItem) {
        this.id = id;
        this.namaItem = namaItem;
        this.kategori = kategori;
        this.depositPerItem = depositPerItem;
        this.dendaPerItem = dendaPerItem;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getNamaItem() { return namaItem; }
    public String getKategori() { return kategori; }
    public double getDepositPerItem() { return depositPerItem; }
    public double getDendaPerItem() { return dendaPerItem; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setNamaItem(String namaItem) { this.namaItem = namaItem; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public void setDepositPerItem(double depositPerItem) { this.depositPerItem = depositPerItem; }
    public void setDendaPerItem(double dendaPerItem) { this.dendaPerItem = dendaPerItem; }

    @Override
    public String toString() {
        return namaItem + " (" + kategori + ")";
    }
}
