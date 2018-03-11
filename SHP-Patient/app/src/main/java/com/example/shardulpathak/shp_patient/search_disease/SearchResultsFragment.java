package com.example.shardulpathak.shp_patient.search_disease;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shardulpathak.shp_patient.IFragmentCommunicator;
import com.example.shardulpathak.shp_patient.PreferencesManagement;
import com.example.shardulpathak.shp_patient.R;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultsFragment extends Fragment {

    private TextView mDiseaseListTextView;
    private TextView mDoctorListTextView;
    private ListView mDiseaseListView;
    private ListView mDoctorListView;

    private DiseaseListAdapter mDiseaseListAdapter;
    private ArrayList<DiseaseDetails> mDiseaseDetailsArrayList;
    private DoctorListAdapter mDoctorListAdapter;
    private ArrayList<DoctorDetails> mDoctorDetailsArrayList;
    ArrayList<String> mSelectedSymptomsList;
    private int mListSize;


    String mDiseaseID;
    String mDiseaseName;
    String mDiseaseType;


    String mDoctorID;
    String mDoctorEmail;
    String mDocCategory;
    String mDocFullName;
    String mDocAddress;
    String mDocCity;
    String mDocMobile;
    String mDocHospitalName;

    private String mAppointmentDoctorID;
    private RequestAppointmentTask mRequestAppointmentTask;

    private PreferencesManagement mPreferencesManagement;

    private static final String TAG = SearchResultsFragment.class.getSimpleName();


    private GetDiseaseAndDoctorTask mDiseaseListTask = null;
    private IFragmentCommunicator mListener;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Set listener
     *
     * @param listener IFragmentCommunicator instance to be initialized
     */
    public void setListener(IFragmentCommunicator listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPreferencesManagement = new PreferencesManagement();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);
        mSelectedSymptomsList = new ArrayList<>();
        mSelectedSymptomsList = getArguments().getStringArrayList("symptoms");
        mListSize = getArguments().getInt("size");
        getDiseaseAndDoctorData();
        initView(view);
        getActivity().setTitle(getString(R.string.search_disease_results_activity_title));
        return view;

    }

    /**
     * Gets the disease and doctor data
     */
    private void getDiseaseAndDoctorData() {
        Log.d(TAG, "Inside getDiseaseAndDoctorData()");
        if (mDiseaseListTask != null) {
            Log.d(TAG, "Inside if, the Async task object is not null. Returning....");
            return;
        }
        if (mDiseaseDetailsArrayList == null) {
            mDiseaseDetailsArrayList = new ArrayList<>();
        }

        if (mDoctorDetailsArrayList == null) {
            mDoctorDetailsArrayList = new ArrayList<>();
        }

        Log.d(TAG, "Calling the Async task for fetching disease and doctor list.");
        mDiseaseListTask = new GetDiseaseAndDoctorTask();
        mDiseaseListTask.execute();
    }


    /**
     * Returns true if the application should support going back
     *
     * @return true if should navigate back , false otherwise
     */
    public boolean shouldGoBack() {

        return true;
    }

    /**
     * AsyncTask to get disease and doctor details
     */
    public class GetDiseaseAndDoctorTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Inside doInBackground(" + params + ")");
            try {
                String getDiseaseAndDoctorListURL = "http://skillab.in/medical_beta/catalog/user/get_desease_by_all_symtoms_api";
                URL url = new URL(getDiseaseAndDoctorListURL);
                JSONObject postDataParams = new JSONObject();
                if (mSelectedSymptomsList != null) {
                    StringBuilder finalString = new StringBuilder("'");
                    for (int i = 0; i < mSelectedSymptomsList.size(); i++) {
                        finalString.append(mSelectedSymptomsList.get(i));
                        if (i < mSelectedSymptomsList.size() - 1) {
                            finalString.append("','");
                        } else if (i == mSelectedSymptomsList.size() - 1) {
                            finalString.append("'");
                        }
                    }
                    postDataParams.put("symtoms", finalString.toString());
                }
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
            mDiseaseListTask = null;

            Log.d("result::", result);

            try {
                JSONObject symptomSearchResults = new JSONObject(result);
                String status = symptomSearchResults.getString("status");
                String message = symptomSearchResults.getString("message");

                if (status.contains("ok")) {
                    //replace with log
                    Log.d("Get search results:", message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                } else {
                    if (result.isEmpty() || status.contains("error"))
                        //replace with log
                        Log.d("Get results failure:", message);
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                ArrayList<String> localSymptoms = new ArrayList<>();
                JSONArray dataDiseaseArray = jsonObject.getJSONArray("data_deseases");

                for (int i = 0; i < dataDiseaseArray.length(); i++) {
                    JSONObject diseaseInfo = dataDiseaseArray.getJSONObject(i);
                    mDiseaseID = diseaseInfo.getString("disease_id");
                    mDiseaseName = diseaseInfo.getString("disease_name");
                    mDiseaseType = diseaseInfo.getString("type");

                    mDiseaseDetailsArrayList.add(new DiseaseDetails(mDiseaseID, mDiseaseName, mDiseaseType));
                    mDiseaseListAdapter.notifyDataSetChanged();


//                    Toast.makeText(getActivity(), "Disease details are: " + mDiseaseID + ", " + mDiseaseName + ", " + mDiseaseType + ".", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Disease details are: " + mDiseaseID + ", " + mDiseaseName + ", " + mDiseaseType + ".");
                }

                JSONArray dataDoctorArray = jsonObject.getJSONArray("data_doctors");
                for (int j = 0; j < dataDoctorArray.length(); j++) {
                    JSONObject doctorInfo = dataDoctorArray.getJSONObject(j);
                    mDoctorID = doctorInfo.getString("doctor_id");
                    mDoctorEmail = doctorInfo.getString("email");
                    mDocCategory = doctorInfo.getString("category");
                    mDocFullName = doctorInfo.getString("fullname");
                    mDocAddress = doctorInfo.getString("address");
                    mDocCity = doctorInfo.getString("city");
                    mDocMobile = doctorInfo.getString("mobile");
                    mDocHospitalName = doctorInfo.getString("hospital_name");
                    mDoctorDetailsArrayList.add(new DoctorDetails(mDoctorID, mDoctorEmail, mDocCategory, mDocFullName,
                            mDocAddress, mDocCity, mDocMobile, mDocHospitalName));
                    mDoctorListAdapter.notifyDataSetChanged();

//                    Toast.makeText(getActivity(), "Doctor details are: " + mDoctorID + ", " + mDoctorEmail + ", " + mDocCategory + ", " +
//                            mDocFullName + ", " + mDocAddress + ", " + mDocCity + ", " + mDocMobile + ", " + mDocHospitalName, Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Doctor details are: " + mDoctorID + ", " + mDoctorEmail + ", " + mDocCategory + ", " +
                            mDocFullName + ", " + mDocAddress + ", " + mDocCity + ", " + mDocMobile + ", " + mDocHospitalName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                buildErrorDialog(e);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mDiseaseListTask = null;
        }
    }

    /**
     * Builds a dialog to show error or exception
     *
     * @param e JSONException instance
     */
    private void buildErrorDialog(JSONException e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Error while obtaining disease and doctor results from the server")
                .setMessage(e.getMessage())
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });

        AlertDialog logoutDialog = builder.create();
        logoutDialog.show();
    }

    /**
     * Get the string for data to be posted to writer
     *
     * @param params parameters
     * @return string for data to be posted
     * @throws JSONException                thrown when Json data is corrupted
     * @throws UnsupportedEncodingException thrown if the encoding is not supported
     */
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

    /**
     * Initialize view for the fragment
     *
     * @param view view to be initialized
     */
    private void initView(View view) {
        mDiseaseListTextView = (TextView) view.findViewById(R.id.disease_list_tv);

        mDiseaseListView = (ListView) view.findViewById(R.id.disease_list);
        mDiseaseListAdapter = new DiseaseListAdapter(getContext(), mDiseaseDetailsArrayList);
        mDiseaseListView.setAdapter(mDiseaseListAdapter);

        mDoctorListTextView = (TextView) view.findViewById(R.id.doctor_list_tv);
        mDoctorListView = (ListView) view.findViewById(R.id.doctor_list);
        mDoctorListAdapter = new DoctorListAdapter(getContext(), mDoctorDetailsArrayList, SearchResultsFragment.this);
        mDoctorListView.setAdapter(mDoctorListAdapter);
    }

    /**
     * Sends appointment request to the doctor
     */
    public void sendAppointmentRequestToDoctor(String doctorID) {
        mAppointmentDoctorID = doctorID;
        if (mRequestAppointmentTask == null) {
            mRequestAppointmentTask = new RequestAppointmentTask();
            mRequestAppointmentTask.execute();
        }
    }

    /**
     * AsyncTask to request appointment to the doctor
     */
    public class RequestAppointmentTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Inside doInBackground(" + Arrays.toString(params) + ")");
            try {
                String requestDoctorAppointmentURL = "http://skillab.in/medical_beta/main/make_appoinment_by_patient";
                URL url = new URL(requestDoctorAppointmentURL);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("patient_id", mPreferencesManagement.getDataFromPreferences(getActivity(), getString(R.string.pref_user_id_key)));
                postDataParams.put("doctor_id", mAppointmentDoctorID);
                postDataParams.put("message_by_patient", "request");
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
            Log.d(TAG, "Inside onPostExecute(" + result + ")");
            super.onPostExecute(result);
            mRequestAppointmentTask = null;
//            Toast.makeText(getActivity(), "result received is :" + result, Toast.LENGTH_LONG).show();
            Log.d("result::", result);

            try {
                JSONObject getDoctorDetailsResult = new JSONObject(result);
                String status = getDoctorDetailsResult.getString("status");
                String message = getDoctorDetailsResult.getString("message");
                if (status.contains("success")) {
                    Log.d("Get Symptoms result: ", status);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                } else {
                    if (result.isEmpty() || status.contains("error"))
                        Log.d("Get Symptoms failure : ", status);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
//
//            JSONArray doctorListArray = getDoctorDetailsResult.getJSONArray("data");
//            for (int i = 0; i < doctorListArray.length(); i++) {
//                JSONObject doctorDetails = doctorListArray.getJSONObject(i);
//                mDoctorID = doctorDetails.getString("user_id");
//                String docFirstName = doctorDetails.getString("fname");
//                String docLastName = doctorDetails.getString("lname");
//                mDocFullName = docFirstName + " " + docLastName;
//                mDoctorEmail = doctorDetails.getString("email");
//                mDocHospitalName = doctorDetails.getString("s_name");
//                mDocCategory = doctorDetails.getString("category");
//                mDocAddress = doctorDetails.getString("address");
//                mDocCity = doctorDetails.getString("city");
//                mDocMobile = doctorDetails.getString("mobile");
//            }
//            if (mDoctorDetailsArrayList == null) {
//                mDoctorDetailsArrayList = new ArrayList<>();
//            }
//            mDoctorDetailsArrayList.add(new DoctorDetails(mDoctorID, mDoctorEmail, mDocCategory, mDocFullName, mDocAddress,
//                    mDocCity, mDocMobile, mDocHospitalName));
//            mDoctorListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mRequestAppointmentTask = null;
        }
    }
}

