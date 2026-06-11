package com.trashcamp.frontend.service;

import com.trashcamp.frontend.model.DetailSampah;
import com.trashcamp.frontend.model.MasterSampah;
import com.trashcamp.frontend.model.Pendakian;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementasi dummy CheckInService untuk tahap frontend-only.
 */
public class DummyCheckInService implements CheckInService {

    private static final List<MasterSampah> MASTER_LIST = new ArrayList<>();

    static {
        MASTER_LIST.add(new MasterSampah(1, "Botol Plastik", "Plastik", 5000, 15000));
        MASTER_LIST.add(new MasterSampah(2, "Kantong Plastik", "Plastik", 3000, 10000));
        MASTER_LIST.add(new MasterSampah(3, "Kaleng Minuman", "Metal", 8000, 20000));
        MASTER_LIST.add(new MasterSampah(4, "Bungkus Makanan", "Plastik", 3000, 10000));
        MASTER_LIST.add(new MasterSampah(5, "Baterai", "B3", 15000, 50000));
        MASTER_LIST.add(new MasterSampah(6, "Botol Kaca", "Kaca", 10000, 25000));
        MASTER_LIST.add(new MasterSampah(7, "Tisu / Kertas", "Organik", 2000, 5000));
        MASTER_LIST.add(new MasterSampah(8, "Sisa Makanan", "Organik", 2000, 8000));
    }

    @Override
    public List<MasterSampah> getMasterSampahList() {
        return new ArrayList<>(MASTER_LIST);
    }

    @Override
    public boolean checkIn(Pendakian pendakian, List<DetailSampah> details) {
        // Simulasi: selalu berhasil di frontend-only
        System.out.println("[DummyCheckIn] Check-in berhasil: " + pendakian.getNamaKetua()
                + " | " + details.size() + " item sampah");
        return true;
    }

    @Override
    public double hitungTotalDeposit(List<DetailSampah> details) {
        return details.stream().mapToDouble(DetailSampah::getTotalDeposit).sum();
    }
}
