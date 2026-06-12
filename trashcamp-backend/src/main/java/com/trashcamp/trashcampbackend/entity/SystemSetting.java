package com.trashcamp.trashcampbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "system_setting")
public class SystemSetting {

    @Id
    private String settingKey; // Contoh: ticketPrice, sanitationFee, namaStasiun

    @Column(nullable = false)
    private String settingValue;

    public SystemSetting() {
    }

    public SystemSetting(String settingKey, String settingValue) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
    }

    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }
    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }
}
