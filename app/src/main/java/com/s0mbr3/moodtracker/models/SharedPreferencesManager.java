package com.s0mbr3.moodtracker.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.s0mbr3.moodtracker.R;

/**
 * SharedPreferenceManager class is used to allow persistance of data across reboot and activities
 */
public class SharedPreferencesManager {
    private Context mContext;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public SharedPreferencesManager(Context context){
        this.mContext = context;
    }
    public Object[] getSelectedHumor(){
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    	int index;
    	int historicDay;
    	String commentTxt;
        index = mPreferences.getInt(
                mContext.getString(R.string.indexKey),
                mContext.getResources().getInteger(R.integer.index));
        historicDay = mPreferences.getInt(
                mContext.getString(R.string.historicDayKey),
                mContext.getResources().getInteger(R.integer.historicDay));
        commentTxt = mPreferences.getString(mContext.getString(R.string.commentKey),
        null);
        return new Object[] {index, historicDay, commentTxt};
    }

	public void deletePreferences(){
    	PreferenceManager.getDefaultSharedPreferences(mContext).edit().remove(mContext.getString(
    			R.string.isErasedKey)).commit();
    	int days=1;
    	mContext.getSharedPreferences(String.valueOf(days), 0).contains(
    			mContext.getString(R.string.indexKey));
    	while(mContext.getSharedPreferences(String.valueOf(days), 0).contains(
    			mContext.getString(R.string.indexKey))) {
    		++days;
		}
		while(days >= 0){
			mContext.getSharedPreferences(String.valueOf(days), 0).edit().clear().commit();
			days--;
		}
		PreferenceManager.getDefaultSharedPreferences(mContext).edit().remove(mContext.getString(
				R.string.totalStreakKey)).commit();
		PreferenceManager.getDefaultSharedPreferences(mContext).edit().remove(mContext.getString(
				R.string.currentStreakKey)).commit();
		PreferenceManager.getDefaultSharedPreferences(mContext).edit().remove(mContext.getString(
				R.string.additonalScoreKey)).commit();
		PreferenceManager.getDefaultSharedPreferences(mContext).edit().remove(mContext.getString(
				R.string.historicDayKey)).commit();

		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mEditor = mPreferences.edit();
		mEditor.putBoolean(mContext.getString(R.string.isErasedKey), true).commit();
	}
    public boolean isErased(){
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

		return mPreferences.contains(mContext.getString(R.string.isErasedKey));
	}

    public void setSelectedHumor(int index, int historicDay, String commentTxt){
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mEditor = mPreferences.edit();
        mEditor.putInt(mContext.getString(R.string.indexKey), index);
        mEditor.putInt(mContext.getString(R.string.historicDayKey), historicDay);
        mEditor.putString(mContext.getString(R.string.commentKey), commentTxt);
        mEditor.apply();
    }

    public Object[] getStreaks(){
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    	int totalStreak;
    	int currentStreak;
    	int addtionalScore;

    	totalStreak = mPreferences.getInt(
    			mContext.getString(R.string.totalStreakKey),
				mContext.getResources().getInteger(R.integer.totalStreak));
    	currentStreak = mPreferences.getInt(
    			mContext.getString(R.string.currentStreakKey),
				mContext.getResources().getInteger(R.integer.currentStreak));
    	addtionalScore = mPreferences.getInt(
    			mContext.getString(R.string.additonalScoreKey),
				mContext.getResources().getInteger(R.integer.additionalScore));

    	return new Object[] {totalStreak, currentStreak, addtionalScore};
	}

	public void setStreaks(int totalStreak, int currentStreak, int additionalScore){
		mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mEditor = mPreferences.edit();
    	mEditor.putInt(mContext.getString(R.string.totalStreakKey), totalStreak);
    	mEditor.putInt(mContext.getString(R.string.currentStreakKey), currentStreak);
    	mEditor.putInt(mContext.getString(R.string.additonalScoreKey), additionalScore);
    	mEditor.apply();
	}

	public int getDaysPerHumor(int index){
    	mPreferences = mContext.getSharedPreferences(String.valueOf(index), Context.MODE_PRIVATE);
    	int daysPerHumor;

    	daysPerHumor = mPreferences.getInt(
    			mContext.getString(R.string.daysPerHumorKey),
				mContext.getResources().getInteger(R.integer.daysPerHumor));

    	return daysPerHumor;

	}

	public int getHistoryDay(){
    	mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    	return mPreferences.getInt(mContext.getString(R.string.historicDayKey),
				mContext.getResources().getInteger(R.integer.historicDay));
	}
	public void resetHistoricDay(){
    	mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    	mEditor = mPreferences.edit();
    	mEditor.putInt(mContext.getString(R.string.historicDayKey), 1);
    	mEditor.apply();
	}

	public void setDaysPerHumor(int daysPerHumor, int index){
		mPreferences = mContext.getSharedPreferences(String.valueOf(index), Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
    	mEditor.putInt(mContext.getString(R.string.daysPerHumorKey), daysPerHumor);
    	mEditor.apply();
	}

	public Object[] getHistoricHumor(int historicDay){
		mPreferences = mContext.getSharedPreferences(String.valueOf(historicDay), Context.MODE_PRIVATE);
		int index;
		String commentTxt;

		index = mPreferences.getInt(
				mContext.getString(R.string.indexKey),
				mContext.getResources().getInteger(R.integer.index));
		commentTxt = mPreferences.getString(
				mContext.getString(R.string.commentKey),
				null);

		return new Object[] {index, commentTxt};
	}

	public void setHistoricHumor(int index, String commentTxt, int historicDay){
		mPreferences = mContext.getSharedPreferences(String.valueOf(historicDay), Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();

		mEditor.putInt(mContext.getString(R.string.indexKey), index);
		mEditor.putString(mContext.getString(R.string.commentKey), commentTxt);
		mEditor.apply();
	}

}
