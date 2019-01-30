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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
        String archiveDir = appStartDriver.getArchiveDir();
        new File(mDirPath + historicDir).mkdir();

        DeserializedHumorFileReader humorData = new DeserializedHumorFileReader(mDirPath);
        humorData.objectDeserializer(currenntHumorFilePath);

        int mIndex = humorData.getIndex();
        String mCommentTxt = humorData.getCommentTxt();
        int currentDayForHistoric = humorData.getCurrentDayForHistoric();


        SerialiazedHumorFileWriter mSerializedHumorForHistoric = new SerialiazedHumorFileWriter();
        mSerializedHumorForHistoric.SerializedHumorFileWriting(mIndex, mCommentTxt,
                currentDayForHistoric,mDirPath + historicDir + String.valueOf(currentDayForHistoric));

        if(currentDayForHistoric >= 8) {
            try {
                List<File> files = new ArrayList<File>();
                //will have to test for empty folder to prevent crashes
                files = Arrays.asList(new File(mDirPath + historicDir).listFiles());
                Collections.sort(files, new MyComparator());
                for(File file : files){
                    Log.d("alar", file.getName());
                }
                files.get(0).renameTo(new File(mDirPath + archiveDir + files.get(0).getName()));
                Log.d("alar", files.get(0).getName());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        ++currentDayForHistoric;

        appStartDriver.setCurrentDayForHistoric(currentDayForHistoric);
        appStartDriver.setCommentTxt(null);
        appStartDriver.setIndex(3);
        HumorUpdater.getInstance().updateTrigger();
        mSerializedHumorForHistoric.SerializedHumorFileWriting(3, null,
                currentDayForHistoric, mDirPath + currenntHumorFilePath);

        boolean f = new File(mDirPath + currenntHumorFilePath).exists();
        Log.d("AlarmReceiver", mCommentTxt + " " + mIndex + " " + currentDayForHistoric + " " + f);
    }
}
