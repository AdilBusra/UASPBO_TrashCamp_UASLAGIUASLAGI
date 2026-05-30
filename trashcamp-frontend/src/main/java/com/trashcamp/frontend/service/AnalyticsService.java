package com.trashcamp.frontend.service;

import java.util.Map;

/**
 * Interface service untuk data Sustainability Analytics.
 */
public interface AnalyticsService {

    /** Mendapatkan eco compliance score (0-100). */
    int getEcoScore();

    /** Mendapatkan total berat sampah yang terkumpul (kg). */
    double getTotalWasteKg();

    /** Mendapatkan rata-rata sampah per pendaki (kg). */
    double getAvgWastePerHiker();

    /** Mendapatkan persentase kepatuhan (0-100). */
    double getComplianceRate();

    /** Mendapatkan distribusi sampah per kategori (kategori -> kg). */
    Map<String, Double> getWasteByCategory();

    /** Mendapatkan total denda yang terkumpul (Rupiah). */
    double getTotalDendaTerkumpul();

    /** Mendapatkan total pendakian bulan ini. */
    int getTotalPendakianBulanIni();
}
