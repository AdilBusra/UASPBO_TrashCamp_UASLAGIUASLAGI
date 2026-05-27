package com.trashcamp.trashcampbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detail_sampah")
public class DetailSampah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pendakian_id", nullable = false)
    private Pendakian pendakian;

    @ManyToOne
    @JoinColumn(name = "master_sampah_id", nullable = false)
    private MasterSampah masterSampah;

    @Column(nullable = false)
    private Integer jumlahBawa; // Jumlah botol/kaleng saat naik

    private Integer jumlahKembali; // Jumlah botol/kaleng saat turun (diisi pas check-out)

    private Double subtotalDenda;

    public DetailSampah() {
    }

    // Getter dan Setter (Encapsulation)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Pendakian getPendakian() { return pendakian; }
    public void setPendakian(Pendakian pendakian) { this.pendakian = pendakian; }
    public MasterSampah getMasterSampah() { return masterSampah; }
    public void setMasterSampah(MasterSampah masterSampah) { this.masterSampah = masterSampah; }
    public Integer getJumlahBawa() { return jumlahBawa; }
    public void setJumlahBawa(Integer jumlahBawa) { this.jumlahBawa = jumlahBawa; }
    public Integer getJumlahKembali() { return jumlahKembali; }
    public void setJumlahKembali(Integer jumlahKembali) { this.jumlahKembali = jumlahKembali; }
    public Double getSubtotalDenda() { return subtotalDenda; }
    public void setSubtotalDenda(Double subtotalDenda) { this.subtotalDenda = subtotalDenda; }
}