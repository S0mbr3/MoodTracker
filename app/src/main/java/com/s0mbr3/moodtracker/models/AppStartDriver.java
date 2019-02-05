package com.s0mbr3.moodtracker.models;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AppStartDriver {
    INSTANCE;

    private boolean mIsAlive = false;
    private int mHeight;
    private int mWidth;
    private int mIndex;
    private String mCommentTxt;
    private int mCurrentDayForHistoric;
    private String mDirPath;
    private static final String HISTORIC_DIR = "/historicdir/";
    public static final String STATISTICS_DIR = "/statistics/";
    private static final String USER_CHOSEN_HUMOR_FILE = "/selectedhumor.txt";
    public static final String NOTIFICATION_FILE = "/notificate.txt";
    private static final List<String> HISTORIC_MESSAGES_LIST = new ArrayList<>(Arrays.asList(
            "Hier",
            "Avant-Hier",
            "Il y a trois jours",
            "Il y a quatre jours",
            "Il y a cinq jours",
            "Il y a six jours",
            "Il y a une semaine"));
    private static final List<String> HUMORS_LIST = new ArrayList<>(Arrays.asList(
            "setSadSmiley",
            "setDisappointedSmiley",
            "setNormalSmiley",
            "setHappySmiley",
            "setSuperHappySmiley"));


    AppStartDriver(){
    }



    public void unSetAlive(){
        this.mIsAlive = false;
    }
    public void setAlive(){
        this.mIsAlive = true;
    }

    public boolean isAlive(){return this.mIsAlive;}

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

    public void setIndex(int index){
        this.mIndex = index;
    }

    public void setCommentTxt(String commentTxt){
        this.mCommentTxt = commentTxt;
    }
    public void setCurrentDayForHistoric(int currentDayForHistoric){
        this.mCurrentDayForHistoric = currentDayForHistoric;

    }

    public void setHeight(int height){
        this.mHeight = height;
    }

    public int getHeight(){return this.mHeight;}

    public void setWidth(int width){
        this.mWidth = width;
    }

    public int getWidth(){
        return this.mWidth;
    }

    public String getHumor(int index){
        return HUMORS_LIST.get(index);

    }
    public String getHistoricMessage(int index){
        return HISTORIC_MESSAGES_LIST.get(index);
    }

    public void configurator(Context context){
        this.mDirPath = context.getFilesDir().getAbsolutePath();
        if(new File(this.mDirPath, USER_CHOSEN_HUMOR_FILE).exists()) {
            DeserializedHumorFileReader humorData = new DeserializedHumorFileReader();
            humorData.objectDeserializer(mDirPath + USER_CHOSEN_HUMOR_FILE);
            this.mIndex = humorData.getIndex();
            this.mCommentTxt = humorData.getCommentTxt();
            //if(mCurrentDayForHistoric >= 8) mCurrentDayForHistoric = 1;
            this.mCurrentDayForHistoric = humorData.getCurrentDayForHistoric();
            //Log.d("add", String.valueOf(mIndex + " " + mCurrentDayForHistoric));
            //new File(mDirPath + HISTORIC_DIR + "8").delete();
        } else {
            this.mIndex = 3;
            this.mCommentTxt = null;
            this.mCurrentDayForHistoric = 1;
            SerialiazedHumorFileWriter humorFileWriter = new SerialiazedHumorFileWriter();
            humorFileWriter.SerializedHumorFileWriting(new SelectedHumorSerializer(
                    mIndex,
                    mCommentTxt,
                    mCurrentDayForHistoric),
                    mDirPath + USER_CHOSEN_HUMOR_FILE);
        }
    }
}
