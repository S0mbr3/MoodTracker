package com.s0mbr3.moodtracker.models;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * DeserializedHumorFileReader read from a file a SelectedHumorSerializer object
 * triggered in the OnReceive event of the AlarmReceiver class
 *
 * @see SelectedHumorSerializer
 */
public class DeserializedHumorFileReader {
    private ObjectInputStream mObjectInputStream;
    private int mIndex;
    private String mCommentTxt;
    private int mCurrentDayForHistoric;

    public DeserializedHumorFileReader(){
        mIndex = 3;
    }
    public void objectDeserializer(String filePath){
        try {
            mObjectInputStream = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(
                                    new File(filePath))));

            SelectedHumorSerializer deserializedHumor = (SelectedHumorSerializer) mObjectInputStream.readObject();
           mIndex = deserializedHumor.getIndex();
           mCommentTxt = deserializedHumor.getCommentTxt();
           mCurrentDayForHistoric = deserializedHumor.getCurrentDayForHistoric();
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

    public String getCommentTxt() {
        return mCommentTxt;
    }

    public int getCurrentDayForHistoric(){return mCurrentDayForHistoric;}
}
