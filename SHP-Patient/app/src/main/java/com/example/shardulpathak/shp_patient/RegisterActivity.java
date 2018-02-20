package com.example.shardulpathak.shp_patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    Button mRegisterNextButton;
    TextView mRegisterYourselfTextView;
    TextView mUserIdTextView;
    TextView mGenderTextView;

    EditText mUserIdValueEditText;
    EditText mNameEditText;
    EditText mAgeEditText;
    EditText mAddressEditText;

    RadioGroup mGenderRadioGroup;
    RadioButton mMaleRadioButton;
    RadioButton mFemaleRadioButton;
    RadioButton mSelectedRadioButton;

    String mId;
    String mName;
    String mAge;
    String mAddress;

    int mSelectedGenderId;
    String mGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.register_patient_title);
        initView();
    }

    private void initView() {
        mRegisterNextButton = (Button) findViewById(R.id.register_next_button);
        mRegisterNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId = mUserIdValueEditText.getText().toString();
                mName = mNameEditText.getText().toString();
                mAge = mAgeEditText.getText().toString();
                mAddress = mAddressEditText.getText().toString();
                mSelectedGenderId = mGenderRadioGroup.getCheckedRadioButtonId();
                mSelectedRadioButton = (RadioButton) findViewById(mSelectedGenderId);
                mGender = mSelectedRadioButton.getText().toString();
                attemptNextClick();

            }
        });
        mRegisterYourselfTextView = (TextView) findViewById(R.id.register_title);
        mUserIdTextView = (TextView) findViewById(R.id.patient_id);
        mGenderTextView = (TextView) findViewById(R.id.patient_gender);

        mUserIdValueEditText = (EditText) findViewById(R.id.patient_id_value);
        mNameEditText = (EditText) findViewById(R.id.patient_name_value);
        mAgeEditText = (EditText) findViewById(R.id.patient_age_value);
        mAddressEditText = (EditText) findViewById(R.id.patient_address_value);

        mGenderRadioGroup = (RadioGroup) findViewById(R.id.patient_gender_value_group);
        mMaleRadioButton = (RadioButton) findViewById(R.id.male_gender_button);
        mFemaleRadioButton = (RadioButton) findViewById(R.id.female_gender_button);
    }

    private void attemptNextClick() {
        performAllChecks();
    }

    private void performAllChecks() {
        // Reset errors.
        mUserIdValueEditText.setError(null);
        mNameEditText.setError(null);
        mAgeEditText.setError(null);
        mAddressEditText.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mId)) {
            mUserIdValueEditText.setError("The User ID field is empty");
            focusView = mUserIdValueEditText;
            cancel = true;
        }

        if (!isUserIDValid(mId)) {
//            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            mUserIdValueEditText.setError("The User ID is invalid");
            focusView = mUserIdValueEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(mName)) {
            mNameEditText.setError("The name field is empty");
            focusView = mNameEditText;
            cancel = true;
        }

        if (!isUserNameValid(mName)) {
//            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            mNameEditText.setError("The name is invalid");
            focusView = mNameEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(mAge)||mAge.equals("")) {
            mAgeEditText.setError("The age field is empty");
            focusView = mAgeEditText;
            cancel = true;
        }

        if (!isUserAgeValid(mAge)) {
//            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            mAgeEditText.setError("The age is invalid");
            focusView = mAgeEditText;
            cancel = true;
        }


        if (TextUtils.isEmpty(mAddress)) {
            mAddressEditText.setError("The address field is empty");
            focusView = mAddressEditText;
            cancel = true;
        }

        if (!isUserAddressValid(mAddress)) {
//            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            mAddressEditText.setError("The address is invalid");
            focusView = mAddressEditText;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //send values to the next activity using intent
            sendValues();
        }
    }

    private boolean isUserAddressValid(String address) {
        return address.length() <= 25;
    }

    private boolean isUserAgeValid(String age) {
        if (age.equals("")){
            mAgeEditText.setError("The age field is empty");
            return false;
        }
        return age.length() <= 3 && Integer.parseInt(age) <= 100;
    }

    private boolean isUserNameValid(String name) {
        return name.length() < 25;
    }

    private boolean isUserIDValid(String id) {
        return id.length() < 20;
    }

    private void sendValues() {
        Intent registerNextIntent = new Intent(this, SecondRegisterActivity.class);
        registerNextIntent.putExtra("userId", mId);
        registerNextIntent.putExtra("userName", mName);
        registerNextIntent.putExtra("userAge", mAge);
        registerNextIntent.putExtra("userAddress", mAddress);
        registerNextIntent.putExtra("userGender", mGender);
        startActivity(registerNextIntent);
    }


    private void generatePatientID() {
        Double my = Math.random();
        int i = my.intValue();
    }
}
