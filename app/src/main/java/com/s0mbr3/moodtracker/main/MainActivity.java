package com.s0mbr3.moodtracker.main;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.historic.HistoricActivity;
import com.s0mbr3.moodtracker.main.controllers.AlarmReceiver;
import com.s0mbr3.moodtracker.main.controllers.MainController;
import com.s0mbr3.moodtracker.main.controllers.MyGestureListener;
import com.s0mbr3.moodtracker.main.controllers.SerialiazedHumorFileWriter;

import java.util.Calendar;


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
    private MainController mMainController;
    private MyGestureListener mMyGestureListener;
    private Calendar mCalendar;
    private SerialiazedHumorFileWriter mSerializedHumorFileWriter;
    public static final int  HISTORIC_ACTIVITY_REQUEST_CODE = 1337;
    public static final String PREF_KEY_COMMENT_TXT = "PREF_KEY_COMMENT_TXT";
    public static final String BUNDLE_EXTRA_COMMENT_TXT = "BUNDLE_EXTRA_COMMENT_TXT";
    public static final String BUNDLE_EXTRA_HUMORS_LIST_INDEX = "BUNDLE_HUMORS_LIST_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCommentTester = false;
        mIndex = 3;
        mCurrentDayForHistoric = 1;
        mDirPath = this.getFilesDir().getPath();
        mFilePath = "selectedHumor.txt";
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.HOUR_OF_DAY, 16);
        mCalendar.set(Calendar.MINUTE, 35);
        mCalendar.set(Calendar.SECOND,0);
        mCalendar.set(Calendar.MILLISECOND,0);

        mIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        mIntent.putExtra(BUNDLE_EXTRA_COMMENT_TXT, mDirPath);
        mAlarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(),mAlarmMgr.INTERVAL_FIFTEEN_MINUTES, mAlarmIntent);

        mSmiley = findViewById(R.id.activity_main_smiley_image);
        mLayout = findViewById(R.id.activity_main_layout);
        mCommentBtn = findViewById(R.id.activity_main_comment_btn);
        mHistoricBtn = findViewById(R.id.activity_main_historic_btn);
        mMainController = new MainController(mLayout, mSmiley);
        mMyGestureListener = new MyGestureListener(3,mMainController);
        mPreferences = getPreferences(MODE_PRIVATE);

        mDetector = new GestureDetectorCompat(this, mMyGestureListener);

        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
        this.historic();


        mMyGestureListener.setIndexListener(new MyGestureListener.IndexGetter() {
            @Override
            public void getIndex(int index) {
                Log.d("getIndex", String.valueOf(index) + mCommentTxt);
                mIndex = index;
                mSerializedHumorFileWriter = new SerialiazedHumorFileWriter(mIndex, mCommentTxt,
                        mCurrentDayForHistoric,mDirPath);
                mSerializedHumorFileWriter.SerializedHumorFileWriting(mFilePath);
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
                        Log.d("addComment", mCommentTxt + " " + mIndex);
                        mSerializedHumorFileWriter = new SerialiazedHumorFileWriter(mIndex,
                                mCommentTxt, mCurrentDayForHistoric, mDirPath);
                        mSerializedHumorFileWriter.SerializedHumorFileWriting(mFilePath);
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

}
