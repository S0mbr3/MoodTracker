package com.s0mbr3.moodtracker.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.s0mbr3.moodtracker.models.MyAlarmManager;

public class DeviceRebootReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ezezez", String.valueOf(context));
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            MyAlarmManager myAlarmManager = new MyAlarmManager();

            myAlarmManager.setAlarm(context);
        }
    }
}
