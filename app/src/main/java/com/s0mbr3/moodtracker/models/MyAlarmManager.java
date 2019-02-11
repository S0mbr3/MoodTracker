package com.s0mbr3.moodtracker.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import com.s0mbr3.moodtracker.controllers.AlarmReceiver;

import java.util.Calendar;

/**
 * class triggered by the MainActivity or the DeveiceRebootReceiver to initialize the calendar
 * and start the schedule task via the AlarmManager
 */
public class MyAlarmManager {
	public MyAlarmManager(){

	}
	public void setAlarm(Context context){
		AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(
				Context.ALARM_SERVICE);
		Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 17);
		calendar.set(Calendar.MINUTE, 5);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		PendingIntent alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
				0, intent,PendingIntent.FLAG_CANCEL_CURRENT);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

	}
}
