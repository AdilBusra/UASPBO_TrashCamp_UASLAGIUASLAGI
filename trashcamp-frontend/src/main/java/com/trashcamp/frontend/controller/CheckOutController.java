package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.model.DetailSampah;
import com.trashcamp.frontend.model.OfficerSession;
import com.trashcamp.frontend.model.Pendakian;
import com.trashcamp.frontend.service.CheckOutService;
import com.trashcamp.frontend.service.DummyCheckOutService;
import com.trashcamp.frontend.service.DummyHikerService;
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

    @FXML private ComboBox<Pendakian> cbPendakian;
    @FXML private Label lblKetua;
    @FXML private Label lblAnggota;
    @FXML private Label lblTrail;
    @FXML private Label lblWaktuNaik;
    @FXML private Label lblDepositAwal;
    @FXML private javafx.scene.layout.VBox infoBox;
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

    private OfficerSession session;
    private final HikerService hikerService = new DummyHikerService();
    private final CheckOutService checkOutService = new DummyCheckOutService();
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
        loadAktifPendakian();
        setupTable();
    }

    private void loadAktifPendakian() {
        if (cbPendakian == null) return;
        List<Pendakian> aktif = hikerService.getAktifPendakian();
        cbPendakian.setItems(FXCollections.observableArrayList(aktif));
        cbPendakian.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Pendakian p) {
                return p == null ? "" : "[" + p.getId() + "] " + p.getNamaKetua() + " — " + p.getTrail();
            }
            @Override public Pendakian fromString(String s) { return null; }
        });
    }

    @FXML
    private void onPendakianSelected() {
        // Preview saat dipilih tapi belum muat data
    }

    @FXML
    private void onLoadData() {
        selectedPendakian = cbPendakian != null ? cbPendakian.getValue() : null;
        if (selectedPendakian == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih kelompok pendakian terlebih dahulu.");
            return;
        }
        updateInfoBox(selectedPendakian);
        loadVerifikasiData(selectedPendakian.getId());
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

        if (lblSummaryDeposit != null)
            lblSummaryDeposit.setText("Rp " + nf.format((long) deposit));
        if (lblTotalDenda != null)
            lblTotalDenda.setText("Rp " + nf.format((long) denda));
        if (lblKembalian != null)
            lblKembalian.setText("Rp " + nf.format((long) Math.max(0, kembalian)));
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

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                String.format("Konfirmasi Check-Out:%n%nKelompok : %s%nDeposit  : Rp %s%nDenda    : Rp %s%nKembalian: Rp %s%n%nLanjutkan?",
                        selectedPendakian.getNamaKetua(),
                        nf.format((long) deposit),
                        nf.format((long) denda),
                        nf.format((long) kembalian)),
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
        if (cbPendakian != null) cbPendakian.setValue(null);
        if (infoBox != null) { infoBox.setVisible(false); infoBox.setManaged(false); }
        if (verifikasiBox != null) { verifikasiBox.setVisible(false); verifikasiBox.setManaged(false); }
        verifikasiData = FXCollections.observableArrayList();
        verifikasiTable.setItems(verifikasiData);
        selectedPendakian = null;
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
