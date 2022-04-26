package com.example.mobileappandroid;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
    private final static String PREF_FILE = "PREF";

    //Setting
    static void setLanguage(Context context, String value) {
        setString(context, "Language_code", "code", value);
    }
    static String getLanguage(Context context) {
        return getString(context, "Language_code", "code");
    }

    //    Driver
    static void setString(Context context, String key1, String key2, String value) {
        SharedPreferences sp = context.getSharedPreferences(key1, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key2, value);
        editor.apply();
    }

    static String getString(Context context, String key1, String key2) {
        SharedPreferences sp = context.getSharedPreferences(key1, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        return sp.getString(key2, null);
    }
}