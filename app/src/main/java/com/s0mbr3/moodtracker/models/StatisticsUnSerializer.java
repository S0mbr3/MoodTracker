package com.s0mbr3.moodtracker.models;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class StatisticsUnSerializer {
	private int mDays;

	public StatisticsUnSerializer(){}

	public void objectUnserializer(String filePath){
		ObjectInputStream objectInputStream = null;
		try{
			objectInputStream = new ObjectInputStream(
					new BufferedInputStream(
							new FileInputStream(
									new File(filePath))));

			StatisticsSerializer unserializedStatistics = (StatisticsSerializer) objectInputStream.readObject();
			this.mDays = unserializedStatistics.getDays();
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
	public int getDays(){
		return this.mDays;

	}
}
