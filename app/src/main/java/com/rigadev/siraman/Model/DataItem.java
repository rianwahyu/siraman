package com.rigadev.siraman.Model;

public class DataItem {
    String barcode, store, name, qty, uom, category, subCategory, minQty,maxQty,active,
            pusrchasePrice, gp1, alias_name, source;

    public DataItem() {
    }

    public DataItem(String barcode, String store, String name, String qty, String uom, String category,
                    String subCategory, String minQty, String maxQty, String active, String pusrchasePrice,
                    String gp1, String alias_name, String source) {
        this.barcode = barcode;
        this.store = store;
        this.name = name;
        this.qty = qty;
        this.uom = uom;
        this.category = category;
        this.subCategory = subCategory;
        this.minQty = minQty;
        this.maxQty = maxQty;
        this.active = active;
        this.pusrchasePrice = pusrchasePrice;
        this.gp1 = gp1;
        this.alias_name = alias_name;
        this.source = source;
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

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getMinQty() {
        return minQty;
    }

    public void setMinQty(String minQty) {
        this.minQty = minQty;
    }

    public String getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(String maxQty) {
        this.maxQty = maxQty;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getPusrchasePrice() {
        return pusrchasePrice;
    }

    public void setPusrchasePrice(String pusrchasePrice) {
        this.pusrchasePrice = pusrchasePrice;
    }

    public String getGp1() {
        return gp1;
    }

    public void setGp1(String gp1) {
        this.gp1 = gp1;
    }

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
