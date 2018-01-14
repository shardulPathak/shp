package com.example.shardulpathak.shp_doctor.notification;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shardulpathak.shp_doctor.DummyContent;
import com.example.shardulpathak.shp_doctor.IFragmentCommunicator;
import com.example.shardulpathak.shp_doctor.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link IFragmentCommunicator}
 * interface.
 */
public class NotificationFragment extends Fragment {

    private IFragmentCommunicator mListener;
    private TextView mNotificationTitle;
    private ListView mNotificationListView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotificationFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotificationFragment newInstance(int columnCount) {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_notifications);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {
        mNotificationTitle = (TextView) v.findViewById(R.id.notification_title);
        mNotificationListView = (ListView) v.findViewById(R.id.notification_list);
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
