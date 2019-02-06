package com.s0mbr3.moodtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.HumorUpdater;
import com.s0mbr3.moodtracker.models.StatisticsUnSerializer;
import com.s0mbr3.moodtracker.models.StreakUnserializer;
import com.s0mbr3.moodtracker.views.MainActivityView;
import com.s0mbr3.moodtracker.views.StatisticsActivityView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {
	private LinearLayout mStatisticsLayout;
	private int mIndex;
	private StatisticsUnSerializer mHumorDayUnserializer;
	private int mSumScore;
	private int mTotalUsageDays;
	private int mHeight;
	private int mWidth;
	private LinearLayout mLinearGraphLayout;
	private ConstraintLayout mMoodLayout;
	private int mTotalScore;
	private String mDirPath;
	private static final float scale = Resources.getSystem().getDisplayMetrics().density;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		mStatisticsLayout = findViewById(R.id.statistics_activity_layout);
		mHumorDayUnserializer = new StatisticsUnSerializer();
		mDirPath = this.getFilesDir().getName();
		mIndex = 0;
		mTotalUsageDays = AppStartDriver.INSTANCE.getCurrentDayForHistoric() - 1;
		Map<String, Integer> sizeStorage = AppStartDriver.INSTANCE.getSize();
		int orientation = getResources().getConfiguration().orientation;
		if(orientation == Configuration.ORIENTATION_PORTRAIT) {
			mHeight = sizeStorage.get("portHeight");
			mWidth = sizeStorage.get("portWidth");
		} else {
			mHeight = sizeStorage.get("landHeight");
			mWidth = sizeStorage.get("landWidth");
		}
		mLinearGraphLayout = new LinearLayout(this);
		mLinearGraphLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mLinearGraphLayout.setLayoutParams(lp);
		mStatisticsLayout.addView(mLinearGraphLayout);
		graphDrawer();
		scoreWriter();
		moodDrawer();
		resetButton();

		HumorUpdater humorUpdater = HumorUpdater.getInstance();

		humorUpdater.setUpdaterListener(new HumorUpdater.UpdateAfterAlarm() {
			@Override
			public void updaterAfterAlarm() {
				//Log.d("ala", "bigTest");
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
		Log.d("isalive", String.valueOf(AppStartDriver.INSTANCE.isAlive()));
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppStartDriver.INSTANCE.unSetAlive();
		Log.d("isalive", "ouloulou");
	}

	public void graphDrawer(){
		if(new File(this.getFilesDir() + AppStartDriver.INSTANCE.STATISTICS_DIR + mIndex).exists()){
			mHumorDayUnserializer.objectUnserializer(
					this.getFilesDir() + AppStartDriver.INSTANCE.STATISTICS_DIR + mIndex);
			int humorDay = mHumorDayUnserializer.getDays();
			mSumScore += humorDay * mIndex;

			TextView graphLine = new TextView(this);
			graphLine.setId(View.generateViewId());

			TextView graphText = new TextView(this);
			graphText.setId(View.generateViewId());
			graphText.setTextColor(Color.parseColor("#FFFFFF"));
			graphText.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			graphText.setText(String.valueOf(humorDay));
			ConstraintLayout grapLayout = new ConstraintLayout(this);
			grapLayout.setId(View.generateViewId());
			StatisticsActivityView drawGraph = new StatisticsActivityView(mLinearGraphLayout, graphLine,
					mHeight, mWidth, mTotalUsageDays, humorDay, grapLayout, graphText);
			drawGraph.getMethodName(mIndex);

		}
		++mIndex;

		if(mIndex <= 4) graphDrawer();
	}


	public void scoreWriter(){

		StreakUnserializer streakUnserializer = new StreakUnserializer();
		streakUnserializer.objectUnserializer(
				this.getFilesDir() + AppStartDriver.INSTANCE.STREAK_FILE);
		int totalStreak = streakUnserializer.getTotalStreak();
		int currentStreak = streakUnserializer.getCurrentStreak();
		int additionalScore = streakUnserializer.getAdditionalScore();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, mHeight / 4 / 3);

		mTotalScore = mTotalScoreCalculation(additionalScore);
		textViewGenerator(lp, "Jours total: " + String.valueOf(mTotalUsageDays));
		textViewGenerator(lp, "Série de bonne humeurs total: " + String.valueOf(totalStreak));
		textViewGenerator(lp,
				"Série de bonne humeurs en cour: " + String.valueOf(currentStreak));
		textViewGenerator(lp,
				"MoodScore : " + String.valueOf(mTotalScore));
	}

	public void textViewGenerator(LinearLayout.LayoutParams lp, String text){
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setTextColor(Color.parseColor("#FFFFFF"));
		textView.setLayoutParams(lp);
		mStatisticsLayout.addView(textView);

	}

	public int mTotalScoreCalculation(int additionalScore){
		int mTotalScore = (int) ((double) mSumScore / (mTotalUsageDays *4) * 1000) + additionalScore;
		if (mTotalScore > 1000) mTotalScore = 1000;
		return mTotalScore;
	}

	public void moodDrawer(){
		mMoodLayout = new ConstraintLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mWidth, mHeight/3);
		mMoodLayout.setLayoutParams(lp);
		mStatisticsLayout.addView(mMoodLayout);

		ImageView smiley = new ImageView(this);
		smiley.setId(View.generateViewId());
		mMoodLayout.addView(smiley);
		ConstraintSet set = new ConstraintSet();
		set.clone(mMoodLayout);
		set.constrainWidth(smiley.getId(), mWidth/2);
		set.constrainHeight(smiley.getId(), mHeight/2/3);
		//set.centerHorizontally(smiley.getId(), ConstraintSet.PARENT_ID);
		set.centerVertically(smiley.getId(), ConstraintSet.PARENT_ID);
		set.applyTo(mMoodLayout);
		int index = 0;
		if(mTotalScore < 200) index = 0;
		else if(mTotalScore >= 200 && mTotalScore < 400) index = 1;
		else if(mTotalScore >= 400 && mTotalScore < 600) index = 2;
		else if(mTotalScore >= 600 && mTotalScore < 800) index = 3;
		else if(mTotalScore >= 800) index = 4;

		Log.d("score", "l'index est: " + index);

		MainActivityView moodSelector = new MainActivityView(mMoodLayout, smiley);
		moodSelector.getMethodName(index);

	}

	public void resetButton(){
		Button resetButton = new Button(this);
		resetButton.setId(View.generateViewId());
		mMoodLayout.addView(resetButton);
		ConstraintSet set = new ConstraintSet();
		set.clone(mMoodLayout);
		set.constrainHeight(resetButton.getId(), ConstraintSet.WRAP_CONTENT);
		set.constrainWidth(resetButton.getId(), ConstraintSet.WRAP_CONTENT);
		set.connect(resetButton.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID,
				ConstraintSet.RIGHT, dpToPx(20));
		set.centerVertically(resetButton.getId(), ConstraintSet.PARENT_ID);
		set.applyTo(mMoodLayout);

		resetButton.setText("RESET");
		resetButton.setTextColor(Color.parseColor("#FFFFFF"));
		resetButton.setBackgroundColor(Color.parseColor("#282828"));

		resetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(StatisticsActivity.this)
						.setTitle("Réinitialiser statistiques")
						.setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								deleteFolders();
								AppStartDriver.INSTANCE.configurator(StatisticsActivity.this);
								Intent intent = getIntent();
								finish();
								startActivity(intent);
							}
						})
						.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						})
						.show();
			}
		});

	}

	public void deleteFolders(){
		List<File> files = new ArrayList<File>();
		Log.d("dele", new File(this.getFilesDir() + AppStartDriver.INSTANCE.STATISTICS_DIR).getAbsolutePath());
		Log.d("dele", String.valueOf(new File(this.getFilesDir().getName())));
		//List<File> test = Arrays.asList(new File(this.getFilesDir().getAbsolutePath() + AppStartDriver.INSTANCE.STATISTICS_DIR).listFiles());
		files = Arrays.asList(new File(this.getFilesDir().getAbsolutePath()).listFiles());
		for(File file : files){
			if(file.isDirectory()){
				Log.d("deleeee", String.valueOf(file));
				List<File> files1 = Arrays.asList(new File(String.valueOf(file)).listFiles());
				for(File file1: files1){
					Log.d("deletion", String.valueOf(file));
					file1.delete();
				}
				Log.d("deleter", String.valueOf(file));
				file.delete();
			} else file.delete();
		}

	}

	private int dpToPx(int dp)
	{
		return (int) (dp * this.scale);
	}
}

