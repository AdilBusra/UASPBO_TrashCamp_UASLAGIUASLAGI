package com.trashcamp.frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SuksesCheckInController {

    @FXML private Label lblKelompok;
    @FXML private Label lblTrail;
    @FXML private Label lblDeposit;
    @FXML private Label lblTiketTitle;
    @FXML private Label lblTiket;
    @FXML private Label lblKebersihan;
    @FXML private Label lblTotal;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSummaryData(String nama, int anggota, String trail, String depositStr,
                               String tiketStr, String kebersihanStr, String totalStr) {
        lblKelompok.setText(nama + " (" + anggota + " orang)");
        lblTrail.setText(trail);
        lblDeposit.setText(depositStr);
        lblTiketTitle.setText("• Tiket (" + anggota + " org)");
        lblTiket.setText(tiketStr);
        lblKebersihan.setText(kebersihanStr);
        lblTotal.setText(totalStr);
    }

    @FXML
    private void onClose() {
        dialogStage.close();
    }
}