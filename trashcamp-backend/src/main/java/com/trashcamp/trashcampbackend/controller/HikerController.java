package com.trashcamp.trashcampbackend.controller;

import com.trashcamp.trashcampbackend.entity.DetailSampah;
import com.trashcamp.trashcampbackend.entity.Pendakian;
import com.trashcamp.trashcampbackend.repository.DetailSampahRepository;
import com.trashcamp.trashcampbackend.repository.PendakianRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hikers")
@CrossOrigin(origins = "*")
public class HikerController {

    private final PendakianRepository pendakianRepository;
    private final DetailSampahRepository detailSampahRepository;

    public HikerController(PendakianRepository pendakianRepository,
                           DetailSampahRepository detailSampahRepository) {
        this.pendakianRepository = pendakianRepository;
        this.detailSampahRepository = detailSampahRepository;
    }

    @GetMapping
    public List<Pendakian> getAll(@RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) String status) {
        List<Pendakian> list = pendakianRepository.findAll();
        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.toLowerCase();
            list = list.stream()
                    .filter(p -> p.getNamaKetua().toLowerCase().contains(kw)
                            || (p.getTrail() != null && p.getTrail().toLowerCase().contains(kw))
                            || p.getNomorHp().contains(keyword))
                    .toList();
        }
        if (status != null && !status.equals("Semua")) {
            list = list.stream()
                    .filter(p -> status.equalsIgnoreCase(p.getStatus()))
                    .toList();
        }
        return list;
    }

    @GetMapping("/aktif")
    public List<Pendakian> getAktif() {
        return pendakianRepository.findByStatus("AKTIF");
    }

    @PostMapping("/checkin")
    public ResponseEntity<?> checkIn(@RequestBody Map<String, Object> payload) {
        try {
            Map<String, Object> pMap = (Map<String, Object>) payload.get("pendakian");
            Pendakian p = new Pendakian();
            p.setNamaKetua((String) pMap.get("namaKetua"));
            p.setNomorHp((String) pMap.get("noHp"));
            p.setJumlahAnggota((Integer) pMap.get("jumlahAnggota"));
            p.setTrail((String) pMap.get("trail"));
            p.setStatus("AKTIF");
            p.setWaktuCheckIn(LocalDateTime.now());
            p.setTotalDeposit(Double.parseDouble(pMap.get("totalDeposit").toString()));
            p.setTotalDenda(0.0);

            Pendakian savedPendakian = pendakianRepository.save(p);

            List<Map<String, Object>> details = (List<Map<String, Object>>) payload.get("details");
            for (Map<String, Object> dMap : details) {
                DetailSampah d = new DetailSampah();
                d.setPendakian(savedPendakian);

                Map<String, Object> msMap = (Map<String, Object>) dMap.get("masterSampah");
                com.trashcamp.trashcampbackend.entity.MasterSampah ms = new com.trashcamp.trashcampbackend.entity.MasterSampah();
                ms.setId(Long.parseLong(msMap.get("id").toString()));
                d.setMasterSampah(ms);

                d.setJumlahBawa((Integer) dMap.get("jumlahNaik"));
                d.setJumlahKembali(0);
                d.setSubtotalDenda(0.0);

                detailSampahRepository.save(d);
            }

            return ResponseEntity.ok(savedPendakian);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Gagal check-in: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/details")
    public List<DetailSampah> getDetailsByPendakianId(@PathVariable Long id) {
        return detailSampahRepository.findByPendakianId(id);
    }

    @PostMapping("/checkout/{id}")
    public ResponseEntity<?> checkOut(@PathVariable Long id, @RequestBody List<Map<String, Object>> payload) {
        try {
            Pendakian p = pendakianRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pendakian tidak ditemukan"));

            double totalDenda = 0.0;
            for (Map<String, Object> dMap : payload) {
                Long detailId = Long.parseLong(dMap.get("id").toString());
                DetailSampah d = detailSampahRepository.findById(detailId)
                        .orElseThrow(() -> new RuntimeException("Detail logistik sampah tidak ditemukan"));

                int qtyKembali = Integer.parseInt(dMap.get("jumlahTurun").toString());
                d.setJumlahKembali(qtyKembali);

                int hilang = Math.max(0, d.getJumlahBawa() - qtyKembali);
                double dendaItem = d.getMasterSampah().getTarifDenda() * hilang;
                d.setSubtotalDenda(dendaItem);

                detailSampahRepository.save(d);
                totalDenda += dendaItem;
            }

            p.setStatus(totalDenda > 0 ? "DENDA" : "SELESAI");
            p.setTotalDenda(totalDenda);
            p.setWaktuCheckOut(LocalDateTime.now());
            pendakianRepository.save(p);

            return ResponseEntity.ok(p);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Gagal check-out: " + e.getMessage());
        }
    }
}
