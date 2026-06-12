package com.trashcamp.frontend.model;

public class OfficerSession {

    private String officerName;
    private String nip;
    private String stasiun;
    private boolean isNewRegistration;

    public OfficerSession() {
    }

    public OfficerSession(String officerName) {
        this.officerName = officerName;
        this.nip = "";
        this.stasiun = "Pos Pendakian Utama";
        this.isNewRegistration = false;
    }

    public OfficerSession(String officerName, String nip, String stasiun, boolean isNewRegistration) {
        this.officerName = officerName;
        this.nip = nip;
        this.stasiun = stasiun;
        this.isNewRegistration = isNewRegistration;
    }

    // --- Getters & Setters ---
    public String getOfficerName() { return officerName; }
    public void setOfficerName(String officerName) { this.officerName = officerName; }
    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }
    public String getStasiun() { return stasiun; }
    public void setStasiun(String stasiun) { this.stasiun = stasiun; }
    public boolean isNewRegistration() { return isNewRegistration; }
    public void setNewRegistration(boolean newRegistration) { isNewRegistration = newRegistration; }
}
