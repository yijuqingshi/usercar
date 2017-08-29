package com.kuxiao.usercar.service;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UserCarService extends Service {

	private BmobRealTimeData mBData = new BmobRealTimeData();
	private String objectId;
	public static final String ACTION_START = "ACTION_START";
	public static final String ACTION_STOP = "ACTION_SOTP";

	private ValueEventListener valueEventListener = new ValueEventListener() {

		@Override
		public void onDataChange(JSONObject arg0) {
				if(!arg0.equals(""))
				{
					 try {
						JSONObject jsonObject = arg0.getJSONObject("data");
						if(!jsonObject.getString("drivieId").equals(""))
						{
							 
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		}

		@Override
		public void onConnectCompleted(Exception arg0) {
			if (mBData.isConnected()) {
				mBData.subRowUpdate("Order", objectId);
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();

	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			if (intent.getAction().equals(ACTION_START)) {
				objectId = intent.getStringExtra("objectId");
				mBData.start(valueEventListener);
			} else if (intent.getAction().equals(ACTION_STOP)) {
				mBData.unsubRowUpdate("Order", objectId);
				    	stopSelf();
			}

		}
		return Service.START_REDELIVER_INTENT;
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
