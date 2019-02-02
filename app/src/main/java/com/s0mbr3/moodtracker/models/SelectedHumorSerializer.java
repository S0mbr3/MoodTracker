package com.s0mbr3.moodtracker.models;

import com.s0mbr3.moodtracker.controllers.MainActivity;

import java.io.Serializable;

/**
 * SelectedHumorSerializer serialize the index used to travel into humorsList in the getIndex
 * method in the MainActivity the actual comment in the addComment method in the MainActivity
 * and the day that will be used to prepare the historic serialization
 *
 * @see MainActivity
 */
public class SelectedHumorSerializer implements Serializable {
    private int mIndex;
    private String mCommentTxt;
    private int mCurrentDayForHistoric;

    public SelectedHumorSerializer(int index, String commentTxt, int currentDayForHistoric) {
        this.mIndex = index;
        this.mCommentTxt = commentTxt;
        this.mCurrentDayForHistoric = currentDayForHistoric;
    }

    int getIndex() {
        return mIndex;
    }

    String getCommentTxt() {
        return mCommentTxt;
    }
    int getCurrentDayForHistoric() {return this.mCurrentDayForHistoric;}


    public String toString() {
        return "The index that will be serialized  is "  + this.mIndex + "\n" +
                "The comment that will be serialized is " + this.mCommentTxt +
                "The current day of the humors historic is " + this.mCurrentDayForHistoric;
    }
}
