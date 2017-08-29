package com.kuxiao.usercar.commom;

import android.app.Application;
import android.util.Log;
import cn.bmob.v3.Bmob;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;

public class MyApplication extends Application{

	public static final String TAG = MyApplication.class.getSimpleName();

	public  static  BDLocation mMyLocation = null;

	@Override
	public void onCreate() {

		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		
		long  time = System.currentTimeMillis();
		SDKInitializer.initialize(this);
		Bmob.initialize(this, "00743c916e9c9eb0edb1f29fe3fc4c29");
		Log.d(TAG, "ʵ������ʱ��Ϊ" + (System.currentTimeMillis() - time));
		super.onCreate();
	}
	
	
	
	
		
		
	
	
}
