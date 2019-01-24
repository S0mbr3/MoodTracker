package com.s0mbr3.moodtracker.main.controllers;


import com.s0mbr3.moodtracker.main.models.SelectedHumorSerializer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerialiazedHumorFileWriter {
    private ObjectOutputStream objectOutputStream;
    private int mIndex;
    private String mCommentTxt;
    private String mDirPath;
    private int mCurrentDayForHistoric;

    public SerialiazedHumorFileWriter(int index, String commentTxt,int currentDayFprHistoric
                                      ,String filePath) {
        this.mDirPath = filePath;
        this.mIndex = index;
        this.mCommentTxt = commentTxt;
        this.mCurrentDayForHistoric = currentDayFprHistoric;
    }

    public void SerializedHumorFileWriting(String filePath) {

        try {
            objectOutputStream = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(mDirPath, filePath))));

            objectOutputStream.writeObject(new SelectedHumorSerializer(this.mIndex,
                    this.mCommentTxt, this.mCurrentDayForHistoric));
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                if(objectOutputStream != null) objectOutputStream.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
