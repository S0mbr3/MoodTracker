package com.s0mbr3.moodtracker.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.s0mbr3.moodtracker.models.MyAlarmManager;

/**
* Launch back the alarm manager if the user reboot the phone
 */
public class DeviceRebootReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            MyAlarmManager myAlarmManager = new MyAlarmManager();

            myAlarmManager.setAlarm(context);
        }
    }
}
