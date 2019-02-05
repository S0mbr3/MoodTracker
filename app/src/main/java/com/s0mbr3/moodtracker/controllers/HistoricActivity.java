package com.s0mbr3.moodtracker.controllers;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.HumorUpdater;
import com.s0mbr3.moodtracker.models.DeserializedHumorFileReader;
import com.s0mbr3.moodtracker.views.HistoricActivityView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HistoricActivity extends AppCompatActivity implements View.OnClickListener {
    private String mMainDir;
    private String mHistoricDir;
    private int mIndex;
    private int mCurrentDayForHistoric;
    private String mCommentTxt;
    private String mAdayMessage;
    private LinearLayout mLayout;
    private AppStartDriver configs;
    private Button mCommentButton;
    private int mHeight;
    private int mWidth;
    private Map<Integer, String> mCommentHash = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) throws OutOfMemoryError {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        mLayout = findViewById(R.id.activity_historic_layout);
        mHeight = AppStartDriver.INSTANCE.getHeight();
        mWidth = AppStartDriver.INSTANCE.getWidth();
        configs = AppStartDriver.INSTANCE;
        mMainDir = configs.getMainDirPath();
        mHistoricDir = configs.getHistoricDir();

        int historicIndex= AppStartDriver.INSTANCE.getCurrentDayForHistoric()-1;
        int currentHistoricDay = AppStartDriver.INSTANCE.getCurrentDayForHistoric();
        if (historicIndex >= 7) historicIndex-=7;
        else historicIndex-=historicIndex;
        Log.d("alarmister", String.valueOf(historicIndex));
        try {
            Map<Integer, File> commentHash = new HashMap<Integer, File>();

            for(int index = currentHistoricDay-1 ; index > historicIndex; index--){
                commentHash.put(index, new File(mMainDir + mHistoricDir + index));
                Log.d("alarmisterr", String.valueOf(commentHash.get(index)));
            }
            for(int index = currentHistoricDay-commentHash.size()
                , dIndex = commentHash.size() - 1; dIndex >= 0; index++, dIndex--) {
                Log.d("alarmist", String.valueOf(index));
                historicLiner(commentHash, index, dIndex);
                if (mCommentTxt != null){
                    mCommentButton.setTag(index);
                    mCommentButton.setOnClickListener(this);
                    mCommentHash.put(index, mCommentTxt);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
HumorUpdater humorUpdater = HumorUpdater.getInstance();

        humorUpdater.setUpdaterListener(new HumorUpdater.UpdateAfterAlarm() {
            @Override
            public void updaterAfterAlarm() {
                //Log.d("ala", "bigTest");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        AppStartDriver.INSTANCE.setAlive();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppStartDriver.INSTANCE.unSetAlive();
    }

    public void historicLiner(Map<Integer, File> filesList, int index, int dIndex){
        String aDayFile = filesList.get(index).getName();

        DeserializedHumorFileReader aDayHumor = new DeserializedHumorFileReader();
        aDayHumor.objectDeserializer(mMainDir + mHistoricDir+ aDayFile);
        mIndex = aDayHumor.getIndex();
        mCommentTxt = aDayHumor.getCommentTxt();
        mCurrentDayForHistoric = aDayHumor.getCurrentDayForHistoric();
        mAdayMessage = configs.getHistoricMessage(dIndex);


        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        TextView historicLine = new TextView(this);
        mCommentButton = new Button(this);
        historicLine.setId(View.generateViewId());
        historicLine.setText(mAdayMessage);
        HistoricActivityView historicActivityView = new HistoricActivityView(historicLine, mLayout, constraintLayout, mHeight, mWidth);
        if(mCommentTxt == null) historicActivityView.createHistoricLine(mIndex);
        else historicActivityView.createHistoricLine(mIndex, mCommentButton);
        Log.d("ala", String.valueOf(mIndex) + " " + filesList.size() + " " + aDayFile + " " + mCurrentDayForHistoric
                + " " + mCommentTxt);
    }

    @Override
    public void onClick(View v) {
        int commentIndex = (int) v.getTag();
        Toast.makeText(this, mCommentHash.get(commentIndex), Toast.LENGTH_SHORT).show();

    }
}
