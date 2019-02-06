package com.s0mbr3.moodtracker.models;


import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

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
public class SerializedObjectFileWriter {

    public SerializedObjectFileWriter() {
    }

    public <T> void SerializedHumorFileWriting(T serializer, String filePath) {
        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(filePath))));

            objectOutputStream.writeObject(serializer);
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
