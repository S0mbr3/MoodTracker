package com.s0mbr3.moodtracker.activities;

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
import com.s0mbr3.moodtracker.core.controllers.MyComparator;
import com.s0mbr3.moodtracker.core.models.DeserializedHumorFileReader;
import com.s0mbr3.moodtracker.core.models.Humor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private List<String> mCommentList = new ArrayList<String>();

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
            files = listReverser(files);
            for(int index = files.size() -1; index >= 0; --index) {
                historicLiner(files, index);
                mCommentButton.setTag(index);
                mCommentButton.setOnClickListener(this);
                if(mCommentTxt != null)mCommentList.add(mCommentTxt);
            }
            if(mCommentList.size() != 0)mCommentList = listReverser(mCommentList);
            for(File file : files){
                Log.d("alarmi", file + " " + mCommentButton.getTag());
            }
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


        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        TextView historicLine = new TextView(this);
        mCommentButton = new Button(this);
        historicLine.setId(View.generateViewId());
        historicLine.setText(mAdayMessage);
        Humor humor = new Humor(historicLine, mLayout, constraintLayout, mHeight, mWidth);
        if(mCommentTxt == null)humor.createHistoricLine(mIndex);
        else humor.createHistoricLine(mIndex, mCommentButton);
        Log.d("ala", String.valueOf(mIndex) + " " + filesList.size() + " " + aDayFile + " " + mCurrentDayForHistoric
                + " " + mCommentTxt);
    }


    public <T> List<T> listReverser(List<T> historyFiles){
        List<T> reversed = new ArrayList<T>();
        for(int i = historyFiles.size() - 1 ; i >= 0; i--){
            reversed.add(historyFiles.get(i));
        }
        return reversed;
    }

    @Override
    public void onClick(View v) {
        int commentIndex = (int) v.getTag();
        Toast.makeText(this, mCommentList.get(commentIndex), Toast.LENGTH_SHORT).show();

    }
}
