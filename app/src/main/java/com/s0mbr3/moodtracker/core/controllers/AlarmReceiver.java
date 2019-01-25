package com.s0mbr3.moodtracker.core.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.s0mbr3.moodtracker.activities.MainActivity;
import com.s0mbr3.moodtracker.core.models.DeserializedHumorFileReader;


/**
 * AlarmReceiver class is the schedule task broadcast receiver
 * Created by Oxhart on 22/01/2019.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String DEBUG_TAG = "Alarm Received";
    private int mIndex;
    private String mCommentTxt;
    private int mCurrentDayForHistoric;
    private String mDirPath;
    private DeserializedHumorFileReader humorData;
    private SerialiazedHumorFileWriter mSerializedHumorForHistoric;

    /**
     * onReceive event is triggered when the schedule task is fired, it read from a serialized
     * humor object containing the index of the humors list and the comment associated if there is
     * one. then it prepare another serialization task for the historic activity
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null) mDirPath = extras.getString(MainActivity.BUNDLE_EXTRA_COMMENT_TXT);
        humorData = new DeserializedHumorFileReader(mDirPath);
        humorData.objectDeserializer("selectedHumor.txt");
        mIndex = humorData.getIndex();
        mCommentTxt = humorData.getCommentTxt();
        mCurrentDayForHistoric = humorData.getCurrentDayForHistoric();
        mSerializedHumorForHistoric = new SerialiazedHumorFileWriter(mIndex, mCommentTxt,
                mCurrentDayForHistoric, mDirPath);
        mSerializedHumorForHistoric.SerializedHumorFileWriting(String.format(
                "day%s.txt", mCurrentDayForHistoric));
        Log.d("AlarmReceiver", mCommentTxt + " " +  mIndex + " " + mCurrentDayForHistoric);
    }
}
