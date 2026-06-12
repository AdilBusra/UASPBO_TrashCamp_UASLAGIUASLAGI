package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.model.HargaKonfigurasi;
import com.trashcamp.frontend.model.OfficerSession;
import com.trashcamp.frontend.service.HttpSettingsService;
import com.trashcamp.frontend.service.SettingsService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;

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

    // Harga / Item Sampah
    @FXML private TableView<HargaKonfigurasi> hargaTable;
    @FXML private TableColumn<HargaKonfigurasi, String>  colNamaItem;
    @FXML private TableColumn<HargaKonfigurasi, String>  colKategoriH;
    @FXML private TableColumn<HargaKonfigurasi, String>  colDeposit;
    @FXML private TableColumn<HargaKonfigurasi, String>  colDendaH;
    @FXML private TableColumn<HargaKonfigurasi, Boolean> colAktif;
    @FXML private Label lblHargaStatus;

    // Tambah Item Baru
    @FXML private TextField tfAddNamaItem;
    @FXML private ComboBox<String> cbAddKategori;
    @FXML private TextField tfAddDeposit;
    @FXML private TextField tfAddDenda;

    // Preferensi Pos
    @FXML private TextField tfNamaStasiun;
    @FXML private Label lblStasiunStatus;

    // Tarif (Tiket & Kebersihan)
    @FXML private TextField tfHargaTiket;
    @FXML private TextField tfBiayaKebersihan;
    @FXML private Label lblTarifStatus;

    // Rute / Trails
    @FXML private ListView<String> lvTrails;
    @FXML private TextField tfNewTrail;
    @FXML private Label lblTrailStatus;

    private OfficerSession session;
    private final SettingsService settingsService = new HttpSettingsService();
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
        setupKategoriCombo();
        loadTarifData();
        loadTrailsData();
    }

    private void loadProfilData() {
        String name = session != null ? session.getOfficerName() : "Petugas";
        String station = session != null && session.getStasiun() != null ? session.getStasiun() : "Pos Ranu Kumbolo";
        String nip = session != null && session.getNip() != null ? session.getNip() : "NIP-2026-001";

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
        String name = tfNamaPetugas != null ? tfNamaPetugas.getText().trim() : "";
        String station = tfStasiun != null ? tfStasiun.getText().trim() : "";
        String nip = tfNip != null ? tfNip.getText().trim() : "";

        if (name.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Nama petugas wajib diisi.");
            return;
        }

        boolean ok = settingsService.updateOfficerProfile(name, station, nip);
        if (ok && session != null) {
            session.setOfficerName(name);
            session.setStasiun(station);
            session.setNip(nip);
            if (lblNamaBig != null) lblNamaBig.setText(name);
            if (lblStasiunBig != null) lblStasiunBig.setText(station);
            if (lblAvatarBig != null && !name.isBlank())
                lblAvatarBig.setText(name.substring(0, 1).toUpperCase());
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
        
        // Agar CheckBox Cell dapat langsung memperbarui data di list saat di-check/uncheck
        colAktif.setOnEditCommit(event -> {
            HargaKonfigurasi row = event.getRowValue();
            row.setAktif(event.getNewValue());
        });
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

    private void setupKategoriCombo() {
        if (cbAddKategori != null) {
            cbAddKategori.setItems(FXCollections.observableArrayList(
                    "Plastik", "Metal", "Organik", "Kaca", "B3", "Lainnya"
            ));
            cbAddKategori.setValue("Plastik");
        }
    }

    @FXML
    private void onAddNewTrashItem() {
        String nama = tfAddNamaItem != null ? tfAddNamaItem.getText().trim() : "";
        String kategori = cbAddKategori != null ? cbAddKategori.getValue() : "Lainnya";
        String depositStr = tfAddDeposit != null ? tfAddDeposit.getText().trim() : "0";
        String dendaStr = tfAddDenda != null ? tfAddDenda.getText().trim() : "0";

        if (nama.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Nama item tidak boleh kosong.");
            return;
        }

        try {
            double deposit = Double.parseDouble(depositStr);
            double denda = Double.parseDouble(dendaStr);
            if (deposit < 0 || denda < 0) throw new NumberFormatException();

            // Kita buat representasi baru
            HargaKonfigurasi newItem = new HargaKonfigurasi(
                    0, // ID 0 agar di-generate backend
                    nama,
                    kategori,
                    deposit,
                    denda,
                    true
            );

            // Tambahkan langsung ke tabel
            hargaList.add(newItem);
            
            // Simpan perubahan ke backend
            settingsService.saveHargaKonfigurasi(new ArrayList<>(hargaList));

            // Reset input
            if (tfAddNamaItem != null) tfAddNamaItem.clear();
            if (tfAddDeposit != null) tfAddDeposit.setText("0");
            if (tfAddDenda != null) tfAddDenda.setText("0");

            loadHargaData(); // Reload agar mendapat ID dari backend
            showStatusLabel(lblHargaStatus, "✅ Item sampah baru berhasil ditambahkan!");
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Deposit dan Denda harus berupa angka positif.");
        }
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

    // --- Biaya Tiket & Kebersihan ---
    private void loadTarifData() {
        if (tfHargaTiket != null) {
            tfHargaTiket.setText(String.format("%.0f", settingsService.getTicketPrice()));
        }
        if (tfBiayaKebersihan != null) {
            tfBiayaKebersihan.setText(String.format("%.0f", settingsService.getSanitationFee()));
        }
    }

    @FXML
    private void onSaveTarif() {
        String tiketStr = tfHargaTiket != null ? tfHargaTiket.getText().trim() : "0";
        String kebersihanStr = tfBiayaKebersihan != null ? tfBiayaKebersihan.getText().trim() : "0";

        try {
            double tiket = Double.parseDouble(tiketStr);
            double kebersihan = Double.parseDouble(kebersihanStr);
            if (tiket < 0 || kebersihan < 0) throw new NumberFormatException();

            settingsService.updateTicketPrice(tiket);
            settingsService.updateSanitationFee(kebersihan);
            showStatusLabel(lblTarifStatus, "✅ Tarif tiket & kebersihan disimpan!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Tarif harus berupa angka positif.");
        }
    }

    // --- Rute / Trails ---
    private void loadTrailsData() {
        if (lvTrails == null) return;
        List<String> list = settingsService.getTrails();
        lvTrails.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    private void onAddTrail() {
        String newTrail = tfNewTrail != null ? tfNewTrail.getText().trim() : "";
        if (newTrail.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Nama jalur baru tidak boleh kosong.");
            return;
        }

        boolean ok = settingsService.addTrail(newTrail);
        if (ok) {
            if (tfNewTrail != null) tfNewTrail.clear();
            loadTrailsData();
            showStatusLabel(lblTrailStatus, "✅ Jalur baru berhasil ditambahkan!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal menambahkan jalur baru.");
        }
    }

    @FXML
    private void onDeleteTrail() {
        if (lvTrails == null) return;
        String selected = lvTrails.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih jalur yang ingin dihapus terlebih dahulu.");
            return;
        }

        boolean ok = settingsService.deleteTrail(selected);
        if (ok) {
            loadTrailsData();
            showStatusLabel(lblTrailStatus, "✅ Jalur berhasil dihapus!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal menghapus jalur.");
        }
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
