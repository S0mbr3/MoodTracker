package com.s0mbr3.moodtracker.controller.MainControllers;

import com.s0mbr3.moodtracker.model.SelectedHumorSerializer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DeserializedHumorFileReader {
    private ObjectInputStream mObjectInputStream;
    private int mIndex;
    private String mCommentTxt;
    private SelectedHumorSerializer deserializedHumor;
    private String mFilePath;

    public DeserializedHumorFileReader(String filePath){
        mIndex = 3;
        mFilePath = filePath;
    }
    public void objectDeserializer(){
        try {
            mObjectInputStream = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(
                                    new File(mFilePath))));

           deserializedHumor = (SelectedHumorSerializer)mObjectInputStream.readObject();
           mIndex = deserializedHumor.getIndex();
           mCommentTxt = deserializedHumor.getCommentTxt();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } finally {
            if(mObjectInputStream != null){
                try {
                    mObjectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getIndex(){
        return mIndex;
    }

    public String getmCommentTxt() {
        return mCommentTxt;
    }
}
