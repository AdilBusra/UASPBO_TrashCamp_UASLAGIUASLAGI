package com.trashcamp.trashcampbackend.controller;

import com.trashcamp.trashcampbackend.entity.MasterSampah;
import com.trashcamp.trashcampbackend.entity.Trail;
import com.trashcamp.trashcampbackend.entity.SystemSetting;
import com.trashcamp.trashcampbackend.repository.MasterSampahRepository;
import com.trashcamp.trashcampbackend.repository.TrailRepository;
import com.trashcamp.trashcampbackend.repository.SystemSettingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "*")
public class ConfigController {

    private final MasterSampahRepository masterSampahRepository;
    private final TrailRepository trailRepository;
    private final SystemSettingRepository systemSettingRepository;

    public ConfigController(MasterSampahRepository masterSampahRepository,
                            TrailRepository trailRepository,
                            SystemSettingRepository systemSettingRepository) {
        this.masterSampahRepository = masterSampahRepository;
        this.trailRepository = trailRepository;
        this.systemSettingRepository = systemSettingRepository;
    }

    // --- Master Sampah ---
    @GetMapping("/items")
    public List<MasterSampah> getItems() {
        return masterSampahRepository.findAll();
    }

    @PostMapping("/items")
    public MasterSampah addItem(@RequestBody MasterSampah item) {
        return masterSampahRepository.save(item);
    }

    @PutMapping("/items")
    public List<MasterSampah> saveItems(@RequestBody List<MasterSampah> items) {
        return masterSampahRepository.saveAll(items);
    }

    // --- Trails ---
    @GetMapping("/trails")
    public List<Trail> getTrails() {
        return trailRepository.findAll();
    }

    @PostMapping("/trails")
    public ResponseEntity<?> addTrail(@RequestBody Trail trail) {
        if (trail.getNamaJalur() == null || trail.getNamaJalur().isBlank()) {
            return ResponseEntity.badRequest().body("Nama jalur tidak boleh kosong.");
        }
        if (trailRepository.findByNamaJalur(trail.getNamaJalur().trim()).isPresent()) {
            return ResponseEntity.badRequest().body("Nama jalur sudah ada.");
        }
        trail.setNamaJalur(trail.getNamaJalur().trim());
        return ResponseEntity.ok(trailRepository.save(trail));
    }

    @DeleteMapping("/trails")
    public ResponseEntity<?> deleteTrail(@RequestParam String name) {
        trailRepository.findByNamaJalur(name.trim()).ifPresent(trailRepository::delete);
        return ResponseEntity.ok().build();
    }

    // --- Fees / Settings ---
    @GetMapping("/settings")
    public ResponseEntity<?> getSettings() {
        String ticketPrice = systemSettingRepository.findById("ticketPrice")
                .map(SystemSetting::getSettingValue).orElse("15000");
        String sanitationFee = systemSettingRepository.findById("sanitationFee")
                .map(SystemSetting::getSettingValue).orElse("20000");
        String namaStasiun = systemSettingRepository.findById("namaStasiun")
                .map(SystemSetting::getSettingValue).orElse("Pos Pendakian Ranu Kumbolo");

        return ResponseEntity.ok(Map.of(
                "ticketPrice", Double.parseDouble(ticketPrice),
                "sanitationFee", Double.parseDouble(sanitationFee),
                "namaStasiun", namaStasiun
        ));
    }

    @PostMapping("/settings")
    public ResponseEntity<?> updateSettings(@RequestBody Map<String, Object> req) {
        if (req.containsKey("ticketPrice")) {
            systemSettingRepository.save(new SystemSetting("ticketPrice", req.get("ticketPrice").toString()));
        }
        if (req.containsKey("sanitationFee")) {
            systemSettingRepository.save(new SystemSetting("sanitationFee", req.get("sanitationFee").toString()));
        }
        if (req.containsKey("namaStasiun")) {
            systemSettingRepository.save(new SystemSetting("namaStasiun", req.get("namaStasiun").toString()));
        }
        return ResponseEntity.ok().build();
    }
}
