package com.example.shardulpathak.shp_doctor.view_details;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shardulpathak.shp_doctor.DummyContent;
import com.example.shardulpathak.shp_doctor.IFragmentCommunicator;
import com.example.shardulpathak.shp_doctor.LoginActivity;
import com.example.shardulpathak.shp_doctor.PreferencesManagement;
import com.example.shardulpathak.shp_doctor.R;
import com.example.shardulpathak.shp_doctor.appointment.MyAppointmentsFragment;
import com.example.shardulpathak.shp_doctor.notification.NotificationFragment;
import com.example.shardulpathak.shp_doctor.view_disease.DiseaseFragment;
import com.example.shardulpathak.shp_doctor.view_patient.PatientFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class DetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IFragmentCommunicator {

    private ImageView mNavImageView;
    private TextView mNavNameTextView;
    private TextView mNavEmailTextView;
    private GetDetailsTask mGetDetailsTask = null;
    private boolean mIsImageAvailable;
    private PreferencesManagement mPreferencesManagement;
    private static final String TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openActivityWithEditableDetails();
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPreferencesManagement = new PreferencesManagement();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        View hView = navigationView.getHeaderView(0);
        mNavImageView = (ImageView) hView.findViewById(R.id.nav_imageView);
        mNavNameTextView = (TextView) hView.findViewById(R.id.nav_nameTextView);
        mNavEmailTextView = (TextView) hView.findViewById(R.id.nav_mailTextView);
        getEmailAndName();
        mNavImageView.setImageResource(R.drawable.doctor_app_icon);
        mNavImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayImageSelectionAlert();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getEmailAndName() {
        mGetDetailsTask = new GetDetailsTask();
        mGetDetailsTask.execute();
    }

    /**
     *
     */
    private void displayImageSelectionAlert() {
        String[] dialogItems = {"Select image from Gallery", "Capture a  photo", "Cancel"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Select Image")
                .setItems(dialogItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
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
                break;

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
        MyAppointmentsFragment myAppointmentsFragment = new MyAppointmentsFragment();
        openFragment(myAppointmentsFragment);
    }

    /**
     * Logout of the app
     */
    private void attemptLogout() {
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

    public class GetDetailsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "Inside doInBackground(" + params + ")");
            try {
                String userId = mPreferencesManagement.getDataFromPreferences(getBaseContext(), getString(R.string.pref_user_id_key));
                String getDetailsURL = "http://skillab.in/medical_beta/main/getDocterListAPI";
                URL url = new URL(getDetailsURL);
                JSONObject postDataParams = new JSONObject();

                postDataParams.put("user_id", userId);

                Log.e("params", postDataParams.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                Log.d(TAG, "All connection parameters setting done.");

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        Log.d(TAG, line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                } else {
                    return "false : " + responseCode;
                }
            } catch (IOException | JSONException e) {
                return "Exception ::" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "Inside onPostExecute(" + result + ")");
            super.onPostExecute(result);
            mGetDetailsTask = null;

            Log.d("result::", result);
//            Toast.makeText(getActivity(), "Result obtained on get details is: " + result, Toast.LENGTH_SHORT).show();

            try {
                JSONObject getUserDetails = new JSONObject(result);
                String status = getUserDetails.getString("status");
                if (status.contains("success")) {
                    Log.d(TAG, "Success in getting email and name and image resource for navigation drawer");
                } else {
                    if (result.isEmpty() || status.contains("error")) {
                        Log.d(TAG, "Failure in getting email and name and image resource for navigation drawer");
                    }
                }

                JSONArray userData = getUserDetails.getJSONArray("data");
                for (int i = 0; i < userData.length(); i++) {
                    JSONObject userDetail = userData.getJSONObject(i);
                    String fName = userDetail.getString("fname");
                    String lName = userDetail.getString("lname");
                    String name = fName + " " + lName;

                    String email = userDetail.getString("email");
                    String imgURL = userDetail.getString("profile_pic");

                    if (!TextUtils.isEmpty(imgURL)) {
                        mNavImageView.setImageResource(Integer.parseInt(imgURL));
                    } else {
                        mIsImageAvailable = false;
                    }
                    mNavNameTextView.setText(name);
                    mNavEmailTextView.setText(email);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mGetDetailsTask = null;
        }
    }

    public String getPostDataString(JSONObject params) throws JSONException, UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();
        while (itr.hasNext()) {
            String key = itr.next();
            Object value = params.get(key);
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}
