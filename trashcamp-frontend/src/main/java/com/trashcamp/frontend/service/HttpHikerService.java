package com.trashcamp.frontend.service;

import com.google.gson.reflect.TypeToken;
import com.trashcamp.frontend.model.Pendakian;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpHikerService implements HikerService {

    @Override
    public List<Pendakian> getAllPendakian() {
        try {
            String json = HttpService.get("/api/hikers");
            Type listType = new TypeToken<ArrayList<Pendakian>>(){}.getType();
            return HttpService.getGson().fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Pendakian> getAktifPendakian() {
        try {
            String json = HttpService.get("/api/hikers/aktif");
            Type listType = new TypeToken<ArrayList<Pendakian>>(){}.getType();
            return HttpService.getGson().fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Pendakian> searchPendakian(String keyword) {
        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            String json = HttpService.get("/api/hikers?keyword=" + encodedKeyword);
            Type listType = new TypeToken<ArrayList<Pendakian>>(){}.getType();
            return HttpService.getGson().fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Pendakian> filterByStatus(String status) {
        try {
            String encodedStatus = URLEncoder.encode(status, StandardCharsets.UTF_8);
            String json = HttpService.get("/api/hikers?status=" + encodedStatus);
            Type listType = new TypeToken<ArrayList<Pendakian>>(){}.getType();
            return HttpService.getGson().fromJson(json, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Pendakian getPendakianById(int id) {
        try {
            // Find in list
            return getAllPendakian().stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
