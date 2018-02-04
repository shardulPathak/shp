package com.example.shardulpathak.shp_doctor.view_patient;

import android.content.Context;
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

import com.example.shardulpathak.shp_doctor.IFragmentCommunicator;
import com.example.shardulpathak.shp_doctor.R;
import com.example.shardulpathak.shp_doctor.view_disease.DiseaseFragment;

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
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link IFragmentCommunicator}
 * interface.
 */
public class PatientFragment extends Fragment {

    private static final String TAG = DiseaseFragment.class.getSimpleName();
    private TextView mViewPatientTextView;
    private ListView mViewPatientListView;
    private PatientListAdapter mViewPatientAdapter;

    private List<Patient> mPatientsList;

    private GetPatientsListTask mPatientListTask = null;


    private String mPatientId;
    private String mPatientName;
    private String mPatientAge;
    private String mPatientAddress;
    private String mPatientGender;
    private String mPatientContact;

    private IFragmentCommunicator mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PatientFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_list, container, false);
        getActivity().setTitle(R.string.title_view_patient);
        getPatientsList();
        initView(view);
        return view;
    }

    private void getPatientsList() {
        Log.d(TAG, "Inside getPatientsList()");
        if (mPatientListTask != null) {
            Log.d(TAG, "Inside if, the Async task object is not null. Returning....");
            return;
        }
        if (mPatientsList == null) {
            mPatientsList = new ArrayList<>();
        }

        Log.d(TAG, "Calling the Async task for fetching patient list.");
        mPatientListTask = new GetPatientsListTask();
        mPatientListTask.execute();
    }

    private void initView(View v) {
        mViewPatientTextView = (TextView) v.findViewById(R.id.view_patient_tv);
        mViewPatientListView = (ListView) v.findViewById(R.id.view_patient_list);
        mViewPatientAdapter = new PatientListAdapter(getActivity(), mPatientsList);
        mViewPatientListView.setAdapter(mViewPatientAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentCommunicator) {
            mListener = (IFragmentCommunicator) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListIFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public class GetPatientsListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Inside doInBackground(" + params + ")");
            try {

                String getPatientsListURL = "http://skillab.in/medical_beta/main/getPatientListAPI";
                URL url = new URL(getPatientsListURL);
                JSONObject postDataParams = new JSONObject();
//                postDataParams.put("symtoms", "'Joint Pain','cold'");
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
            mPatientListTask = null;

            Log.d("result::", result);
//            Toast.makeText(getActivity(), "Result obtained on view patient is: " + result, Toast.LENGTH_SHORT).show();

            try {
                JSONObject symptomSearchResults = new JSONObject(result);
                String status = symptomSearchResults.getString("status");

                if (status.contains("success")) {
                    //replace with log
                    Log.d("Get search results:", status);
                    Toast.makeText(getActivity(), status, Toast.LENGTH_LONG).show();
                } else {
                    if (result.isEmpty() || status.contains("error"))
                        //replace with log
                        Log.d("Get results failure:", status);
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                ArrayList<String> localSymptoms = new ArrayList<>();
                JSONArray dataPatientArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataPatientArray.length(); i++) {
                    JSONObject patientInfo = dataPatientArray.getJSONObject(i);
                    mPatientId = patientInfo.getString("user_id");
                    String fname = patientInfo.getString("fname");
                    String lname = patientInfo.getString("lname");
                    mPatientName = fname + " " + lname;
                    mPatientAge=patientInfo.getString("age");
                    mPatientAddress = patientInfo.getString("address");
                    mPatientGender = patientInfo.getString("gender");
                    mPatientContact = patientInfo.getString("mobile");

                    mPatientsList.add(new Patient(mPatientId, mPatientName, mPatientAge, mPatientAddress, mPatientGender, mPatientContact));
                    mViewPatientAdapter.notifyDataSetChanged();


//                    Toast.makeText(getActivity(), "Disease details are: " + mDiseaseID + ", " + mDiseaseName + ", " + mDiseaseType + ".", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Disease details are: " + mPatientId + ", " + mPatientName + ", " + mPatientAge + ".");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mPatientListTask = null;
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
