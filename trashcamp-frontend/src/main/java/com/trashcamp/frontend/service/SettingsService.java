package com.trashcamp.frontend.service;

import com.trashcamp.frontend.model.HargaKonfigurasi;
import com.trashcamp.frontend.model.OfficerSession;

import java.util.List;

/**
 * Interface service untuk konfigurasi sistem (Settings).
 */
public interface SettingsService {

    /** Mendapatkan profil petugas yang sedang login. */
    OfficerSession getOfficerProfile();

    /** Update nama dan stasiun petugas. */
    boolean updateOfficerProfile(String name, String station, String nip);

    /** Mendapatkan daftar konfigurasi harga item sampah. */
    List<HargaKonfigurasi> getHargaKonfigurasi();

    /** Menyimpan perubahan konfigurasi harga. */
    boolean saveHargaKonfigurasi(List<HargaKonfigurasi> configs);

    /** Mendapatkan nama stasiun pos. */
    String getNamaStasiun();

    /** Update nama stasiun pos. */
    boolean updateNamaStasiun(String namaStasiun);
}
