package com.example.shardulpathak.shp_patient.search_disease;

/**
 * Disease details
 */
public class DiseaseDetails {

    private String mDiseaseId;
    private String mDiseaseName;
    private String mDiseaseType;


    public DiseaseDetails(String id, String name, String type) {
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
