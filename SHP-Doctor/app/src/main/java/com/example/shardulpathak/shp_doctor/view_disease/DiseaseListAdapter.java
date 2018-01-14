package com.example.shardulpathak.shp_doctor.view_disease;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shardulpathak.shp_doctor.DummyContent;
import com.example.shardulpathak.shp_doctor.IFragmentCommunicator;
import com.example.shardulpathak.shp_doctor.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyContent.DummyItem} and makes a call to the
 * specified {@link IFragmentCommunicator}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DiseaseListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Disease> mDiseaseList;

    public DiseaseListAdapter(Context context, List<Disease> diseaseList) {
        this.mContext = context;
        this.mDiseaseList = diseaseList;
    }

    @Override
    public int getCount() {
        return mDiseaseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDiseaseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Disease disease = mDiseaseList.get(position);

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

        holder.mDiseaseId.setText(disease.getDiseaseId());
        holder.mDiseaseName.setText(disease.getDiseaseName());
        holder.mDiseaseType.setText(disease.getDiseaseType());
        return convertView;
    }

    static class ViewHolder {
        TextView mDiseaseId;
        TextView mDiseaseName;
        TextView mDiseaseType;
    }
}
