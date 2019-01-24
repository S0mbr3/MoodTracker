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
    private int mIndex;
    private String mCommentTxt;
    private String mFilepath;
    private DeserializedHumorFileReader humorData;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null) mFilepath = extras.getString(MainActivity.BUNDLE_EXTRA_COMMENT_TXT);
        humorData = new DeserializedHumorFileReader(mFilepath);
        humorData.objectDeserializer();
        mIndex = humorData.getIndex();
        mCommentTxt = humorData.getmCommentTxt();
            Log.d("AlarmReceiver", mCommentTxt + " " + mIndex);
    }
}
