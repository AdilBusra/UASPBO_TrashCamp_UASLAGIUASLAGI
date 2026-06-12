package com.trashcamp.trashcampbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trail")
public class Trail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String namaJalur;

    public Trail() {
    }

    public Trail(String namaJalur) {
        this.namaJalur = namaJalur;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamaJalur() { return namaJalur; }
    public void setNamaJalur(String namaJalur) { this.namaJalur = namaJalur; }
}
