package com.example.shardulpathak.shp_patient.search_doctor;

import android.os.Bundle;

import com.example.shardulpathak.shp_patient.IBaseActivity;
import com.example.shardulpathak.shp_patient.R;
import com.example.shardulpathak.shp_patient.patient_details.DetailsActivity;

public class SearchDoctorActivity extends DetailsActivity implements IBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);
    }

    @Override
    public void attemptLogout() {
        super.attemptLogout();
    }

    @Override
    public void goToSearchDoctor() {
        //do nothing
    }

    @Override
    public void goToDetails() {
        super.goToDetails();
    }

    @Override
    public void goToFeedback() {
        super.goToFeedback();
    }
}
