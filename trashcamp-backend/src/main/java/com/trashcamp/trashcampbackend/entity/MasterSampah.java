package com.trashcamp.trashcampbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "master_sampah")
public class MasterSampah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String namaItem;

    @Column(nullable = false)
    private String kategori; // Plastik, Organik, Metal, Kaca, B3, dll.

    @Column(nullable = false)
    private Double tarifDeposit;

    @Column(nullable = false)
    private Double tarifDenda;

    @Column(nullable = false)
    private Boolean aktif = true;

    // Constructor Kosong (Wajib untuk JPA)
    public MasterSampah() {
    }

    // Constructor Lengkap
    public MasterSampah(String namaItem, String kategori, Double tarifDeposit, Double tarifDenda, Boolean aktif) {
        this.namaItem = namaItem;
        this.kategori = kategori;
        this.tarifDeposit = tarifDeposit;
        this.tarifDenda = tarifDenda;
        this.aktif = aktif;
    }

    // Constructor lama agar seeder awal tidak error
    public MasterSampah(String namaItem, Double tarifDenda) {
        this.namaItem = namaItem;
        this.kategori = "Lainnya";
        this.tarifDeposit = tarifDenda * 0.4; // Estimasi deposit
        this.tarifDenda = tarifDenda;
        this.aktif = true;
    }

    // Getter dan Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamaItem() { return namaItem; }
    public void setNamaItem(String namaItem) { this.namaItem = namaItem; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public Double getTarifDeposit() { return tarifDeposit; }
    public void setTarifDeposit(Double tarifDeposit) { this.tarifDeposit = tarifDeposit; }
    public Double getTarifDenda() { return tarifDenda; }
    public void setTarifDenda(Double tarifDenda) { this.tarifDenda = tarifDenda; }
    public Boolean getAktif() { return aktif; }
    public void setAktif(Boolean aktif) { this.aktif = aktif; }
}