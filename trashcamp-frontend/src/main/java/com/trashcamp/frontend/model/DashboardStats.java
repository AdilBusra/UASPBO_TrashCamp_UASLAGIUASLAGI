package com.trashcamp.frontend.model;

public class DashboardStats {

    private int totalHikers;
    private int activeTrips;
    private int bagsTracked;
    private int totalDeposit;

    public DashboardStats() {
    }

    public DashboardStats(int totalHikers, int activeTrips, int bagsTracked, int totalDeposit) {
        this.totalHikers = totalHikers;
        this.activeTrips = activeTrips;
        this.bagsTracked = bagsTracked;
        this.totalDeposit = totalDeposit;
    }

    public int getTotalHikers() {
        return totalHikers;
    }

    public void setTotalHikers(int totalHikers) {
        this.totalHikers = totalHikers;
    }

    public int getActiveTrips() {
        return activeTrips;
    }

    public void setActiveTrips(int activeTrips) {
        this.activeTrips = activeTrips;
    }

    public int getBagsTracked() {
        return bagsTracked;
    }

    public void setBagsTracked(int bagsTracked) {
        this.bagsTracked = bagsTracked;
    }

    public int getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(int totalDeposit) {
        this.totalDeposit = totalDeposit;
    }
}

