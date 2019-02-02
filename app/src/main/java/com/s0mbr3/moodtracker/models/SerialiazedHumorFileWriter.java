package com.s0mbr3.moodtracker.models;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * SerializedHumorFileWriterClass write into a file a SelectedHumorSerializer object
 * Containing the humorsList index the actual comment and the day of the week we are
 *
 * @see SelectedHumorSerializer
 */
public class SerialiazedHumorFileWriter {
    private ObjectOutputStream objectOutputStream;
    private int mIndex;
    private String mCommentTxt;
    private int mCurrentDayForHistoric;

    public SerialiazedHumorFileWriter() {
    }

    public void SerializedHumorFileWriting(int index, String commentTxt,int currentDayFprHistoric, String filePath) {
        this.mIndex = index;
        this.mCommentTxt = commentTxt;
        this.mCurrentDayForHistoric = currentDayFprHistoric;

        try {
            objectOutputStream = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(filePath))));

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
