package com.example.shardulpathak.shp_patient;

import android.app.Application;


public class SHPApplication extends Application {
    public PreferencesManagement mPreferencesManagement;

    PreferencesManagement getSharedPreferencesAccess() {
        return new PreferencesManagement();
    }
}
