package com.example.shardulpathak.shp_patient.patient_details;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.shardulpathak.shp_patient.IBaseActivity;
import com.example.shardulpathak.shp_patient.IFragmentCommunicator;
import com.example.shardulpathak.shp_patient.LoginActivity;
import com.example.shardulpathak.shp_patient.R;
import com.example.shardulpathak.shp_patient.feedback.FeedbackFragment;
import com.example.shardulpathak.shp_patient.search_disease.SearchDiseaseActivity;
import com.example.shardulpathak.shp_patient.search_disease.SearchDiseaseFragment;
import com.example.shardulpathak.shp_patient.search_doctor.SearchDoctorActivity;
import com.example.shardulpathak.shp_patient.search_doctor.SearchDoctorFragment;

public class DetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IFragmentCommunicator, IBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        goToDetails();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                openSearchDiseaseActivity();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View imageView = navigationView.findViewById(R.id.navDrawerImageView);
        View emailTextView = navigationView.findViewById(R.id.navDrawerEmailView);
        View nameTExtView = navigationView.findViewById(R.id.navDrwerNameTextView);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void openSearchDiseaseActivity() {
        SearchDiseaseFragment searchDiseaseFragment=new SearchDiseaseFragment();
        openFragment(searchDiseaseFragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //do nothing
        }
    }

    /**
     *
     */
    @Override
    public void attemptLogout() {
        showLogoutDialog();
    }

    public void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(getText(R.string.logout_dialog_title));
        alertBuilder.setMessage(R.string.logout_dialog_message)
                .setPositiveButton(R.string.dialog_yes_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent logoutIntent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(logoutIntent);
                    }
                })
                .setNeutralButton(R.string.dialog_no_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });


        AlertDialog logoutDialog = alertBuilder.create();
        logoutDialog.show();

    }

    /**
     *
     */
    public void goToSearchDoctor() {
        SearchDoctorFragment searchDoctorFragment=new SearchDoctorFragment();
        openFragment(searchDoctorFragment);
    }

    /**
     *
     */
    public void goToFeedback() {
        FeedbackFragment feedbackFragment = new FeedbackFragment();
        openFragment(feedbackFragment);
    }


    /**
     *
     */
    public void goToDetails() {
        DetailsFragment details = new DetailsFragment();
        openFragment(details);

    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.detail_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_my_details:
                goToDetails();
                break;

            case R.id.nav_feedback:
                goToFeedback();
                break;
            case R.id.nav_search_doctor:
                goToSearchDoctor();
                break;
            case R.id.nav_logout:
                attemptLogout();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction() {

    }
}
