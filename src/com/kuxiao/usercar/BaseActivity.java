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
	// ������������
	protected SensorManager mSersorManager;
	// �������Ľ����������
	protected float[] mResult = new float[3];

	// ���ٶȴ���������
	protected float[] accValues = new float[3];
	// �شŴ���������
	protected float[] magValues = new float[3];
	// ��ת������������ų��ͼ��ٶȵ�����
	protected float r[] = new float[9];
	// ����������
	protected Sensor acc_Sensor = null;
	// �شŴ�����
	protected Sensor mag_Sensor = null;

	// ����0-360��
	protected int direction = 0;

	// �ҵ�λ������
	protected MyLocationData mLocationData = null;
	// �·���
	protected int newDirection = 0;
	// ��ͼ���
	protected TextureMapView mMapView = null;

	// �ٶȵ�ͼ������
	protected BaiduMap mBaiduMap;

	protected UiSettings mSetting;
	// �ҵ�λ��
	protected BDLocation mLocation;

	// ��ͼ�Ƿ�������
	protected boolean isLoadFinish = false;

	protected abstract void initViews();

	protected abstract void initEvents();

	protected void init() {
		// ��ȡϵͳ������������
		mSersorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// ��ȡ����������
		acc_Sensor = mSersorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// ��ȡ�Ÿ�Ӧ������
		mag_Sensor = mSersorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		// ��ȡ��ͼ��������������
		mSetting = mBaiduMap.getUiSettings();
		// ��ֹ��ת����
		mSetting.setRotateGesturesEnabled(false);
		// ������ͨͼ
		mBaiduMap.setTrafficEnabled(true);
		mBaiduMap.setMyLocationEnabled(true);
	}

	/**
	 * ����λ��
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
		Log.d("MainAcivity", "location��Direction = " + location.getDirection());
		// ����ҵ�λ��ָʾ��ͼƬ
		BitmapDescriptor mBitma1 = BitmapDescriptorFactory
				.fromResource(R.drawable.zhishiqi1);
		MyLocationConfiguration mConfiguration = new MyLocationConfiguration(
				com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.FOLLOWING,
				true, mBitma1);
		mBaiduMap.setMyLocationConfigeration(mConfiguration);

	}

	/**
	 * ���·���
	 * 
	 * @param location
	 * @param direction
	 */
	protected void updateDirection(BDLocation location, int direction) {
		mLocationData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
				.direction(direction).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		mBaiduMap.setMyLocationData(mLocationData);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int type = event.sensor.getType();
		switch (type) {
		case Sensor.TYPE_ACCELEROMETER:
			// ��¡һ���������ٶȸı��¼�������
			accValues = event.values.clone();
			break;

		case Sensor.TYPE_MAGNETIC_FIELD:
			// ��¡һ���������ٶȸı��¼�������
			magValues = null;
			magValues = event.values.clone();
			break;
		}

		/**
		 * public static boolean getRotationMatrix (float[] R, float[] I,
		 * float[] gravity, float[] geomagnetic) �����ת����r r��Ҫ������ת����
		 * I:���ų�����ת����ʵ�ʵ����������� һ��Ĭ������¿�������Ϊnull gravity:���ٶȴ���������
		 * geomagnetic���شŴ���������
		 */
		SensorManager.getRotationMatrix(r, null, accValues, magValues);
		SensorManager.getOrientation(r, mResult);
		float f = mResult[0];
		f = (float) Math.toDegrees(f);
		if (f < 0) {
			f = 180 + (180 + f);
		}
		direction = (int) f;

		// �ж��ҵ�λ���Ƿ�����Ļ��
		if (isLoadFinish &&!mBaiduMap.getMapStatus().bound.contains(new LatLng(mLocation
				.getLatitude(), mLocation.getLongitude()))) {
			return;
		}
		// ���·���
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
	 * ����
	 */
	protected void back() {
		finish();
	}

	// ��ת
	protected <T> void jump(Activity activity, Class<T> clazz) {
		Intent intent = new Intent(activity, clazz);
		startActivity(intent);
	}

	// ���������ת
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
	 * ��ӡ
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
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
		// unregisterReceiver(mMyReceiver);
		mSersorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();

		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
