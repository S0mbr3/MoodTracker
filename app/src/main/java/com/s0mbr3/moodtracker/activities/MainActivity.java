package com.s0mbr3.moodtracker.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.core.controllers.AlarmReceiver;
import com.s0mbr3.moodtracker.core.controllers.AppStartDriver;
import com.s0mbr3.moodtracker.core.controllers.HumorUpdater;
import com.s0mbr3.moodtracker.core.controllers.MainController;
import com.s0mbr3.moodtracker.core.controllers.MyGestureListener;
import com.s0mbr3.moodtracker.core.controllers.SerialiazedHumorFileWriter;

import java.util.Calendar;


/**
 * MainActivity class is the first activity of the application
 * it allows user to select a smiley by swiping on the screen, add a comment to detail their humor
 * and watch their weekly humor historic
 */
public class MainActivity extends AppCompatActivity {
    private AlarmManager mAlarmMgr;
    private PendingIntent mAlarmIntent;
    private Intent mIntent;
    private ImageView mSmiley;
    private Button mCommentBtn;
    private Button mHistoricBtn;
    private ConstraintLayout mLayout;
    private GestureDetectorCompat mDetector;
    private String mCommentTxt;
    private int mIndex;
    private int mCurrentDayForHistoric;
    private String mDirPath;
    private String mFilePath;
    private boolean mCommentTester;
    private SharedPreferences mPreferences;
    private AppStartDriver appStartDriver;
    private Calendar mCalendar;
    private SerialiazedHumorFileWriter mSerializedHumorFileWriter;
    public static final int  HISTORIC_ACTIVITY_REQUEST_CODE = 1337;
    public static final String PREF_KEY_COMMENT_TXT = "PREF_KEY_COMMENT_TXT";
    public static final String BUNDLE_EXTRA_COMMENT_TXT = "BUNDLE_EXTRA_COMMENT_TXT";
    public static final String BUNDLE_EXTRA_HUMORS_LIST_INDEX = "BUNDLE_HUMORS_LIST_INDEX";

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

        mSmiley = findViewById(R.id.activity_main_smiley_image);
        mLayout = findViewById(R.id.activity_main_layout);
        mCommentBtn = findViewById(R.id.activity_main_comment_btn);
        mHistoricBtn = findViewById(R.id.activity_main_historic_btn);

        mSerializedHumorFileWriter = new SerialiazedHumorFileWriter();
        appStartDriver = AppStartDriver.INSTANCE;
        appStartDriver.configurator(MainActivity.this);
        mIndex = appStartDriver.getIndex();
        mDirPath = appStartDriver.getMainDirPath();
        mFilePath = appStartDriver.getHumorFilePath();
        mCommentTxt = appStartDriver.getmCommentTxt();

        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.HOUR_OF_DAY, 12);
        mCalendar.set(Calendar.MINUTE, 51);
        mCalendar.set(Calendar.SECOND,0);
        mCalendar.set(Calendar.MILLISECOND,0);

        mIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        mAlarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //dev purpose i will remove the updat ecurrent flag of the pending intent
        mAlarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(),mAlarmMgr.INTERVAL_FIFTEEN_MINUTES, mAlarmIntent);

        mPreferences = getPreferences(MODE_PRIVATE);


        Log.d("index", String.valueOf(appStartDriver.getIndex()));
        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
        this.historic();


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
        final MainController mainController = new MainController(mLayout, mSmiley);
        mainController.getMethodName(appStartDriver.getIndex());
        mCommentTxt = appStartDriver.getmCommentTxt();
        MyGestureListener myGestureListener = new MyGestureListener(mainController);
        myGestureListener.setIndexListener(new MyGestureListener.IndexGetter() {
            /**
             * getIndex is a custom listener to catch the index used to travel in the humorsList
             * serialize the weekly day, index  and comment and write it into a dedicated file
             * @param index
             */
            @Override
            public void getIndex(int index) {
                Log.d("getIndex", String.valueOf(index) +  " " + mCommentTxt + " " + mCurrentDayForHistoric);
                mIndex = index;
                mCurrentDayForHistoric = appStartDriver.getCurrentDayForHistoric();
                mSerializedHumorFileWriter.SerializedHumorFileWriting(mIndex, mCommentTxt,
                        mCurrentDayForHistoric, mDirPath + mFilePath);
            }
        });
        mDetector = new GestureDetectorCompat(this, myGestureListener);

        HumorUpdater humorUpdater = HumorUpdater.getInstance();

        humorUpdater.setUpdaterListener(new HumorUpdater.UpdateAfterAlarm() {
            @Override
            public void updaterAfterAlarm() {
                Log.d("ala", "bigTest");
                mainController.getMethodName(appStartDriver.getIndex());
            }
        });
        Log.d("alarmR", "Deretour");
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
                        Log.d("addComment", mCommentTxt + " " + mIndex);
                        appStartDriver.setCommentTxt(mCommentTxt);
                        mSerializedHumorFileWriter.SerializedHumorFileWriting(mIndex,
                                mCommentTxt, mCurrentDayForHistoric, mDirPath + mFilePath);
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

                Intent historicActivityIntent = new Intent(MainActivity.this, HistoricActivity.class);
                startActivityForResult(historicActivityIntent, HISTORIC_ACTIVITY_REQUEST_CODE);
            }
        });
    }

}
