package com.s0mbr3.moodtracker.core.controllers;

import android.content.Context;
import android.util.Log;

import com.s0mbr3.moodtracker.core.models.DeserializedHumorFileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public enum AppStartDriver {
    INSTANCE;

    private int mIndex;
    private String mCommentTxt;
    private int mCurrentDayForHistoric;
    private String mDirPath;
    private static final String HISTORIC_DIR = "/historicdir";
    private static final String USER_CHOSEN_HUMOR_FILE = "/selectedhumor.txt";
    private List<String> mHistoricMessgesList = new ArrayList<>();


    AppStartDriver(){
        this.mHistoricMessgesList.add("Hier");
        this.mHistoricMessgesList.add("Avant-hier");
        this.mHistoricMessgesList.add("Il y a trois jours");
        this.mHistoricMessgesList.add("Il y a quatre jours");
        this.mHistoricMessgesList.add("Il y a cinqu jours");
        this.mHistoricMessgesList.add("Il y a six jours");
        this.mHistoricMessgesList.add("Il y a une semaine");
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
    public String getHistoricMessage(int index){
        return this.mHistoricMessgesList.get(index);
    }

    public void configurator(Context context){
        this.mDirPath = context.getFilesDir().getAbsolutePath();
        if(new File(this.mDirPath, USER_CHOSEN_HUMOR_FILE).exists()) {
            DeserializedHumorFileReader humorData = new DeserializedHumorFileReader(mDirPath);
            humorData.objectDeserializer(USER_CHOSEN_HUMOR_FILE);
            this.mIndex = humorData.getIndex();
            this.mCommentTxt = humorData.getCommentTxt();
            if(this.mCurrentDayForHistoric >=7) mCurrentDayForHistoric = 1;
            else this.mCurrentDayForHistoric = humorData.getCurrentDayForHistoric();
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
