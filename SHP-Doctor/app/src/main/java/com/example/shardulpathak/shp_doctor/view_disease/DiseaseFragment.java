package com.example.shardulpathak.shp_doctor.view_disease;

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
public class DiseaseFragment extends Fragment {

    private static final String TAG = DiseaseFragment.class.getSimpleName();
    private TextView mViewDiseaseTextView;
    private ListView mViewDiseaseListView;
    private DiseaseListAdapter mViewDiseaseAdapter;

    private List<Disease> mDiseasesList;
    private IFragmentCommunicator mListener;


    private GetDiseasesListTask mDiseaseListTask = null;
    private String mDiseaseID;
    private String mDiseaseName;
    private String mDiseaseType;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DiseaseFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_view_disease);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_list, container, false);
        getDiseasesList();
        initView(view);
        return view;
    }

    private void initView(View view) {
        mViewDiseaseTextView = (TextView) view.findViewById(R.id.view_disease_tv);
        mViewDiseaseListView = (ListView) view.findViewById(R.id.view_disease_list);
        mViewDiseaseAdapter = new DiseaseListAdapter(getActivity(), mDiseasesList);
        mViewDiseaseListView.setAdapter(mViewDiseaseAdapter);
    }


    private void getDiseasesList() {
        Log.d(TAG, "Inside getDiseasesList()");
        if (mDiseaseListTask != null) {
            Log.d(TAG, "Inside if, the Async task object is not null. Returning....");
            return;
        }
        if (mDiseasesList == null) {
            mDiseasesList = new ArrayList<>();
        }

        Log.d(TAG, "Calling the Async task for fetching disease list.");
        mDiseaseListTask = new GetDiseasesListTask();
        mDiseaseListTask.execute();
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


    public class GetDiseasesListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Log.d(TAG, "Inside doInBackground(" + params + ")");
            try {

                String getDiseasesListURL = "http://skillab.in/medical_beta/main/getDiseaseAPI";
                URL url = new URL(getDiseasesListURL);
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
            mDiseaseListTask = null;

            Log.d("result::", result);
//            Toast.makeText(getActivity(), "Result obtained on view disease is: " + result, Toast.LENGTH_SHORT).show();

            try {
                JSONObject symptomSearchResults = new JSONObject(result);
                String status = symptomSearchResults.getString("status");

                if (status.contains("success")) {
                    //replace with log
                    Log.d("Get disease results:", status);
                    Toast.makeText(getActivity(), status, Toast.LENGTH_LONG).show();
                } else {
                    if (result.isEmpty() || status.contains("error"))
                        //replace with log
                        Log.d("disease result failure:", status);
                    Toast.makeText(getActivity(), status, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                ArrayList<String> localSymptoms = new ArrayList<>();
                JSONArray dataDiseaseArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataDiseaseArray.length(); i++) {
                    JSONObject diseaseInfo = dataDiseaseArray.getJSONObject(i);
                    mDiseaseID = diseaseInfo.getString("disease_id");
                    mDiseaseName = diseaseInfo.getString("disease_name");
                    mDiseaseType = diseaseInfo.getString("type");

                    mDiseasesList.add(new Disease(mDiseaseID, mDiseaseName, mDiseaseType));
                    mViewDiseaseAdapter.notifyDataSetChanged();


//                    Toast.makeText(getActivity(), "Disease details are: " + mDiseaseID + ", " + mDiseaseName + ", " + mDiseaseType + ".", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Disease details are: " + mDiseaseID + ", " + mDiseaseName + ", " + mDiseaseType + ".");
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
}
