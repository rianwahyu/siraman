package com.rigadev.siraman.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.rigadev.siraman.BuildConfig;


public class SessionLogin {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SessionLogin(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void login(String id, String username, String level) {
        editor.putString("id", id);
        editor.putString("username", username);
        editor.putString("level", level);
        editor.putBoolean("logged", true);
        editor.commit();
    }

    public void logout() {
        editor.putString("id", "");
        editor.putString("username", "");
        editor.putString("level", "");
        editor.putBoolean("logged", false);
        editor.commit();
    }

    public String getID() {
        return preferences.getString("id", "");
    }

    public String getUsername() {
        return preferences.getString("username", "");
    }
    public String getStore() {
        return preferences.getString("store", "");
    }
    public String getLevel() {
        return preferences.getString("level", "");
    }

    public boolean isLoggedin() {
        return preferences.getBoolean("logged", false);
    }

}
