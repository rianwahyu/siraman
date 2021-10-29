package com.rigadev.siraman.Model;

import java.io.Serializable;

public class DataCart implements Serializable {
    String  id,barcode, name, qty, price, alias_name;

    public DataCart() {
    }

    public DataCart(String id, String barcode, String name, String qty, String price, String alias_name) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.alias_name = alias_name;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }
}
