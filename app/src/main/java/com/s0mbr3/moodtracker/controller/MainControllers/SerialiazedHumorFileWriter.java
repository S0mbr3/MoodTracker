package com.s0mbr3.moodtracker.controller.MainControllers;


import com.s0mbr3.moodtracker.model.SelectedHumorSerializer;

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
    private String mFilePath;

    public SerialiazedHumorFileWriter(int index, String commentTxt, String filePath) {
        mFilePath = filePath;
        this.mIndex = index;
        this.mCommentTxt = commentTxt;
    }

    public void SerializedHumorFileWriting() {

        try {
            objectOutputStream = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(mFilePath))));

            objectOutputStream.writeObject(new SelectedHumorSerializer(this.mIndex, this.mCommentTxt));
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
