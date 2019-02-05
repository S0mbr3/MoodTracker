package com.s0mbr3.moodtracker.views;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.models.AppStartDriver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StatisticsActivityView {
	private LinearLayout mStatisticsLayout;
	private ConstraintLayout mGraphLayout;
	private TextView mGraphLine;
	private int mHeight;
	private int mWidth;
	private int mTotalUseDays;
	private int mHumorDays;

	public StatisticsActivityView(LinearLayout statisticsLayout, TextView graphLine, int height,
								  int width, int totalUseDays, int humorDays, ConstraintLayout grapLayout){
		this.mStatisticsLayout = statisticsLayout;
		this.mGraphLine = graphLine;
		this.mHeight = height;
		this.mWidth = width;
		this.mTotalUseDays = totalUseDays;
		this.mHumorDays = humorDays;
		this.mGraphLayout = grapLayout;
	}

	public void getMethodName(int index){
		int height = 0;
		Class<?> c;
		try {
			c = Class.forName(StatisticsActivityView.class.getName());
			Method method = c.getMethod(AppStartDriver.INSTANCE.getHumor(index), (Class[]) null);
			height = (int) method.invoke(this,(Object[]) null);
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		} catch(NoSuchMethodException e){
			e.printStackTrace();
		} catch(IllegalAccessException e){
			e.printStackTrace();
		} catch(InvocationTargetException e){
			e.printStackTrace();
		}

		Log.d("hight", String.valueOf(height));
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mWidth/5, mHeight/4);
		this.mGraphLayout.setLayoutParams(lp);
		mStatisticsLayout.addView(this.mGraphLayout);
		this.mGraphLayout.addView(this.mGraphLine);
		ConstraintSet set = new ConstraintSet();
		set.clone(this.mGraphLayout);
		set.constrainHeight(this.mGraphLine.getId(), height);
		set.constrainWidth(this.mGraphLine.getId(), mWidth/5);
		set.connect(this.mGraphLine.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
		set.applyTo(this.mGraphLayout);
	}

	public int setSadSmiley(){
		this.mGraphLine.setBackgroundResource(R.color.faded_red);
		return graphLineHeight() ;
	}

	public int setDisappointedSmiley(){
		this.mGraphLine.setBackgroundResource(R.color.warm_grey);
		return graphLineHeight() ;
	}

	public int setNormalSmiley(){
		this.mGraphLine.setBackgroundResource(R.color.cornflower_blue_65);
		return graphLineHeight() ;
	}

	public int setHappySmiley(){
		this.mGraphLine.setBackgroundResource(R.color.light_sage);
		return graphLineHeight() ;
	}

	public int setSuperHappySmiley(){
		this.mGraphLine.setBackgroundResource(R.color.banana_yellow);
		return graphLineHeight() ;
	}

	private int graphLineHeight(){
		int daysRatio = mHumorDays * 100 / mTotalUseDays;
		Log.d("higgggggg", String.valueOf(daysRatio));
	return daysRatio * (mHeight/2) / 100;
	}
}
