package com.shipfindpeople.app.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sonnd on 9/26/2016.
 */

public class AppPreference {
    private static final String AFFILIATE_PREF = "AffiliatePreference";
    private static AppPreference sinPref;
    private SharedPreferences sharedPreference;

    private AppPreference(SharedPreferences sharedPreferences) {
        this.sharedPreference = sharedPreferences;
    }

    public static AppPreference getInstance(Context context) {
        if (sinPref == null) {
            sinPref = new AppPreference(context.getSharedPreferences(AFFILIATE_PREF, 0));
        }
        return sinPref;
    }

    public String getString(String key, String defValue) {
        return this.sharedPreference.getString(key, defValue);
    }

    int getInt(String key, int defValue) {
        return this.sharedPreference.getInt(key, defValue);
    }

    String setString(String key, String value) {
        this.sharedPreference.edit().putString(key, value).apply();
        return key;
    }

    void setInt(String key, int value) {
        this.sharedPreference.edit().putInt(key, value).apply();
    }

    public void setBoolean(String key, boolean value) {
        this.sharedPreference.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return this.sharedPreference.getBoolean(key, defValue);
    }

    public void setLong(String key, long value) {
        this.sharedPreference.edit().putLong(key, value).apply();
    }

    public long getLong(String key, long defValue) {
        return this.sharedPreference.getLong(key, defValue);
    }

    void remove(String key) {
        this.sharedPreference.edit().remove(key).apply();
    }
}
