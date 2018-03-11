package com.example.shardulpathak.shp_patient.feedback;

import android.os.Bundle;

import com.example.shardulpathak.shp_patient.IBaseActivity;
import com.example.shardulpathak.shp_patient.R;
import com.example.shardulpathak.shp_patient.patient_details.DetailsActivity;

public class FeedbackActivity extends DetailsActivity implements IBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle(R.string.feedback_title);
    }

    @Override
    public void attemptLogout() {
        super.attemptLogout();
    }

    @Override
    public void goToSearchDoctor() {
        super.goToSearchDoctor();
    }

    @Override
    public void goToDetails() {
        super.goToDetails();
    }

    @Override
    public void goToFeedback() {
        //do nothing
    }
}
