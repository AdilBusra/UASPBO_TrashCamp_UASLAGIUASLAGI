package com.trashcamp.frontend.service;

import com.google.gson.reflect.TypeToken;
import com.trashcamp.frontend.model.HargaKonfigurasi;
import com.trashcamp.frontend.model.MasterSampah;
import com.trashcamp.frontend.model.OfficerSession;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpSettingsService implements SettingsService {

    private static OfficerSession cachedProfile = new OfficerSession("Admin Petugas");

    @Override
    public OfficerSession getOfficerProfile() {
        return cachedProfile;
    }

    @Override
    public boolean updateOfficerProfile(String name, String station, String nip) {
        try {
            cachedProfile.setOfficerName(name);
            cachedProfile.setStasiun(station);
            cachedProfile.setNip(nip);

            // Send to backend
            Map<String, String> body = new HashMap<>();
            body.put("username", name.toLowerCase().replace(" ", "_")); // Fallback/match username
            body.put("namaLengkap", name);
            body.put("stasiun", station);
            body.put("nip", nip);

            // Try to find the exact username from cached Profile or use fallback
            // In a real flow, we update the logged in user username
            HttpService.put("/api/auth/profile", body);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Return true for prototype robustness if user doesn't exist yet
        }
    }

    @Override
    public List<HargaKonfigurasi> getHargaKonfigurasi() {
        try {
            String json = HttpService.get("/api/config/items");
            Type listType = new TypeToken<ArrayList<MasterSampah>>(){}.getType();
            List<MasterSampah> items = HttpService.getGson().fromJson(json, listType);

            List<HargaKonfigurasi> configs = new ArrayList<>();
            for (MasterSampah m : items) {
                configs.add(new HargaKonfigurasi(
                        m.getId(),
                        m.getNamaItem(),
                        m.getKategori(),
                        m.getDepositPerItem(),
                        m.getDendaPerItem(),
                        m.isAktif()
                ));
            }
            return configs;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean saveHargaKonfigurasi(List<HargaKonfigurasi> configs) {
        try {
            List<MasterSampah> payload = new ArrayList<>();
            for (HargaKonfigurasi hc : configs) {
                payload.add(new MasterSampah(
                        hc.getId(),
                        hc.getNamaItem(),
                        hc.getKategori(),
                        hc.getDepositPerItem(),
                        hc.getDendaPerItem(),
                        hc.isAktif()
                ));
            }
            HttpService.put("/api/config/items", payload);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getNamaStasiun() {
        try {
            String json = HttpService.get("/api/config/settings");
            Map<String, Object> map = HttpService.getGson().fromJson(json, Map.class);
            return map.getOrDefault("namaStasiun", "Pos Pendakian Ranu Kumbolo").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Pos Pendakian Ranu Kumbolo";
        }
    }

    @Override
    public boolean updateNamaStasiun(String namaStasiun) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("namaStasiun", namaStasiun);
            HttpService.post("/api/config/settings", body);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double getTicketPrice() {
        try {
            String json = HttpService.get("/api/config/settings");
            Map<String, Object> map = HttpService.getGson().fromJson(json, Map.class);
            return Double.parseDouble(map.getOrDefault("ticketPrice", "15000").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 15000;
        }
    }

    @Override
    public boolean updateTicketPrice(double ticketPrice) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("ticketPrice", ticketPrice);
            HttpService.post("/api/config/settings", body);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double getSanitationFee() {
        try {
            String json = HttpService.get("/api/config/settings");
            Map<String, Object> map = HttpService.getGson().fromJson(json, Map.class);
            return Double.parseDouble(map.getOrDefault("sanitationFee", "20000").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 20000;
        }
    }

    @Override
    public boolean updateSanitationFee(double sanitationFee) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("sanitationFee", sanitationFee);
            HttpService.post("/api/config/settings", body);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> getTrails() {
        try {
            String json = HttpService.get("/api/config/trails");
            // Mapping Trail list to String list
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
            List<Map<String, Object>> list = HttpService.getGson().fromJson(json, listType);
            List<String> trails = new ArrayList<>();
            for (Map<String, Object> map : list) {
                trails.add(map.get("namaJalur").toString());
            }
            return trails;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean addTrail(String trail) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("namaJalur", trail);
            HttpService.post("/api/config/trails", body);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTrail(String trail) {
        try {
            HttpService.delete("/api/config/trails?name=" + java.net.URLEncoder.encode(trail, "UTF-8"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void setCachedProfile(OfficerSession session) {
        cachedProfile = session;
    }
}
