package com.trashcamp.frontend.service;

import com.trashcamp.frontend.model.DetailSampah;
import com.trashcamp.frontend.model.MasterSampah;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementasi dummy CheckOutService untuk tahap frontend-only.
 */
public class DummyCheckOutService implements CheckOutService {

    @Override
    public List<DetailSampah> getDetailSampahByPendakianId(int pendakianId) {
        // Simulasi data berdasarkan ID pendakian
        List<DetailSampah> result = new ArrayList<>();
        switch (pendakianId % 4) {
            case 1:
                result.add(new DetailSampah(new MasterSampah(1, "Botol Plastik", "Plastik", 5000, 15000), 5, 3));
                result.add(new DetailSampah(new MasterSampah(3, "Kaleng Minuman", "Metal", 8000, 20000), 4, 4));
                result.add(new DetailSampah(new MasterSampah(7, "Tisu / Kertas", "Organik", 2000, 5000), 10, 9));
                break;
            case 2:
                result.add(new DetailSampah(new MasterSampah(1, "Botol Plastik", "Plastik", 5000, 15000), 3, 3));
                result.add(new DetailSampah(new MasterSampah(4, "Bungkus Makanan", "Plastik", 3000, 10000), 6, 5));
                break;
            case 3:
                result.add(new DetailSampah(new MasterSampah(2, "Kantong Plastik", "Plastik", 3000, 10000), 8, 6));
                result.add(new DetailSampah(new MasterSampah(5, "Baterai", "B3", 15000, 50000), 4, 2));
                result.add(new DetailSampah(new MasterSampah(6, "Botol Kaca", "Kaca", 10000, 25000), 2, 2));
                break;
            default:
                result.add(new DetailSampah(new MasterSampah(1, "Botol Plastik", "Plastik", 5000, 15000), 6, 6));
                result.add(new DetailSampah(new MasterSampah(8, "Sisa Makanan", "Organik", 2000, 8000), 5, 5));
                break;
        }
        return result;
    }

    @Override
    public double hitungTotalDenda(List<DetailSampah> details) {
        return details.stream().mapToDouble(DetailSampah::getTotalDenda).sum();
    }

    @Override
    public boolean checkOut(int pendakianId, List<DetailSampah> details) {
        double denda = hitungTotalDenda(details);
        System.out.println("[DummyCheckOut] Check-out ID=" + pendakianId
                + " | Denda: Rp " + String.format("%.0f", denda));
        return true;
    }
}
