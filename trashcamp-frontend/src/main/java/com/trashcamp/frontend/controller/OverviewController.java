package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.model.OfficerSession;
import com.trashcamp.frontend.model.RecentActivityItem;
import com.trashcamp.frontend.service.DashboardService;
import com.trashcamp.frontend.service.DummyAnalyticsService;
import com.trashcamp.frontend.service.DummyDashboardService;
import com.trashcamp.frontend.service.AnalyticsService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Controller untuk halaman Overview/Dashboard.
 */
public class OverviewController implements ContentController {

    @FXML private Label lblTotalHikers;
    @FXML private Label lblHikersChange;
    @FXML private Label lblTotalWaste;
    @FXML private Label lblWasteChange;
    @FXML private Label lblActiveTrips;
    @FXML private Label lblTotalDeposit;
    @FXML private BarChart<String, Number> wasteBarChart;
    @FXML private Canvas ecoScoreCanvas;
    @FXML private Label lblEcoScore;
    @FXML private ListView<String> lvRecentActivity;

    private OfficerSession session;
    private final DashboardService dashboardService = new DummyDashboardService();
    private final AnalyticsService analyticsService = new DummyAnalyticsService();

    @Override
    public void setSession(OfficerSession session) {
        this.session = session;
    }

    @Override
    public void initData() {
        loadStats();
        loadBarChart();
        loadEcoScore();
        loadRecentActivity();
    }

    private void loadStats() {
        var stats = dashboardService.getDashboardStats();
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));

        if (lblTotalHikers != null)
            lblTotalHikers.setText(nf.format(stats.getTotalHikers()));
        if (lblTotalWaste != null)
            lblTotalWaste.setText(nf.format(stats.getBagsTracked()) + " kg");
        if (lblActiveTrips != null)
            lblActiveTrips.setText(String.valueOf(stats.getActiveTrips()));
        if (lblTotalDeposit != null) {
            double dep = stats.getTotalDeposit();
            if (dep >= 1_000_000) {
                lblTotalDeposit.setText(String.format("Rp %.1f Jt", dep / 1_000_000));
            } else {
                lblTotalDeposit.setText("Rp " + nf.format((long) dep));
            }
        }
    }

    private void loadBarChart() {
        if (wasteBarChart == null) return;
        wasteBarChart.getData().clear();

        Map<String, Double> categories = analyticsService.getWasteByCategory();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sampah (kg)");
        for (Map.Entry<String, Double> entry : categories.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        wasteBarChart.getData().add(series);
    }

    private void loadEcoScore() {
        int score = analyticsService.getEcoScore();
        if (lblEcoScore != null) lblEcoScore.setText(String.valueOf(score));

        if (ecoScoreCanvas != null) {
            drawEcoGauge(ecoScoreCanvas, score);
        }
    }

    /** Menggambar gauge circular eco-score pada Canvas. */
    public static void drawEcoGauge(Canvas canvas, int score) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double cx = w / 2;
        double cy = h / 2;
        double r = Math.min(w, h) / 2 - 12;

        gc.clearRect(0, 0, w, h);

        // Background ring
        gc.setStroke(Color.web("#E8F5EE"));
        gc.setLineWidth(14);
        gc.strokeArc(cx - r, cy - r, r * 2, r * 2, -220, 260, javafx.scene.shape.ArcType.OPEN);

        // Score arc (dari -220 ke score proportion of 260)
        double arcLen = (score / 100.0) * 260;
        gc.setLineWidth(14);
        gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);

        // Gradient color: green for high score
        Color arcColor = score >= 90 ? Color.web("#0F8A5B")
                : score >= 70 ? Color.web("#F59E0B")
                : Color.web("#E53935");
        gc.setStroke(arcColor);
        gc.strokeArc(cx - r, cy - r, r * 2, r * 2, -220, arcLen, javafx.scene.shape.ArcType.OPEN);

        // Center dot
        gc.setFill(Color.web("#F0FAF5"));
        gc.fillOval(cx - r + 18, cy - r + 18, (r - 18) * 2, (r - 18) * 2);
    }

    private void loadRecentActivity() {
        if (lvRecentActivity == null) return;
        List<RecentActivityItem> recent = dashboardService.getRecentActivity();
        List<String> displayItems = new java.util.ArrayList<>();
        for (RecentActivityItem item : recent) {
            displayItems.add("  " + item.getTitle() + "  ·  " + item.getDetail()
                    + "    (" + item.getTime() + ")");
        }
        lvRecentActivity.setItems(FXCollections.observableArrayList(displayItems));
    }

    @FXML
    private void onViewAllActivity() {
        // Navigasi ke halaman hiker management via parent dashboard
        // (untuk saat ini tampilkan info)
    }
}
