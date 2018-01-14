package com.example.shardulpathak.shp_doctor.view_disease;

/**
 * Created by shardul.pathak on 14-01-2018.
 */

public class Disease {


    private String mDiseaseId;
    private String mDiseaseName;
    private String mDiseaseType;

    public Disease(String id, String name, String type) {
        this.mDiseaseId = id;
        this.mDiseaseName = name;
        this.mDiseaseType = type;
    }

    public String getDiseaseId() {
        return mDiseaseId;
    }

    public void setDiseaseId(String mDiseaseId) {
        this.mDiseaseId = mDiseaseId;
    }

    public String getDiseaseName() {
        return mDiseaseName;
    }

    public void setDiseaseName(String mDiseaseName) {
        this.mDiseaseName = mDiseaseName;
    }

    public String getDiseaseType() {
        return mDiseaseType;
    }

    public void setDiseaseType(String mDiseaseType) {
        this.mDiseaseType = mDiseaseType;
    }
}
