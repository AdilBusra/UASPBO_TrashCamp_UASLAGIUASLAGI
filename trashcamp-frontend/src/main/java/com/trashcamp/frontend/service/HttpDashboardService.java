package com.trashcamp.frontend.service;

import com.trashcamp.frontend.model.DashboardStats;
import com.trashcamp.frontend.model.Pendakian;
import com.trashcamp.frontend.model.RecentActivityItem;

import java.util.ArrayList;
import java.util.List;

public class HttpDashboardService implements DashboardService {

    private final HikerService hikerService = new HttpHikerService();
    private final AnalyticsService analyticsService = new HttpAnalyticsService();

    @Override
    public DashboardStats getDashboardStats() {
        List<Pendakian> all = hikerService.getAllPendakian();
        int totalHikerGroups = all.size();
        
        long activeGroups = all.stream().filter(p -> "AKTIF".equals(p.getStatus())).count();
        double dendaTotal = all.stream().mapToDouble(Pendakian::getTotalDenda).sum();
        double ecoScore = analyticsService.getEcoScore();

        return new DashboardStats(
                (int) activeGroups,
                totalHikerGroups,
                (int) ecoScore,
                (int) dendaTotal
        );
    }

    @Override
    public List<RecentActivityItem> getRecentActivity() {
        List<Pendakian> all = hikerService.getAllPendakian();
        List<RecentActivityItem> activities = new ArrayList<>();
        
        int count = 0;
        for (int i = all.size() - 1; i >= 0 && count < 5; i--) {
            Pendakian p = all.get(i);
            String title;
            String desc;
            String timeString;

            if ("AKTIF".equals(p.getStatus())) {
                title = "Check-In Baru";
                desc = p.getNamaKetua() + " (" + p.getJumlahAnggota() + " org) menuju " + p.getTrail();
                timeString = "Aktif";
            } else if ("SELESAI".equals(p.getStatus())) {
                title = "Check-Out Bersih";
                desc = p.getNamaKetua() + " mengembalikan semua sampah.";
                timeString = "Selesai";
            } else {
                title = "Check-Out Denda";
                desc = p.getNamaKetua() + " didenda Rp " + String.format("%.0f", p.getTotalDenda()) + " karena kehilangan sampah.";
                timeString = "Denda";
            }

            activities.add(new RecentActivityItem(title, desc, timeString));
            count++;
        }

        if (activities.isEmpty()) {
            activities.add(new RecentActivityItem("Belum ada aktivitas", "Aktivitas check-in/out pendaki akan muncul di sini.", "Info"));
        }

        return activities;
    }
}
