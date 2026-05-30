package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.model.OfficerSession;

/**
 * Interface yang wajib diimplementasi oleh semua sub-controller halaman.
 * Memungkinkan DashboardController mengirimkan session data ke setiap halaman
 * yang di-load secara dinamis ke contentArea.
 */
public interface ContentController {

    /**
     * Menerima data sesi petugas dari DashboardController.
     * @param session sesi login petugas yang sedang aktif
     */
    void setSession(OfficerSession session);

    /**
     * Dipanggil setelah setSession() untuk memuat & menampilkan data awal.
     */
    void initData();
}
