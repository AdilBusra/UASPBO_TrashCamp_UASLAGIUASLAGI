package com.trashcamp.trashcampbackend.repository;

import com.trashcamp.trashcampbackend.entity.Petugas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PetugasRepository extends JpaRepository<Petugas, Long> {

    // Fungsi kustom untuk mencari petugas berdasarkan username saat proses login
    Optional<Petugas> findByUsername(String username);
}