package com.example.shardulpathak.shp_doctor.view_patient;


public class Patient {
    private String mPatientId;
    private String mPatientName;
    private String mPatientAge;


    public Patient(String patientId, String patientName, String patientAge) {
        this.mPatientId = patientId;
        this.mPatientName = patientName;
        this.mPatientAge = patientAge;
    }

    public String getPatientId() {
        return mPatientId;
    }

    public void setPatientId(String mPatientId) {
        this.mPatientId = mPatientId;
    }

    public String getPatientName() {
        return mPatientName;
    }

    public void setPatientName(String mPatientName) {
        this.mPatientName = mPatientName;
    }

    public String getPatientAge() {
        return mPatientAge;
    }

    public void setPatientAge(String mPatientAge) {
        this.mPatientAge = mPatientAge;
    }
}
