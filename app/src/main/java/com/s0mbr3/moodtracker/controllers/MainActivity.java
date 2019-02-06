package com.s0mbr3.moodtracker.controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.StatisticsActivity;
import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.HumorUpdater;
import com.s0mbr3.moodtracker.models.MyAlarmManager;
import com.s0mbr3.moodtracker.models.SelectedHumorSerializer;
import com.s0mbr3.moodtracker.views.MainActivityView;
import com.s0mbr3.moodtracker.models.SerialiazedHumorFileWriter;


/**
 * MainActivity class is the first activity of the application
 * it allows user to select a smiley by swiping on the screen, add a comment to detail their humor
 * and watch their weekly humor historic
 */
public class MainActivity extends AppCompatActivity {
    private ImageView mSmiley;
    private Button mCommentBtn;
    private Button mHistoricBtn;
    private ConstraintLayout mLayout;
    private GestureDetectorCompat mDetector;
    private String mCommentTxt;
    private int mIndex;
    private MediaPlayer mSound;
    private MediaPlayer mPreviousSound;
    private int mCurrentDayForHistoric;
    private String mDirPath;
    private String mFilePath;
    private SharedPreferences mPreferences;
    private AppStartDriver appStartDriver;
    private SerialiazedHumorFileWriter mSerializedHumorFileWriter;
    public static final int  HISTORIC_ACTIVITY_REQUEST_CODE = 1337;
    public static final String PREF_KEY_COMMENT_TXT = "PREF_KEY_COMMENT_TXT";
    private Button mStatisticsButton;

    /**
     * onCreate events initialize members variables of the activities at it creation as their
     * widgets view screens (smiley, comment button, historic button)
     * Instanciation of the Alarm manager to schedule daily saving of humor and comment at midnight
     * Instanciation of the MyGestureListener to catch screen gestures of the user
     * Instanciation of  the  comment button click listener mCommentBtn
     * Instanciation of the historic  button click listener by this historic method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatisticsButton = findViewById(R.id.activity_main_statistics_btn);
        mSmiley = findViewById(R.id.activity_main_smiley_image);
        mLayout = findViewById(R.id.activity_main_layout);
        mCommentBtn = findViewById(R.id.activity_main_comment_btn);
        mHistoricBtn = findViewById(R.id.activity_main_historic_btn);

        mSerializedHumorFileWriter = new SerialiazedHumorFileWriter();
        appStartDriver = AppStartDriver.INSTANCE;
        appStartDriver.configurator(MainActivity.this);
        mIndex = appStartDriver.getIndex();
        mPreviousSound = MediaPlayer.create(this, appStartDriver.INSTANCE.getSound(mIndex));
        mDirPath = appStartDriver.getMainDirPath();
        mFilePath = appStartDriver.getHumorFilePath();
        mCommentTxt = appStartDriver.getmCommentTxt();

        MyAlarmManager alarmManager = new MyAlarmManager();
        alarmManager.setAlarm(MainActivity.this);

        mPreferences = getPreferences(MODE_PRIVATE);

        Log.d("index", String.valueOf(appStartDriver.getIndex()));
        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
        this.historic();
        this.statistics();
        this.sizeManager();

    }

    /**
     * Event to catch screen gestures
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    @Override
    protected void onResume() {
        super.onResume();
        final MainActivityView mainActivityView = new MainActivityView(mLayout, mSmiley);
        mainActivityView.constrainSet();
        mainActivityView.getMethodName(appStartDriver.getIndex());
        mCommentTxt = appStartDriver.getmCommentTxt();
        MyGestureListener myGestureListener = new MyGestureListener(mainActivityView);
        myGestureListener.setIndexListener(new MyGestureListener.IndexGetter() {
            /**
             * getIndex is a custom listener to catch the index used to travel in the humorsList
             * serialize the weekly day, index  and comment and write it into a dedicated file
             * @param index
             */
            @Override
            public void getIndex(int index) {
                mIndex = index;
                mSound = MediaPlayer.create(MainActivity.this,
                        appStartDriver.INSTANCE.getSound(mIndex));
                indexTester();
                mSound.start();
                mCurrentDayForHistoric = appStartDriver.getCurrentDayForHistoric();
                mSerializedHumorFileWriter.SerializedHumorFileWriting(new SelectedHumorSerializer(
                                mIndex, mCommentTxt, mCurrentDayForHistoric),
                        mDirPath + mFilePath);
            }
        });
        mDetector = new GestureDetectorCompat(this, myGestureListener);

        appStartDriver.setAlive();
        HumorUpdater humorUpdater = HumorUpdater.getInstance();

        humorUpdater.setUpdaterListener(new HumorUpdater.UpdateAfterAlarm() {
            @Override
            public void updaterAfterAlarm() {
                if(appStartDriver.isAlive()) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });
        Log.d("isalive", "Deretour");
    }

    public void indexTester(){
    	if(mPreviousSound.isPlaying()){
    	    mPreviousSound.stop();
    	    mPreviousSound.reset();
    	    mPreviousSound.release();
        }
    	mPreviousSound = mSound;
    }

    @Override
    protected void onPause() {
        super.onPause();
        appStartDriver.unSetAlive();
        if(mSound.isPlaying()) {
            mSound.stop();
            mSound.reset();
            mSound.release();
        }
        Log.d("isalive", "ouloulou");
    }

    /**
     * addComment method manage the mCommentBtn and show an AlertDialog to let the user comment is
     * daily humor, then serialize the index, comment and weekly day and write the object into a
     * dedicated file
     */
    private void addComment(){
        mCurrentDayForHistoric = appStartDriver.getCurrentDayForHistoric();
        final EditText commentInput = new EditText(this);
        commentInput.setText(mCommentTxt);
        if(mCommentTxt != null) commentInput.setSelection(mCommentTxt.length());
        commentInput.setHint("Commentez votre Humeur!");
        new AlertDialog.Builder(this)
                .setTitle("Commentaire")
                .setView(commentInput)
                .setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(commentInput.getText().toString().length() == 0) mCommentTxt = null;
                        else mCommentTxt = commentInput.getText().toString();
                        //Log.d("addComment", mCommentTxt + " " + mIndex);
                        appStartDriver.setCommentTxt(mCommentTxt);
                        mSerializedHumorFileWriter.SerializedHumorFileWriting(
                                new SelectedHumorSerializer(mIndex, mCommentTxt, mCurrentDayForHistoric),
                                mDirPath + mFilePath);
                    }
                })
                .setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();

    }

    /**
     * the historic method contain manage the historic button activity when triggered it send
     * the user to the historic activity
     */
    private void historic(){
        mHistoricBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreferences.edit().putString(PREF_KEY_COMMENT_TXT, mCommentTxt).apply();

                Intent historicActivityIntent = new Intent(
                        MainActivity.this, HistoricActivity.class);
                startActivityForResult(historicActivityIntent, HISTORIC_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void statistics(){
        mStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statisticsActivityIntent = new Intent(
                        MainActivity.this, StatisticsActivity.class);
                startActivity(statisticsActivityIntent);
            }
        });

    }

    public void sizeManager(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        final int deviceWidth = displayMetrics.widthPixels;
        final int deviceHeight = displayMetrics.heightPixels;
        Log.d("deviceSize",deviceHeight + " " +  deviceWidth);
        appStartDriver.setDeviceSize(deviceWidth, deviceHeight);
        mLayout.post(new Runnable(){
            public void run(){
                int height = mLayout.getMeasuredHeight();
                int width = mLayout.getMeasuredWidth();
                Log.d("deviceLayoutSize",height + " " +  width);
                int orientation = getResources().getConfiguration().orientation;
                if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                    appStartDriver.setPortLayoutSize(width, height);

                    int widthDiff = deviceWidth - width;
                    int heightDiff = deviceHeight - height;

                    int landWidth = height + heightDiff - widthDiff;
                    int landHeight = width - heightDiff + widthDiff;
                    appStartDriver.setLandLayoutSize(landWidth, landHeight);
                    Log.d("devicediff", heightDiff + " " + widthDiff);
                } else {
                    appStartDriver.setLandLayoutSize(width, height);

                    int widthDiff = deviceWidth - width;
                    int heightDiff = deviceHeight - height;

                    int landWidth = height + heightDiff - widthDiff;
                    int landHeight = width - heightDiff + widthDiff;
                    appStartDriver.setPortLayoutSize(landWidth, landHeight);
                }
            }
        });
    }


}
