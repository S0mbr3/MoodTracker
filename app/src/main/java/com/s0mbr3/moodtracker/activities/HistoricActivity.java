package com.s0mbr3.moodtracker.activities;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.core.controllers.AppStartDriver;
import com.s0mbr3.moodtracker.core.controllers.MainController;
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
        MainController mMaincontroller = MainController.INSTANCE;
        DeserializedHumorFileReader aDayHumor = new DeserializedHumorFileReader(mMainDir + mHistoricDir);
        aDayHumor.objectDeserializer(aDayFile);
        mIndex = aDayHumor.getIndex();
        mCommentTxt = aDayHumor.getCommentTxt();
        mCurrentDayForHistoric = aDayHumor.getCurrentDayForHistoric();
        mAdayMessage = configs.getHistoricMessage(index);


        TextView historicLine = new TextView(this);
        mMaincontroller.setHistoricLayout(historicLine, mHeight, mWidth);
        historicLine.setText(mAdayMessage);
        mMaincontroller.getMethodName(mIndex, true);
        mLayout.addView(historicLine);
        Log.d("mich", String.valueOf(mIndex) + " " + filesList.size());
        ++index;
        if (filesList.size() > index) historicLiner(filesList, index);
    }
}
