package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.MainApp;
import com.trashcamp.frontend.model.OfficerSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Controller utama dashboard shell.
 * Bertanggung jawab atas navigasi sidebar dan swap konten halaman.
 */
public class DashboardController {

    @FXML private Button btnOverview;
    @FXML private Button btnHikers;
    @FXML private Button btnCheckIn;
    @FXML private Button btnCheckOut;
    @FXML private Button btnAnalytics;
    @FXML private Button btnSettings;
    @FXML private Button btnLogout;

    @FXML private Label lblPageTitle;
    @FXML private Label lblPageSub;
    @FXML private Label lblOfficerName;
    @FXML private Label lblTopbarAvatar;

    @FXML private StackPane contentArea;

    private OfficerSession officerSession;
    private Button currentActiveButton;

    public void setOfficerSession(OfficerSession session) {
        this.officerSession = session;
    }

    /** Dipanggil oleh MainApp setelah set session. */
    public void refresh() {
        updateTopbarInfo();
        // Default halaman: Overview
        navigateTo(btnOverview, "Overview", "Dashboard ringkasan keseluruhan", "overview_content.fxml");
    }

    private void updateTopbarInfo() {
        if (officerSession != null && lblOfficerName != null) {
            String name = officerSession.getOfficerName();
            lblOfficerName.setText(name);
            if (lblTopbarAvatar != null && !name.isBlank()) {
                lblTopbarAvatar.setText(name.substring(0, 1).toUpperCase());
            }
        }
    }

    @FXML
    private void onMenuClicked(javafx.event.ActionEvent event) {
        Button clicked = (Button) event.getSource();
        if (clicked == btnOverview) {
            navigateTo(clicked, "Overview", "Dashboard ringkasan keseluruhan", "overview_content.fxml");
        } else if (clicked == btnHikers) {
            navigateTo(clicked, "Manajemen Pendaki", "Kelola data pendakian", "hiker_management_content.fxml");
        } else if (clicked == btnCheckIn) {
            navigateTo(clicked, "Hiker Check-In", "Daftarkan kelompok baru", "check_in_content.fxml");
        } else if (clicked == btnCheckOut) {
            navigateTo(clicked, "Check-Out & Verifikasi", "Verifikasi sampah & hitung denda", "check_out_content.fxml");
        } else if (clicked == btnAnalytics) {
            navigateTo(clicked, "Sustainability Analytics", "Laporan kepatuhan lingkungan", "analytics_content.fxml");
        } else if (clicked == btnSettings) {
            navigateTo(clicked, "System Settings", "Konfigurasi sistem", "settings_content.fxml");
        }
    }

    @FXML
    private void onLogoutClicked() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Apakah Anda yakin ingin logout?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Konfirmasi Logout");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES) {
                try {
                    MainApp.getInstance().showLogin(null);
                } catch (IOException e) {
                    showError("Gagal kembali ke halaman login.");
                }
            }
        });
    }

    /**
     * Navigasi ke halaman tertentu dengan memuat sub-FXML ke contentArea.
     */
    private void navigateTo(Button activeBtn, String title, String sub, String fxmlFile) {
        // --- Update active button style ---
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("sidebar-button-active");
        }
        if (!activeBtn.getStyleClass().contains("sidebar-button-active")) {
            activeBtn.getStyleClass().add("sidebar-button-active");
        }
        currentActiveButton = activeBtn;

        // --- Update topbar ---
        if (lblPageTitle != null) lblPageTitle.setText(title);
        if (lblPageSub != null) lblPageSub.setText(sub);

        // --- Load content FXML ---
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/" + fxmlFile));
            Node content = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ContentController) {
                ContentController cc = (ContentController) controller;
                cc.setSession(officerSession);
                cc.initData();
            }

            contentArea.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Gagal memuat halaman: " + fxmlFile);
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
