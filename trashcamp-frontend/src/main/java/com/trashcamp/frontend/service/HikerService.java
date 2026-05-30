package com.trashcamp.frontend.service;

import com.trashcamp.frontend.model.Pendakian;

import java.util.List;

/**
 * Interface service untuk operasi data pendakian.
 * Backend akan mengimplementasikan interface ini via REST API.
 */
public interface HikerService {

    /** Mendapatkan semua data pendakian. */
    List<Pendakian> getAllPendakian();

    /** Mendapatkan pendakian yang masih aktif (status = AKTIF). */
    List<Pendakian> getAktifPendakian();

    /** Mencari pendakian berdasarkan keyword (nama ketua / trail). */
    List<Pendakian> searchPendakian(String keyword);

    /** Filter pendakian berdasarkan status. */
    List<Pendakian> filterByStatus(String status);

    /** Mendapatkan pendakian berdasarkan ID. */
    Pendakian getPendakianById(int id);
}
