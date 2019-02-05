package com.s0mbr3.moodtracker;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.s0mbr3.moodtracker.models.AppStartDriver;
import com.s0mbr3.moodtracker.models.StatisticsUnSerializer;
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
		graphDrawer();
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
			StatisticsActivityView drawGraph = new StatisticsActivityView(mStatisticsLayout, graphLine,
					mHeight, mWidth, mTotalUsageDays, humorDay, grapLayout);
			drawGraph.getMethodName(mIndex);

		}
		++mIndex;

		if(mIndex <= 4) graphDrawer();
	}

	public void scoreWriter(){

	}
}

