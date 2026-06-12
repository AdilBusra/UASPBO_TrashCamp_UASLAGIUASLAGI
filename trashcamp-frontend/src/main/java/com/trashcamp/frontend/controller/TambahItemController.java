package com.trashcamp.frontend.controller;

import com.trashcamp.frontend.model.MasterSampah;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.util.List;

public class TambahItemController {

    @FXML private ComboBox<MasterSampah> cbItems;
    @FXML private TextField tfQty;

    private Stage dialogStage;
    private MasterSampah selectedMasterSampah;
    private int qty = 0;
    private boolean isSaveClicked = false;

    @FXML
    public void initialize() {
        cbItems.setConverter(new StringConverter<>() {
            @Override
            public String toString(MasterSampah object) {
                return object == null ? "" : object.getNamaItem();
            }
            @Override
            public MasterSampah fromString(String string) {
                return null;
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMasterList(List<MasterSampah> masterList) {
        cbItems.setItems(FXCollections.observableArrayList(masterList));
        if (!masterList.isEmpty()) {
            cbItems.setValue(masterList.get(0));
        }
    }

    public boolean isSaveClicked() {
        return isSaveClicked;
    }

    public MasterSampah getSelectedMasterSampah() {
        return selectedMasterSampah;
    }

    public int getQty() {
        return qty;
    }

    @FXML
    private void onSave() {
        if (cbItems.getValue() == null) return;

        try {
            int inputQty = Integer.parseInt(tfQty.getText().trim());
            if (inputQty <= 0) throw new NumberFormatException();

            this.selectedMasterSampah = cbItems.getValue();
            this.qty = inputQty;
            this.isSaveClicked = true;
            dialogStage.close();
        } catch (NumberFormatException e) {
            System.out.println("Masukkan kuantitas angka positif!");
        }
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }
}