package com.trashcamp.trashcampbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "petugas")
public class Petugas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // Nanti ini akan kita simpan dalam bentuk terenkripsi (Bcrypt)

    @Column(nullable = false)
    private String namaLengkap;

    // Constructor Kosong (Wajib untuk JPA)
    public Petugas() {
    }

    // Constructor dengan Parameter untuk mempermudah saat Data Seeding nanti
    public Petugas(String username, String password, String namaLengkap) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
    }

    // ==========================================
    // IMPLEMENTASI PILAR PBO: ENCAPSULATION
    // ==========================================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }
}