package com.s0mbr3.moodtracker.models;

import java.io.Serializable;

public class StreakSerializer implements Serializable {
	private int mTotalStreak;
	private int mCurrentStreak;
	private int mAdditionalScore;

	public StreakSerializer(int totalStreak, int currentStreak, int additionalScore){
		this.mTotalStreak = totalStreak;
		this.mCurrentStreak = currentStreak;
		this.mAdditionalScore = additionalScore;
	}

	public int getTotalStreak(){
		return this.mTotalStreak;
	}

	public int getCurrentStreak(){
		return this.mCurrentStreak;
	}

	public int getAdditionalScore(){
		return this.mAdditionalScore;
	}
}
