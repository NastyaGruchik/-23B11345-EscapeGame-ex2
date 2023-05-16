package com.example.ex2.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class MSP {
    private static final String SP_FILE = "EscapeApp";
    private SharedPreferences preferences;
    private static MSP msP;

    private MSP(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static MSP getInstance(Context context) {
        if (msP == null) {
            msP = new MSP(context);
        }
        return msP;
    }

    public int getIntSP(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putIntSP(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getStringSP(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void putStringSP(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
