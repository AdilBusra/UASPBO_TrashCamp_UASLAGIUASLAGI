package com.trashcamp.frontend.service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementasi dummy AnalyticsService untuk tahap frontend-only.
 */
public class DummyAnalyticsService implements AnalyticsService {

    @Override
    public int getEcoScore() { return 94; }

    @Override
    public double getTotalWasteKg() { return 450.5; }

    @Override
    public double getAvgWastePerHiker() { return 1.83; }

    @Override
    public double getComplianceRate() { return 91.5; }

    @Override
    public Map<String, Double> getWasteByCategory() {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("Plastik", 180.0);
        map.put("Metal", 95.0);
        map.put("Organik", 120.0);
        map.put("Kaca", 30.0);
        map.put("B3", 25.5);
        return map;
    }

    @Override
    public double getTotalDendaTerkumpul() { return 2_750_000; }

    @Override
    public int getTotalPendakianBulanIni() { return 85; }
}
