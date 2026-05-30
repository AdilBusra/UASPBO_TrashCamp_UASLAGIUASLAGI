package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.controller.OverviewController;
import com.trashcamp.frontend.model.OfficerSession;
import com.trashcamp.frontend.service.AnalyticsService;
import com.trashcamp.frontend.service.DummyAnalyticsService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Controller untuk halaman Sustainability Analytics.
 */
public class AnalyticsController implements ContentController {

    @FXML private Label lblTotalWaste;
    @FXML private Label lblAvgWaste;
    @FXML private Label lblCompliance;
    @FXML private Label lblTotalDenda;

    @FXML private Canvas ecoCanvas;
    @FXML private Label lblEcoScore;

    @FXML private ProgressBar pbPlastik;
    @FXML private ProgressBar pbMetal;
    @FXML private ProgressBar pbOrganik;
    @FXML private ProgressBar pbB3;
    @FXML private Label lblPbPlastik;
    @FXML private Label lblPbMetal;
    @FXML private Label lblPbOrganik;
    @FXML private Label lblPbB3;

    @FXML private BarChart<String, Number> categoryChart;

    @FXML private TableView<String[]> summaryTable;
    @FXML private TableColumn<String[], String> colKategori;
    @FXML private TableColumn<String[], String> colBeratKg;
    @FXML private TableColumn<String[], String> colProporsi;
    @FXML private TableColumn<String[], String> colTrendLabel;

    private OfficerSession session;
    private final AnalyticsService analyticsService = new DummyAnalyticsService();
    private final NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    @Override
    public void setSession(OfficerSession session) {
        this.session = session;
    }

    @Override
    public void initData() {
        loadStatCards();
        loadEcoScore();
        loadProgressBars();
        loadBarChart();
        loadSummaryTable();
    }

    private void loadStatCards() {
        if (lblTotalWaste != null)
            lblTotalWaste.setText(String.format("%.1f kg", analyticsService.getTotalWasteKg()));
        if (lblAvgWaste != null)
            lblAvgWaste.setText(String.format("%.2f kg", analyticsService.getAvgWastePerHiker()));
        if (lblCompliance != null)
            lblCompliance.setText(String.format("%.1f%%", analyticsService.getComplianceRate()));
        if (lblTotalDenda != null) {
            double denda = analyticsService.getTotalDendaTerkumpul();
            lblTotalDenda.setText(String.format("Rp %.2f Jt", denda / 1_000_000));
        }
    }

    private void loadEcoScore() {
        int score = analyticsService.getEcoScore();
        if (lblEcoScore != null) lblEcoScore.setText(String.valueOf(score));
        if (ecoCanvas != null) {
            OverviewController.drawEcoGauge(ecoCanvas, score);
        }
    }

    private void loadProgressBars() {
        Map<String, Double> cats = analyticsService.getWasteByCategory();
        double total = cats.values().stream().mapToDouble(Double::doubleValue).sum();

        setBar(pbPlastik, lblPbPlastik, cats.getOrDefault("Plastik", 0.0), total);
        setBar(pbMetal,   lblPbMetal,   cats.getOrDefault("Metal",   0.0), total);
        setBar(pbOrganik, lblPbOrganik, cats.getOrDefault("Organik", 0.0), total);
        setBar(pbB3,      lblPbB3,      cats.getOrDefault("B3",      0.0), total);
    }

    private void setBar(ProgressBar pb, Label lbl, double val, double total) {
        if (pb == null || lbl == null) return;
        double pct = total > 0 ? val / total : 0;
        pb.setProgress(pct);
        lbl.setText(String.format("%.0f%%", pct * 100));
    }

    private void loadBarChart() {
        if (categoryChart == null) return;
        categoryChart.getData().clear();

        Map<String, Double> cats = analyticsService.getWasteByCategory();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Berat (kg)");
        for (Map.Entry<String, Double> entry : cats.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        categoryChart.getData().add(series);
    }

    private void loadSummaryTable() {
        if (summaryTable == null) return;

        colKategori.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        colBeratKg.setCellValueFactory(d ->  new SimpleStringProperty(d.getValue()[1]));
        colProporsi.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        colTrendLabel.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));

        Map<String, Double> cats = analyticsService.getWasteByCategory();
        double total = cats.values().stream().mapToDouble(Double::doubleValue).sum();

        ObservableList<String[]> rows = FXCollections.observableArrayList();
        String[] trends = {"↑ Naik", "→ Stabil", "↓ Turun", "→ Stabil", "↑ Naik"};
        int i = 0;
        for (Map.Entry<String, Double> entry : cats.entrySet()) {
            double pct = total > 0 ? (entry.getValue() / total) * 100 : 0;
            rows.add(new String[]{
                    entry.getKey(),
                    String.format("%.1f kg", entry.getValue()),
                    String.format("%.1f%%", pct),
                    trends[i % trends.length]
            });
            i++;
        }
        summaryTable.setItems(rows);
    }
}
