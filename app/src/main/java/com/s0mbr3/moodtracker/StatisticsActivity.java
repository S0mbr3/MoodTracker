package com.s0mbr3.moodtracker;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.HumorUpdater;
import com.s0mbr3.moodtracker.models.StatisticsUnSerializer;
import com.s0mbr3.moodtracker.models.StreakUnserializer;
import com.s0mbr3.moodtracker.views.StatisticsActivityView;

import java.io.File;

public class StatisticsActivity extends AppCompatActivity {
	private LinearLayout mStatisticsLayout;
	private int mIndex;
	private StatisticsUnSerializer mHumorDayUnserializer;
	private int mSumScore;
	private int mTotalUsageDays;
	private int mHeight;
	private int mWidth;
	private LinearLayout mLinearGraphLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		mStatisticsLayout = findViewById(R.id.statistics_activity_layout);
		mHumorDayUnserializer = new StatisticsUnSerializer();
		mIndex = 0;
		mTotalUsageDays = AppStartDriver.INSTANCE.getCurrentDayForHistoric() - 1;
		mHeight = AppStartDriver.INSTANCE.getHeight();
		mWidth = AppStartDriver.INSTANCE.getWidth();
		mLinearGraphLayout = new LinearLayout(this);
		mLinearGraphLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mLinearGraphLayout.setLayoutParams(lp);
		mStatisticsLayout.addView(mLinearGraphLayout);
		graphDrawer();
		scoreWriter();

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

		AppStartDriver.INSTANCE.setAlive();
	}

	@Override
	protected void onStop() {
		super.onStop();
		AppStartDriver.INSTANCE.unSetAlive();
	}



	public void graphDrawer(){
		if(new File(this.getFilesDir() + AppStartDriver.INSTANCE.STATISTICS_DIR + mIndex).exists()){
			mHumorDayUnserializer.objectUnserializer(
					this.getFilesDir() + AppStartDriver.INSTANCE.STATISTICS_DIR + mIndex);
			int humorDay = mHumorDayUnserializer.getDays();
			mSumScore += humorDay * mIndex;

			TextView graphLine = new TextView(this);
			graphLine.setId(View.generateViewId());
			graphLine.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			graphLine.setText(String.valueOf(humorDay));
			ConstraintLayout grapLayout = new ConstraintLayout(this);
			grapLayout.setId(View.generateViewId());
			StatisticsActivityView drawGraph = new StatisticsActivityView(mLinearGraphLayout, graphLine,
					mHeight, mWidth, mTotalUsageDays, humorDay, grapLayout);
			drawGraph.getMethodName(mIndex);

		}
		++mIndex;

		if(mIndex <= 4) graphDrawer();
	}

	public void scoreWriter(){
		if(new File(this.getFilesDir() + AppStartDriver.INSTANCE.STREAK_FILE).exists()){
			StreakUnserializer streakUnserializer = new StreakUnserializer();
			streakUnserializer.objectUnserializer(
					this.getFilesDir() + AppStartDriver.INSTANCE.STREAK_FILE);
			int totalStreak = streakUnserializer.getTotalStreak();
			int currentStreak = streakUnserializer.getCurrentStreak();

			TextView totalDays = new TextView(this);
			totalDays.setText("Jours total: " + String.valueOf(mTotalUsageDays));
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			totalDays.setLayoutParams(lp);
			mStatisticsLayout.addView(totalDays);

			TextView totalStreakView = new TextView(this);
			totalStreakView.setText("Série de bonne humeurs total: " + String.valueOf(totalStreak));
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			totalStreakView.setLayoutParams(lp1);
			mStatisticsLayout.addView(totalStreakView);

			TextView currentStreakView = new TextView(this);
			currentStreakView.setText("Série de bonne humeurs en cours: " + String.valueOf(currentStreak));
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			currentStreakView.setLayoutParams(lp2);
			mStatisticsLayout.addView(currentStreakView);
		}

	}
}

