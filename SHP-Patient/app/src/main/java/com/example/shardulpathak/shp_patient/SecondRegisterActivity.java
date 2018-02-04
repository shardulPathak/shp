package com.example.shardulpathak.shp_patient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SecondRegisterActivity extends AppCompatActivity {

    private Button mRegisterButton;
    EditText mContactEditText;
    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mConfirmPasswordEditText;


    String mId;
    String mName;
    String mAge;
    String mAddress;
    String mGender;
    String mContact;
    String mEmail;
    String mPassword;
    String mConfirmPassword;

    private Map<String, String> mValueMap;

    private static final String TAG = SecondRegisterActivity.class.getSimpleName();

    private UserRegistrationTask mAuthTask = null;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContact = mContactEditText.getText().toString();
            mEmail = mEmailEditText.getText().toString();
            mPassword = mPasswordEditText.getText().toString();
            mConfirmPassword = mConfirmPasswordEditText.getText().toString();

            attemptRegistration();


        }
    };

    private void attemptRegistration() {
        performAllChecks();
    }

    private void performAllChecks() {
        // Reset errors.
        mContactEditText.setError(null);
        mEmailEditText.setError(null);
        mPasswordEditText.setError(null);
        mConfirmPasswordEditText.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mContact)) {
            mContactEditText.setError("The contact field is empty");
            focusView = mContactEditText;
            cancel = true;
        }

        if (!isContactValid(mContact)) {
//            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            mContactEditText.setError("The contact field should have ten values");
            focusView = mContactEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(mEmail)) {
            mEmailEditText.setError("The email field is empty");
            focusView = mEmailEditText;
            cancel = true;
        }

        if (!isEmailValid(mEmail)) {
//            Toast.makeText(getApplicationContext(), "The email id is not valid", Toast.LENGTH_SHORT).show();
            mEmailEditText.setError("The email id is not valid");
            focusView = mEmailEditText;
            cancel = true;
        }


        if (TextUtils.isEmpty(mPassword)) {
            mPasswordEditText.setError("The password field is empty");
            focusView = mPasswordEditText;
            cancel = true;
        }

        if (!isPasswordValid(mPassword)) {
//            Toast.makeText(getApplicationContext(), "The email id is not valid", Toast.LENGTH_SHORT).show();
            mPasswordEditText.setError("The password is not valid");
            focusView = mPasswordEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(mConfirmPassword)) {
            mConfirmPasswordEditText.setError("The confirm password field is empty");
            focusView = mConfirmPasswordEditText;
            cancel = true;
        }

        //check if password and confirm password match
        if (!checkPasswordMatches()) {
            Toast.makeText(getApplicationContext(), "The passwords didn't match", Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //send values to server
            sendValues();
        }

    }

    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".com");
    }

    private boolean isContactValid(String contact) {
        return contact.length() == 10;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_register);
        initView();
        getOtherRegistrationValues();
    }

    /**
     * Gets other registration values from register activity through intent
     */
    private void getOtherRegistrationValues() {
        Intent receiveRegistrationValuesIntent = getIntent();
        mId = receiveRegistrationValuesIntent.getStringExtra("userId");
        mName = receiveRegistrationValuesIntent.getStringExtra("userName");
        mAge = receiveRegistrationValuesIntent.getStringExtra("userAge");
        mAddress = receiveRegistrationValuesIntent.getStringExtra("userAddress");
        mGender = receiveRegistrationValuesIntent.getStringExtra("userGender");
    }

    private void initView() {
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(mOnClickListener);

        mContactEditText = (EditText) findViewById(R.id.patient_contact_value);
        mEmailEditText = (EditText) findViewById(R.id.patient_email_value);
        mPasswordEditText = (EditText) findViewById(R.id.patient_password_value);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.patient_password_confirm_value);
    }

    private boolean checkPasswordMatches() {
        return mPassword.equals(mConfirmPassword);
    }

    private void sendValues() {
        mValueMap = new HashMap<>();
        mValueMap.put("id", mId);
        mValueMap.put("name", mName);
        mValueMap.put("age", mAge);
        mValueMap.put("address", mAddress);
        mValueMap.put("gender", mGender);
        mValueMap.put("contact", mContact);
        mValueMap.put("email", mEmail);
        mValueMap.put("password", mPassword);
        mValueMap.put("confirmpassword", mConfirmPassword);

//        mValueList.add(mId);
//        mValueList.add(mName);
//        mValueList.add(mAge);
//        mValueList.add(mAddress);
//        mValueList.add(mGender);
//        mValueList.add(mContact);
//        mValueList.add(mEmail);
//        mValueList.add(mPassword);
//        mValueList.add(mConfirmPassword);
        mAuthTask = new UserRegistrationTask(mValueMap);
        mAuthTask.execute();
    }


    private class UserRegistrationTask extends AsyncTask<Map<String, String>, Void, String> {

        Map<String, String> mMap;

        UserRegistrationTask(Map<String, String> valueMap) {
            mMap = valueMap;
        }

        @SafeVarargs
        @Override
        protected final String doInBackground(Map<String, String>... params) {
            try {
//                mHandler = new HttpHandler(mLoginURL);
                String registrationURL = "http://skillab.in/medical_beta/catalog/user/user_patient_register_api";
                URL url = new URL(registrationURL);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", mMap.get("id"));
                postDataParams.put("password", mMap.get("password"));
                postDataParams.put("confirm_password", mMap.get("confirmpassword"));
                postDataParams.put("fname", mMap.get("name"));
                postDataParams.put("age", mMap.get("age"));
                postDataParams.put("email", mMap.get("email"));
                postDataParams.put("gender", mMap.get("gender"));
                postDataParams.put("mobile", mMap.get("contact"));
                postDataParams.put("address", mMap.get("address"));
                postDataParams.put("user_role", mMap.get("3"));


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

                    StringBuilder sb = new StringBuilder("");
                    String line;
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
        protected void onPostExecute(String result) {
            Log.d(TAG, "Inside of onPostExecute(" + result + ")");
            super.onPostExecute(result);
//            Toast.makeText(SecondRegisterActivity.this, result, Toast.LENGTH_LONG).show();

            Log.d("result::", result);

            try {
                JSONObject registrationResult = new JSONObject(result);

                String status = registrationResult.getString("status");


                if (status.contains("success")) {
                    String message = registrationResult.getString("msg");
                    Log.d("Registration successful", message);
                    Toast.makeText(SecondRegisterActivity.this, message, Toast.LENGTH_LONG).show();
                    finish();
                    goToLoginActivityWithEmail();
                } else {
                    if (result.isEmpty() || status.contains("error")) {
                        String error = registrationResult.getString("errors");
                        Log.d("Registration error: ", error);
                        Toast.makeText(SecondRegisterActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void goToLoginActivityWithEmail() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.putExtra("email", mEmail);
        startActivity(loginIntent);
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


