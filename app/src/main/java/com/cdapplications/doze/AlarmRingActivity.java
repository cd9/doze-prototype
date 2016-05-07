package com.cdapplications.doze;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Colin on 2015-08-25.
 */
public class AlarmRingActivity extends SingleFragActivity{
    private final String TAG = "AlarmRingActivity";
    @Override
    protected Fragment createFragment(){
        return AlarmRingFragment.newInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Log.i(TAG, "RING ACTIVITY STARTED");
    }

}
