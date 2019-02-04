package com.s0mbr3.moodtracker.controllers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.DeserializedHumorFileReader;
import com.s0mbr3.moodtracker.models.HumorUpdater;
import com.s0mbr3.moodtracker.models.SelectedHumorSerializer;
import com.s0mbr3.moodtracker.models.SerialiazedHumorFileWriter;
import com.s0mbr3.moodtracker.models.StatisticsSerializer;
import com.s0mbr3.moodtracker.models.StatisticsUnSerializer;

import java.io.File;


/**
 * AlarmReceiver class is the schedule task broadcast receiver
 * Created by Oxhart on 22/01/2019.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager mNotificationManager;
    private String mDirPath;
    private SerialiazedHumorFileWriter mSerializedHumorForHistoric;
    private int mIndex;

    /**
     * onReceive event is triggered when the schedule task is fired, it read from a serialized
     * humor object containing the index of the humors list and the comment associated if there is
     * one. then it prepare another serialization task for the historic activity
     *
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        AppStartDriver appStartDriver = AppStartDriver.INSTANCE;
        mDirPath = context.getFilesDir().getAbsolutePath();
        String currenntHumorFilePath = appStartDriver.getHumorFilePath();
        String historicDir = appStartDriver.getHistoricDir();
        new File(mDirPath + historicDir).mkdir();

        DeserializedHumorFileReader humorData = new DeserializedHumorFileReader();
        humorData.objectDeserializer(mDirPath + currenntHumorFilePath);

        mIndex = humorData.getIndex();
        String mCommentTxt = humorData.getCommentTxt();
        int currentDayForHistoric = humorData.getCurrentDayForHistoric();



        mSerializedHumorForHistoric = new SerialiazedHumorFileWriter();
        mSerializedHumorForHistoric.SerializedHumorFileWriting(new SelectedHumorSerializer(
                mIndex,
                mCommentTxt,
                currentDayForHistoric),
                mDirPath + historicDir + String.valueOf(currentDayForHistoric));

        ++currentDayForHistoric;

        appStartDriver.setCurrentDayForHistoric(currentDayForHistoric);
        appStartDriver.setCommentTxt(null);
        appStartDriver.setIndex(3);
        HumorUpdater.getInstance().updateTrigger();
        mSerializedHumorForHistoric.SerializedHumorFileWriting(new SelectedHumorSerializer(
                mIndex,
                mCommentTxt,
                currentDayForHistoric),
                mDirPath + currenntHumorFilePath );

        statistics();
        if(!appStartDriver.isAlive()) {
            showNotification(context);
            Notification(context);
        }

        boolean f = new File(mDirPath + currenntHumorFilePath).exists();
        Log.d("AlarmReceiver", mCommentTxt + " " + mIndex + " " + currentDayForHistoric + " " + f);
    }

    private void Notification(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Une nouvelle journée commence")
                .setContentText("N'oubliez pas de venir suivre votre humeur")
                .setAutoCancel(true);
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 1, intent, 0);
        builder.setContentIntent(pi);
        mNotificationManager.notify(1, builder.build());
    }

    private void showNotification(Context context) {
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "default",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Alarm triggered");
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    private void statistics(){
        StatisticsUnSerializer humorDay = new StatisticsUnSerializer();
        humorDay.objectUnserializer(mDirPath + AppStartDriver.INSTANCE.STATISTICS_DIR + mIndex);
        int dayHumor = humorDay.getDays();

        mSerializedHumorForHistoric.SerializedHumorFileWriting(new StatisticsSerializer(
                ++dayHumor),
                mDirPath + AppStartDriver.INSTANCE.STATISTICS_DIR + mIndex);

    }
}
