package com.trashcamp.frontend.controller;

import com.google.gson.JsonObject;
import com.trashcamp.frontend.MainApp;
import com.trashcamp.frontend.model.OfficerSession;
import com.trashcamp.frontend.service.HttpService;
import com.trashcamp.frontend.service.HttpSettingsService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller untuk halaman Login.
 * Terkoneksi dengan REST API backend.
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

        if (password.length() < 8) {
            showError("Password minimal harus 8 karakter.");
            shakeError();
            return;
        }

        try {
            // Melakukan request login ke backend
            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", username);
            credentials.put("password", password);

            String jsonResponse = HttpService.post("/api/auth/login", credentials);
            JsonObject obj = HttpService.getGson().fromJson(jsonResponse, JsonObject.class);

            // Buat session
            String namaLengkap = obj.get("namaLengkap").getAsString();
            String nip = obj.has("nip") && !obj.get("nip").isJsonNull() ? obj.get("nip").getAsString() : "";
            String stasiun = obj.has("stasiun") && !obj.get("stasiun").isJsonNull() ? obj.get("stasiun").getAsString() : "";

            OfficerSession loggedSession = new OfficerSession(namaLengkap, nip, stasiun, false);
            HttpSettingsService.setCachedProfile(loggedSession);

            hideError();
            MainApp.getInstance().showDashboard(loggedSession);
        } catch (Exception e) {
            e.printStackTrace();
            String errMsg = e.getMessage();
            if (errMsg == null || errMsg.isBlank() || errMsg.contains("Connection refused")) {
                errMsg = "Tidak dapat terhubung ke server backend (offline).";
            }
            if (errMsg.contains("Akun belum ditemukan") || errMsg.contains("silakan registrasi")) {
                showError(errMsg);
            } else {
                showError("Gagal Masuk: " + errMsg);
            }
            shakeError();
        }
    }

    @FXML
    private void onRegisterClicked() {
        // Pop-up dialog register kustom (Poin 4, 10)
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Daftar Pos Baru");
        dialog.setHeaderText("Masukkan detail akun pos baru Anda:");

        ButtonType registerButtonType = new ButtonType("Daftar Akun", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField tfUser = new TextField();
        tfUser.setPromptText("Username");
        
        PasswordField pfPass = new PasswordField();
        pfPass.setPromptText("Password (min 8 karakter)");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(tfUser, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(pfPass, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                return new Pair<>(tfUser.getText().trim(), pfPass.getText().trim());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(pair -> {
            String user = pair.getKey();
            String pass = pair.getValue();

            if (user.length() < 3 || pass.length() < 8) {
                showAlert(Alert.AlertType.ERROR, "Registrasi Gagal: Username minimal 3 karakter & Password minimal 8 karakter.");
                return;
            }

            try {
                Map<String, String> payload = new HashMap<>();
                payload.put("username", user);
                payload.put("password", pass);

                String jsonResponse = HttpService.post("/api/auth/register", payload);
                JsonObject obj = HttpService.getGson().fromJson(jsonResponse, JsonObject.class);

                // Auto-login setelah register sukses dengan penanda isNewRegistration = true (Poin 4)
                String namaLengkap = obj.get("namaLengkap").getAsString();
                String nip = obj.has("nip") && !obj.get("nip").isJsonNull() ? obj.get("nip").getAsString() : "";
                String stasiun = obj.has("stasiun") && !obj.get("stasiun").isJsonNull() ? obj.get("stasiun").getAsString() : "";

                OfficerSession loggedSession = new OfficerSession(namaLengkap, nip, stasiun, true);
                HttpSettingsService.setCachedProfile(loggedSession);

                showAlert(Alert.AlertType.INFORMATION, "✅ Registrasi Berhasil!\nSelamat datang, " + namaLengkap + ". Anda akan diarahkan ke halaman pengaturan sistem untuk konfigurasi pos.");
                
                MainApp.getInstance().showDashboard(loggedSession);
            } catch (Exception e) {
                e.printStackTrace();
                String errMsg = e.getMessage();
                if (errMsg == null || errMsg.isBlank() || errMsg.contains("Connection refused")) {
                    errMsg = "Tidak dapat terhubung ke server backend (offline).";
                }
                showAlert(Alert.AlertType.ERROR, "Registrasi Gagal: " + errMsg);
            }
        });
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

    private void shakeError() {
        if (tfUsername == null) return;
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

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
