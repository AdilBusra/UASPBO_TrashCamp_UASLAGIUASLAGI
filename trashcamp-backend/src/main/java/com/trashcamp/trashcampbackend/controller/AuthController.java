package com.trashcamp.trashcampbackend.controller;

import com.trashcamp.trashcampbackend.entity.Petugas;
import com.trashcamp.trashcampbackend.repository.PetugasRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final PetugasRepository petugasRepository;

    public AuthController(PetugasRepository petugasRepository) {
        this.petugasRepository = petugasRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Petugas loginReq) {
        Optional<Petugas> opt = petugasRepository.findByUsername(loginReq.getUsername());
        if (opt.isPresent() && opt.get().getPassword().equals(loginReq.getPassword())) {
            return ResponseEntity.ok(opt.get());
        }
        return ResponseEntity.status(401).body("Username atau password salah.");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Petugas registerReq) {
        if (registerReq.getUsername() == null || registerReq.getUsername().trim().length() < 3) {
            return ResponseEntity.badRequest().body("Username minimal 3 karakter.");
        }
        if (registerReq.getPassword() == null || registerReq.getPassword().trim().length() < 8) {
            return ResponseEntity.badRequest().body("Password minimal harus 8 karakter.");
        }
        if (petugasRepository.findByUsername(registerReq.getUsername().trim()).isPresent()) {
            return ResponseEntity.badRequest().body("Username sudah digunakan.");
        }

        Petugas p = new Petugas();
        p.setUsername(registerReq.getUsername().trim());
        p.setPassword(registerReq.getPassword().trim());
        p.setNamaLengkap(registerReq.getNamaLengkap() != null ? registerReq.getNamaLengkap().trim() : registerReq.getUsername().trim());
        p.setNip("");
        p.setStasiun("Pos Pendakian Utama");

        Petugas saved = petugasRepository.save(p);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Petugas petugasReq) {
        Optional<Petugas> opt = petugasRepository.findByUsername(petugasReq.getUsername());
        if (opt.isPresent()) {
            Petugas p = opt.get();
            p.setNamaLengkap(petugasReq.getNamaLengkap().trim());
            p.setStasiun(petugasReq.getStasiun().trim());
            p.setNip(petugasReq.getNip().trim());
            petugasRepository.save(p);
            return ResponseEntity.ok(p);
        }
        return ResponseEntity.notFound().build();
    }
}
