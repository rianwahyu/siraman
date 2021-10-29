package com.rigadev.siraman.Model;

import java.io.Serializable;

public class DataReceive implements Serializable {
    String  id,barcode, name, qty;

    public DataReceive() {
    }

    public DataReceive(String id, String barcode, String name, String qty) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
