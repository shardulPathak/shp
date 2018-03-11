package com.example.shardulpathak.shp_doctor.appointment;


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
import com.example.shardulpathak.shp_doctor.PreferencesManagement;
import com.example.shardulpathak.shp_doctor.R;

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
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MyAppointmentsFragment extends Fragment {
    private TextView mAppointmentInfoView;
    private TextView mNoAppointmentsView;
    private ListView mAppointmentListView;
    private List<Appointment> mAppointmentsList;
    private AppointmentListAdapter mAppointmentListAdapter;
    private IFragmentCommunicator mListener;

    private AppointmentRequestTask mAppointmentRequestTask = null;
    private DoctorResponseTask mDoctorResponseTask = null;
    private PreferencesManagement mPreferencesManagement;
    private static final String TAG = MyAppointmentsFragment.class.getSimpleName();

    private String mAppointmentID;
    private String mDoctorID;
    private String mPatientID;


    private String mPatientMessage;
    private String mDoctorMessage;

    private int mIsAccepted;
    private String mResponseByDoctor;
    private String mRespondedAppointmentID;

    public MyAppointmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_my_appointments);
        mPreferencesManagement = new PreferencesManagement();
        checkForAppointmentRequests();
        setRetainInstance(true);
    }

    private void checkForAppointmentRequests() {
        if (mAppointmentRequestTask == null) {
            mAppointmentRequestTask = new AppointmentRequestTask();
            mAppointmentRequestTask.execute();
        }
    }

    private void initView(View view) {
        mAppointmentInfoView = (TextView) view.findViewById(R.id.appointment_info_header);
        mNoAppointmentsView = (TextView) view.findViewById(R.id.no_appointment_view);
        mAppointmentListView = (ListView) view.findViewById(R.id.appointment_list);
        mAppointmentListAdapter = new AppointmentListAdapter(getActivity(), mAppointmentsList, MyAppointmentsFragment.this);
        mAppointmentListView.setAdapter(mAppointmentListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_appointments, container, false);
        mAppointmentsList = new ArrayList<>();
        mAppointmentsList.add(new Appointment("1", "10/10/10", "10:11", "Mohan"));
        initView(view);
        return view;
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentCommunicator) {
            mListener = (IFragmentCommunicator) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IFragmentCommunicator");
        }
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     *
     */
    public class AppointmentRequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Inside doInBackground(" + params + ")");
            try {
                String getAppointmentRequests = "http://skillab.in/medical_beta/main/show_appoinment_to_doctor";
                URL url = new URL(getAppointmentRequests);
                JSONObject postDataParams = new JSONObject();
//                postDataParams.put("doctor_id", mPreferencesManagement.getDataFromPreferences(getActivity(), getString(R.string.pref_user_id_key)));
                postDataParams.put("doctor_id", "50");
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
            mAppointmentRequestTask = null;
//            Toast.makeText(getActivity(), "result received is :" + result, Toast.LENGTH_LONG).show();
            Log.d("result::", result);

            try {
                JSONObject getAppointmentRequestResults = new JSONObject(result);
                String status = getAppointmentRequestResults.getString("status");
                if (status.contains("success")) {
                    Log.d("Get Symptoms result: ", status);
                    Toast.makeText(getActivity(), status, Toast.LENGTH_LONG).show();
                    mNoAppointmentsView.setVisibility(View.GONE);
                    mAppointmentListView.setVisibility(View.VISIBLE);
                } else {
                    if (result.isEmpty() || status.contains("error"))
                        Log.d("Get Symptoms failure : ", status);
                    Toast.makeText(getActivity(), status, Toast.LENGTH_LONG).show();
                }

                JSONArray appointmentArray = getAppointmentRequestResults.getJSONArray("data");
                for (int i = 0; i < appointmentArray.length(); i++) {
                    JSONObject appointmentDetails = appointmentArray.getJSONObject(i);

                    mAppointmentID = appointmentDetails.getString("id");
                    mDoctorID = appointmentDetails.getString("doctor_id");
                    mPatientID = appointmentDetails.getString("patient_id");
                    mPatientMessage = appointmentDetails.getString("message_by_patient");
                    mDoctorMessage = appointmentDetails.getString("message_by_doctor");
                    mAppointmentsList.add(new Appointment(mAppointmentID, "", "", mPatientID));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mAppointmentRequestTask = null;
        }
    }

    /**
     * @param params
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
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

    public void sendDoctorResponse(boolean isAccepted, String respondedAppointmentID, String appointmentDate, String appointmentTime) {
        if (isAccepted) {
            mIsAccepted = 1;
            mRespondedAppointmentID = respondedAppointmentID;
            mResponseByDoctor = "appointmentDate: " + appointmentDate + " appointmentTime: " + appointmentTime;
        } else {
            mIsAccepted = 0;
            mAppointmentListAdapter.notifyDataSetChanged();
            mRespondedAppointmentID = respondedAppointmentID;
            mResponseByDoctor = "Denied";
        }
        if (mDoctorResponseTask == null) {
            mDoctorResponseTask = new DoctorResponseTask();
            mDoctorResponseTask.execute();
        }
    }


    /**
     *
     */
    public class DoctorResponseTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Inside doInBackground(" + params + ")");
            try {
                String getAppointmentRequests = "http://skillab.in/medical_beta/main/response_by_doctor";
                URL url = new URL(getAppointmentRequests);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("id", mRespondedAppointmentID);
                postDataParams.put("doctor_id", mDoctorID);
                postDataParams.put("is_accepted_by_doctor", mIsAccepted);
                postDataParams.put("message_by_doctor", mResponseByDoctor);
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
            mDoctorResponseTask = null;
//            Toast.makeText(getActivity(), "result received is :" + result, Toast.LENGTH_LONG).show();
            Log.d("result::", result);

            try {
                JSONObject getAppointmentRequestResults = new JSONObject(result);
                String status = getAppointmentRequestResults.getString("status");
                String message = getAppointmentRequestResults.getString("message");
                if (status.contains("success")) {
                    Log.d("Get Symptoms result: ", status);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    mNoAppointmentsView.setVisibility(View.GONE);
                    mAppointmentListView.setVisibility(View.VISIBLE);
                } else {
                    if (result.isEmpty() || status.contains("error"))
                        Log.d("Get Symptoms failure : ", status);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mDoctorResponseTask = null;
        }
    }
}
