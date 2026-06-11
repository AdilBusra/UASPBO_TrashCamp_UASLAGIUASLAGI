package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.model.HargaKonfigurasi;
import com.trashcamp.frontend.model.OfficerSession;
import com.trashcamp.frontend.service.DummySettingsService;
import com.trashcamp.frontend.service.SettingsService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller untuk halaman System Settings.
 */
public class SettingsController implements ContentController {

    // Profil
    @FXML private Label lblAvatarBig;
    @FXML private Label lblNamaBig;
    @FXML private Label lblStasiunBig;
    @FXML private TextField tfNamaPetugas;
    @FXML private TextField tfNip;
    @FXML private TextField tfStasiun;
    @FXML private TextField tfUsername;
    @FXML private Label lblProfilStatus;

    // Harga
    @FXML private TableView<HargaKonfigurasi> hargaTable;
    @FXML private TableColumn<HargaKonfigurasi, String>  colNamaItem;
    @FXML private TableColumn<HargaKonfigurasi, String>  colKategoriH;
    @FXML private TableColumn<HargaKonfigurasi, String>  colDeposit;
    @FXML private TableColumn<HargaKonfigurasi, String>  colDendaH;
    @FXML private TableColumn<HargaKonfigurasi, Boolean> colAktif;
    @FXML private Label lblHargaStatus;

    // Preferensi
    @FXML private TextField tfNamaStasiun;
    @FXML private Label lblStasiunStatus;

    private OfficerSession session;
    private final DummySettingsService settingsService = new DummySettingsService();
    private ObservableList<HargaKonfigurasi> hargaList;
    private final NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    @Override
    public void setSession(OfficerSession session) {
        this.session = session;
    }

    @Override
    public void initData() {
        loadProfilData();
        setupHargaTable();
        loadHargaData();
        loadPreferensiData();
    }

    private void loadProfilData() {
        String name = session != null ? session.getOfficerName() : "Petugas";
        String station = settingsService.getOfficerStation();
        String nip = settingsService.getOfficerNip();

        if (lblAvatarBig != null && !name.isBlank())
            lblAvatarBig.setText(name.substring(0, 1).toUpperCase());
        if (lblNamaBig != null) lblNamaBig.setText(name);
        if (lblStasiunBig != null) lblStasiunBig.setText(station);
        if (tfNamaPetugas != null) tfNamaPetugas.setText(name);
        if (tfNip != null) tfNip.setText(nip);
        if (tfStasiun != null) tfStasiun.setText(station);
        if (tfUsername != null) tfUsername.setText(name.toLowerCase().replace(" ", "_"));
    }

    @FXML
    private void onSaveProfil() {
        String nama = tfNamaPetugas != null ? tfNamaPetugas.getText().trim() : "";
        String stasiun = tfStasiun != null ? tfStasiun.getText().trim() : "";
        String nip = tfNip != null ? tfNip.getText().trim() : "";

        if (nama.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Nama petugas wajib diisi.");
            return;
        }

        boolean ok = settingsService.updateOfficerProfile(nama, stasiun, nip);
        if (ok && session != null) {
            session.setOfficerName(nama);
            if (lblNamaBig != null) lblNamaBig.setText(nama);
            if (lblStasiunBig != null) lblStasiunBig.setText(stasiun);
            if (lblAvatarBig != null && !nama.isBlank())
                lblAvatarBig.setText(nama.substring(0, 1).toUpperCase());
            showStatusLabel(lblProfilStatus, "✅ Profil berhasil disimpan!");
        }
    }

    @FXML
    private void onResetProfil() {
        loadProfilData();
        hideStatusLabel(lblProfilStatus);
    }

    private void setupHargaTable() {
        if (hargaTable == null) return;
        hargaTable.setEditable(true);

        colNamaItem.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaItem()));
        colKategoriH.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKategori()));

        colDeposit.setCellValueFactory(d ->
                new SimpleStringProperty("Rp " + nf.format((long) d.getValue().getDepositPerItem()))
        );
        colDendaH.setCellValueFactory(d ->
                new SimpleStringProperty("Rp " + nf.format((long) d.getValue().getDendaPerItem()))
        );

        colAktif.setCellValueFactory(d ->
                new SimpleBooleanProperty(d.getValue().isAktif())
        );
        colAktif.setCellFactory(CheckBoxTableCell.forTableColumn(colAktif));
    }

    private void loadHargaData() {
        List<HargaKonfigurasi> list = settingsService.getHargaKonfigurasi();
        hargaList = FXCollections.observableArrayList(list);
        if (hargaTable != null) hargaTable.setItems(hargaList);
    }

    @FXML
    private void onSaveHarga() {
        boolean ok = settingsService.saveHargaKonfigurasi(new ArrayList<>(hargaList));
        if (ok) showStatusLabel(lblHargaStatus, "✅ Konfigurasi harga disimpan!");
    }

    private void loadPreferensiData() {
        if (tfNamaStasiun != null) tfNamaStasiun.setText(settingsService.getNamaStasiun());
    }

    @FXML
    private void onSaveStasiun() {
        String nama = tfNamaStasiun != null ? tfNamaStasiun.getText().trim() : "";
        boolean ok = settingsService.updateNamaStasiun(nama);
        if (ok) showStatusLabel(lblStasiunStatus, "✅ Nama stasiun disimpan!");
    }

    private void showStatusLabel(Label lbl, String msg) {
        if (lbl != null) {
            lbl.setText(msg);
            lbl.setVisible(true);
            lbl.setManaged(true);
        }
    }

    private void hideStatusLabel(Label lbl) {
        if (lbl != null) { lbl.setVisible(false); lbl.setManaged(false); }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
