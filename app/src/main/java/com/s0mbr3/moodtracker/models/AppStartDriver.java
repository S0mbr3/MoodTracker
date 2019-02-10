package com.s0mbr3.moodtracker.models;

import android.app.PendingIntent;
import android.content.Context;
import android.util.ArrayMap;
import android.util.SparseArray;

import com.s0mbr3.moodtracker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum AppStartDriver {
    INSTANCE;

    private PendingIntent mIntent;
    private boolean mIsAlive = false;
    private int mIndex;
    private String mCommentTxt;
    private int mCurrentDayForHistoric;
    private String mDirPath;
    private static final String HISTORIC_DIR = "/historicdir/";
    public static final String STATISTICS_DIR = "/statistics/";
    private static final String USER_CHOSEN_HUMOR_FILE = "/selectedhumor.txt";
    public static final String NOTIFICATION_FILE = "/notificate.txt";
    public static final String STREAK_FILE = "/streak.txt";
    private ArrayMap<String, Integer> mSize = new ArrayMap<String, Integer>();
    private SparseArray<Integer> mSounds = new SparseArray<Integer>();
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
        mSounds.put(0, R.raw.sad);
        mSounds.put(1, R.raw.disappointed);
        mSounds.put(2, R.raw.normal);
        mSounds.put(3, R.raw.happy);
        mSounds.put(4, R.raw.superhappy);
    }



    public int getSound(int index){
        return this.mSounds.get(index);
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

    public String getCommentTxt(){
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

    public void setDeviceSize(int width, int height){
        this.mSize.put("deviceWidth", width);
        this.mSize.put("deviceHeight", height);
    }

    public void setPortLayoutSize(int width, int height){
        this.mSize.put("portWidth", width);
        this.mSize.put("portHeight", height);
    }

    public void setLandLayoutSize(int width, int height){
        this.mSize.put("landWidth", width);
        this.mSize.put("landHeight", height);
    }

    public Map<String, Integer> getSize(){
        return this.mSize;
    }

    public String getHumor(int index){
        return HUMORS_LIST.get(index);

    }
    public boolean pendingTester(PendingIntent intent){
        boolean test = false;
        if (mIntent != null)  test = mIntent.equals(intent);
        mIntent = intent;
        return test;
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
            this.mCurrentDayForHistoric = humorData.getCurrentDayForHistoric();
        } else {
            this.mIndex = 3;
            this.mCommentTxt = null;
            this.mCurrentDayForHistoric = 1;
            SerializedObjectFileWriter humorFileWriter = new SerializedObjectFileWriter();
            humorFileWriter.SerializedHumorFileWriting(new SelectedHumorSerializer(
                            mIndex,
                            mCommentTxt,
                            mCurrentDayForHistoric),
                    mDirPath + USER_CHOSEN_HUMOR_FILE);
        }
    }
}
