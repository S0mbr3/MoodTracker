package com.s0mbr3.moodtracker.models;

import android.annotation.SuppressLint;
import android.util.ArrayMap;
import android.util.SparseArray;

import com.s0mbr3.moodtracker.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Application driver, used to exchange data between activities very fast, no need to write and read
 * when the application is alive to trade data
 * Also used to keep some constants at the same place
 */
public enum AppStartDriver {
    INSTANCE;

    private boolean mIsAlive = false;
    private int mIndex;
    private String mCommentTxt;
    private int mCurrentDayForHistoric;
    private ArrayMap<String, Integer> mSize = new ArrayMap<>();
    @SuppressLint("UseSparseArrays")
    private SparseArray<Integer> mSounds = new SparseArray<>();
    private boolean mSet;
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
        this.mSet = false;
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

    public void init(int index, int historicDay, String commentTxt){
        this.mIndex = index;
        this.mCurrentDayForHistoric = historicDay;
        this.mCommentTxt = commentTxt;
    }
    public boolean isAlarmSet(){
        return this.mSet;
    }

    public void setAlarm(){
        this.mSet = true;
    }
}
