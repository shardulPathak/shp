package com.example.shardulpathak.shp_patient.search_disease;


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
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mDiseaseListTextView;
    private TextView mDoctorListTextView;
    private ListView mDiseaseListView;
    private ListView mDoctorListView;

    private DiseaseListAdapter mDiseaseListAdapter;
    private ArrayList<DiseaseDetails> mDiseaseDetailsArrayList;
    private DoctorListAdapter mDoctorListAdapter;
    private ArrayList<DoctorDetails> mDoctorDetailsArrayList;


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

    private static final String TAG = SearchResultsFragment.class.getSimpleName();


    private GetDiseaseAndDoctorTask mDiseaseListTask = null;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultsFragment newInstance(String param1, String param2) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);
        getDiseaseAndDoctorData();
        initView(view);
        getActivity().setTitle(getString(R.string.search_disease_results_activity_title));
        return view;

    }

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

    public class GetDiseaseAndDoctorTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Inside doInBackground(" + params + ")");
            try {

                String getDiseaseAndDoctorListURL = "http://skillab.in/medical_beta/catalog/user/get_desease_by_all_symtoms_api";
                URL url = new URL(getDiseaseAndDoctorListURL);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("symtoms", "'Joint Pain','cold'");
//                postDataParams.put("password", mPassword);
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

                    mDiseaseDetailsArrayList.add(new DiseaseDetails("1", "abcd", "heart"));
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
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mDiseaseListTask = null;
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

    private void initView(View view) {
        mDiseaseListTextView = (TextView) view.findViewById(R.id.disease_list_tv);

        mDiseaseListView = (ListView) view.findViewById(R.id.disease_list);
        mDiseaseListAdapter = new DiseaseListAdapter(getContext(), mDiseaseDetailsArrayList);
        mDiseaseListView.setAdapter(mDiseaseListAdapter);

        mDoctorListTextView = (TextView) view.findViewById(R.id.doctor_list_tv);
        mDoctorListView = (ListView) view.findViewById(R.id.doctor_list);
        mDoctorListAdapter = new DoctorListAdapter(getContext(), mDoctorDetailsArrayList);
        mDoctorListView.setAdapter(mDoctorListAdapter);
    }

}
