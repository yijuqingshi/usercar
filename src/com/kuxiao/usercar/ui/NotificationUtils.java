package com.kuxiao.usercar.ui;

import android.app.NotificationManager;
import android.content.Context;

public class NotificationUtils {

	private Context context;

	private static NotificationUtils mNotifityUtils = null;

	private NotificationManager mNotificationManager = null;

	private NotificationUtils(Context context) {
		this.context = context.getApplicationContext();
		mNotificationManager = (NotificationManager) this.context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public static NotificationUtils getInstance(Context context) {
		if (mNotifityUtils == null) {
			synchronized (NotificationUtils.class) {
				if (mNotifityUtils == null) {
					mNotifityUtils = new NotificationUtils(context);
				}
			}
		}
		return mNotifityUtils;
	}
	
	
	
	
	

}
