package com.s0mbr3.moodtracker.models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.controllers.MainActivity;

import java.util.List;

/**
 * Instanciate by the AlarmReceiver class to send a notification when a new day is starting
 * @see com.s0mbr3.moodtracker.controllers.AlarmReceiver
 */
public class Notifications {
	private Context mContext;
	private NotificationManager mNotificationManager;
	private List<String> mNotifications;

	public Notifications(Context context, List<String> notifications) {
		this.mContext = context;
		mNotifications = notifications;
	}

	public void Notification(String title) {
		String message = this.messagePicker();
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "default")
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(title)
				.setContentText(message)
				.setStyle(new NotificationCompat.BigTextStyle()
						.bigText(message))
				.setAutoCancel(true);
		Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(mContext, 1, intent, 0);
		builder.setContentIntent(pi);
		mNotificationManager.notify(1, builder.build());
	}

	public void showNotification() {
		mNotificationManager =
				(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel("default",
					"default",
					NotificationManager.IMPORTANCE_DEFAULT);
			channel.setDescription("Alarm triggered");
			mNotificationManager.createNotificationChannel(channel);
		}
	}

	private String messagePicker(){
		int max = 17;
		int min = 0;
		int randomNumber = min + (int)(Math.random() * ((max - min) + 1));

		return this.mNotifications.get(randomNumber);
	}


}
