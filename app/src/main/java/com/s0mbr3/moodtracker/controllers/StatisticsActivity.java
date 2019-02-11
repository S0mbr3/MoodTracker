package com.s0mbr3.moodtracker.controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.HumorUpdater;
import com.s0mbr3.moodtracker.models.SharedPreferencesManager;
import com.s0mbr3.moodtracker.models.SizeManager;
import com.s0mbr3.moodtracker.views.MainActivityView;
import com.s0mbr3.moodtracker.views.StatisticsActivityView;

/**
 * Statistics controller, compute, prepare, draw
 */
public class StatisticsActivity extends AppCompatActivity {
	private LinearLayout mStatisticsLayout;
	private int mSumScore;
	private int mTotalUsageDays;
	private int mHeight;
	private int mWidth;
	private LinearLayout mLinearGraphLayout;
	private ConstraintLayout mMoodLayout;
	private int mTotalScore;
	private static final float scale = Resources.getSystem().getDisplayMetrics().density;
	private SharedPreferencesManager mSharedPreferencesManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		mSharedPreferencesManager = new SharedPreferencesManager(this);
		mStatisticsLayout = findViewById(R.id.statistics_activity_layout);
		mTotalUsageDays = mSharedPreferencesManager.getHistoryDay() - 1;
		SizeManager sizeManager = new SizeManager();
		Object[] obj = sizeManager.sizeManager();
		mHeight = (int) obj[0];
		mWidth = (int) obj[1];
		setLinearGraphLayout();
		graphDrawer();
		scoreWriter();
		moodDrawer();
		resetButton();

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

	//sub layout to handle graphs and texts graphs
	private void setLinearGraphLayout(){
		mLinearGraphLayout = new LinearLayout(this);
		mLinearGraphLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mLinearGraphLayout.setLayoutParams(lp);
		mStatisticsLayout.addView(mLinearGraphLayout);
	}


	/**
	 * Prepare the elements to draw the graphs, fetch the data and initliaze basic elements
	 * @see StatisticsActivityView
	 */
	public void graphDrawer(){
		for(int i = 0; i <= 4; i++){
			int humorDay = mSharedPreferencesManager.getDaysPerHumor(i);
			mSumScore += humorDay * i;

			TextView graphLine = new TextView(this);
			graphLine.setId(View.generateViewId());

			TextView graphText = new TextView(this);
			graphText.setId(View.generateViewId());
			graphText.setTextColor(Color.parseColor("#FFFFFF"));
			graphText.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			graphText.setText(String.valueOf(humorDay));
			ConstraintLayout grapLayout = new ConstraintLayout(this);
			grapLayout.setId(View.generateViewId());
			if(humorDay != 0) {
				StatisticsActivityView drawGraph = new StatisticsActivityView(mLinearGraphLayout, graphLine,
						mHeight, mWidth, mTotalUsageDays, humorDay, grapLayout, graphText);
				drawGraph.getMethodName(i);
			}
		}
	}


	//draw the statistics elements to the user middle screen
	public void scoreWriter(){
		Object[] streaks = mSharedPreferencesManager.getStreaks();
		int totalStreak = (int) streaks[0];
		int currentStreak = (int) streaks[1];
		int additionalScore = (int) streaks[2];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, mHeight / 4 / 3);

		mTotalScore = mTotalScoreCalculation(additionalScore);
		textViewGenerator(lp,getString(R.string.totalDays) + String.valueOf(mTotalUsageDays));
		textViewGenerator(lp, getString(R.string.maxStreak) + String.valueOf(totalStreak));
		textViewGenerator(lp, getString(R.string.currentStreak)+ String.valueOf(currentStreak));
		textViewGenerator(lp, getString(R.string.moodScore)+ String.valueOf(mTotalScore) +
				"/1000");
	}

	public void textViewGenerator(LinearLayout.LayoutParams lp, String text){
		TextView textView = new TextView(this);
		textView.setText(text);
		float size = getResources().getDimension(R.dimen.historic_text_size);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		textView.setTextColor(Color.parseColor("#FFFFFF"));
		textView.setLayoutParams(lp);
		mStatisticsLayout.addView(textView);

	}

	public int mTotalScoreCalculation(int additionalScore){
		int mTotalScore = (int) ((double) mSumScore / (mTotalUsageDays *4) * 1000) + additionalScore;
		if (mTotalScore > 1000) mTotalScore = 1000;
		return mTotalScore;
	}

	/**
	 * Draw the actual mood corresponding to the score of the user
	 * showing a reset button to erase the stats
	 */
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
		set.centerVertically(smiley.getId(), ConstraintSet.PARENT_ID);
		set.applyTo(mMoodLayout);
		int index = 0;
		if(mTotalScore < 200) index = 0;
		else if(mTotalScore >= 200 && mTotalScore < 400) index = 1;
		else if(mTotalScore >= 400 && mTotalScore < 600) index = 2;
		else if(mTotalScore >= 600 && mTotalScore < 800) index = 3;
		else if(mTotalScore >= 800) index = 4;

		MainActivityView moodSelector = new MainActivityView(mMoodLayout, smiley);
		moodSelector.getMethodName(index);

	}

	public void resetButton(){
		Button resetButton = new Button(this);
		resetButton.setId(View.generateViewId());
		resetButton.setText("RESET");
		Float size = getResources().getDimension(R.dimen.statistics_reset_button);
		resetButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		resetButton.setTextColor(Color.parseColor("#FFFFFF"));
		resetButton.setBackgroundColor(Color.parseColor("#282828"));
		mMoodLayout.addView(resetButton);
		ConstraintSet set = new ConstraintSet();
		set.clone(mMoodLayout);
		set.constrainHeight(resetButton.getId(), ConstraintSet.WRAP_CONTENT);
		set.constrainWidth(resetButton.getId(), ConstraintSet.WRAP_CONTENT);
		set.connect(resetButton.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID,
				ConstraintSet.RIGHT, dpToPx(20));
		set.centerVertically(resetButton.getId(), ConstraintSet.PARENT_ID);
		set.applyTo(mMoodLayout);


		resetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(StatisticsActivity.this)
						.setTitle(R.string.statsReset)
						.setPositiveButton(R.string.VALIDATE, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								deletePreferences();
								Intent intent = new Intent(
										StatisticsActivity.this, MainActivity.class);
								finish();
								startActivity(intent);
							}
						})
						.setNegativeButton(getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						})
						.show();
			}
		});

	}

	private void deletePreferences(){
		Context context = getApplicationContext();
		for(int i = mTotalUsageDays; i >= 0; i--){
			context.getSharedPreferences(String.valueOf(i), 0).edit().clear().apply();
		}
		PreferenceManager.getDefaultSharedPreferences(context).edit().remove(getString(
				R.string.historicDayKey)).apply();
		PreferenceManager.getDefaultSharedPreferences(context).edit().remove(getString(
				R.string.totalStreakKey)).apply();
		PreferenceManager.getDefaultSharedPreferences(context).edit().remove(getString(
				R.string.currentStreakKey)).apply();
		PreferenceManager.getDefaultSharedPreferences(context).edit().remove(getString(
				R.string.additonalScoreKey)).apply();
	}

	private int dpToPx(int dp)
	{
		return (int) (dp * this.scale);
	}
}

