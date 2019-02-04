package com.s0mbr3.moodtracker.models;

import java.io.Serializable;

public class StatisticsSerializer implements Serializable {
	private int mDays;

	public StatisticsSerializer(int days){
		this.mDays = days;
	}

	int getDays(){
		return this.mDays;
	}

	public String toString(){
		return "Serialize the day for statistic purpose";
	}
}
