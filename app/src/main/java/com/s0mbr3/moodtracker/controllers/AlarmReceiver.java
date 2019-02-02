package com.s0mbr3.moodtracker.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.DeserializedHumorFileReader;
import com.s0mbr3.moodtracker.models.HumorUpdater;
import com.s0mbr3.moodtracker.models.SerialiazedHumorFileWriter;

import java.io.File;


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
        String dirPath = appStartDriver.getMainDirPath();
        String currenntHumorFilePath = appStartDriver.getHumorFilePath();
        String historicDir = appStartDriver.getHistoricDir();
        String archiveDir = appStartDriver.getArchiveDir();
        new File(dirPath + historicDir).mkdir();

        DeserializedHumorFileReader humorData = new DeserializedHumorFileReader();
        humorData.objectDeserializer(dirPath + currenntHumorFilePath);

        int mIndex = humorData.getIndex();
        String mCommentTxt = humorData.getCommentTxt();
        int currentDayForHistoric = humorData.getCurrentDayForHistoric();


        SerialiazedHumorFileWriter mSerializedHumorForHistoric = new SerialiazedHumorFileWriter();
        mSerializedHumorForHistoric.SerializedHumorFileWriting(mIndex, mCommentTxt,
                currentDayForHistoric,dirPath + historicDir + String.valueOf(currentDayForHistoric));

        ++currentDayForHistoric;

        appStartDriver.setCurrentDayForHistoric(currentDayForHistoric);
        appStartDriver.setCommentTxt(null);
        appStartDriver.setIndex(3);
        HumorUpdater.getInstance().updateTrigger();
        mSerializedHumorForHistoric.SerializedHumorFileWriting(3, null,
                currentDayForHistoric, dirPath + currenntHumorFilePath);

        boolean f = new File(dirPath + currenntHumorFilePath).exists();
        Log.d("AlarmReceiver", mCommentTxt + " " + mIndex + " " + currentDayForHistoric + " " + f);
    }
}
