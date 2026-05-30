package com.trashcamp.frontend.model;

/**
 * Model untuk data kelompok pendakian.
 * Menerapkan Encapsulation: semua field private, akses via getter/setter.
 */
public class Pendakian {

    private int id;
    private String namaKetua;
    private String noHp;
    private int jumlahAnggota;
    private String trail;
    private String status; // "AKTIF", "SELESAI", "DENDA"
    private String waktuNaik;
    private String waktuTurun;
    private double totalDeposit;
    private double totalDenda;

    public Pendakian() {}

    public Pendakian(int id, String namaKetua, String noHp, int jumlahAnggota,
                     String trail, String status, String waktuNaik, String waktuTurun,
                     double totalDeposit, double totalDenda) {
        this.id = id;
        this.namaKetua = namaKetua;
        this.noHp = noHp;
        this.jumlahAnggota = jumlahAnggota;
        this.trail = trail;
        this.status = status;
        this.waktuNaik = waktuNaik;
        this.waktuTurun = waktuTurun;
        this.totalDeposit = totalDeposit;
        this.totalDenda = totalDenda;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getNamaKetua() { return namaKetua; }
    public String getNoHp() { return noHp; }
    public int getJumlahAnggota() { return jumlahAnggota; }
    public String getTrail() { return trail; }
    public String getStatus() { return status; }
    public String getWaktuNaik() { return waktuNaik; }
    public String getWaktuTurun() { return waktuTurun; }
    public double getTotalDeposit() { return totalDeposit; }
    public double getTotalDenda() { return totalDenda; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setNamaKetua(String namaKetua) { this.namaKetua = namaKetua; }
    public void setNoHp(String noHp) { this.noHp = noHp; }
    public void setJumlahAnggota(int jumlahAnggota) { this.jumlahAnggota = jumlahAnggota; }
    public void setTrail(String trail) { this.trail = trail; }
    public void setStatus(String status) { this.status = status; }
    public void setWaktuNaik(String waktuNaik) { this.waktuNaik = waktuNaik; }
    public void setWaktuTurun(String waktuTurun) { this.waktuTurun = waktuTurun; }
    public void setTotalDeposit(double totalDeposit) { this.totalDeposit = totalDeposit; }
    public void setTotalDenda(double totalDenda) { this.totalDenda = totalDenda; }

    @Override
    public String toString() {
        return "[" + id + "] " + namaKetua + " (" + jumlahAnggota + " org) - " + trail;
    }
}
