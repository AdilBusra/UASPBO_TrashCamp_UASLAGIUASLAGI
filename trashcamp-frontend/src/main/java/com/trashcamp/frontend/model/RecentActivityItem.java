package com.trashcamp.frontend.model;

public class RecentActivityItem {

    private String title;
    private String detail;
    private String time;

    public RecentActivityItem() {
    }

    public RecentActivityItem(String title, String detail, String time) {
        this.title = title;
        this.detail = detail;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String toDisplayString() {
        return String.format("%s - %s (%s)", title, detail, time);
    }
}

