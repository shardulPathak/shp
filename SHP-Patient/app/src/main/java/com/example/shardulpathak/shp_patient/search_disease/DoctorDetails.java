package com.example.shardulpathak.shp_patient.search_disease;


/**
 * Doctor details
 */
public class DoctorDetails {


    public DoctorDetails(String doctorID, String doctorEmail,
                         String docCategory, String docFullName, String docAddress,
                         String docCity, String docMobile, String docHospitalName) {
        this.mDoctorID = doctorID;
        this.mDoctorEmail = doctorEmail;
        this.mDocCategory = docCategory;
        this.mDocFullName = docFullName;
        this.mDocAddress = docAddress;
        this.mDocCity = docCity;
        this.mDocMobile = docMobile;
        this.mDocHospitalName = docHospitalName;
    }

    private String mDoctorID;
    private String mDoctorEmail;
    private String mDocCategory;
    private String mDocFullName;
    private String mDocAddress;
    private String mDocCity;
    private String mDocMobile;
    private String mDocHospitalName;


    public String getDoctorID() {
        return mDoctorID;
    }

    public void setDoctorID(String mDoctorID) {
        this.mDoctorID = mDoctorID;
    }

    public String getDoctorEmail() {
        return mDoctorEmail;
    }

    public void setDoctorEmail(String mDoctorEmail) {
        this.mDoctorEmail = mDoctorEmail;
    }

    public String getDocCategory() {
        return mDocCategory;
    }

    public void setDocCategory(String mDocCategory) {
        this.mDocCategory = mDocCategory;
    }

    public String getDocFullName() {
        return mDocFullName;
    }

    public void setDocFullName(String mDocFullName) {
        this.mDocFullName = mDocFullName;
    }

    public String getDocAddress() {
        return mDocAddress;
    }

    public void setDocAddress(String mDocAddress) {
        this.mDocAddress = mDocAddress;
    }

    public String getDocCity() {
        return mDocCity;
    }

    public void setDocCity(String mDocCity) {
        this.mDocCity = mDocCity;
    }

    public String getDocMobile() {
        return mDocMobile;
    }

    public void setDocMobile(String mDocMobile) {
        this.mDocMobile = mDocMobile;
    }

    public String getDocHospitalName() {
        return mDocHospitalName;
    }

    public void setDocHospitalName(String mDocHospitalName) {
        this.mDocHospitalName = mDocHospitalName;
    }
}
