package com.example.shardulpathak.shp_doctor.view_patient;


public class Patient {
    private String mPatientId;
    private String mPatientName;
    private String mPatientAge;
    private String mPatientContact;
    private String mPatientGender;
    private String mPatientAddress;

    public Patient(String patientId, String patientName, String patientAge, String patientAddress, String patientGender,
                   String patientContact) {
        this.mPatientId = patientId;
        this.mPatientName = patientName;
        this.mPatientAge = patientAge;
        this.mPatientAddress = patientAddress;
        this.mPatientGender = patientGender;
        this.mPatientContact = patientContact;
    }

    public String getPatientContact() {
        return mPatientContact;
    }

    public void setPatientContact(String patientContact) {
        this.mPatientContact = patientContact;
    }

    public String getPatientGender() {
        return mPatientGender;
    }

    public void setPatientGender(String patientGender) {
        this.mPatientGender = patientGender;
    }

    public String getPatientAddress() {
        return mPatientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.mPatientAddress = patientAddress;
    }

    public String getPatientId() {
        return mPatientId;
    }

    public void setPatientId(String patientId) {
        this.mPatientId = patientId;
    }

    public String getPatientName() {
        return mPatientName;
    }

    public void setPatientName(String patientName) {
        this.mPatientName = patientName;
    }

    public String getPatientAge() {
        return mPatientAge;
    }

    public void setPatientAge(String patientAge) {
        this.mPatientAge = patientAge;
    }
}
