package com.s0mbr3.moodtracker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.core.controllers.AppStartDriver;
import com.s0mbr3.moodtracker.core.controllers.MyComparator;
import com.s0mbr3.moodtracker.core.models.DeserializedHumorFileReader;
import com.s0mbr3.moodtracker.core.models.Humor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private DisplayMetrics mDisplayMetrics;
    private int mHeight;
    private int mWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        mDisplayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mHeight= mDisplayMetrics.heightPixels;
        mWidth = mDisplayMetrics.widthPixels;
        configs = AppStartDriver.INSTANCE;
        mMainDir = configs.getMainDirPath();
        mHistoricDir = configs.getHistoricDir();

        try {
            List<File> files = new ArrayList<File>();
            files = Arrays.asList(new File(mMainDir + mHistoricDir).listFiles());
            Log.d("siz", String.valueOf(files.size()));
            Collections.sort(files, new MyComparator());
            for(File file : files){
                Log.d("ala", file.getName());
            }
            files = listReverser(files);
            historicLiner(files, files.size() - 1);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void historicLiner(List<File> filesList, int index){
        String aDayFile = filesList.get(index).getName();

        mLayout = findViewById(R.id.activity_historic_layout);
        DeserializedHumorFileReader aDayHumor = new DeserializedHumorFileReader(mMainDir + mHistoricDir);
        aDayHumor.objectDeserializer(aDayFile);
        mIndex = aDayHumor.getIndex();
        mCommentTxt = aDayHumor.getCommentTxt();
        mCurrentDayForHistoric = aDayHumor.getCurrentDayForHistoric();
        mAdayMessage = configs.getHistoricMessage(index);


        TextView historicLine = new TextView(this);
        historicLine.setText(mAdayMessage);
        Humor humor = Humor.INSTANCE;
        humor.setHistoricLayout(historicLine, mLayout, this, mHeight, mWidth);
        humor.createHistoricLine(mIndex);
        Log.d("ala", String.valueOf(mIndex) + " " + filesList.size() + " " + aDayFile + " " + mCurrentDayForHistoric
                + " " + mCommentTxt);
        --index;
        if (index >= 0) historicLiner(filesList, index);
    }

    public List<File> listReverser(List<File> historyFiles){
        List<File> reversed = new ArrayList<File>();
        for(int i = historyFiles.size() - 1 ; i >= 0; i--){
            reversed.add(historyFiles.get(i));
        }
        return reversed;
    }

}
