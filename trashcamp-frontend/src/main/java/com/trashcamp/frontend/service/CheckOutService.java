package com.trashcamp.frontend.service;

import com.trashcamp.frontend.model.DetailSampah;
import com.trashcamp.frontend.model.Pendakian;

import java.util.List;

/**
 * Interface service untuk operasi Check-Out & Verifikasi sampah.
 */
public interface CheckOutService {

    /** Mendapatkan daftar detail sampah untuk kelompok tertentu. */
    List<DetailSampah> getDetailSampahByPendakianId(int pendakianId);

    /**
     * Menghitung total denda berdasarkan item yang hilang.
     * @param details daftar item sampah dengan jumlah naik dan turun
     * @return total denda dalam Rupiah
     */
    double hitungTotalDenda(List<DetailSampah> details);

    /**
     * Melakukan proses check-out: update status pendakian + catat denda.
     * @param pendakianId ID pendakian
     * @param details daftar item sampah hasil verifikasi
     * @return true jika berhasil
     */
    boolean checkOut(int pendakianId, List<DetailSampah> details);
}
