package com.trashcamp.trashcampbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pendakian")
public class Pendakian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String namaKetua;

    @Column(nullable = false)
    private String nomorHp;

    @Column(nullable = false)
    private Integer jumlahAnggota;

    private LocalDateTime waktuCheckIn;
    private LocalDateTime waktuCheckOut;

    @Column(nullable = false)
    private String status; // Contoh: "AKTIF" (di atas gunung) atau "SELESAI" (sudah turun)

    private Double totalDenda;

    public Pendakian() {
    }

    // Getter dan Setter (Encapsulation)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamaKetua() { return namaKetua; }
    public void setNamaKetua(String namaKetua) { this.namaKetua = namaKetua; }
    public String getNomorHp() { return nomorHp; }
    public void setNomorHp(String nomorHp) { this.nomorHp = nomorHp; }
    public Integer getJumlahAnggota() { return jumlahAnggota; }
    public void setJumlahAnggota(Integer jumlahAnggota) { this.jumlahAnggota = jumlahAnggota; }
    public LocalDateTime getWaktuCheckIn() { return waktuCheckIn; }
    public void setWaktuCheckIn(LocalDateTime waktuCheckIn) { this.waktuCheckIn = waktuCheckIn; }
    public LocalDateTime getWaktuCheckOut() { return waktuCheckOut; }
    public void setWaktuCheckOut(LocalDateTime waktuCheckOut) { this.waktuCheckOut = waktuCheckOut; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getTotalDenda() { return totalDenda; }
    public void setTotalDenda(Double totalDenda) { this.totalDenda = totalDenda; }
}