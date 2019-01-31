package com.s0mbr3.moodtracker.activities;

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
import com.s0mbr3.moodtracker.core.controllers.AppStartDriver;
import com.s0mbr3.moodtracker.core.controllers.HumorUpdater;
import com.s0mbr3.moodtracker.core.models.DeserializedHumorFileReader;
import com.s0mbr3.moodtracker.core.models.Humor;

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
    private DisplayMetrics mDisplayMetrics;
    private Button mCommentButton;
    private int mHeight;
    private int mWidth;
    private Map<Integer, String> mCommentHash = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) throws OutOfMemoryError {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        mDisplayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mHeight= mDisplayMetrics.heightPixels;
        mWidth = mDisplayMetrics.widthPixels;
        configs = AppStartDriver.INSTANCE;
        mMainDir = configs.getMainDirPath();
        mHistoricDir = configs.getHistoricDir();

        AppStartDriver appStartDriver = AppStartDriver.INSTANCE;
        int hIndex= appStartDriver.getCurrentDayForHistoric()-1;
        try {
            Map<Integer, File> commentHash = new HashMap<Integer, File>();

            for(int index = hIndex ; index > hIndex-7; index--){
                commentHash.put(index, new File(mMainDir + mHistoricDir + index));
            }
            for(int index = hIndex-6, dIndex = commentHash.size() - 1; dIndex >= 0; index++, dIndex--) {
                //Log.d("alarmist", String.valueOf(index));
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
    }


    public void historicLiner(Map<Integer, File> filesList, int index, int dIndex){
        String aDayFile = filesList.get(index).getName();

        mLayout = findViewById(R.id.activity_historic_layout);
        DeserializedHumorFileReader aDayHumor = new DeserializedHumorFileReader(mMainDir + mHistoricDir);
        aDayHumor.objectDeserializer(aDayFile);
        mIndex = aDayHumor.getIndex();
        mCommentTxt = aDayHumor.getCommentTxt();
        mCurrentDayForHistoric = aDayHumor.getCurrentDayForHistoric();
        mAdayMessage = configs.getHistoricMessage(dIndex);


        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        TextView historicLine = new TextView(this);
        mCommentButton = new Button(this);
        historicLine.setId(View.generateViewId());
        historicLine.setText(mAdayMessage);
        Humor humor = new Humor(historicLine, mLayout, constraintLayout, mHeight, mWidth);
        if(mCommentTxt == null)humor.createHistoricLine(mIndex);
        else humor.createHistoricLine(mIndex, mCommentButton);
        /*Log.d("ala", String.valueOf(mIndex) + " " + filesList.size() + " " + aDayFile + " " + mCurrentDayForHistoric
                + " " + mCommentTxt);*/
    }

    @Override
    public void onClick(View v) {
        int commentIndex = (int) v.getTag();
        Toast.makeText(this, mCommentHash.get(commentIndex), Toast.LENGTH_SHORT).show();

    }
}
