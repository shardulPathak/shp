package com.example.shardulpathak.shp_doctor.view_details;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shardulpathak.shp_doctor.DummyContent;
import com.example.shardulpathak.shp_doctor.IFragmentCommunicator;
import com.example.shardulpathak.shp_doctor.LoginActivity;
import com.example.shardulpathak.shp_doctor.R;
import com.example.shardulpathak.shp_doctor.notification.NotificationFragment;
import com.example.shardulpathak.shp_doctor.view_disease.DiseaseFragment;
import com.example.shardulpathak.shp_doctor.view_patient.PatientFragment;

public class DetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IFragmentCommunicator {


    private static final String TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openActivityWithEditableDetails();
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_notification);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                openNotificationFragment();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        finish();

    }

    /**
     * Navigates to notification activity
     */
    private void openNotificationFragment() {
        NotificationFragment notificationFragment = new NotificationFragment();
        openFragment(notificationFragment);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                //do nothing
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_details:
                openActivityWithEditableDetails();
                break;

            case R.id.nav_disease:
                openDiseaseFragment();
                break;

            case R.id.nav_patient:
                openPatientFragment();
                break;

            case R.id.nav_appointment:
                openAppointmentFragment();

            case R.id.nav_logout:
                attemptLogout();
                break;

            default:
                Log.d(TAG, "Illegal argument for nav drawer item selection");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *
     */
    private void openAppointmentFragment() {
        Toast.makeText(getBaseContext(), "Appointments option selected", Toast.LENGTH_SHORT).show();
    }

    /**
     * Logout of the app
     */
    private void attemptLogout() {
//TODO check if the user is in session and that it is the user that logged in
        showLogoutDialog();
    }


    /**
     * Displays logout dialog to user
     */
    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(getText(R.string.logout_dialog_title))
                .setMessage(R.string.logout_dialog_message)
                .setPositiveButton(getText(R.string.dialog_yes_button_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToLoginActivity();

                    }
                });
        alertBuilder.setNeutralButton(getText(R.string.dialog_no_button_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        AlertDialog logoutDialog = alertBuilder.create();
        logoutDialog.show();

    }

    private void goToLoginActivity() {
        Intent logoutIntent = new Intent(getBaseContext(), LoginActivity.class);
//        logoutIntent.putExtra("logoutFlag", mIsInSession);
        startActivity(logoutIntent);
    }

    /**
     * Navigate to patient activity to view patient details
     */
    private void openPatientFragment() {
        PatientFragment patientFragment = new PatientFragment();
        openFragment(patientFragment);

    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.detail_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Navigate to disease activity to view disease details
     */
    private void openDiseaseFragment() {
        DiseaseFragment diseaseFragment = new DiseaseFragment();
        openFragment(diseaseFragment);

    }

    /**
     * Open this activity with editable details
     */
    private void openActivityWithEditableDetails() {
        openEditableFragment();
    }

    private void openEditableFragment() {
        DetailsFragment detailsFragment = new DetailsFragment();
        openFragment(detailsFragment);

    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem mItem) {

    }
}
