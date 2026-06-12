package com.trashcamp.trashcampbackend.repository;

import com.trashcamp.trashcampbackend.entity.Pendakian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PendakianRepository extends JpaRepository<Pendakian, Long> {
    List<Pendakian> findByStatus(String status);
}
