package com.s0mbr3.moodtracker.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.DeserializedHumorFileReader;
import com.s0mbr3.moodtracker.models.HumorUpdater;
import com.s0mbr3.moodtracker.models.Notifications;
import com.s0mbr3.moodtracker.models.SelectedHumorSerializer;
import com.s0mbr3.moodtracker.models.SerializedObjectFileWriter;
import com.s0mbr3.moodtracker.models.StatisticsSerializer;
import com.s0mbr3.moodtracker.models.StatisticsUnSerializer;
import com.s0mbr3.moodtracker.models.StreakSerializer;
import com.s0mbr3.moodtracker.models.StreakUnserializer;

import java.io.File;


/**
 * AlarmReceiver class is the schedule task broadcast receiver
 * Created by Oxhart on 22/01/2019.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private String mDirPath;
    private SerializedObjectFileWriter mSerializedHumorForHistoric;
    private int mIndex;

    /**
     * onReceive event is triggered when the schedule task is fired, it read from a serialized
     * humor object containing the index of the humors list and the comment associated if there is
     * one. then it prepare another serialization task for the historic activity
     *
     */
    @Override
    public void onReceive(Context context, Intent intent) {
            AppStartDriver appStartDriver = AppStartDriver.INSTANCE;
            mDirPath = context.getFilesDir().getAbsolutePath();
            String currenntHumorFilePath = appStartDriver.getHumorFilePath();
            String historicDir = appStartDriver.getHistoricDir();
            new File(mDirPath + historicDir).mkdir();
            new File(mDirPath + AppStartDriver.INSTANCE.STATISTICS_DIR).mkdir();

            DeserializedHumorFileReader humorData = new DeserializedHumorFileReader();
            humorData.objectDeserializer(mDirPath + currenntHumorFilePath);

            mIndex = humorData.getIndex();
            String mCommentTxt = humorData.getCommentTxt();
            int currentDayForHistoric = humorData.getCurrentDayForHistoric();


            mSerializedHumorForHistoric = new SerializedObjectFileWriter();
            mSerializedHumorForHistoric.SerializedHumorFileWriting(new SelectedHumorSerializer(
                            mIndex,
                            mCommentTxt,
                            currentDayForHistoric),
                    mDirPath + historicDir + String.valueOf(currentDayForHistoric));

            ++currentDayForHistoric;

            appStartDriver.setCurrentDayForHistoric(currentDayForHistoric);
            appStartDriver.setCommentTxt(null);
            appStartDriver.setIndex(3);
            HumorUpdater.getInstance().updateTrigger();
            mSerializedHumorForHistoric.SerializedHumorFileWriting(new SelectedHumorSerializer(
                            3,
                            null,
                            currentDayForHistoric),
                    mDirPath + currenntHumorFilePath);

            statistics();
            streak();
            if (!appStartDriver.isAlive()) {
                Notifications notificate = new Notifications(context);
                notificate.showNotification();
                notificate.Notification();
            }

            boolean f = new File(mDirPath + currenntHumorFilePath).exists();
    }


    private void statistics(){
        StatisticsUnSerializer humorDay = new StatisticsUnSerializer();
        humorDay.objectUnserializer(mDirPath + AppStartDriver.INSTANCE.STATISTICS_DIR + mIndex);
        int dayHumor = humorDay.getDays();

        mSerializedHumorForHistoric.SerializedHumorFileWriting(new StatisticsSerializer(
                        ++dayHumor),
                mDirPath + AppStartDriver.INSTANCE.STATISTICS_DIR + mIndex);

    }

    public void streak(){
        StreakUnserializer streakUnserializer = new StreakUnserializer();
        streakUnserializer.objectUnserializer(mDirPath + AppStartDriver.INSTANCE.STREAK_FILE);
        int totalStreak = streakUnserializer.getTotalStreak();
        int currentStreak = streakUnserializer.getCurrentStreak();
        int additionalScore = streakUnserializer.getAdditionalScore();
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

        mSerializedHumorForHistoric.SerializedHumorFileWriting(new StreakSerializer(
                        totalStreak, currentStreak, additionalScore),
                mDirPath + AppStartDriver.INSTANCE.STREAK_FILE);
    }

}
