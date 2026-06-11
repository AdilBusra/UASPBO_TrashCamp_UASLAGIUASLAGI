package com.trashcamp.frontend.service;

import com.trashcamp.frontend.model.HargaKonfigurasi;
import com.trashcamp.frontend.model.OfficerSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementasi dummy SettingsService untuk tahap frontend-only.
 */
public class DummySettingsService implements SettingsService {

    private String officerName = "Admin Petugas";
    private String officerStation = "Pos Ranu Kumbolo";
    private String officerNip = "NIP-2026-001";
    private String namaStasiun = "Pos Pendakian Ranu Kumbolo";

    private final List<HargaKonfigurasi> hargaList;

    public DummySettingsService() {
        hargaList = new ArrayList<>();
        hargaList.add(new HargaKonfigurasi(1, "Botol Plastik", "Plastik", 5000, 15000, true));
        hargaList.add(new HargaKonfigurasi(2, "Kantong Plastik", "Plastik", 3000, 10000, true));
        hargaList.add(new HargaKonfigurasi(3, "Kaleng Minuman", "Metal", 8000, 20000, true));
        hargaList.add(new HargaKonfigurasi(4, "Bungkus Makanan", "Plastik", 3000, 10000, true));
        hargaList.add(new HargaKonfigurasi(5, "Baterai", "B3", 15000, 50000, true));
        hargaList.add(new HargaKonfigurasi(6, "Botol Kaca", "Kaca", 10000, 25000, true));
        hargaList.add(new HargaKonfigurasi(7, "Tisu / Kertas", "Organik", 2000, 5000, true));
        hargaList.add(new HargaKonfigurasi(8, "Sisa Makanan", "Organik", 2000, 8000, true));
    }

    @Override
    public OfficerSession getOfficerProfile() {
        return new OfficerSession(officerName);
    }

    @Override
    public boolean updateOfficerProfile(String name, String station, String nip) {
        this.officerName = name;
        this.officerStation = station;
        this.officerNip = nip;
        System.out.println("[DummySettings] Profil diperbarui: " + name);
        return true;
    }

    @Override
    public List<HargaKonfigurasi> getHargaKonfigurasi() {
        return new ArrayList<>(hargaList);
    }

    @Override
    public boolean saveHargaKonfigurasi(List<HargaKonfigurasi> configs) {
        System.out.println("[DummySettings] Harga disimpan: " + configs.size() + " item");
        return true;
    }

    @Override
    public String getNamaStasiun() { return namaStasiun; }

    @Override
    public boolean updateNamaStasiun(String namaStasiun) {
        this.namaStasiun = namaStasiun;
        return true;
    }

    public String getOfficerStation() { return officerStation; }
    public String getOfficerNip() { return officerNip; }
}
