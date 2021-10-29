package com.rigadev.siraman.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.rigadev.siraman.BuildConfig;


public class HeaderManager {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public HeaderManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void header(String invoice, String store, String user, String time, String salesValue,
                                String paidValue, String returnValue, String year) {
        editor.putString("invoice", invoice);
        editor.putString("store", store);
        editor.putString("user", user);
        editor.putString("time", time);
        editor.putString("salesValue", salesValue);
        editor.putString("paidValue", paidValue);
        editor.putString("returnValue", returnValue);
        editor.putString("year", year);
        editor.commit();
    }

    public String getInvoice() {
        return preferences.getString("invoice", "");
    }

    public String getStore() {
        return preferences.getString("store", "");
    }

    public String getUser() {
        return preferences.getString("user", "");
    }

    public String getTime() {
        return preferences.getString("time", "");
    }

    public String getSales() {
        return preferences.getString("salesValue", "");
    }

    public String getPaid() {
        return preferences.getString("paidValue", "");
    }

    public String getReturn() {
        return preferences.getString("returnValue", "");
    }

    public String getYear() {
        return preferences.getString("year", "");
    }

    public void headeri() {
        editor.putString("invoice", null);
        editor.putString("store", null);
        editor.putString("user", null);
        editor.putString("time", null);
        editor.putString("salesValue", null);
        editor.putString("paidValue", null);
        editor.putString("returnValue", null);
        editor.putString("year", null);
        editor.commit();
    }
}
