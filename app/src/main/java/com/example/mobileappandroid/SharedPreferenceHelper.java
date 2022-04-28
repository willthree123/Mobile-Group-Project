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
        String result = getString(context, "Language_code", "code");
        if (result == null) {
            setString(context, "Language_code", "code", "en");
            result = "en";
        }
        return result;
    }

    static void clearRecords(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Records", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
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
        String result = sp.getString(key2, null);
        if (result==null){
            return "";
        }
        return result;
    }
}