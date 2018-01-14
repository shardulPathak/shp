package com.example.shardulpathak.shp_doctor.view_patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shardulpathak.shp_doctor.R;

import java.util.List;

/**
 *
 */
public class PatientListAdapter extends BaseAdapter {


    private Context mContext;
    private List<Patient> mPatientList;

    public PatientListAdapter(Context context, List<Patient> patientList) {
        this.mContext = context;
        this.mPatientList = patientList;
    }

    @Override
    public int getCount() {
        return mPatientList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPatientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Patient patient = mPatientList.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.patient_list_cell_content, null);
            holder.mPatientId = (TextView) convertView.findViewById(R.id.patient_id);
            holder.mPatientName = (TextView) convertView.findViewById(R.id.patient_name);
            holder.mPatientAge = (TextView) convertView.findViewById(R.id.patient_age);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mPatientId.setText(patient.getPatientId());
        holder.mPatientName.setText(patient.getPatientName());
        holder.mPatientAge.setText(patient.getPatientAge());
        return convertView;
    }

    static class ViewHolder {
        TextView mPatientId;
        TextView mPatientName;
        TextView mPatientAge;
    }
}
