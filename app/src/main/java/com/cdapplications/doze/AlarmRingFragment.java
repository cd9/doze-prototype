package com.cdapplications.doze;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin on 2015-08-25.
 */
public class AlarmRingFragment extends Fragment{
    public static AlarmRingFragment newInstance(){
        return new AlarmRingFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        View v = inflater.inflate(R.layout.alarm_ring, container, false);
        return v;

    }



}
