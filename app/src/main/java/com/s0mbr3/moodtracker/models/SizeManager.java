package com.s0mbr3.moodtracker.models;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Map;

public class SizeManager {

	public SizeManager(){}

	public Object[] sizeManager(){
		int height, width;
		double ratio =0;
		Map<String, Integer> sizeStorage = AppStartDriver.INSTANCE.getSize();
		int orientation = Resources.getSystem().getConfiguration().orientation;
		if(orientation == Configuration.ORIENTATION_PORTRAIT) {
			height = sizeStorage.get("portHeight");
			width = sizeStorage.get("portWidth");
		} else {
			height = sizeStorage.get("landHeight");
			width = sizeStorage.get("landWidth");
			ratio = 0.3;
		}
		return new Object[] {height, width, ratio};
	}
}
