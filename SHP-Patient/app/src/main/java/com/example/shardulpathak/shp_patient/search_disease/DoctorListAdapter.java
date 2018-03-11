package com.example.shardulpathak.shp_patient.search_disease;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.shardulpathak.shp_patient.R;
import com.example.shardulpathak.shp_patient.search_doctor.SearchDoctorFragment;

import java.util.ArrayList;


public class DoctorListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DoctorDetails> mDoctorDetailsList;
    private SearchResultsFragment mFragment;

    public DoctorListAdapter(Context context, ArrayList<DoctorDetails> doctorDetailsList, SearchResultsFragment fragment) {
        this.mContext = context;
        this.mDoctorDetailsList = doctorDetailsList;
        this.mFragment=fragment;
    }

    @Override
    public int getCount() {
        return mDoctorDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDoctorDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        DoctorDetails doctorDetails = mDoctorDetailsList.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.doctor_list_cell_content, null);

            holder.mDoctorID = (TextView) convertView.findViewById(R.id.doctor_id);
            holder.mDoctorEmail = (TextView) convertView.findViewById(R.id.doctor_email);
            holder.mDocCategory = (TextView) convertView.findViewById(R.id.doc_category);
            holder.mDocFullName = (TextView) convertView.findViewById(R.id.doc_full_name);
            holder.mDocAddress = (TextView) convertView.findViewById(R.id.doc_address);
            holder.mDocCity = (TextView) convertView.findViewById(R.id.doc_city);
            holder.mDocMobile = (TextView) convertView.findViewById(R.id.doc_mobile);
            holder.mDocHospitalName = (TextView) convertView.findViewById(R.id.doc_hospital_name);
            Button appointmentButton=(Button)convertView.findViewById(R.id.book_appointment_btn);
            appointmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mFragment instanceof SearchResultsFragment){
                        mFragment.sendAppointmentRequestToDoctor(holder.mDoctorID.getText().toString());
                    }
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mDoctorID.setText(doctorDetails.getDoctorID());
        holder.mDoctorEmail.setText(doctorDetails.getDoctorEmail());
        holder.mDocCategory.setText(doctorDetails.getDocCategory());
        holder.mDocFullName.setText(doctorDetails.getDocFullName());
        holder.mDocAddress.setText(doctorDetails.getDocAddress());
        holder.mDocCity.setText(doctorDetails.getDocCity());
        holder.mDocMobile.setText(doctorDetails.getDocMobile());
        holder.mDocHospitalName.setText(doctorDetails.getDocHospitalName());
        return convertView;
    }

    private void sendAppointmentRequestToDoctor() {
    }

    static class ViewHolder {
        TextView mDoctorID;
        TextView mDoctorEmail;
        TextView mDocCategory;
        TextView mDocFullName;
        TextView mDocAddress;
        TextView mDocCity;
        TextView mDocMobile;
        TextView mDocHospitalName;
    }
}
