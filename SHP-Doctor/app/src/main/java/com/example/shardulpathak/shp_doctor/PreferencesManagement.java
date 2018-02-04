package com.example.shardulpathak.shp_doctor;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shardul.pathak on 28-01-2018.
 */

public class PreferencesManagement {

    private static final String PREFER_NAME = "SHP Preferences";

    private SharedPreferences.Editor mEditor;


    /**
     * @param context
     * @param key
     * @return
     */
    public String getDataFromPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    /**
     * @param context
     * @param key
     * @param value
     */
    public void putDataInPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        mEditor = sharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }

    /**
     *
     */
    public void clearUserData() {
        mEditor.clear();
        mEditor.commit();
    }
}
