package com.cdapplications.doze;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class DozeHomeFragment extends Fragment {


    public static final int SLEEP_CYCLE_LENGTH = 90;




    private Handler mHandler;
    private Button mSleepButton;
    private static final int ALARM_REQUEST = 0;
    private static final int UPDATE_INTERVAL = 1000;
    public static final String RING_INTENT = "com.cdapplications.doze.RING_ACTION";
    private static final String TAG = "DozeHomeFragment";
    private Timer mTimer;
    private TimerTask mTimerTask;
    private WakeAdapter mWakeAdapter;
    private String mName = "Colin";
    //todo personalize nName;
    private Calendar mAlarmTime;
    private TextView mPersonalTextView;
    private WakeTime mWakeTime;
    private boolean mTimeSet;
    private TextView mStatBar;
    private Button mHowWorksButton;
    private Button mSetupButton;
    private RelativeLayout mButtonWrapper;

    List<WakeTime> mWakeTimes;
    private RecyclerView mWakeTimesRecycleView;


//useless
    public static DozeHomeFragment newInstance(){
        return new DozeHomeFragment();
    }

    private void activateSleepButton(Boolean b){
        if(b){
            mSleepButton.setEnabled(true);

        }else{
            mSleepButton.setEnabled(false);

        }
    }

    private void initWakeTimes(){
        //todo automatically adjust wake times based on user prefs

        mWakeTimes = new ArrayList<>();
        for (int i = 0; i<10;i++){
            mWakeTimes.add(new WakeTime());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, SLEEP_CYCLE_LENGTH * (i+1));
            mWakeTimes.get(i).setAlarmTime(calendar);
        }
    }

    private void updateWakeTimes(){
        for (int i = 0; i<mWakeTimes.size();i++){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, SLEEP_CYCLE_LENGTH * (i+1));
            mWakeTimes.get(i).setAlarmTime(calendar);
        }
    }


    private class WakeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTimeIndicator;
        private LinearLayout mListItemWrapper;

        public WakeHolder(View itemView) {
            super(itemView);
            mTimeIndicator = (TextView) itemView.findViewById(R.id.wake_item_time_view);
            mListItemWrapper = (LinearLayout) itemView.findViewById(R.id.list_item_wrapper);
            mListItemWrapper.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (mWakeTimes.get(getAdapterPosition()).isActive()){
                activateSleepButton(false);
                Log.i(TAG, "Alarm deactivated");
                mStatBar.setText(getResources().getString(R.string.status_bar_deactivated));
                mTimeSet = false;
            }
            else {
                mAlarmTime = mWakeTimes.get(getAdapterPosition()).getAlarmCalendarTime();
                String mTimeDisplay = mWakeTimes.get(getAdapterPosition()).getAlarmTime();
                Log.i(TAG, "mCalendar set to " + mAlarmTime.getTime().toString());
                String difference = differenceBetweenCalendars(Calendar.getInstance(), mWakeTimes.get(getAdapterPosition()).getAlarmCalendarTime());
                mStatBar.setText(String.format(getResources().getString(R.string.sleep_status_bar_text), mTimeDisplay, difference));
                activateSleepButton(true);
                mTimeSet = true;
            }


            mListItemWrapper.setActivated(!mListItemWrapper.isActivated());
            mWakeTimes.get(getAdapterPosition()).setIsActive(!mWakeTimes.get(getAdapterPosition()).isActive());
            for (int i = 0;i<mWakeTimes.size();i++){
                if (i!=getAdapterPosition()){
                    mWakeTimes.get(i).setIsActive(false);
                }
            }
            mWakeAdapter.notifyDataSetChanged();
        }
    }

    private class WakeAdapter extends RecyclerView.Adapter<WakeHolder>{


        @Override
        public WakeHolder onCreateViewHolder(ViewGroup viewGroup, int i){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.wake_view, viewGroup, false);
            return new WakeHolder(view);
        }

        @Override
        public void onBindViewHolder (WakeHolder wakeHolder, int i) {
            mWakeTime = mWakeTimes.get(i);
            wakeHolder.mTimeIndicator.setText(mWakeTime.getAlarmTime());
            if(mWakeTimes.get(i).isActive()){
                wakeHolder.mListItemWrapper.setActivated(true);
            }else{
                wakeHolder.mListItemWrapper.setActivated(false);
            }
        }

        @Override
        public int getItemCount() {
            return mWakeTimes.size();
        }

    }

    private String differenceBetweenCalendars(Calendar c1, Calendar c2){
        long difference = Math.abs(c1.getTimeInMillis() - c2.getTimeInMillis());

        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int mins = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60) + 1 ;
        if (mins==60){
            mins = 0;
            hours++;
        }
        if (hours==24){
            hours = 0;
            days++;
        }
        //Takes care of plurals and empty values;
        String hourString = " hours";
        String minString = " and "+mins+" minutes";
        if (hours==1) hourString = " hour";
        if (mins==0) minString = "";
        return hours+hourString+minString;
    }

    //When fragment is resumed, timer starts updating
    @Override
    public void onResume(){
        super.onResume();
        setUpdating(true);
    }

    @Override
    public void onPause(){
        super.onPause();
        setUpdating(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWakeTimes();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.doze_home, container, false);
        mSleepButton = (Button) v.findViewById(R.id.sleep_button);


        mButtonWrapper = (RelativeLayout) v.findViewById(R.id.button_wrapper);

        mSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimeSet) {
                    setAlarm(mAlarmTime);
                }
            }
        });
        if (!mTimeSet) {
            activateSleepButton(false);
        }

        mPersonalTextView = (TextView) v.findViewById(R.id.personal_welcome_text_view);
        mPersonalTextView.setText(String.format(getResources().getString(R.string.welcome_message), mName));
       // setSizes();
        mWakeTimesRecycleView = (RecyclerView) v.findViewById(R.id.home_screen_recycler_view);
        mWakeTimesRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWakeAdapter = new WakeAdapter();
        mWakeTimesRecycleView.setAdapter(mWakeAdapter);
        mStatBar = (TextView) v.findViewById(R.id.status_bar);


        mHowWorksButton = (Button) v.findViewById(R.id.bottom_how_works_button);
        mHowWorksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mSetupButton= (Button) v.findViewById(R.id.bottom_setup_button);
        mSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;


    }

    private void setSizes(){
        //Changes the width based on the height to ensure a perfect circle;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        double sectionSize = dm.heightPixels * 0.4;
        int buttonSize = (int) Math.round(sectionSize - 150);
        ViewGroup.LayoutParams params = mSleepButton.getLayoutParams();
        params.width= (int) Math.round(buttonSize*1.5);
        params.height=buttonSize;
        mSleepButton.setLayoutParams(params);
        mSleepButton.setBackgroundResource(R.drawable.sleep_button);

        /*
        ViewGroup.LayoutParams barParams = (ViewGroup.LayoutParams) mPersonalTextView.getLayoutParams();
        barParams.width = Math.round(dm.widthPixels * 0.80);
        mPersonalTextView.setLayoutParams(barParams);*/

    }

    private void setAlarm(Calendar calendarTime){
        Intent i = new Intent();
        i.setAction(RING_INTENT);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity(), ALARM_REQUEST, i, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendarTime.getTimeInMillis(), alarmIntent);
    }

    private void setUpdating(boolean setOn) {
        if (setOn){
            try {
                mTimer = new Timer();
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Log.i(TAG, "WakeTimes Updated");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateWakeTimes();
                                mWakeAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                };
                mTimer.schedule(mTimerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);

            }catch (IllegalStateException e){
                Log.e(TAG, "error with timer", e);
            }
        }else{
            mTimer.cancel();
        }

    }





}
