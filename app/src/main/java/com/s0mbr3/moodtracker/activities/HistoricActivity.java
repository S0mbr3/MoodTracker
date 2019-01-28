package com.s0mbr3.moodtracker.activities;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.core.controllers.AppStartDriver;
import com.s0mbr3.moodtracker.core.models.DeserializedHumorFileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HistoricActivity extends AppCompatActivity {
    private String mMainDir;
    private String mHistoricDir;
    private int mIndex;
    private int mCurrentDayForHistoric;
    private String mCommentTxt;
    private String mAdayMessage;
    private LinearLayout mLayout;
    private AppStartDriver configs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        configs = AppStartDriver.INSTANCE;
        mMainDir = configs.getMainDirPath();
        mHistoricDir = configs.getHistoricDir();
        List<String> results = new ArrayList<String>();
        File[] files = new File(mMainDir + mHistoricDir).listFiles();
//If this pathname does not denote a directory, then listFiles() returns null.

        //will have to test for empty folder to prevent crashes
        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
                Log.d("ala", file.getName());
            }
        }
        historicLiner(results, 0);
    }

    public void historicLiner(List<String> filesList, int index){
        String aDayFile = filesList.get(index);

        mLayout = findViewById(R.id.activity_historic_layout);
        DeserializedHumorFileReader aDayHumor = new DeserializedHumorFileReader(mMainDir + mHistoricDir);
        aDayHumor.objectDeserializer(aDayFile);
        mIndex = aDayHumor.getIndex();
        mCommentTxt = aDayHumor.getCommentTxt();
        mCurrentDayForHistoric = aDayHumor.getCurrentDayForHistoric();
        mAdayMessage = configs.getHistoricMessage(index);


        TextView historicLine = new TextView(this);
        LinearLayout.LayoutParams loparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,100/7);
        historicLine.setLayoutParams(loparams);
        //ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //historicLine.setLayoutParams(lp);
        historicLine.setText(mAdayMessage);
        mLayout.addView(historicLine);
        historicLine.setBackgroundColor(0xffde3c50);
        Log.d("mich", String.valueOf(mCurrentDayForHistoric) + " " + filesList.size());
        ++index;
        if (filesList.size() > index) historicLiner(filesList, index);
    }
}
