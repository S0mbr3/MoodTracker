package com.s0mbr3.moodtracker.models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.SparseArray;

import com.s0mbr3.moodtracker.R;
import com.s0mbr3.moodtracker.controllers.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class Notifications {
	private Context mContext;
	private NotificationManager mNotificationManager;
	private SparseArray<String> mNotifications = new SparseArray<>();

	public Notifications(Context context) {
		this.mContext = context;
		this.setMessagesMap();
	}

	public void Notification() {
		String message = this.messagePicker();
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "default")
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle("Une nouvelle journée commence")
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
		int randomNumber = 0 + (int)(Math.random() * ((18 - 0) + 1));
		String message = this.mNotifications.get(randomNumber);

		return message;
	}

	private void setMessagesMap(){
		this.mNotifications.put(0, "Faire de l'exercice permet de se sentir mieux dans son corps " +
				"et d'être de meilleur humeur.");
		this.mNotifications.put(1, "Prenez du temps pour vous. Pratiquer une activité que l'on " +
				"affectionne fait baisser le stress.");
		this.mNotifications.put(2, "Une petite balade en plein air ? La nature est source d'apaisement ");
		this.mNotifications.put(3, "La pratique de la méditation au quotidien est une clé efficace " +
				"pour se recentrer, percevoir le monde différemment, augmenter sa concentration " +
				", mieux gérer ses émotions et bien d'autres vertus!");
		this.mNotifications.put(4, "Tentez de nouvelles expériences, la routine est source de stress " +
				", d'ennuie et de dépression.");
		this.mNotifications.put(5, "Passez du temps avec des proches. Ces connections sont  " +
				"inestimable pour votre bien être !");
		this.mNotifications.put(6, "Faites de nouvelles rencontres, échangez, partagez.  " +
				"Le monde est vaste et chaque personne est unique!");
		this.mNotifications.put(7, "Une alimentation saine et varié est la clé d'une santé durable, " +
				"physiquement et mentalement !!!");
		this.mNotifications.put(8, "Accordez vous du repos, le surmenage accompagne fatigue et " +
				"saute d'humeur");
		this.mNotifications.put(9, "Fixez vous des objectifs, quelle joie immense de les accomplir !");
		this.mNotifications.put(10, "Jetez votre réveil aux oubliettes, une nuit de sommeil complète" +
				" est un allier de choix pour une journée débordante");
		this.mNotifications.put(11, "Rendez-vous utile. Aider les autres emplit de satisfaction, " +
				"chaque geste compte.");
		this.mNotifications.put(12, "Soyez curieux, le cerveau est avide d'apprendre, et vous " +
				"découvrirez de nouvelles acitivités passionnante");
		this.mNotifications.put(13, "À peine debout et déjà démotivé de votre journée ? " +
				"Si ça arrive trop souvent il est peut être temps de faire le point, " +
				"il n'est jamais trop tard pour changer de vie !!!");
		this.mNotifications.put(15, "Si je vous dis que 1 + 1 = 2, logique n'est-ce pas ? " +
				"Et si je vous dis maintenant que pour être heureux il suffit de le vouloir ? " +
				"Facile à dire je sais !");
		this.mNotifications.put(16, "l'auto suggestions est une arme redoutable pour déprogrammer de " +
				"mauvais comportements ou des pensées néfaste à votre bien être.");

		this.mNotifications.put(17, "les Excitants tel la caféine, théine, nicotine... Font monter " +
				"le cortisol et donc le stress.");
		this.mNotifications.put(18, "Faites vous plaisir, les resctrictions sont catastrophique " +
				"sur votre équilibre. Aucun excès n'est bon qu'ils soient abusif ou restrictif.");
	}


}
