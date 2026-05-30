package com.trashcamp.frontend.model;

/**
 * Model untuk rincian item sampah bawaan per pendakian.
 * Menerapkan Encapsulation: semua field private, akses via getter/setter.
 */
public class DetailSampah {

    private MasterSampah masterSampah;
    private int jumlahNaik;
    private int jumlahTurun;

    public DetailSampah() {}

    public DetailSampah(MasterSampah masterSampah, int jumlahNaik, int jumlahTurun) {
        this.masterSampah = masterSampah;
        this.jumlahNaik = jumlahNaik;
        this.jumlahTurun = jumlahTurun;
    }

    // --- Derived Calculations ---

    /** Jumlah item yang tidak dikembalikan (hilang/tertinggal). */
    public int getJumlahHilang() {
        return Math.max(0, jumlahNaik - jumlahTurun);
    }

    /** Total deposit yang disetor saat Check-In. */
    public double getTotalDeposit() {
        if (masterSampah == null) return 0;
        return masterSampah.getDepositPerItem() * jumlahNaik;
    }

    /** Total denda yang dikenakan (per item hilang). */
    public double getTotalDenda() {
        if (masterSampah == null) return 0;
        return masterSampah.getDendaPerItem() * getJumlahHilang();
    }

    // --- Getters ---
    public MasterSampah getMasterSampah() { return masterSampah; }
    public int getJumlahNaik() { return jumlahNaik; }
    public int getJumlahTurun() { return jumlahTurun; }

    /** Nama item untuk display di tabel. */
    public String getNamaItem() {
        return masterSampah != null ? masterSampah.getNamaItem() : "-";
    }

    /** Kategori item untuk display di tabel. */
    public String getKategori() {
        return masterSampah != null ? masterSampah.getKategori() : "-";
    }

    /** Deposit per item (Rp) untuk display di tabel. */
    public double getDepositPerItem() {
        return masterSampah != null ? masterSampah.getDepositPerItem() : 0;
    }

    // --- Setters ---
    public void setMasterSampah(MasterSampah masterSampah) { this.masterSampah = masterSampah; }
    public void setJumlahNaik(int jumlahNaik) { this.jumlahNaik = jumlahNaik; }
    public void setJumlahTurun(int jumlahTurun) { this.jumlahTurun = jumlahTurun; }
}
