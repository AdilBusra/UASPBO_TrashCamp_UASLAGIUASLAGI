package com.trashcamp.frontend.service;

import com.trashcamp.frontend.model.DashboardStats;
import com.trashcamp.frontend.model.RecentActivityItem;

import java.util.List;

public class DummyDashboardService implements DashboardService {

    @Override
    public DashboardStats getDashboardStats() {
        // Dummy values untuk tahap frontend-only.
        return new DashboardStats(
                1240,
                85,
                450,
                1200
        );
    }

    @Override
    public List<RecentActivityItem> getRecentActivity() {
        return List.of(
                new RecentActivityItem("Check-In", "Kelompok Pendaki berhasil terdaftar", "Hari ini 08:12"),
                new RecentActivityItem("Waste Log", "Log sampah masuk: 3 item", "Hari ini 08:41"),
                new RecentActivityItem("Check-Out", "Penggunaan jaminan deposit diperbarui", "Kemarin 17:05"),
                new RecentActivityItem("Analytics", "Rekap denda terbaru telah diperbarui", "Kemarin 16:22")
        );
    }
}

