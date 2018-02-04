package com.example.shardulpathak.shp_patient.feedback;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shardulpathak.shp_patient.IFragmentCommunicator;
import com.example.shardulpathak.shp_patient.PreferencesManagement;
import com.example.shardulpathak.shp_patient.R;

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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IFragmentCommunicator} interface
 * to handle interaction events.
 */
public class FeedbackFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    PreferencesManagement mPreferencesManagement;
    private IFragmentCommunicator mListener;

    private String mFeedback;
    private final static String TAG = FeedbackFragment.class.getSimpleName();

    private SendFeedbackTask mAuthTask = null;
    EditText mFeedbackEditText;
    Button mSubmitFeedbackButton;

    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.feedback_title);
        setRetainInstance(true);
        mPreferencesManagement = new PreferencesManagement();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mFeedbackEditText = (EditText) view.findViewById(R.id.feedback_edit_text);
        mSubmitFeedbackButton = (Button) view.findViewById(R.id.submit_feedback_button);
        mSubmitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Submit feedback button clicked");
                sendFeedbackToAdmin();
                mFeedbackEditText.getText().clear();
            }
        });

    }

    private void sendFeedbackToAdmin() {
        mFeedback = mFeedbackEditText.getText().toString();
        if (mAuthTask != null) {
            return;
        }
        mAuthTask = new SendFeedbackTask();
        mAuthTask.execute();
    }


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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     *
     */
    public class SendFeedbackTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String userID = mPreferencesManagement.getDataFromPreferences(getActivity(), getString(R.string.pref_user_id_key));
                String patientFeedbackURL = "http://skillab.in/medical_beta/main/feedbackFormPostAPI";
                URL url = new URL(patientFeedbackURL);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", userID);
                postDataParams.put("message", mFeedback);
                Log.e("params", postDataParams.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoInput(true);
                connection.setDoOutput(true);

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
        protected void onCancelled(String s) {
            super.onCancelled(s);
            mAuthTask = null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mAuthTask = null;

            Log.d("result::", result);
            try {
                JSONObject jsonObject = new JSONObject(result);

                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");

                if (status.contains("success")) {
                    Log.d(TAG, "Feedback submitted successfully with result :" + message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                } else if (result.isEmpty() || status.contains("error")) {
                    Log.d(TAG, "Feedback submission failure with result :" + message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
}
