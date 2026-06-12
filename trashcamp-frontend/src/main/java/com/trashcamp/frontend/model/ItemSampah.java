package com.trashcamp.frontend.model;

import javafx.beans.property.*;

public class ItemSampah {
    private final StringProperty item;
    private final StringProperty kategori;
    private final IntegerProperty qty;
    private final LongProperty deposit;
    private final LongProperty subtotal;

    public ItemSampah(String item, String kategori, int qty, long deposit) {
        this.item = new SimpleStringProperty(item);
        this.kategori = new SimpleStringProperty(kategori);
        this.qty = new SimpleIntegerProperty(qty);
        this.deposit = new SimpleLongProperty(deposit);
        this.subtotal = new SimpleLongProperty(qty * deposit);
    }

    public StringProperty itemProperty() { return item; }
    public StringProperty kategoriProperty() { return kategori; }
    public IntegerProperty qtyProperty() { return qty; }
    public LongProperty depositProperty() { return deposit; }
    public LongProperty subtotalProperty() { return subtotal; }

    public String getItem() { return item.get(); }
    public String getKategori() { return kategori.get(); }
    public int getQty() { return qty.get(); }
    public long getDeposit() { return deposit.get(); }
    public long getSubtotal() { return subtotal.get(); }
}