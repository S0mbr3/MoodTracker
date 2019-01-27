package com.s0mbr3.moodtracker.core.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.s0mbr3.moodtracker.activities.MainActivity;
import com.s0mbr3.moodtracker.core.models.DeserializedHumorFileReader;

import java.io.File;


/**
 * AlarmReceiver class is the schedule task broadcast receiver
 * Created by Oxhart on 22/01/2019.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private String mDirPath;
    private static final String HISTORIC_DIR = "/historicDir";
    private static final String USER_CHOSEN_HUMOR_FILE = "selectedhumor.txt";

    /**
     * onReceive event is triggered when the schedule task is fired, it read from a serialized
     * humor object containing the index of the humors list and the comment associated if there is
     * one. then it prepare another serialization task for the historic activity
     *
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null) mDirPath = extras.getString(MainActivity.BUNDLE_EXTRA_COMMENT_TXT);
        new File(mDirPath + HISTORIC_DIR).mkdir();

        DeserializedHumorFileReader humorData = new DeserializedHumorFileReader(mDirPath);
        humorData.objectDeserializer(USER_CHOSEN_HUMOR_FILE);

        int mIndex = humorData.getIndex();
        String mCommentTxt = humorData.getCommentTxt();
        int mCurrentDayForHistoric = humorData.getCurrentDayForHistoric();

        SerialiazedHumorFileWriter mSerializedHumorForHistoric = new SerialiazedHumorFileWriter(mIndex, mCommentTxt,
                mCurrentDayForHistoric, mDirPath + HISTORIC_DIR);
        mSerializedHumorForHistoric.SerializedHumorFileWriting(String.format(
                "day%s.txt", mCurrentDayForHistoric));

        Log.d("AlarmReceiver", mCommentTxt + " " + mIndex + " " + mCurrentDayForHistoric + " " + mDirPath);
    }
}
