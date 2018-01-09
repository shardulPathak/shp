package com.example.shardulpathak.shp_patient.patient_details;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.shardulpathak.shp_patient.IFragmentCommunicator;
import com.example.shardulpathak.shp_patient.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IFragmentCommunicator} interface
 * to handle interaction events.
 */
public class DetailsFragment extends Fragment {

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

    private IFragmentCommunicator mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_activity_details);
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
                makeViewEditable();
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
}
