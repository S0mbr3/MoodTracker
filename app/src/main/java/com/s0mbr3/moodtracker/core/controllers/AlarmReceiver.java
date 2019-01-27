package com.s0mbr3.moodtracker.core.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.s0mbr3.moodtracker.activities.MainActivity;
import com.s0mbr3.moodtracker.core.models.DeserializedHumorFileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * AlarmReceiver class is the schedule task broadcast receiver
 * Created by Oxhart on 22/01/2019.
 */
public class AlarmReceiver extends BroadcastReceiver {

    /**
     * onReceive event is triggered when the schedule task is fired, it read from a serialized
     * humor object containing the index of the humors list and the comment associated if there is
     * one. then it prepare another serialization task for the historic activity
     *
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        AppStartDriver appStartDriver = AppStartDriver.INSTANCE;
        Bundle extras = intent.getExtras();
        //if(extras != null) mDirPath = extras.getString(MainActivity.BUNDLE_EXTRA_COMMENT_TXT);
        String mDirPath = appStartDriver.getMainDirPath();
        String currenntHumorFilePath = appStartDriver.getHumorFilePath();
        String historicDir = appStartDriver.getHistoricDir();
        new File(mDirPath + historicDir).mkdir();

        DeserializedHumorFileReader humorData = new DeserializedHumorFileReader(mDirPath);
        humorData.objectDeserializer(currenntHumorFilePath);

        int mIndex = humorData.getIndex();
        String mCommentTxt = humorData.getCommentTxt();
        int currentDayForHistoric = humorData.getCurrentDayForHistoric();

        SerialiazedHumorFileWriter mSerializedHumorForHistoric = new SerialiazedHumorFileWriter();
        mSerializedHumorForHistoric.SerializedHumorFileWriting(mIndex, mCommentTxt,
                currentDayForHistoric, String.format( mDirPath + historicDir +  "/day%s.txt", currentDayForHistoric));

        if (currentDayForHistoric == 7) currentDayForHistoric = 1;
        else ++currentDayForHistoric;

        appStartDriver.setCurrentDayForHistoric(currentDayForHistoric);
        mSerializedHumorForHistoric.SerializedHumorFileWriting(mIndex, mCommentTxt,
                currentDayForHistoric, mDirPath + currenntHumorFilePath);

        boolean f = new File(mDirPath + currenntHumorFilePath).exists();
        List<String> results = new ArrayList<String>();


        File[] files = new File(mDirPath + historicDir).listFiles();
//If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
                Log.d("ala", file.getName());
            }
        }
        Log.d("AlarmReceiver", mCommentTxt + " " + mIndex + " " + currentDayForHistoric + " " + f);
    }
}
