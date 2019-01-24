package com.s0mbr3.moodtracker.controller;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.SettingInjectorService;
import android.os.Build;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.controller.MainControllers.AlarmReceiver;
import com.s0mbr3.moodtracker.controller.MainControllers.MainController;
import com.s0mbr3.moodtracker.controller.MainControllers.MyGestureListener;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Intent mIntent;
    private ImageView mSmiley;
    private Button mCommentBtn;
    private Button mHistoricBtn;
    private ConstraintLayout mLayout;
    private GestureDetectorCompat mDetector;
    private String mCommentTxt;
    private SharedPreferences mPreferences;
    private MainController mMainController;
    private MyGestureListener mMyGestureListener;
    private Calendar mCalendar;
    public static final int  HISTORIC_ACTIVITY_REQUEST_CODE = 1337;
    public static final String PREF_KEY_COMMENT_TXT = "PREF_KEY_COMMENT_TXT";
    public static final String BUNDLE_EXTRA_COMMENT_TXT = "BUNDLE_EXTRA_COMMENT_TXT";
    public static final String BUNDLE_EXTRA_HUMORS_LIST_INDEX = "BUNDLE_HUMORS_LIST_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mIntent = new Intent(MainActivity.this, AlarmReceiver.class);

        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mSmiley = findViewById(R.id.activity_main_smiley_image);
        mLayout = findViewById(R.id.activity_main_layout);
        mCommentBtn = findViewById(R.id.activity_main_comment_btn);
        mHistoricBtn = findViewById(R.id.activity_main_historic_btn);
        mMainController = new MainController(mLayout, mSmiley);
        mMyGestureListener = new MyGestureListener(3,mMainController);
        mPreferences = getPreferences(MODE_PRIVATE);

        // Set the alarm to start at approximately 2:00 p.m.
        mDetector = new GestureDetectorCompat(this, mMyGestureListener);

        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
        this.historic();


// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
        mMyGestureListener.setIndexListener(new MyGestureListener.IndexGetter() {
            @Override
            public void getIndex(int index) {
                Log.d("getIndex", String.valueOf(index) + mCommentTxt);
                setAlarm(BUNDLE_EXTRA_HUMORS_LIST_INDEX, index);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    private void addComment(){
        final EditText commentInput = new EditText(this);
        commentInput.setHint("Commentez votre Humeur!");
        new AlertDialog.Builder(this)
                .setTitle("Commentaire")
                .setView(commentInput)
                .setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mCommentTxt = commentInput.getText().toString();
                        Log.d("addComment", mCommentTxt);
                        setAlarm(BUNDLE_EXTRA_COMMENT_TXT, mCommentTxt);
                    }
                })
                .setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();

    }

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

    private void setCalendar(){
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis() + 60*60*24*1000);
        mCalendar.set(Calendar.HOUR_OF_DAY, 17);
        mCalendar.set(Calendar.MINUTE, 48);
        mCalendar.set(Calendar.SECOND,0);
        mCalendar.set(Calendar.MILLISECOND,0);
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), alarmIntent);
        Log.d("Alarm3", String.valueOf(mCalendar.getTimeInMillis()) + "    " + String.valueOf(System.currentTimeMillis()));
        Log.d("Alarm4", String.valueOf(mCalendar.getTimeInMillis() < System.currentTimeMillis()));
    }
    private void setAlarmCore(){
        String alarmState = null;
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try{
                alarmState = String.valueOf(alarmMgr.getNextAlarmClock());
                Log.d("Alarm", alarmState);
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            alarmState = Settings.System.getString(getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED);
            Log.d("Alarm1", alarmState);
        }
        if(alarmState == null){
            setCalendar();
        } else
            Log.d("Alarm1", alarmState);
    }


    private void setAlarm(String constant, String value) {
        mIntent.putExtra(constant, value);
    }

    @SuppressLint("NewApi")
    private void setAlarm(String constant, int value ){
        //Log.d("Alarm Clock", alarmMgr.getNextAlarmClock().toString());
        mIntent.putExtra(constant, value);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        setAlarmCore();
    }
}
