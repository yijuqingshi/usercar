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

		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		
		long  time = System.currentTimeMillis();
		SDKInitializer.initialize(this);
		Bmob.initialize(this, "00743c916e9c9eb0edb1f29fe3fc4c29");
		Log.d(TAG, "实例化的时间为" + (System.currentTimeMillis() - time));
		super.onCreate();
	}
	
	
	
	
		
		
	
	
}
