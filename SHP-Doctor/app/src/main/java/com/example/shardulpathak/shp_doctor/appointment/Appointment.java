package com.example.shardulpathak.shp_doctor.appointment;

public class Appointment {

    private String mAppointmentID;
    private String mAppointmentDate;
    private String mAppointmentTime;
    private String mAppointmentPatientName;

    public Appointment(String appointmentID, String appointmentDate, String appointmentTime, String appointmentPatientName) {
        this.mAppointmentID = appointmentID;
        this.mAppointmentDate = appointmentDate;
        this.mAppointmentTime = appointmentTime;
        this.mAppointmentPatientName = appointmentPatientName;
    }

    public String getAppointmentID() {
        return mAppointmentID;
    }

    public void setAppointmentID(String mAppointmentID) {
        this.mAppointmentID = mAppointmentID;
    }

    public String getAppointmentDate() {
        return mAppointmentDate;
    }

    public void setAppointmentDate(String mAppointmentDate) {
        this.mAppointmentDate = mAppointmentDate;
    }

    public String getAppointmentTime() {
        return mAppointmentTime;
    }

    public void setAppointmentTime(String mAppointmentTime) {
        this.mAppointmentTime = mAppointmentTime;
    }

    public String getAppointmentPatientName() {
        return mAppointmentPatientName;
    }

    public void setAppointmentPatientName(String mAppointmentPatientName) {
        this.mAppointmentPatientName = mAppointmentPatientName;
    }
}
