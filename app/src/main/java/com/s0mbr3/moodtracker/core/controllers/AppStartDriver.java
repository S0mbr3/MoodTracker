package com.s0mbr3.moodtracker.core.controllers;

import android.content.Context;
import android.util.Log;

import com.s0mbr3.moodtracker.core.models.DeserializedHumorFileReader;

import java.io.File;

public enum AppStartDriver {
    INSTANCE;

    private int mIndex;
    private String mCommentTxt;
    private int mCurrentDayForHistoric;
    private String mDirPath;
    private static final String HISTORIC_DIR = "/historicdir";
    private static final String USER_CHOSEN_HUMOR_FILE = "/selectedhumor.txt";


    AppStartDriver(){
    }


    public int getIndex(){
        return this.mIndex;
    }

    public String getmCommentTxt(){
        return this.mCommentTxt;
    }

    public int getCurrentDayForHistoric(){
        return this.mCurrentDayForHistoric;
    }

    public String getHistoricDir(){
        return HISTORIC_DIR;
    }

    public String getHumorFilePath(){
        return USER_CHOSEN_HUMOR_FILE;
    }

    public String getMainDirPath(){
        return this.mDirPath;
    }

    public void setCurrentDayForHistoric(int currentDayForHistoric){
        this.mCurrentDayForHistoric = currentDayForHistoric;
    }

    public void configurator(Context context){
        this.mDirPath = context.getFilesDir().getAbsolutePath();
        if(new File(this.mDirPath, USER_CHOSEN_HUMOR_FILE).exists()) {
            DeserializedHumorFileReader humorData = new DeserializedHumorFileReader(mDirPath);
            humorData.objectDeserializer(USER_CHOSEN_HUMOR_FILE);
            this.mIndex = humorData.getIndex();
            this.mCommentTxt = humorData.getCommentTxt();
            this.mCurrentDayForHistoric = humorData.getCurrentDayForHistoric();
            Log.d("add", String.valueOf(mIndex + " " + mCurrentDayForHistoric));
        } else {
            this.mIndex = 3;
            this.mCommentTxt = null;
            this.mCurrentDayForHistoric = 1;
        }
        MainController smileyStarter = MainController.INSTANCE;
        smileyStarter.getMethodName(mIndex);
    }
}
