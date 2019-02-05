package com.s0mbr3.moodtracker.models;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class StreakUnserializer {
	private int mTotalStreak;
	private int mCurrentStreak;
	private int mAdditionalScore;

	public StreakUnserializer(){}

	public void objectUnserializer(String filePath){
		ObjectInputStream objectInputStream = null;
		try{
			objectInputStream = new ObjectInputStream(
					new BufferedInputStream(
							new FileInputStream(
									new File(filePath))));

			StreakSerializer unserializedStreak = (StreakSerializer) objectInputStream.readObject();
			this.mTotalStreak = unserializedStreak.getTotalStreak();
			this.mCurrentStreak = unserializedStreak.getCurrentStreak();
			this.mAdditionalScore = unserializedStreak.getAdditionalScore();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		} finally {
			if(objectInputStream != null){
				try{
					objectInputStream.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	public int getTotalStreak(){
		return this.mTotalStreak;
	}

	public int getCurrentStreak(){
		return  this.mCurrentStreak;
	}

	public int getAdditionalScore(){
		return this.mAdditionalScore;
	}
}
