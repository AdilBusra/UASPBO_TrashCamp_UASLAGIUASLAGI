package com.trashcamp.frontend.service;

import com.google.gson.reflect.TypeToken;
import com.trashcamp.frontend.model.DetailSampah;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpCheckOutService implements CheckOutService {

    @Override
    public List<DetailSampah> getDetailSampahByPendakianId(int pendakianId) {
        try {
            String json = HttpService.get("/api/hikers/" + pendakianId + "/details");
            Type listType = new TypeToken<ArrayList<DetailSampah>>(){}.getType();
            return HttpService.getGson().fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public double hitungTotalDenda(List<DetailSampah> details) {
        return details.stream().mapToDouble(DetailSampah::getTotalDenda).sum();
    }

    @Override
    public boolean checkOut(int pendakianId, List<DetailSampah> details) {
        try {
            List<Map<String, Object>> payload = new ArrayList<>();
            for (DetailSampah d : details) {
                Map<String, Object> dMap = new HashMap<>();
                dMap.put("id", d.getId());
                dMap.put("jumlahTurun", d.getJumlahTurun());
                payload.add(dMap);
            }
            HttpService.post("/api/hikers/checkout/" + pendakianId, payload);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
