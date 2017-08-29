package com.kuxiao.usercar.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtils {

	private Context mContext;

	private static NetWorkUtils mNewWorkUtils;
	
	private NetWorkUtils(Context mContext) {
		this.mContext = mContext.getApplicationContext();
	}

	public static NetWorkUtils getInstance(Context mContext) {
		if (mNewWorkUtils == null) {
			synchronized (new Object()) {
				if (mNewWorkUtils == null) {
					mNewWorkUtils = new NetWorkUtils(mContext);
				}
			}
		}
		return mNewWorkUtils;
	}
  
	/**
    * ¼ì²éÍøÂç
    * @return
    */
	public boolean isHasNetWrok() {
		ConnectivityManager  connManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager != null) {
			NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				return networkInfo.isConnected();
			}
		}
		return false;
	}

	
	
   
}
