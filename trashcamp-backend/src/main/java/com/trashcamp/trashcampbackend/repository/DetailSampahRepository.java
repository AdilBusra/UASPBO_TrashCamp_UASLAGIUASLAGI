package com.trashcamp.trashcampbackend.repository;

import com.trashcamp.trashcampbackend.entity.DetailSampah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetailSampahRepository extends JpaRepository<DetailSampah, Long> {
    List<DetailSampah> findByPendakianId(Long pendakianId);
}
