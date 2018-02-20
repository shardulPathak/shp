package com.example.shardulpathak.shp_patient.patient_details;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.shardulpathak.shp_patient.IBaseActivity;
import com.example.shardulpathak.shp_patient.IFragmentCommunicator;
import com.example.shardulpathak.shp_patient.LoginActivity;
import com.example.shardulpathak.shp_patient.PreferencesManagement;
import com.example.shardulpathak.shp_patient.R;
import com.example.shardulpathak.shp_patient.appointments.MyAppointmentsFragment;
import com.example.shardulpathak.shp_patient.feedback.FeedbackFragment;
import com.example.shardulpathak.shp_patient.search_disease.SearchDiseaseFragment;
import com.example.shardulpathak.shp_patient.search_disease.SearchResultsFragment;
import com.example.shardulpathak.shp_patient.search_doctor.SearchDoctorFragment;

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
        implements NavigationView.OnNavigationItemSelectedListener, IFragmentCommunicator, IBaseActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private GetDetailsTask mGetDetailsTask = null;

    private ImageView mImageView;
    private TextView mEmailTextView;
    private TextView mNameTextView;
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private boolean mIsImageAvailable;

    private PreferencesManagement mPreferencesManagement;
    private SearchResultsFragment mSearchResultsFragment;
    private SearchDiseaseFragment mSearchDiseaseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        goToDetails();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPreferencesManagement = new PreferencesManagement();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                navigateToSearchDisease();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        View hView = navigationView.getHeaderView(0);
        mImageView = (ImageView) hView.findViewById(R.id.navDrawerImageView);
        mImageView.setImageResource(R.drawable.patient_app_icon);
        mEmailTextView = (TextView) hView.findViewById(R.id.navDrawerEmailView);
        mNameTextView = (TextView) hView.findViewById(R.id.navDrwerNameTextView);
        getEmailAndName();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getEmailAndName() {
        mGetDetailsTask = new GetDetailsTask();
        mGetDetailsTask.execute();
    }

    /**
     * Navigates to the search disease screen
     */
    private void navigateToSearchDisease() {
        mSearchDiseaseFragment = new SearchDiseaseFragment();
        mSearchResultsFragment=new SearchResultsFragment();
        mSearchResultsFragment.setListener(this);
        openSearchDiseaseFragment(mSearchDiseaseFragment);
    }

    /**
     * Navigates to the search disease screen
     *
     * @param fragment fragment to be opened
     */
    private void openSearchDiseaseFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.detail_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mSearchResultsFragment.shouldGoBack()) {
                openSearchDiseaseFragment(mSearchDiseaseFragment);
            } else {
                //do nothing
            }
        }
    }

    /**
     * Shows the logout dialog
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
     * Navigates to the search doctor screen
     */
    public void goToSearchDoctor() {
        SearchDoctorFragment searchDoctorFragment = new SearchDoctorFragment();
        openFragment(searchDoctorFragment);
    }

    /**
     * Navigates to the feedback screen
     */
    public void goToFeedback() {
        FeedbackFragment feedbackFragment = new FeedbackFragment();
        openFragment(feedbackFragment);
    }


    /**
     * Navigates to the details screen
     */
    public void goToDetails() {
        DetailsFragment details = new DetailsFragment();
        openFragment(details);

    }

    /**
     * Opens a particular fragment
     *
     * @param fragment fragment to be opened
     */
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
        String title = item.getTitle().toString();
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

            case R.id.appointments:
                goToAppointments();
                break;

            case R.id.nav_logout:
                attemptLogout();
                break;

            default:
                throw new IllegalArgumentException();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Navigates to the appointments screen
     */
    private void goToAppointments() {
        MyAppointmentsFragment myAppointmentsFragment = new MyAppointmentsFragment();
        openFragment(myAppointmentsFragment);
    }

    @Override
    public void onListFragmentInteraction() {

    }


    public class GetDetailsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Inside doInBackground(" + params + ")");
            try {
                String userId = mPreferencesManagement.getDataFromPreferences(getBaseContext(), getString(R.string.pref_user_id_key));
                String getDetailsURL = "http://skillab.in/medical_beta/main/getPatientListAPI";
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
                        mImageView.setImageResource(Integer.parseInt(imgURL));
                    } else {
                        mIsImageAvailable = false;
                    }

                    mNameTextView.setText(name);
                    mEmailTextView.setText(email);
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
