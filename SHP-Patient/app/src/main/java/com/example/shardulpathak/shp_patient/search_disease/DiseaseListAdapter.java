package com.example.shardulpathak.shp_patient.search_disease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shardulpathak.shp_patient.R;

import java.util.ArrayList;


public class DiseaseListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<DiseaseDetails> mDiseaseDetailsList;

    public DiseaseListAdapter(Context context, ArrayList<DiseaseDetails> diseaseDetailsList) {
        this.mContext = context;
        this.mDiseaseDetailsList = diseaseDetailsList;
    }

    @Override
    public int getCount() {
        return mDiseaseDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDiseaseDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DiseaseDetails diseaseDetails = mDiseaseDetailsList.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.disease_list_cell_content, null);
            holder.mDiseaseId = (TextView) convertView.findViewById(R.id.disease_id);
            holder.mDiseaseName = (TextView) convertView.findViewById(R.id.disease_name);
            holder.mDiseaseType = (TextView) convertView.findViewById(R.id.disease_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mDiseaseId.setText(diseaseDetails.getDiseaseId());
        holder.mDiseaseName.setText(diseaseDetails.getDiseaseName());
        holder.mDiseaseType.setText(diseaseDetails.getDiseaseType());
        return convertView;
    }

    static class ViewHolder {
        TextView mDiseaseId;
        TextView mDiseaseName;
        TextView mDiseaseType;
    }
}
