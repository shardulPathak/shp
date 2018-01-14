package com.example.shardulpathak.shp_doctor.view_details;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shardulpathak.shp_doctor.IFragmentCommunicator;
import com.example.shardulpathak.shp_doctor.R;

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
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {


    private static final String TAG = DetailsFragment.class.getSimpleName();
    private IFragmentCommunicator mListener;

    EditText mNameEditText;
    EditText mAddressEditText;
    EditText mContactEditText;
    EditText mEmailEditText;

    TextInputLayout mNameInputLayout;
    TextInputLayout mAddressInputLayout;
    TextInputLayout mContactInputLayout;
    TextInputLayout mEmailInputLayout;

    Button mEditButton;
    Button mCancelButton;

    private String mNameToBeSent;
    private String mAddressToBeSent;
    private String mContactToBeSent;
    private String mEmailToBeSent;


    private EditDetailsTask mEditDetailsTask = null;


    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.title_details_fragment);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mNameEditText = (EditText) view.findViewById(R.id.name_value);
        mAddressEditText = (EditText) view.findViewById(R.id.address_value);
        mContactEditText = (EditText) view.findViewById(R.id.contact_value);
        mEmailEditText = (EditText) view.findViewById(R.id.email_value);

        mNameInputLayout = (TextInputLayout) view.findViewById(R.id.name_view);
        mAddressInputLayout = (TextInputLayout) view.findViewById(R.id.address_view);
        mContactInputLayout = (TextInputLayout) view.findViewById(R.id.contact_view);
        mEmailInputLayout = (TextInputLayout) view.findViewById(R.id.email_view);

        mEditButton = (Button) view.findViewById(R.id.button_update);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = "";
                if (v instanceof Button) {
                    buttonText = ((Button) v).getText().toString();
                }
                if (buttonText.equals(getString(R.string.edit_details_edit_btn_text))) {
                    makeViewEditable();
                } else if (buttonText.equals(getString(R.string.edit_details_submit_btn_text))) {
                    getUpdatedValues();
                    sendUpdatedDetails();
                    makeViewNotEditable();
                }

            }


        });
        mCancelButton = (Button) view.findViewById(R.id.edit_details_cancel_btn);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeViewNotEditable();
            }
        });
    }

    private void getUpdatedValues() {
        mNameToBeSent = mNameEditText.getText().toString();
        mAddressToBeSent = mAddressEditText.getText().toString();
        mContactToBeSent = mContactEditText.getText().toString();
        mEmailToBeSent = mEmailEditText.getText().toString();
    }

    private void sendUpdatedDetails() {
        Log.d(TAG, "Inside sendUpdatedDetails()");
        if (mEditDetailsTask != null) {
            Log.d(TAG, "Inside if, the Async task object is not null. Returning....");
            return;
        }

        Log.d(TAG, "Calling the Async task for editing the doctor details");
        mEditDetailsTask = new EditDetailsTask();
        mEditDetailsTask.execute();
    }

    private void makeViewNotEditable() {
        mNameEditText.setEnabled(false);
        mAddressEditText.setEnabled(false);
        mContactEditText.setEnabled(false);
        mEmailEditText.setEnabled(false);

        mNameInputLayout.setHintEnabled(false);
        mAddressInputLayout.setHintEnabled(false);
        mContactInputLayout.setHintEnabled(false);
        mEmailInputLayout.setHintEnabled(false);


        mCancelButton.setVisibility(View.GONE);
        mEditButton.setText(getString(R.string.edit_details_edit_btn_text));
    }

    private void makeViewEditable() {

        mNameEditText.setEnabled(true);
        mAddressEditText.setEnabled(true);
        mContactEditText.setEnabled(true);
        mEmailEditText.setEnabled(true);
        mNameInputLayout.setHintEnabled(true);
        mAddressInputLayout.setHintEnabled(true);
        mContactInputLayout.setHintEnabled(true);
        mEmailInputLayout.setHintEnabled(true);

        mCancelButton.setVisibility(View.VISIBLE);
        mEditButton.setText(getString(R.string.edit_details_submit_btn_text));
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


    public class EditDetailsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Inside doInBackground(" + params + ")");
            try {

                String editDetailsURL = "http://skillab.in/medical_beta/catalog/user/user_doctor_register_api_edit";
                URL url = new URL(editDetailsURL);
                JSONObject postDataParams = new JSONObject();

                postDataParams.put("fname", mNameToBeSent);
                postDataParams.put("address", mAddressToBeSent);
                postDataParams.put("mobile", mContactToBeSent);
                postDataParams.put("email", mEmailToBeSent);

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
            mEditDetailsTask = null;

            Log.d("result::", result);
//            Toast.makeText(getActivity(), "Result obtained on view disease is: " + result, Toast.LENGTH_SHORT).show();

            try {
                JSONObject symptomSearchResults = new JSONObject(result);
                String status = symptomSearchResults.getString("status");
                String message = symptomSearchResults.getString("msg");

                if (status.contains("success")) {
                    //replace with log
                    Log.d("Edit details results:", message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                } else {
                    if (result.isEmpty() || status.contains("error"))
                        //replace with log
                        Log.d("Edit details failure:", message);
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mEditDetailsTask = null;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", mNameToBeSent);
        outState.putString("address", mAddressToBeSent);
        outState.putString("contact", mContactToBeSent);
        outState.putString("email", mEmailToBeSent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString("name");
            String address = savedInstanceState.getString("address");
            String contact = savedInstanceState.getString("contact");
            String email = savedInstanceState.getString("email");
            mNameEditText.setText(name);
            mAddressEditText.setText(address);
            mContactEditText.setText(contact);
            mEmailEditText.setText(email);
        }
    }
}
