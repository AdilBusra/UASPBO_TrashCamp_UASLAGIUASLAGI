package com.trashcamp.frontend.service;

import com.trashcamp.frontend.model.MasterSampah;
import com.trashcamp.frontend.model.DetailSampah;
import com.trashcamp.frontend.model.Pendakian;

import java.util.List;

/**
 * Interface service untuk operasi Check-In pendakian.
 */
public interface CheckInService {

    /** Mendapatkan daftar semua jenis sampah dari master data. */
    List<MasterSampah> getMasterSampahList();

    /**
     * Melakukan proses check-in: menyimpan data kelompok + logistik sampah.
     * @param pendakian data kelompok pendaki
     * @param details daftar item sampah bawaan
     * @return true jika berhasil
     */
    boolean checkIn(Pendakian pendakian, List<DetailSampah> details);

    /**
     * Menghitung total deposit berdasarkan item sampah yang dibawa.
     * @param details daftar item sampah
     * @return total deposit dalam Rupiah
     */
    double hitungTotalDeposit(List<DetailSampah> details);
}
