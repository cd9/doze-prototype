package com.cdapplications.doze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Colin on 2015-08-25.
 */
public class RingReceiver extends BroadcastReceiver {
    private static final String TAG = "RingReceiver";
    @Override
    public void onReceive(Context context, Intent intent){
        Log.i(TAG, "INTENT RECEIVED, STARTING RING");
        Intent i = new Intent(context, AlarmRingActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
