package com.example.shardulpathak.shp_patient.search_disease;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shardulpathak.shp_patient.IFragmentCommunicator;
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
import java.util.List;

import static android.R.layout.simple_spinner_item;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IFragmentCommunicator} interface
 * to handle interaction events.
 */
public class SearchDiseaseFragment extends Fragment implements AdapterView.OnItemSelectedListener, OnClickListener {

    private String TAG = SearchDiseaseFragment.class.getSimpleName();

    private IFragmentCommunicator mListener;

    private GetSymptomsTask mAuthTask = null;


    List<String> mSymptomSpinnerList;

    ArrayAdapter<String> mSymptomAdapter;

    String mSelectedSymptom;
    String mFirstSymptom;

    TextView mFragmentTitle;
    TextView mFragmentDetails;
    EditText mFirstSymptomEditText;

    Spinner mSymptomSpinner;
    Button mNextButton;
    Button mDoneButton;
    OnClickListener mOnNextButtonClickedListener;
    TextView mSelectedSymptomChipView;
    OnClickListener mOnChipDeleteSelectedListener;


    public SearchDiseaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.search_disease_title);
    }

    private void addItemsToList() {
        mSymptomSpinnerList = new ArrayList<>();
        mSymptomSpinnerList.add(getActivity().getString(R.string.search_disease_symptom_spinner_prompt));
        mSymptomSpinnerList.add("");
        mSymptomSpinnerList.add("");
        mSymptomSpinnerList.add("");
        mSymptomSpinnerList.add("");
        mSymptomSpinnerList.add("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_disease, container, false);
        addItemsToList();
        initView(view);
        return view;
    }

    private void initView(View view) {

        mFragmentTitle = (TextView) view.findViewById(R.id.search_disease_tv);
        mFragmentDetails = (TextView) view.findViewById(R.id.other_symptom_question_tv);

        mFirstSymptomEditText = (EditText) view.findViewById(R.id.first_symptom_et);

        mSymptomSpinner = (Spinner) view.findViewById(R.id.symptom_spinner);
        mSymptomSpinner.setOnItemSelectedListener(this);
        mSymptomAdapter = new ArrayAdapter<>(getActivity(), simple_spinner_item, mSymptomSpinnerList);
        mSymptomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSymptomSpinner.setAdapter(mSymptomAdapter);

        mNextButton = (Button) view.findViewById(R.id.select_symptom_next_button);
        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Next button clicked");
                mFirstSymptom = mFirstSymptomEditText.getText().toString();
                handleFirstNextButtonClick();
                // send the entered value to api, get other values from api, set them to spinner,
                sendAndGetOtherValues();
            }
        });

        mDoneButton = (Button) view.findViewById(R.id.submit_button);
        mDoneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Done button clicked");
                Toast.makeText(getActivity(), "Done button clicked", Toast.LENGTH_SHORT).show();
                //send all symptoms to api, get the disease and the doctors list
                sendAllSymptomsToResultsFragment();

                SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
                openFragment(searchResultsFragment);

            }
        });

        mSelectedSymptomChipView = (TextView) view.findViewById(R.id.selected_symptoms_chip_view);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.detail_fragment, fragment);
        fragmentTransaction.commit();
    }

    private void sendAllSymptomsToResultsFragment() {

    }

    private void sendAndGetOtherValues() {
        Log.d(TAG, "Inside sendAndGetOtherValues()");
        if (mAuthTask != null) {
            Log.d(TAG, "Inside if, the Async task object is not null. Returning....");
            return;
        }

        String getOtherSymptomsURL = "http://skillab.in/medical_beta/catalog/user/get_symptoms_api";
        Log.d(TAG, "Calling the Async task for fetching other symptoms list");
        mAuthTask = new GetSymptomsTask();
        mAuthTask.execute();
    }


    public class GetSymptomsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Inside doInBackground(" + params + ")");
            try {

                String getOtherSymptomsURL = "http://skillab.in/medical_beta/catalog/user/get_symtoms_api";
                URL url = new URL(getOtherSymptomsURL);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("symtom", "Joint Pain");
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
            Log.d(TAG, "Inside onPostExecute(" + result + ")");
            super.onPostExecute(result);
            mAuthTask = null;

            Log.d("result::", result);
            if (result.contains("ok")) {
                //replace with log
                Toast.makeText(getActivity(), "Got other response", Toast.LENGTH_LONG).show();
            } else {
                if (result.isEmpty() || result.contains("error"))
                    //replace with log
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                ArrayList<String> localSymptoms = new ArrayList<>();
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject symptomInfo = dataArray.getJSONObject(i);
                    localSymptoms.add(symptomInfo.getString("symtom_name"));
//                    Toast.makeText(getActivity(), "Other symptoms obtained are: " + localSymptoms, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Other symptoms obtained are: " + localSymptoms);
                }
                mSymptomSpinnerList = localSymptoms;
                mSymptomAdapter.clear();
                mSymptomAdapter.addAll(localSymptoms);
                mSymptomAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mAuthTask = null;
        }
    }


    private void handleFirstNextButtonClick() {
        mSelectedSymptomChipView.setVisibility(View.VISIBLE);
        mSelectedSymptomChipView.append(mFirstSymptom);
        mFirstSymptomEditText.setText("");
        mFirstSymptomEditText.setHint("Please select other symptoms from the spinner now.");
        mFirstSymptomEditText.setEnabled(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentCommunicator) {
            mListener = (IFragmentCommunicator) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedSymptom = parent.getItemAtPosition(position).toString();
        if (!mSelectedSymptom.equals(getActivity().getString(R.string.search_disease_symptom_spinner_prompt))) {
            Log.d(TAG, "Selected symptom from the symptom spinner is: " + mSelectedSymptom);
            mSelectedSymptomChipView.setVisibility(View.VISIBLE);
//            mSelectedSymptomChipView.append(mSelectedSymptom);
            Toast.makeText(getActivity(), "Selected the " + mSelectedSymptom + " symptom from the list", Toast.LENGTH_SHORT).show();
            makeAChip(mSelectedSymptom);
        } else {
            Log.d(TAG, "No symptom selected from the symptom spinner");
            Toast.makeText(getActivity(), "Please select a symptom from the list", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeAChip(String label) {
//        int marginLeft = mSelectedSymptomChipView.getLeft();
//        int marginRight = mSelectedSymptomChipView.getRight();
//        int marginTop = mSelectedSymptomChipView.getTop();
//        int marginBottom = mSelectedSymptomChipView.getBottom();
//        ChipView chipView = new ChipView(getActivity());
//        chipView.setLabel(label);
//        chipView.setLeft(marginLeft + 15);
//        chipView.setRight(marginRight - 15);
//        chipView.setTop(marginTop);
//        chipView.setBottom(marginBottom);
//        mSelectedSymptomChipView.addView(chipView);
        mSelectedSymptomChipView.append(", " + label);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(), "No item selected from the symptom dropdown", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

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
