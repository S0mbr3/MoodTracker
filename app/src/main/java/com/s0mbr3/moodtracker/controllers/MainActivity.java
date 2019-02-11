package com.s0mbr3.moodtracker.controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.HumorUpdater;
import com.s0mbr3.moodtracker.models.MyAlarmManager;
import com.s0mbr3.moodtracker.models.SharedPreferencesManager;
import com.s0mbr3.moodtracker.views.MainActivityView;


/**
 * MainActivity class is the first activity of the application
 * it allows user to select a smiley by swiping on the screen, add a comment to detail their humor
 * watch their weekly humor historic, their statistics, and hearing a sweet sound when they do change
 * humor
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
	private AppStartDriver appStartDriver;
	private Button mStatisticsButton;
	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;

	/**
	 * Initialisation of the main variables, and prepare for the other activities
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		mEditor = mPreferences.edit();
		mStatisticsButton = findViewById(R.id.activity_main_statistics_btn);
		mSmiley = findViewById(R.id.activity_main_smiley_image);
		mLayout = findViewById(R.id.activity_main_layout);
		mCommentBtn = findViewById(R.id.activity_main_comment_btn);
		mHistoricBtn = findViewById(R.id.activity_main_historic_btn);

		appStartDriver = AppStartDriver.INSTANCE;

			MyAlarmManager alarmManager = new MyAlarmManager();
			alarmManager.setAlarm(this);

		mCommentBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addComment();
			}
		});
		this.historic();
		this.statistics();
		this.getSize();
		Log.d("on create", "ouioui");


	}
	//fetching saved data regarding the humor and in which day we are in
	private void starter(){
		SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(
				MainActivity.this.getApplicationContext());
		Object[] data = sharedPreferencesManager.getSelectedHumor();
		mIndex = (int) data[0];
		mCurrentDayForHistoric = (int) data[1];
		mCommentTxt = (String) data[2];
		if(!sharedPreferencesManager.isErased()){
			sharedPreferencesManager.deletePreferences();
		}
	}

	/**
	 * Event to catch screen gestures
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);

	}

	/**
	 * Launch the gestures catch, sound play, and save the index into the Shared preferences
	 * custom even listener humorUpdater to refresh the activity if the user is infront of it
	 * when the AlarmManager ring
	 */
	@Override
	protected void onResume() {
		super.onResume();
		this.starter();
		final MainActivityView mainActivityView = new MainActivityView(mLayout, mSmiley);
		mLayout.post(new Runnable() {
			@Override
			public void run() {
				mainActivityView.constrainSet();
				mainActivityView.getMethodName(mIndex);
			}
		});
		mPreviousSound = MediaPlayer.create(this, appStartDriver.INSTANCE.getSound(mIndex));
		MyGestureListener myGestureListener = new MyGestureListener(mainActivityView, mIndex);
		myGestureListener.setIndexListener(new MyGestureListener.IndexGetter() {
			/**
			 * getIndex is a custom listener to catch the index used to travel in the humorsList
			 * serialize the weekly day, index  and comment and write it into a dedicated file
			 * @param index
			 * 		Bring the index needed to change the humor by reflection
			 */
			@Override
			public void getIndex(int index) {
				mIndex = index;
				mSound = MediaPlayer.create(MainActivity.this,
						appStartDriver.INSTANCE.getSound(mIndex));
				indexTester();
				mSound.start();
				mEditor.putInt(getString(R.string.indexKey), mIndex);
				mEditor.apply();
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
	}

	//little trick to cut the sound when swiping
	public void indexTester(){
		try {
			if (mPreviousSound.isPlaying()) {
				mPreviousSound.stop();
				mPreviousSound.reset();
				mPreviousSound.release();
			}
			mPreviousSound = mSound;
		} catch (IllegalStateException e){
			e.printStackTrace();
		}
	}

	//reseting the MediaPlayer when quiting the activity
	@Override
	protected void onPause() {
		super.onPause();
		appStartDriver.unSetAlive();
		try {
			if (mSound != null) {
				if (mSound.isPlaying())
					mSound.stop();
				mSound.reset();
				mSound.release();
				mSound = null;
			}
		} catch (NullPointerException e){
			e.printStackTrace();
		}
	}

	/**
	 * Manage the comment button and the saving of it's messages
	 */
	private void addComment(){
		final EditText commentInput = new EditText(this);
		commentInput.setText(mCommentTxt);
		if(mCommentTxt != null) commentInput.setSelection(mCommentTxt.length());
		commentInput.setHint(getString(R.string.commentaryHint));
		new AlertDialog.Builder(this)
				.setTitle(getString(R.string.commentary))
				.setView(commentInput)
				.setPositiveButton(getString(R.string.VALIDATE), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						if(commentInput.getText().toString().length() == 0) mCommentTxt = null;
						else mCommentTxt = commentInput.getText().toString();
						mEditor.putString(getString(R.string.commentKey), mCommentTxt);
						mEditor.apply();
					}
				})
				.setNegativeButton(getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
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
				Intent historicActivityIntent = new Intent(
						MainActivity.this, HistoricActivity.class);
				startActivity(historicActivityIntent);
			}
		});
	}

	/**
	 * Manage the statistics button and launch it's activity
	 */
	private void statistics(){
		mStatisticsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mCurrentDayForHistoric > 1) {
					Intent statisticsActivityIntent = new Intent(
							MainActivity.this, StatisticsActivity.class);
					startActivity(statisticsActivityIntent);
				} else {
					Toast.makeText(MainActivity.this,getString(R.string.statisticButton),
							Toast.LENGTH_SHORT).show();

				}
			}
		});

	}

	/**
	 * Grabbing the size of the screen width/height and calculate for both portrait/landscape
	 * it's real layout size (required to draw the historic, statistics and adjust humor icon)
	 */
	public void getSize(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
		final int deviceWidth = displayMetrics.widthPixels;
		final int deviceHeight = displayMetrics.heightPixels;
		mLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						int height = mLayout.getMeasuredHeight();
						int width = mLayout.getMeasuredWidth();
						int orientation = getResources().getConfiguration().orientation;
						if(orientation == Configuration.ORIENTATION_PORTRAIT) {
							appStartDriver.setPortLayoutSize(width, height);

							int widthDiff = deviceWidth - width;
							int heightDiff = deviceHeight - height;

							int landWidth = height + heightDiff - widthDiff;
							int landHeight = width - heightDiff + widthDiff;
							appStartDriver.setLandLayoutSize(landWidth, landHeight);
						} else {
							appStartDriver.setLandLayoutSize(width, height);

							int widthDiff = deviceWidth - width;
							int heightDiff = deviceHeight - height;

							int landWidth = height + heightDiff - widthDiff;
							int landHeight = width - heightDiff + widthDiff;
							appStartDriver.setPortLayoutSize(landWidth, landHeight);
						}
						mLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

					}
				}
		);
	}

}
