package com.trashcamp.trashcampbackend.repository;

import com.trashcamp.trashcampbackend.entity.MasterSampah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterSampahRepository extends JpaRepository<MasterSampah, Long> {
    // Dengan meng-extend JpaRepository, Spring otomatis menyediakan fungsi
    // findAll(), findById(), save(), dan deleteById() tanpa kita perlu tulis kode SQL sama sekali.
}