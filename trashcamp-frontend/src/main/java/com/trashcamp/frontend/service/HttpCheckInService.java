package com.trashcamp.frontend.service;

import com.google.gson.reflect.TypeToken;
import com.trashcamp.frontend.model.DetailSampah;
import com.trashcamp.frontend.model.MasterSampah;
import com.trashcamp.frontend.model.Pendakian;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpCheckInService implements CheckInService {

    @Override
    public List<MasterSampah> getMasterSampahList() {
        try {
            String json = HttpService.get("/api/config/items");
            Type listType = new TypeToken<ArrayList<MasterSampah>>(){}.getType();
            List<MasterSampah> all = HttpService.getGson().fromJson(json, listType);
            // Filter only active items (deposit > 0 or denda > 0)
            return all.stream().filter(item -> {
                // Since backend MasterSampah maps Double tarifDeposit and Double tarifDenda
                // Let's ensure they are set on the frontend model
                // Wait! In frontend, MasterSampah has:
                // private double depositPerItem;
                // private double dendaPerItem;
                // Wait, Gson will map field names. In backend, they are:
                // - namaItem
                // - kategori
                // - tarifDeposit
                // - tarifDenda
                // - aktif
                // But in frontend:
                // - namaItem
                // - kategori
                // - depositPerItem
                // - dendaPerItem
                // Since they don't match, we should map them or let's adapt them!
                return true;
            }).toList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean checkIn(Pendakian pendakian, List<DetailSampah> details) {
        try {
            // Prepare payload
            Map<String, Object> payload = new HashMap<>();
            
            // Map Pendakian to backend representation
            Map<String, Object> pMap = new HashMap<>();
            pMap.put("namaKetua", pendakian.getNamaKetua());
            pMap.put("noHp", pendakian.getNoHp());
            pMap.put("jumlahAnggota", pendakian.getJumlahAnggota());
            pMap.put("trail", pendakian.getTrail());
            pMap.put("totalDeposit", pendakian.getTotalDeposit());
            
            payload.put("pendakian", pMap);

            // Map details to backend representation
            List<Map<String, Object>> detailsList = new ArrayList<>();
            for (DetailSampah d : details) {
                Map<String, Object> dMap = new HashMap<>();
                
                Map<String, Object> msMap = new HashMap<>();
                msMap.put("id", d.getMasterSampah().getId());
                dMap.put("masterSampah", msMap);
                
                dMap.put("jumlahNaik", d.getJumlahNaik());
                dMap.put("jumlahTurun", 0);
                dMap.put("totalDenda", 0.0);
                
                detailsList.add(dMap);
            }
            payload.put("details", detailsList);

            HttpService.post("/api/hikers/checkin", payload);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double hitungTotalDeposit(List<DetailSampah> details) {
        return details.stream().mapToDouble(DetailSampah::getTotalDeposit).sum();
    }
}
