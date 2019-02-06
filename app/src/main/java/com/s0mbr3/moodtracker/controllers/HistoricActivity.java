package com.s0mbr3.moodtracker.controllers;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.HumorUpdater;
import com.s0mbr3.moodtracker.models.DeserializedHumorFileReader;
import com.s0mbr3.moodtracker.models.SizeManager;
import com.s0mbr3.moodtracker.views.HistoricActivityView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HistoricActivity extends AppCompatActivity implements View.OnClickListener {
    private String mMainDir;
    private String mHistoricDir;
    private int mIndex;
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
        SizeManager sizeManager = new SizeManager();
        Object[] obj = sizeManager.sizeManager(mHeight, mWidth);
        mHeight = (int) obj[0];
        mWidth = (int) obj[1];
        configs = AppStartDriver.INSTANCE;
        mMainDir = configs.getMainDirPath();
        mHistoricDir = configs.getHistoricDir();

        int historicIndex= AppStartDriver.INSTANCE.getCurrentDayForHistoric()-1;
        int currentHistoricDay = AppStartDriver.INSTANCE.getCurrentDayForHistoric();
        if (historicIndex >= 7) historicIndex-=7;
        else historicIndex-=historicIndex;

        this.historicBuilder(currentHistoricDay, historicIndex);
        HumorUpdater humorUpdater = HumorUpdater.getInstance();
        humorUpdater.setUpdaterListener(new HumorUpdater.UpdateAfterAlarm() {
            @Override
            public void updaterAfterAlarm() {
                if(AppStartDriver.INSTANCE.isAlive()) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        AppStartDriver.INSTANCE.setAlive();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppStartDriver.INSTANCE.unSetAlive();
    }

    public void historicBuilder(int currentHistoricDay, int historicIndex){
        try {
            Map<Integer, File> commentHash = new HashMap<Integer, File>();

            for(int index = currentHistoricDay-1 ; index > historicIndex; index--){
                commentHash.put(index, new File(mMainDir + mHistoricDir + index));
            }
            for(int index = currentHistoricDay-commentHash.size()
                , dIndex = commentHash.size() - 1; dIndex >= 0; index++, dIndex--) {
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
    }
    public void historicLiner(Map<Integer, File> filesList, int index, int dIndex){
        String aDayFile = filesList.get(index).getName();

        DeserializedHumorFileReader aDayHumor = new DeserializedHumorFileReader();
        aDayHumor.objectDeserializer(mMainDir + mHistoricDir+ aDayFile);
        mIndex = aDayHumor.getIndex();
        mCommentTxt = aDayHumor.getCommentTxt();
        mAdayMessage = configs.getHistoricMessage(dIndex);


        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        TextView historicLine = new TextView(this);
        mCommentButton = new Button(this);
        historicLine.setId(View.generateViewId());
        historicLine.setText(mAdayMessage);
        HistoricActivityView historicActivityView = new HistoricActivityView(historicLine, mLayout, constraintLayout, mHeight, mWidth);
        if(mCommentTxt == null) historicActivityView.createHistoricLine(mIndex);
        else historicActivityView.createHistoricLine(mIndex, mCommentButton);
    }

    @Override
    public void onClick(View v) {
        int commentIndex = (int) v.getTag();
        Toast.makeText(this, mCommentHash.get(commentIndex), Toast.LENGTH_SHORT).show();

    }
}
