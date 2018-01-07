package com.example.shardulpathak.shp_patient.search_doctor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shardulpathak.shp_patient.IFragmentCommunicator;
import com.example.shardulpathak.shp_patient.R;
import com.example.shardulpathak.shp_patient.search_disease.DoctorDetails;
import com.example.shardulpathak.shp_patient.search_disease.DoctorListAdapter;

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

import static android.R.layout.simple_spinner_dropdown_item;
import static android.R.layout.simple_spinner_item;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IFragmentCommunicator} interface
 * to handle interaction events.
 */
public class SearchDoctorFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private final static String TAG = SearchDoctorFragment.class.getSimpleName();
    private IFragmentCommunicator mListener;
    private TextView mSearchDoctorTitle;

    private Spinner mSearchDoctorSpinner;
    private ArrayAdapter<String> mSearchDoctorAdapter;

    private EditText mSelectedOptionValue;
    TextInputLayout mSelectedOptionValueLayout;

    Button mSearchDoctorButton;
    ListView mSearchResultsList;

    private DoctorListAdapter mDoctorListAdapter;

    private List<String> mDoctorSearchOptions;
    private ArrayList<DoctorDetails> mDoctorDetailsArrayList;
    private String mSelectedOption;


    private GetDoctorSearchResultsTask mAuthTask = null;

    public SearchDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.search_doctor_title);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_doctor, container, false);
        addOptionsToList();
        initView(view);
        return view;
    }

    private void initView(View v) {
        mSearchDoctorTitle = (TextView) v.findViewById(R.id.search_doctor_tv);


        mSearchDoctorSpinner = (Spinner) v.findViewById(R.id.search_doctor_spinner);
        mSearchDoctorSpinner.setOnItemSelectedListener(this);

        mSearchDoctorAdapter = new ArrayAdapter<>(getActivity(), simple_spinner_item, mDoctorSearchOptions);
        mSearchDoctorAdapter.setDropDownViewResource(simple_spinner_dropdown_item);
        mSearchDoctorSpinner.setAdapter(mSearchDoctorAdapter);

        mSelectedOptionValueLayout = (TextInputLayout) v.findViewById(R.id.selected_option_view);
        mSelectedOptionValue = (EditText) v.findViewById(R.id.selected_option_value);

        mSearchDoctorButton = (Button) v.findViewById(R.id.search_doc_submit_btn);
        mSearchDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Search Doctor button clicked", Toast.LENGTH_SHORT).show();

                attemptDoctorSearch();
            }
        });

        addSearchResultsToList();
        mSearchResultsList = (ListView) v.findViewById(R.id.search_doc_results_list);
        mDoctorListAdapter = new DoctorListAdapter(getContext(), mDoctorDetailsArrayList);
        mSearchResultsList.setAdapter(mDoctorListAdapter);

    }

    private void attemptDoctorSearch() {

        Log.d(TAG, "Inside attemptDoctorSearch()");
        if (mAuthTask != null) {
            Log.d(TAG, "Inside if, the Async task object is not null. Returning....");
            return;
        }

        String getOtherSymptomsURL = "http://skillab.in/medical_beta/catalog/user/get_symptoms_api";
        Log.d(TAG, "Calling the Async task for fetching other symptoms list");
        mAuthTask = new GetDoctorSearchResultsTask();
        mAuthTask.execute();
    }

    private void addSearchResultsToList() {
        mDoctorDetailsArrayList = new ArrayList<>();
        mDoctorDetailsArrayList.add(new DoctorDetails("1", "abcd@gmail.com", "General Physician", "Ramesh Mahajan", "Sitabuildi", "Nagpur", "9876548798", "Mahatme Hospital"));
    }

    private void addOptionsToList() {
        mDoctorSearchOptions = new ArrayList<>();
        mDoctorSearchOptions.add(getString(R.string.search_doc_opt_type_name));
        mDoctorSearchOptions.add(getString(R.string.search_doc_opt_type_address));
        mDoctorSearchOptions.add(getString(R.string.search_doc_opt_type_category));
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
        mSelectedOption = parent.getItemAtPosition(position).toString();
        Log.d(TAG, "Selected option from spinner as: " + mSelectedOption);
        Log.d(TAG, "Setting the value in the enter text edit text as: " + mSelectedOption);
        mSelectedOptionValueLayout.setHint("Enter " + mSelectedOption);
        mSelectedOptionValue.getText().clear();
        Toast.makeText(getActivity(), "Searching doctors by: " + mSelectedOption, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "Nothing selected from the spinner");
    }


    public class GetDoctorSearchResultsTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Inside doInBackground(" + params + ")");
            try {

                String getOtherSymptomsURL = "";
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

            try {
                JSONObject getSymptomsResults = new JSONObject(result);

                String status = getSymptomsResults.getString("status");
                String message = getSymptomsResults.getString("message");
                if (status.contains("ok")) {
                    Log.d("Get Symptoms result: ", message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                } else {
                    if (result.isEmpty() || status.contains("error"))
                        Log.d("Get Symptoms failure : ", message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                ArrayList<String> localSymptoms = new ArrayList<>();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mAuthTask = null;
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
}
