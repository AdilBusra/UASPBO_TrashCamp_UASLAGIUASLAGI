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
    private String password;

    @Column(nullable = false)
    private String namaLengkap;

    private String nip;

    private String stasiun;

    // Constructor Kosong (Wajib untuk JPA)
    public Petugas() {
    }

    // Constructor lengkap
    public Petugas(String username, String password, String namaLengkap) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.nip = "";
        this.stasiun = "Pos Pendakian Utama";
    }

    public Petugas(String username, String password, String namaLengkap, String nip, String stasiun) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.nip = nip;
        this.stasiun = stasiun;
    }

    // Getter dan Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }
    public String getStasiun() { return stasiun; }
    public void setStasiun(String stasiun) { this.stasiun = stasiun; }
}