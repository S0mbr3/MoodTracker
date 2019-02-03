package com.s0mbr3.moodtracker.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.s0mbr3.moodtracker.controllers.AlarmReceiver;

import java.util.Calendar;

public class MyAlarmManager {

    public MyAlarmManager(){

    }
    public void setAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3600*24*1000, alarmIntent);
        Log.d("time", String.valueOf(calendar.getTimeInMillis() - System.currentTimeMillis()));

    }
}
