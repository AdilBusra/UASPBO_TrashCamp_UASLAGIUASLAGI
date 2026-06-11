package com.trashcamp.frontend.service;

import com.trashcamp.frontend.model.DashboardStats;
import com.trashcamp.frontend.model.RecentActivityItem;

import java.util.List;

public interface DashboardService {

    DashboardStats getDashboardStats();

    List<RecentActivityItem> getRecentActivity();
}

