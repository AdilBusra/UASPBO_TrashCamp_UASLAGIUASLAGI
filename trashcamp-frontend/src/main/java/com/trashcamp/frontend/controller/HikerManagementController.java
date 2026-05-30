package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.model.OfficerSession;
import com.trashcamp.frontend.model.Pendakian;
import com.trashcamp.frontend.service.DummyHikerService;
import com.trashcamp.frontend.service.HikerService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Controller untuk halaman Manajemen Pendaki.
 */
public class HikerManagementController implements ContentController {

    @FXML private TextField tfSearch;
    @FXML private ComboBox<String> cbFilterStatus;
    @FXML private Label lblTotal;
    @FXML private Label lblResultCount;

    @FXML private TableView<Pendakian> hikersTable;
    @FXML private TableColumn<Pendakian, Number>  colNo;
    @FXML private TableColumn<Pendakian, String>  colNama;
    @FXML private TableColumn<Pendakian, Number>  colAnggota;
    @FXML private TableColumn<Pendakian, String>  colTrail;
    @FXML private TableColumn<Pendakian, String>  colStatus;
    @FXML private TableColumn<Pendakian, String>  colWaktuNaik;
    @FXML private TableColumn<Pendakian, String>  colDeposit;
    @FXML private TableColumn<Pendakian, Void>    colAksi;

    private OfficerSession session;
    private final HikerService hikerService = new DummyHikerService();
    private ObservableList<Pendakian> allData;
    private final NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    @Override
    public void setSession(OfficerSession session) {
        this.session = session;
    }

    @Override
    public void initData() {
        setupFilterCombo();
        setupTableColumns();
        loadAllData();
    }

    private void setupFilterCombo() {
        if (cbFilterStatus != null) {
            cbFilterStatus.setItems(FXCollections.observableArrayList(
                    "Semua", "AKTIF", "SELESAI", "DENDA"
            ));
            cbFilterStatus.setValue("Semua");
        }
    }

    private void setupTableColumns() {
        if (hikersTable == null) return;

        colNo.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        hikersTable.getItems().indexOf(data.getValue()) + 1
                )
        );

        colNama.setCellValueFactory(new PropertyValueFactory<>("namaKetua"));
        colAnggota.setCellValueFactory(new PropertyValueFactory<>("jumlahAnggota"));
        colTrail.setCellValueFactory(new PropertyValueFactory<>("trail"));
        colWaktuNaik.setCellValueFactory(new PropertyValueFactory<>("waktuNaik"));

        // Status column dengan badge-style
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(status);
                    String styleClass = switch (status) {
                        case "AKTIF"   -> "badge-green";
                        case "SELESAI" -> "badge-blue";
                        case "DENDA"   -> "badge-red";
                        default        -> "badge-gray";
                    };
                    badge.getStyleClass().add(styleClass);
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        // Deposit column
        colDeposit.setCellValueFactory(data ->
                new SimpleStringProperty("Rp " + nf.format((long) data.getValue().getTotalDeposit()))
        );

        // Aksi column
        colAksi.setCellFactory(col -> new TableCell<>() {
            private final Button btnDetail = new Button("Detail");
            {
                btnDetail.getStyleClass().add("icon-button");
                btnDetail.setOnAction(e -> {
                    Pendakian p = getTableView().getItems().get(getIndex());
                    showDetail(p);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnDetail);
            }
        });
    }

    private void loadAllData() {
        allData = FXCollections.observableArrayList(hikerService.getAllPendakian());
        hikersTable.setItems(allData);
        updateResultCount(allData.size());
    }

    @FXML
    private void onSearch() {
        applyFilter();
    }

    @FXML
    private void onFilterChange() {
        applyFilter();
    }

    private void applyFilter() {
        String keyword = tfSearch != null ? tfSearch.getText() : "";
        String status  = cbFilterStatus != null ? cbFilterStatus.getValue() : "Semua";

        List<Pendakian> filtered = hikerService.searchPendakian(keyword);
        if (status != null && !status.equals("Semua")) {
            filtered = filtered.stream()
                    .filter(p -> status.equalsIgnoreCase(p.getStatus()))
                    .toList();
        }

        ObservableList<Pendakian> result = FXCollections.observableArrayList(filtered);
        hikersTable.setItems(result);
        updateResultCount(result.size());
    }

    private void updateResultCount(int count) {
        if (lblResultCount != null) {
            lblResultCount.setText(count + " data ditemukan");
        }
        if (lblTotal != null) {
            lblTotal.setText("Menampilkan " + count + " data pendakian");
        }
    }

    @FXML
    private void onNewCheckIn() {
        Alert info = new Alert(Alert.AlertType.INFORMATION,
                "Gunakan menu 'Hiker Check-In' di sidebar untuk mendaftarkan kelompok baru.");
        info.setHeaderText("New Check-In");
        info.showAndWait();
    }

    private void showDetail(Pendakian p) {
        String msg = String.format(
                "ID          : %d%n" +
                "Nama Ketua  : %s%n" +
                "No. HP      : %s%n" +
                "Anggota     : %d orang%n" +
                "Trail       : %s%n" +
                "Status      : %s%n" +
                "Waktu Naik  : %s%n" +
                "Waktu Turun : %s%n" +
                "Deposit     : Rp %s%n" +
                "Denda       : Rp %s",
                p.getId(), p.getNamaKetua(), p.getNoHp(), p.getJumlahAnggota(),
                p.getTrail(), p.getStatus(), p.getWaktuNaik(),
                p.getWaktuTurun().equals("-") ? "Masih aktif" : p.getWaktuTurun(),
                nf.format((long) p.getTotalDeposit()),
                nf.format((long) p.getTotalDenda())
        );
        Alert detail = new Alert(Alert.AlertType.INFORMATION, msg);
        detail.setTitle("Detail Pendakian");
        detail.setHeaderText("📋 " + p.getNamaKetua());
        detail.getDialogPane().setPrefWidth(420);
        detail.showAndWait();
    }
}
