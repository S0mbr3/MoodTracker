package com.s0mbr3.moodtracker.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.HumorUpdater;
import com.s0mbr3.moodtracker.models.Notifications;
import com.s0mbr3.moodtracker.models.SharedPreferencesManager;

import java.util.Arrays;


/**
 * AlarmReceiver class is the schedule task broadcast receiver
 * Created by Oxhart on 22/01/2019.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private String mDirPath;
    private SharedPreferencesManager mPreferencesManager;
    private int mIndex;

    /**
     * onReceive event is triggered when the schedule task is fired, it read from a the Shared
     * Preferences containing the index of the humors list and the comment associated if there is
     * one, prepare the history, the notifications, the streak and reload the eye focus activity
     * by the user if he is on the application
     *
     */
    @Override
    public void onReceive(Context context, Intent intent) {
            mDirPath = context.getFilesDir().getAbsolutePath();

            mPreferencesManager = new SharedPreferencesManager(context.getApplicationContext());
            Object[] data = mPreferencesManager.getSelectedHumor();
            mIndex = (int) data[0];
            int currentDayForHistoric = (int) data[1];
            String mCommentTxt = (String) data[2];

            mPreferencesManager.setHistoricHumor(mIndex, mCommentTxt, currentDayForHistoric);

            ++currentDayForHistoric;

            mPreferencesManager.setSelectedHumor(3, currentDayForHistoric, null);

            statistics();
            streak();
            if (AppStartDriver.INSTANCE.isAlive()) {
                Notifications notificate = new Notifications(context,
                        Arrays.asList(context.getApplicationContext().getResources().getStringArray(
                                R.array.notifications)));
                notificate.showNotification();
                notificate.Notification(context.getApplicationContext().getString(R.string.notificate));
            }
        HumorUpdater.getInstance().updateTrigger();
    }


    private void statistics(){
        int dayHumor = mPreferencesManager.getDaysPerHumor(mIndex);

        mPreferencesManager.setDaysPerHumor(++dayHumor, mIndex);

    }

    public void streak(){
        Object[] pref = mPreferencesManager.getStreaks();
        int totalStreak = (int) pref[0];
        int currentStreak = (int) pref[1];
        int additionalScore = (int) pref[2];
        switch(mIndex) {
            case 4:
                additionalScore += 10;
                ++currentStreak;
                break;
            case 3:
                additionalScore += 5;
                ++currentStreak;
                break;
            default:
            	additionalScore = 0;
                currentStreak = 0;
        }

        if (currentStreak > totalStreak) totalStreak = currentStreak;

		mPreferencesManager.setStreaks(totalStreak, currentStreak, additionalScore);
    }

}
