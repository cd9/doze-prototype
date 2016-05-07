package com.cdapplications.doze;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Colin on 2015-08-25.
 */
public class WakeTime {

    private Calendar alarmTime;
    private boolean isActive;

    public WakeTime(){
        //constructor for current time
        alarmTime = Calendar.getInstance();
        isActive = false;
    }

    public String getAlarmTime() {
        SimpleDateFormat simpleDate = new SimpleDateFormat("h:mm a");
        return simpleDate.format(alarmTime.getTime());
    }

    public Calendar getAlarmCalendarTime(){
        return alarmTime;
    }

    public void setAlarmTime(Calendar alarmTime) {
        this.alarmTime = alarmTime;
    }



    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }



}
