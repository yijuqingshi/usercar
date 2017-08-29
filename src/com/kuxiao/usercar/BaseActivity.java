package com.kuxiao.usercar;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

public abstract class BaseActivity extends Activity implements
		SensorEventListener {
	// 传感器管理者
	protected SensorManager mSersorManager;
	// 传感器的结果保存数据
	protected float[] mResult = new float[3];

	// 加速度传感器数据
	protected float[] accValues = new float[3];
	// 地磁传感器数据
	protected float[] magValues = new float[3];
	// 旋转矩阵，用来保存磁场和加速度的数据
	protected float r[] = new float[9];
	// 重力传感器
	protected Sensor acc_Sensor = null;
	// 地磁传感器
	protected Sensor mag_Sensor = null;

	// 方向（0-360）
	protected int direction = 0;

	// 我的位置数据
	protected MyLocationData mLocationData = null;
	// 新方向
	protected int newDirection = 0;
	// 地图相关
	protected TextureMapView mMapView = null;

	// 百度地图控制器
	protected BaiduMap mBaiduMap;

	protected UiSettings mSetting;
	// 我的位置
	protected BDLocation mLocation;

	// 地图是否加载完毕
	protected boolean isLoadFinish = false;

	protected abstract void initViews();

	protected abstract void initEvents();

	protected void init() {
		// 获取系统传感器管理者
		mSersorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// 获取重力传感器
		acc_Sensor = mSersorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// 获取磁感应传感器
		mag_Sensor = mSersorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		// 获取地图控制器的设置器
		mSetting = mBaiduMap.getUiSettings();
		// 禁止旋转手势
		mSetting.setRotateGesturesEnabled(false);
		// 开启交通图
		mBaiduMap.setTrafficEnabled(true);
		mBaiduMap.setMyLocationEnabled(true);
	}

	/**
	 * 更新位置
	 * 
	 * @param location
	 * @param direction
	 */
	protected void updateLoction(BDLocation location, int direction) {

		LatLng mLatlng = new LatLng(mLocation.getLatitude(),
				mLocation.getLongitude());
		MapStatus maStatus = new MapStatus.Builder().target(mLatlng).zoom(14)
		// .overlook(-45)
				.build();
		MapStatusUpdate mUpdate = MapStatusUpdateFactory.newMapStatus(maStatus);
		mBaiduMap.setMapStatus(mUpdate);
		updateDirection(location, this.direction);
		Log.d("MainAcivity", "location的Direction = " + location.getDirection());
		// 添加我的位置指示器图片
		BitmapDescriptor mBitma1 = BitmapDescriptorFactory
				.fromResource(R.drawable.zhishiqi1);
		MyLocationConfiguration mConfiguration = new MyLocationConfiguration(
				com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.FOLLOWING,
				true, mBitma1);
		mBaiduMap.setMyLocationConfigeration(mConfiguration);

	}

	/**
	 * 更新方向
	 * 
	 * @param location
	 * @param direction
	 */
	protected void updateDirection(BDLocation location, int direction) {
		mLocationData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(direction).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		mBaiduMap.setMyLocationData(mLocationData);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int type = event.sensor.getType();
		switch (type) {
		case Sensor.TYPE_ACCELEROMETER:
			// 克隆一份重力加速度改变事件的数据
			accValues = event.values.clone();
			break;

		case Sensor.TYPE_MAGNETIC_FIELD:
			// 克隆一份重力加速度改变事件的数据
			magValues = null;
			magValues = event.values.clone();
			break;
		}

		/**
		 * public static boolean getRotationMatrix (float[] R, float[] I,
		 * float[] gravity, float[] geomagnetic) 填充旋转数组r r：要填充的旋转数组
		 * I:将磁场数据转换进实际的重力坐标中 一般默认情况下可以设置为null gravity:加速度传感器数据
		 * geomagnetic：地磁传感器数据
		 */
		SensorManager.getRotationMatrix(r, null, accValues, magValues);
		SensorManager.getOrientation(r, mResult);
		float f = mResult[0];
		f = (float) Math.toDegrees(f);
		if (f < 0) {
			f = 180 + (180 + f);
		}
		direction = (int) f;

		// 判断我的位置是否在屏幕中
		if (isLoadFinish &&!mBaiduMap.getMapStatus().bound.contains(new LatLng(mLocation
				.getLatitude(), mLocation.getLongitude()))) {
			return;
		}
		// 更新方向
		if (Math.abs(f - newDirection) > 5 && isLoadFinish) {
			updateDirection(mLocation, newDirection);
			direction = newDirection;
			newDirection = (int) f;
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	/**
	 * 返回
	 */
	protected void back() {
		finish();
	}

	// 跳转
	protected <T> void jump(Activity activity, Class<T> clazz) {
		Intent intent = new Intent(activity, clazz);
		startActivity(intent);
	}

	// 带请求的跳转
	protected <T> void jumpForResult(Activity activity, Class<T> clazz,
			int requestCode) {
		Intent intent = new Intent(activity, clazz);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * Toast
	 * 
	 * @param activity
	 * @param text
	 */
	protected void Toast(Activity activity, String text) {
		Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 打印
	 * 
	 * @param clazz
	 * @param text
	 */
	protected <T> void Log(Class<T> clazz, String text) {
		Log.d(clazz.getSimpleName(), text);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		// unregisterReceiver(mMyReceiver);
		mSersorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();

		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
