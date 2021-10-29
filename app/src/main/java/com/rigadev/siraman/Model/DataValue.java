package com.rigadev.siraman.Model;

import java.io.Serializable;

public class DataValue implements Serializable {
    String invoice, barcode, store, name , qty, price, total, alias_name;

    public DataValue() {
    }

    public DataValue(String invoice, String barcode, String store, String name, String qty,
                     String price, String total, String alias_name) {
        this.invoice = invoice;
        this.barcode = barcode;
        this.store = store;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.total = total;
        this.alias_name = alias_name;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }
}
