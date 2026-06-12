package com.trashcamp.trashcampbackend.repository;

import com.trashcamp.trashcampbackend.entity.Trail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Long> {
    Optional<Trail> findByNamaJalur(String namaJalur);
}
