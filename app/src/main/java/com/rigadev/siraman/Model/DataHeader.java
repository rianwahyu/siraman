package com.rigadev.siraman.Model;

public class DataHeader {
    String invoice, store, user, time, salesValue, paidValue, returnValue, year;

    public DataHeader() {
    }

    public DataHeader(String invoice, String store, String user, String time, String salesValue, String paidValue, String returnValue, String year) {
        this.invoice = invoice;
        this.store = store;
        this.user = user;
        this.time = time;
        this.salesValue = salesValue;
        this.paidValue = paidValue;
        this.returnValue = returnValue;
        this.year = year;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSalesValue() {
        return salesValue;
    }

    public void setSalesValue(String salesValue) {
        this.salesValue = salesValue;
    }

    public String getPaidValue() {
        return paidValue;
    }

    public void setPaidValue(String paidValue) {
        this.paidValue = paidValue;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
