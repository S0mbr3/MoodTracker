package com.s0mbr3.moodtracker.main.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.s0mbr3.moodtracker.main.MainActivity;
import com.s0mbr3.moodtracker.main.models.DeserializedHumorFileReader;


/**
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
