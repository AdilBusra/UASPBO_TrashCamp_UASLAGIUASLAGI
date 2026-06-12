package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.model.DetailSampah;
import com.trashcamp.frontend.model.OfficerSession;
import com.trashcamp.frontend.model.Pendakian;
import com.trashcamp.frontend.service.CheckOutService;
import com.trashcamp.frontend.service.HttpCheckOutService;
import com.trashcamp.frontend.service.HttpHikerService;
import com.trashcamp.frontend.service.HikerService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller untuk halaman Check-Out & Verifikasi Sampah.
 */
public class CheckOutController implements ContentController {

    @FXML private TextField tfSearchActive;
    @FXML private TableView<Pendakian> tblActiveGroups;
    @FXML private TableColumn<Pendakian, Number> colActiveId;
    @FXML private TableColumn<Pendakian, String> colActiveKetua;
    @FXML private TableColumn<Pendakian, Number> colActiveAnggota;
    @FXML private TableColumn<Pendakian, String> colActiveTrail;
    @FXML private TableColumn<Pendakian, String> colActiveDeposit;
    @FXML private TableColumn<Pendakian, String> colActiveWaktuNaik;

    @FXML private Label lblKetua;
    @FXML private Label lblAnggota;
    @FXML private Label lblTrail;
    @FXML private Label lblWaktuNaik;
    @FXML private Label lblDepositAwal;
    @FXML private javafx.scene.layout.HBox infoBox; // FIXED: Changed VBox to HBox to match FXML class type
    @FXML private javafx.scene.layout.VBox verifikasiBox;

    @FXML private TableView<DetailSampah> verifikasiTable;
    @FXML private TableColumn<DetailSampah, String>  colNamaItem;
    @FXML private TableColumn<DetailSampah, String>  colKategori;
    @FXML private TableColumn<DetailSampah, Number>  colQtyNaik;
    @FXML private TableColumn<DetailSampah, Integer> colQtyTurun;
    @FXML private TableColumn<DetailSampah, Number>  colHilang;
    @FXML private TableColumn<DetailSampah, String>  colDenda;

    @FXML private Label lblSummaryDeposit;
    @FXML private Label lblTotalDenda;
    @FXML private Label lblKembalian;
    @FXML private Label lblPembayaranTambahan;

    private ObservableList<Pendakian> allActiveGroups;
    private javafx.collections.transformation.FilteredList<Pendakian> filteredActiveGroups;

    private OfficerSession session;
    private final HikerService hikerService = new HttpHikerService();
    private final CheckOutService checkOutService = new HttpCheckOutService();
    private ObservableList<DetailSampah> verifikasiData;
    private Pendakian selectedPendakian;
    private final NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    @Override
    public void setSession(OfficerSession session) {
        this.session = session;
    }

    @Override
    public void initData() {
        verifikasiData = FXCollections.observableArrayList();
        setupActiveGroupsTable();
        loadAktifPendakian();
        setupTable();
    }

    private void setupActiveGroupsTable() {
        if (tblActiveGroups == null) return;

        colActiveId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()));
        colActiveKetua.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaKetua()));
        colActiveAnggota.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getJumlahAnggota()));
        colActiveTrail.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTrail()));
        colActiveDeposit.setCellValueFactory(d -> new SimpleStringProperty("Rp " + nf.format((long) d.getValue().getTotalDeposit())));
        colActiveWaktuNaik.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getWaktuNaik()));

        tblActiveGroups.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedPendakian = newVal;
                updateInfoBox(selectedPendakian);
                loadVerifikasiData(selectedPendakian.getId());
            }
        });
    }

    private void loadAktifPendakian() {
        if (tblActiveGroups == null) return;
        List<Pendakian> aktif = hikerService.getAktifPendakian();
        allActiveGroups = FXCollections.observableArrayList(aktif);
        filteredActiveGroups = new javafx.collections.transformation.FilteredList<>(allActiveGroups, p -> true);

        if (tfSearchActive != null) {
            tfSearchActive.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredActiveGroups.setPredicate(group -> {
                    if (newValue == null || newValue.isBlank()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (group.getNamaKetua() != null && group.getNamaKetua().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (group.getTrail() != null && group.getTrail().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
        }
        tblActiveGroups.setItems(filteredActiveGroups);
    }

    private void updateInfoBox(Pendakian p) {
        if (infoBox == null) return;
        infoBox.setVisible(true);
        infoBox.setManaged(true);
        if (lblKetua != null) lblKetua.setText(p.getNamaKetua());
        if (lblAnggota != null) lblAnggota.setText(p.getJumlahAnggota() + " orang");
        if (lblTrail != null) lblTrail.setText(p.getTrail());
        if (lblWaktuNaik != null) lblWaktuNaik.setText(p.getWaktuNaik());
        if (lblDepositAwal != null)
            lblDepositAwal.setText("Rp " + nf.format((long) p.getTotalDeposit()));
    }

    private void loadVerifikasiData(int pendakianId) {
        List<DetailSampah> data = checkOutService.getDetailSampahByPendakianId(pendakianId);
        verifikasiData = FXCollections.observableArrayList(data);
        verifikasiTable.setItems(verifikasiData);
        verifikasiTable.setEditable(true);

        if (verifikasiBox != null) {
            verifikasiBox.setVisible(true);
            verifikasiBox.setManaged(true);
        }
        updateSummary();
    }

    private void setupTable() {
        if (verifikasiTable == null) return;
        verifikasiTable.setEditable(true);

        colNamaItem.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaItem()));
        colKategori.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKategori()));
        colQtyNaik.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getJumlahNaik()));

        // Editable qty turun column
        colQtyTurun.setCellValueFactory(d ->
                new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getJumlahTurun())
        );
        colQtyTurun.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colQtyTurun.setEditable(true);
        colQtyTurun.setOnEditCommit(event -> {
            DetailSampah item = event.getRowValue();
            int newVal = event.getNewValue() != null ? event.getNewValue() : 0;
            // Validasi: tidak boleh melebihi qty naik
            newVal = Math.max(0, Math.min(newVal, item.getJumlahNaik()));
            item.setJumlahTurun(newVal);
            verifikasiTable.refresh();
            updateSummary();
        });

        colHilang.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getJumlahHilang()));
        colHilang.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setText(null); setStyle(""); return; }
                setText(String.valueOf(val.intValue()));
                setStyle(val.intValue() > 0
                        ? "-fx-text-fill: #B91C1C; -fx-font-weight: bold;"
                        : "-fx-text-fill: #0F8A5B;");
            }
        });

        colDenda.setCellValueFactory(d ->
                new SimpleStringProperty("Rp " + nf.format((long) d.getValue().getTotalDenda()))
        );
        colDenda.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setText(null); setStyle(""); return; }
                setText(val);
                setStyle(val.equals("Rp 0")
                        ? "-fx-text-fill: #0F8A5B; -fx-font-weight: bold;"
                        : "-fx-text-fill: #B91C1C; -fx-font-weight: bold;");
            }
        });
    }

    @FXML
    private void onHitungDenda() {
        verifikasiTable.refresh();
        updateSummary();
    }

    private void updateSummary() {
        if (verifikasiData == null) return;
        List<DetailSampah> list = new ArrayList<>(verifikasiData);
        double deposit = selectedPendakian != null ? selectedPendakian.getTotalDeposit() : 0;
        double denda = checkOutService.hitungTotalDenda(list);
        double kembalian = deposit - denda;
        double tambahan = denda - deposit;

        if (lblSummaryDeposit != null)
            lblSummaryDeposit.setText("Rp " + nf.format((long) deposit));
        if (lblTotalDenda != null)
            lblTotalDenda.setText("Rp " + nf.format((long) denda));
        if (lblKembalian != null)
            lblKembalian.setText("Rp " + nf.format((long) Math.max(0, kembalian)));
        if (lblPembayaranTambahan != null)
            lblPembayaranTambahan.setText("Rp " + nf.format((long) Math.max(0, tambahan)));
    }

    @FXML
    private void onCheckOut() {
        if (selectedPendakian == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih kelompok pendakian terlebih dahulu.");
            return;
        }
        List<DetailSampah> list = new ArrayList<>(verifikasiData);
        double denda = checkOutService.hitungTotalDenda(list);
        double deposit = selectedPendakian.getTotalDeposit();
        double kembalian = Math.max(0, deposit - denda);
        double tambahan = Math.max(0, denda - deposit);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                String.format("Konfirmasi Check-Out:%n%nKelompok : %s%nDeposit  : Rp %s%nDenda    : Rp %s%nKembalian: Rp %s%nPembayaran Tambahan: Rp %s%n%nLanjutkan?",
                        selectedPendakian.getNamaKetua(),
                        nf.format((long) deposit),
                        nf.format((long) denda),
                        nf.format((long) kembalian),
                        nf.format((long) tambahan)),
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Konfirmasi Check-Out");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.YES) {
                boolean ok = checkOutService.checkOut(selectedPendakian.getId(), list);
                if (ok) {
                    showAlert(Alert.AlertType.INFORMATION,
                            "✅ Check-Out berhasil!\n" + selectedPendakian.getNamaKetua() + " telah selesai pendakian.");
                    onReset();
                }
            }
        });
    }

    @FXML
    private void onReset() {
        if (tfSearchActive != null) tfSearchActive.clear();
        if (tblActiveGroups != null) tblActiveGroups.getSelectionModel().clearSelection();
        if (infoBox != null) { infoBox.setVisible(false); infoBox.setManaged(false); }
        if (verifikasiBox != null) { verifikasiBox.setVisible(false); verifikasiBox.setManaged(false); }
        verifikasiData = FXCollections.observableArrayList();
        verifikasiTable.setItems(verifikasiData);
        selectedPendakian = null;
        loadAktifPendakian(); // reload table
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
