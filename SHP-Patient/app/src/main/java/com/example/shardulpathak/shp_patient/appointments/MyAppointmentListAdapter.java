package com.example.shardulpathak.shp_patient.appointments;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.shardulpathak.shp_patient.R;

import java.util.List;

public class MyAppointmentListAdapter extends BaseAdapter {


    private Context mContext;
    private List<Appointment> mAppointmentList;
    private MyAppointmentsFragment mAppointmentsFragment;

    public MyAppointmentListAdapter(Context context, List<Appointment> appointmentList, MyAppointmentsFragment fragment) {
        this.mContext = context;
        this.mAppointmentList = appointmentList;
        this.mAppointmentsFragment = fragment;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mAppointmentList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return mAppointmentList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        Appointment appointment = mAppointmentList.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.appointment_list_cell_content, null);
            holder.mAppointmentIDView = (TextView) convertView.findViewById(R.id.appointment_id);
            holder.mAppointmentDateView = (TextView) convertView.findViewById(R.id.appointment_date);
            holder.mAppointmentTimeView = (TextView) convertView.findViewById(R.id.appointment_time);
            holder.mAppointmentPatientNameView = (TextView) convertView.findViewById(R.id.appointment_patient_name);
            Button acceptButton = (Button) convertView.findViewById(R.id.btn_accept);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String appointmentTime = holder.mAppointmentTimeView.getText().toString();
                    mAppointmentsFragment.sendPatientResponse(true, holder.mAppointmentIDView.getText().toString());
                }
            });
            Button denyButton = (Button) convertView.findViewById(R.id.btn_deny);
            denyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAppointmentList.size() != position) {
                        mAppointmentList.remove(mAppointmentList.get(position));
                    }
                    mAppointmentsFragment.sendPatientResponse(false, holder.mAppointmentIDView.getText().toString());
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mAppointmentIDView.setText(appointment.getAppointmentID());
        holder.mAppointmentDateView.setText(appointment.getAppointmentDate());
        holder.mAppointmentTimeView.setText(appointment.getAppointmentTime());
        holder.mAppointmentPatientNameView.setText(appointment.getAppointmentPatientName());
        return convertView;
    }

    private static class ViewHolder {
        TextView mAppointmentIDView;
        TextView mAppointmentDateView;
        TextView mAppointmentTimeView;
        TextView mAppointmentPatientNameView;
    }

}
