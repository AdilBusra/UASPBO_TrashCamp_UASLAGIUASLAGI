package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.model.DetailSampah;
import com.trashcamp.frontend.model.MasterSampah;
import com.trashcamp.frontend.model.OfficerSession;
import com.trashcamp.frontend.model.Pendakian;
import com.trashcamp.frontend.service.CheckInService;
import com.trashcamp.frontend.service.DummyCheckInService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller untuk halaman Hiker Check-In.
 * Menangani form data kelompok + logistik sampah + kalkulasi deposit.
 */
public class CheckInController implements ContentController {

    // --- Form Data Kelompok ---
    @FXML private TextField tfNamaKetua;
    @FXML private TextField tfNoHp;
    @FXML private Spinner<Integer> spJumlahAnggota;
    @FXML private ComboBox<String> cbTrail;
    @FXML private DatePicker dpTanggalNaik;
    @FXML private DatePicker dpEstimasiTurun;
    @FXML private TextArea taCatatan;

    // --- Logistik Sampah ---
    @FXML private TableView<DetailSampah> itemTable;
    @FXML private TableColumn<DetailSampah, String>  colItem;
    @FXML private TableColumn<DetailSampah, String>  colKategori;
    @FXML private TableColumn<DetailSampah, Number>  colQty;
    @FXML private TableColumn<DetailSampah, String>  colDeposit;
    @FXML private TableColumn<DetailSampah, String>  colSubtotal;
    @FXML private TableColumn<DetailSampah, Void>    colHapus;

    @FXML private Label lblTotalDeposit;
    @FXML private Label lblDepositDetail;

    private OfficerSession session;
    private final CheckInService checkInService = new DummyCheckInService();
    private ObservableList<DetailSampah> itemList;
    private final NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    @Override
    public void setSession(OfficerSession session) {
        this.session = session;
    }

    @Override
    public void initData() {
        itemList = FXCollections.observableArrayList();
        setupTrailCombo();
        setupTable();
        setDefaultDate();
        updateSummary();
    }

    private void setupTrailCombo() {
        if (cbTrail != null) {
            cbTrail.setItems(FXCollections.observableArrayList(
                    "Ranu Kumbolo", "Mahameru Summit", "Oro-oro Ombo",
                    "Kalimati", "Arcopodo", "Ranupani Base Camp"
            ));
        }
    }

    private void setDefaultDate() {
        if (dpTanggalNaik != null) dpTanggalNaik.setValue(LocalDate.now());
        if (dpEstimasiTurun != null) dpEstimasiTurun.setValue(LocalDate.now().plusDays(2));
    }

    private void setupTable() {
        if (itemTable == null) return;

        colItem.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaItem()));
        colKategori.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKategori()));

        colQty.setCellValueFactory(d ->
                new javafx.beans.property.SimpleIntegerProperty(d.getValue().getJumlahNaik())
        );

        colDeposit.setCellValueFactory(d ->
                new SimpleStringProperty("Rp " + nf.format((long) d.getValue().getDepositPerItem()))
        );

        colSubtotal.setCellValueFactory(d ->
                new SimpleStringProperty("Rp " + nf.format((long) d.getValue().getTotalDeposit()))
        );

        // Tombol Hapus
        colHapus.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("✕");
            {
                btn.getStyleClass().add("danger-button");
                btn.setStyle("-fx-padding: 4 8 4 8; -fx-font-size:11px;");
                btn.setOnAction(e -> {
                    DetailSampah item = getTableView().getItems().get(getIndex());
                    itemList.remove(item);
                    updateSummary();
                });
            }
            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });

        itemTable.setItems(itemList);
    }

    @FXML
    private void onAddItem() {
        // Dialog pilih item sampah
        List<MasterSampah> masterList = checkInService.getMasterSampahList();
        ChoiceDialog<MasterSampah> dialog = new ChoiceDialog<>(masterList.get(0), masterList);
        dialog.setTitle("Tambah Item Sampah");
        dialog.setHeaderText("Pilih jenis sampah yang dibawa:");
        dialog.setContentText("Item:");

        dialog.showAndWait().ifPresent(selectedItem -> {
            // Cek apakah item sudah ada
            boolean exists = itemList.stream()
                    .anyMatch(d -> d.getMasterSampah().getId() == selectedItem.getId());
            if (exists) {
                showAlert(Alert.AlertType.WARNING,
                        "Item '" + selectedItem.getNamaItem() + "' sudah ada di daftar.");
                return;
            }

            // Dialog jumlah qty
            TextInputDialog qtyDialog = new TextInputDialog("1");
            qtyDialog.setTitle("Jumlah Item");
            qtyDialog.setHeaderText("Berapa unit " + selectedItem.getNamaItem() + " yang dibawa?");
            qtyDialog.setContentText("Jumlah:");
            qtyDialog.showAndWait().ifPresent(qtyStr -> {
                try {
                    int qty = Integer.parseInt(qtyStr.trim());
                    if (qty <= 0) throw new NumberFormatException();
                    itemList.add(new DetailSampah(selectedItem, qty, 0));
                    updateSummary();
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Jumlah tidak valid. Masukkan angka positif.");
                }
            });
        });
    }

    @FXML
    private void onCheckIn() {
        // Validasi
        if (tfNamaKetua == null || tfNamaKetua.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Nama Ketua Kelompok wajib diisi.");
            return;
        }
        if (tfNoHp == null || tfNoHp.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Nomor HP wajib diisi.");
            return;
        }
        if (cbTrail == null || cbTrail.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Trail pendakian wajib dipilih.");
            return;
        }
        if (itemList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Minimal satu item sampah harus dicatat.");
            return;
        }

        // Buat objek Pendakian
        int anggota = spJumlahAnggota != null ? spJumlahAnggota.getValue() : 1;
        double totalDeposit = checkInService.hitungTotalDeposit(new ArrayList<>(itemList));
        String tanggal = dpTanggalNaik != null && dpTanggalNaik.getValue() != null
                ? dpTanggalNaik.getValue().toString() : LocalDate.now().toString();

        Pendakian p = new Pendakian();
        p.setNamaKetua(tfNamaKetua.getText().trim());
        p.setNoHp(tfNoHp.getText().trim());
        p.setJumlahAnggota(anggota);
        p.setTrail(cbTrail.getValue());
        p.setStatus("AKTIF");
        p.setWaktuNaik(tanggal + " " + java.time.LocalTime.now().withSecond(0).withNano(0));
        p.setWaktuTurun("-");
        p.setTotalDeposit(totalDeposit);

        boolean ok = checkInService.checkIn(p, new ArrayList<>(itemList));
        if (ok) {
            Alert success = new Alert(Alert.AlertType.INFORMATION,
                    String.format("✅ Check-In berhasil!%n%nKelompok: %s%nAnggota : %d orang%nTrail   : %s%nDeposit : Rp %s",
                            p.getNamaKetua(), p.getJumlahAnggota(), p.getTrail(),
                            nf.format((long) totalDeposit)));
            success.setTitle("Check-In Berhasil");
            success.setHeaderText(null);
            success.showAndWait();
            onReset();
        }
    }

    @FXML
    private void onReset() {
        if (tfNamaKetua != null) tfNamaKetua.clear();
        if (tfNoHp != null) tfNoHp.clear();
        if (cbTrail != null) cbTrail.setValue(null);
        if (taCatatan != null) taCatatan.clear();
        if (dpTanggalNaik != null) dpTanggalNaik.setValue(LocalDate.now());
        if (dpEstimasiTurun != null) dpEstimasiTurun.setValue(LocalDate.now().plusDays(2));
        if (spJumlahAnggota != null) spJumlahAnggota.getValueFactory().setValue(2);
        itemList.clear();
        updateSummary();
    }

    private void updateSummary() {
        double total = checkInService.hitungTotalDeposit(new ArrayList<>(itemList));
        if (lblTotalDeposit != null)
            lblTotalDeposit.setText("Rp " + nf.format((long) total));
        if (lblDepositDetail != null)
            lblDepositDetail.setText(itemList.size() + " item · Total deposit yang harus dibayar pendaki");
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
