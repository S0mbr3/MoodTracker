package com.s0mbr3.moodtracker.main.models;

import java.io.Serializable;

public class SelectedHumorSerializer implements Serializable {
    private int mIndex;
    private String mCommentTxt;
    private int mCurrentDayForHistoric;

    public SelectedHumorSerializer(int index, String commentTxt, int currentDayForHistoric) {
        this.mIndex = index;
        this.mCommentTxt = commentTxt;
        this.mCurrentDayForHistoric = currentDayForHistoric;
    }

    public int getIndex() {
        return mIndex;
    }

    public String getCommentTxt() {
        return mCommentTxt;
    }
    public int getmCurrentDayForHistoric() {return this.mCurrentDayForHistoric;}


    public String toString() {
        return "The index that will be serialized  is "  + this.mIndex + "\n" +
                "The comment that will be serialized is " + this.mCommentTxt +
                "The current day of the humors historic is " + this.mCurrentDayForHistoric;
    }
}
