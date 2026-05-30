package com.trashcamp.frontend.service;

import com.trashcamp.frontend.model.Pendakian;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementasi dummy HikerService untuk tahap frontend-only.
 * Semua data bersifat statis/simulasi.
 */
public class DummyHikerService implements HikerService {

    private final List<Pendakian> dummyData;

    public DummyHikerService() {
        dummyData = new ArrayList<>();
        dummyData.add(new Pendakian(1, "Budi Santoso", "08111234567", 5, "Ranu Kumbolo", "AKTIF", "2026-05-30 06:00", "-", 125000, 0));
        dummyData.add(new Pendakian(2, "Julian Thorne", "08222345678", 3, "Mahameru Summit", "AKTIF", "2026-05-30 07:30", "-", 75000, 0));
        dummyData.add(new Pendakian(3, "Sari Dewi", "08333456789", 7, "Oro-oro Ombo", "SELESAI", "2026-05-29 05:00", "2026-05-30 15:00", 175000, 0));
        dummyData.add(new Pendakian(4, "Ahmad Reza", "08444567890", 4, "Ranu Kumbolo", "DENDA", "2026-05-28 06:00", "2026-05-29 14:00", 100000, 50000));
        dummyData.add(new Pendakian(5, "Maya Putri", "08555678901", 6, "Mahameru Summit", "SELESAI", "2026-05-27 05:30", "2026-05-28 16:00", 150000, 0));
        dummyData.add(new Pendakian(6, "Rizki Firmansyah", "08666789012", 2, "Oro-oro Ombo", "AKTIF", "2026-05-30 08:00", "-", 50000, 0));
        dummyData.add(new Pendakian(7, "Dina Oktavia", "08777890123", 8, "Ranu Kumbolo", "DENDA", "2026-05-26 06:00", "2026-05-27 13:00", 200000, 75000));
        dummyData.add(new Pendakian(8, "Hendra Wijaya", "08888901234", 4, "Mahameru Summit", "SELESAI", "2026-05-25 07:00", "2026-05-26 17:00", 100000, 0));
    }

    @Override
    public List<Pendakian> getAllPendakian() {
        return new ArrayList<>(dummyData);
    }

    @Override
    public List<Pendakian> getAktifPendakian() {
        return dummyData.stream()
                .filter(p -> "AKTIF".equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pendakian> searchPendakian(String keyword) {
        if (keyword == null || keyword.isBlank()) return getAllPendakian();
        String lower = keyword.toLowerCase();
        return dummyData.stream()
                .filter(p -> p.getNamaKetua().toLowerCase().contains(lower)
                        || p.getTrail().toLowerCase().contains(lower)
                        || p.getNoHp().contains(keyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pendakian> filterByStatus(String status) {
        if (status == null || status.isBlank() || status.equals("Semua")) return getAllPendakian();
        return dummyData.stream()
                .filter(p -> status.equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public Pendakian getPendakianById(int id) {
        return dummyData.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
