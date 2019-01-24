package com.s0mbr3.moodtracker.model;

import java.io.Serializable;

public class SelectedHumorSerializer implements Serializable {
    private int mIndex;
    private String mCommentTxt;

    public SelectedHumorSerializer(int index, String commentTxt) {
        this.mIndex = index;
        this.mCommentTxt = commentTxt;
    }

    public int getIndex() {
        return mIndex;
    }

    public String getCommentTxt() {
        return mCommentTxt;
    }


    public String toString() {
        return "The index that will be serialized  is "  + this.mIndex + "\n" +
                "The comment that will be serialized is " + this.mCommentTxt;
    }
}
