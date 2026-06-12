package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.model.DetailSampah;
import com.trashcamp.frontend.model.MasterSampah;
import com.trashcamp.frontend.model.OfficerSession;
import com.trashcamp.frontend.model.Pendakian;
import com.trashcamp.frontend.service.CheckInService;
import com.trashcamp.frontend.service.HttpCheckInService;
import com.trashcamp.frontend.service.HttpSettingsService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller untuk halaman Hiker Check-In.
 * Menangani form data kelompok + logistik sampah + kalkulasi deposit & biaya masuk.
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

    // --- Summary Labels ---
    @FXML private Label lblTotalDeposit;      // Kita gunakan ini untuk Total Pembayaran Awal
    @FXML private Label lblDepositDetail;     // Dan rincian breakdown detailnya

    private OfficerSession session;
    private final CheckInService checkInService = new HttpCheckInService();
    private final HttpSettingsService settingsService = new HttpSettingsService();
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

        // Pemicu kalkulasi ulang saat jumlah anggota berganti
        if (spJumlahAnggota != null) {
            spJumlahAnggota.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        }
    }

    private void setupTrailCombo() {
        if (cbTrail != null) {
            // Mengambil rute pendakian dinamis dari settings service (backend)
            List<String> trails = settingsService.getTrails();
            if (trails.isEmpty()) {
                trails = List.of("Ranu Kumbolo", "Mahameru Summit", "Oro-oro Ombo");
            }
            cbTrail.setItems(FXCollections.observableArrayList(trails));
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
        List<MasterSampah> masterList = checkInService.getMasterSampahList();
        if (masterList.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Daftar master sampah kosong. Konfigurasikan item di halaman Settings.");
            return;
        }

        // Dialog kustom terpadu (Kombinasi Dropdown Pilihan Sampah & Qty dalam 1 Window)
        Dialog<Pair<MasterSampah, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Tambah Item Sampah");
        dialog.setHeaderText("Pilih jenis sampah dan masukkan jumlah bawaan:");

        ButtonType btnOkType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnOkType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<MasterSampah> cbItems = new ComboBox<>(FXCollections.observableArrayList(masterList));
        cbItems.setValue(masterList.get(0));
        cbItems.setMaxWidth(Double.MAX_VALUE);

        TextField tfQty = new TextField("1");
        tfQty.setPromptText("Jumlah item");

        grid.add(new Label("Jenis Sampah:"), 0, 0);
        grid.add(cbItems, 1, 0);
        grid.add(new Label("Jumlah Bawa:"), 0, 1);
        grid.add(tfQty, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnOkType) {
                try {
                    int qty = Integer.parseInt(tfQty.getText().trim());
                    if (qty <= 0) throw new NumberFormatException();
                    return new Pair<>(cbItems.getValue(), qty);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Masukkan angka jumlah yang valid (positif).");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(pair -> {
            MasterSampah selectedItem = pair.getKey();
            int qty = pair.getValue();

            // Cek apakah item sudah ada
            boolean exists = itemList.stream()
                    .anyMatch(d -> d.getMasterSampah().getId() == selectedItem.getId());
            if (exists) {
                showAlert(Alert.AlertType.WARNING,
                        "Item '" + selectedItem.getNamaItem() + "' sudah ada di daftar.");
                return;
            }

            itemList.add(new DetailSampah(selectedItem, qty, 0));
            updateSummary();
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
        double depositSampah = checkInService.hitungTotalDeposit(new ArrayList<>(itemList));
        
        // Perhitungan biaya tiket & kebersihan (Poin 7)
        double hargaTiket = settingsService.getTicketPrice();
        double biayaKebersihan = settingsService.getSanitationFee();
        double totalTiket = hargaTiket * anggota;
        double totalBayar = depositSampah + totalTiket + biayaKebersihan;

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
        p.setTotalDeposit(depositSampah); // Simpan hanya deposit yang bisa direfund ke database

        boolean ok = checkInService.checkIn(p, new ArrayList<>(itemList));
        if (ok) {
            Alert success = new Alert(Alert.AlertType.INFORMATION,
                    String.format("✅ Check-In berhasil!%n%n" +
                                    "Kelompok : %s (%d orang)%n" +
                                    "Trail    : %s%n%n" +
                                    "Rincian Pembayaran Awal:%n" +
                                    "• Deposit Sampah    : Rp %s%n" +
                                    "• Tiket (%d org)     : Rp %s%n" +
                                    "• Layanan Kebersihan: Rp %s%n" +
                                    "-----------------------------%n" +
                                    "Total Bayar di Pos   : Rp %s",
                            p.getNamaKetua(), p.getJumlahAnggota(), p.getTrail(),
                            nf.format((long) depositSampah),
                            anggota, nf.format((long) totalTiket),
                            nf.format((long) biayaKebersihan),
                            nf.format((long) totalBayar)));
            success.setTitle("Check-In Berhasil");
            success.setHeaderText(null);
            success.showAndWait();
            onReset();
        } else {
            showAlert(Alert.AlertType.ERROR, "Terjadi kesalahan saat menyimpan data check-in ke server backend.");
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
        setupTrailCombo(); // reload list
        updateSummary();
    }

    private void updateSummary() {
        double deposit = checkInService.hitungTotalDeposit(new ArrayList<>(itemList));
        int anggota = spJumlahAnggota != null ? spJumlahAnggota.getValue() : 1;
        
        double hargaTiket = settingsService.getTicketPrice();
        double biayaKebersihan = settingsService.getSanitationFee();
        
        double totalTiket = hargaTiket * anggota;
        double totalBayar = deposit + totalTiket + biayaKebersihan;

        if (lblTotalDeposit != null)
            lblTotalDeposit.setText("Rp " + nf.format((long) totalBayar));
            
        if (lblDepositDetail != null) {
            String detailStr = String.format("%d sampah · Deposit: Rp %s · Tiket (%d org): Rp %s · Kebersihan: Rp %s",
                    itemList.size(),
                    nf.format((long) deposit),
                    anggota,
                    nf.format((long) totalTiket),
                    nf.format((long) biayaKebersihan));
            lblDepositDetail.setText(detailStr);
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
