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
    private Double tarifDenda;

    // ==========================================
    // IMPLEMENTASI PILAR PBO: ENCAPSULATION
    // ==========================================

    // Constructor Kosong (Wajib untuk JPA)
    public MasterSampah() {
    }

    // Constructor dengan Parameter untuk mempermudah instansiasi
    public MasterSampah(String namaItem, Double tarifDenda) {
        this.namaItem = namaItem;
        this.tarifDenda = tarifDenda;
    }

    // Getter dan Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public Double getTarifDenda() {
        return tarifDenda;
    }

    public void setTarifDenda(Double tarifDenda) {
        this.tarifDenda = tarifDenda;
    }
}