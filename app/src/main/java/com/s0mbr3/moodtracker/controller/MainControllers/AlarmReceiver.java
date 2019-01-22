package com.s0mbr3.moodtracker.controller.MainControllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Oxhart on 22/01/2019.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String DEBUG_TAG = "DEBUG_TAG";
    @Override
    public void onReceive(Context context, Intent intent) {
            Log.d(DEBUG_TAG, "OHHHHHHHHHHHHHHHHHHH");
    }
}
