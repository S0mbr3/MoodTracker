package com.s0mbr3.moodtracker.controller.MainControllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.s0mbr3.moodtracker.controller.MainActivity;


/**
 * Created by Oxhart on 22/01/2019.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String DEBUG_TAG = "Alarm Received";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String commentTxt = extras.getString(MainActivity.BUNDLE_EXTRA_COMMENT_TXT);
            int index = extras.getInt(MainActivity.BUNDLE_EXTRA_HUMORS_LIST_INDEX);
            Log.d("AlarmReceiver", "timer testing");
        }
    }
}
