package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.MainApp;
import com.trashcamp.frontend.model.OfficerSession;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Controller untuk halaman Login.
 * Validasi input frontend-only; auth sebenarnya dilakukan backend.
 */
public class LoginController {

    @FXML private TextField tfUsername;
    @FXML private PasswordField pfPassword;
    @FXML private Label lblError;

    private OfficerSession session;

    public void setSession(OfficerSession session) {
        this.session = session;
    }

    @FXML
    private void onLoginClicked() {
        String username = tfUsername != null ? tfUsername.getText().trim() : "";
        String password = pfPassword != null ? pfPassword.getText().trim() : "";

        // Validasi: field tidak boleh kosong
        if (username.isBlank() || password.isBlank()) {
            showError("Username dan Password wajib diisi.");
            shakeError();
            return;
        }

        // Validasi panjang minimal
        if (username.length() < 3) {
            showError("Username minimal 3 karakter.");
            shakeError();
            return;
        }

        // Frontend-only: login dianggap berhasil
        hideError();
        OfficerSession created = new OfficerSession(username);
        try {
            MainApp.getInstance().showDashboard(created);
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Gagal membuka Dashboard. Coba lagi.");
            a.setHeaderText(null);
            a.showAndWait();
        }
    }

    private void showError(String msg) {
        if (lblError != null) {
            lblError.setText(msg);
            lblError.setVisible(true);
            lblError.setManaged(true);
        }
    }

    private void hideError() {
        if (lblError != null) {
            lblError.setText("");
            lblError.setVisible(false);
            lblError.setManaged(false);
        }
    }

    /** Animasi shake pada field error */
    private void shakeError() {
        if (tfUsername == null) return;
        double originX = tfUsername.getLayoutX();
        Timeline shake = new Timeline(
                new KeyFrame(Duration.millis(0),   e -> tfUsername.setTranslateX(0)),
                new KeyFrame(Duration.millis(50),  e -> tfUsername.setTranslateX(-8)),
                new KeyFrame(Duration.millis(100), e -> tfUsername.setTranslateX(8)),
                new KeyFrame(Duration.millis(150), e -> tfUsername.setTranslateX(-6)),
                new KeyFrame(Duration.millis(200), e -> tfUsername.setTranslateX(6)),
                new KeyFrame(Duration.millis(250), e -> tfUsername.setTranslateX(0))
        );
        shake.play();
    }
}
