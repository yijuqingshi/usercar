package com.kuxiao.usercar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.kuxiao.usercar.adapter.SearchPointAdapter;
import com.kuxiao.usercar.bean.Order;
import com.kuxiao.usercar.commom.MyApplication;
import com.kuxiao.usercar.db.PointContentProvider;
import com.kuxiao.usercar.db.PointDao;

public class MainActivity extends BaseActivity implements BDLocationListener,
		OnClickListener {

	// 定位器
	private LocationClient mLocationClient = null;

	// 是否为第一次定位
	private boolean isFirst = true;

	private TextView tv_location, tv_navigation, tv_Call, tv_me;

	private EditText ev_search;

	private PoiSearch mPoiSearch;

	private Dialog dialog;

	private PoiResult mPoiResult;
	private Dialog dialog1;
	private SearchPointAdapter mPointAdapter;
	private OnGetPoiSearchResultListener mPoiSearchResultListener = new OnGetPoiSearchResultListener() {

		@Override
		public void onGetPoiResult(PoiResult mPoiResult) {

			MainActivity.this.mPoiResult = mPoiResult;
			List<PoiInfo> pois = mPoiResult.getAllPoi();
			if (pois.size() > 0) {
				for (int i = 0; i < 5; i++) {
					MarkerOptions markerOptions = new MarkerOptions();
					markerOptions
							.position(pois.get(i).location)
							.rotate((float) (Math.random() * 360))
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.hos));
					mBaiduMap.addOverlay(markerOptions);
				}

			} else {
				Toast(MainActivity.this, "收到的结果0");
			}

		}

		@Override
		public void onGetPoiIndoorResult(PoiIndoorResult arg0) {

		}

		@Override
		public void onGetPoiDetailResult(PoiDetailResult arg0) {

		}
	};

	// private AlertDialog mDialog;
	// private MyReceiver mMyReceiver = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 获取地图控件引用
		mMapView = (TextureMapView) findViewById(R.id.bmapView);
		mPoiSearch = PoiSearch.newInstance();
		super.init();
		initViews();
		initEvents();
		mPoiSearch.setOnGetPoiSearchResultListener(mPoiSearchResultListener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient.start();

	}

	@Override
	protected void onStop() {
		super.onStop();
		mLocationClient.stop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSersorManager.registerListener(this, acc_Sensor,
				SensorManager.SENSOR_DELAY_GAME);
		mSersorManager.registerListener(this, mag_Sensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSersorManager.unregisterListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPoiSearch.destroy();
	}

	// 获取位置
	private void getLocation() {
		mLocationClient = new LocationClient(getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		// 设置定位模式为仅设备
		option.setLocationMode(LocationMode.Battery_Saving);
		// 设置返回结果为坐标系
		option.setCoorType("bd09ll");
		int span = 1000;
		option.setScanSpan(span);
		// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

		option.setIsNeedAddress(true);
		// 可选，设置是否需要地址信息，默认不需要

		option.setOpenGps(true);
		// 可选，默认false,设置是否使用gps

		option.setLocationNotify(true);
		// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		mLocationClient.setLocOption(option);

	}

	@Override
	public void onReceiveLocation(BDLocation location) {

		// map view 销毁后不在处理新接收的位置
		if (location == null || mMapView == null) {
			return;
		}
		mLocation = location;
		MyApplication.mMyLocation = mLocation;
		if (isFirst) {
			isFirst = false;
			isLoadFinish = true;
			updateLoction(location, (int) location.getDirection());
			mPoiSearch.searchNearby(new PoiNearbySearchOption()
					.keyword("医院")
					.location(
							new LatLng(mLocationData.latitude,
									mLocationData.longitude)).pageNum(10)
					.radius(30000)
					.sortType(PoiSortType.distance_from_near_to_far));
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.id_ev_search:
			break;

		case R.id.id_tv_navigation:
			Intent intent = new Intent(this, NavigationActivity.class);
			startActivity(intent);
			break;
		case R.id.id_tv_me:
			Intent intent2 = new Intent(this, MeActivity.class);
			startActivity(intent2);
			break;
		case R.id.id_tv_location:
			// 更新位置
			if (mLocation == null) {
				return;
			}
			updateLoction(this.mLocation, direction);
			break;
		case R.id.id_tv_call:
			if (mPoiResult != null) {
				showSearchResult(mPoiResult);
			}

			break;

		}

	}

	private void showSearchResult(PoiResult mPoiResult) {
		List<PoiInfo> data = new ArrayList<PoiInfo>();
		data.addAll(mPoiResult.getAllPoi());
		mPointAdapter.setPoints(data);
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.search_result);
		ListView lv = (ListView) window.findViewById(R.id.id_lv_result);
		Button btn_cancel = (Button) window.findViewById(R.id.id_btn_cancel);
		lv.setAdapter(mPointAdapter);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dialog.cancel();
				inputOrder();

			}

		});
	}

	@Override
	protected void initViews() {
		tv_location = (TextView) findViewById(R.id.id_tv_location);
		tv_navigation = (TextView) findViewById(R.id.id_tv_navigation);
		tv_Call = (TextView) findViewById(R.id.id_tv_call);
		tv_me = (TextView) findViewById(R.id.id_tv_me);
		ev_search = (EditText) findViewById(R.id.id_ev_search);
		getLocation();
		dialog = new AlertDialog.Builder(this).create();
		// 注册位置监听
		mLocationClient.registerLocationListener(this);
		dialog1 = new AlertDialog.Builder(this).create();
		mPointAdapter = new SearchPointAdapter(this, null);
	}

	@Override
	protected void initEvents() {
		tv_location.setOnClickListener(this);
		tv_navigation.setOnClickListener(this);
		tv_Call.setOnClickListener(this);
		tv_me.setOnClickListener(this);
		ev_search.setOnClickListener(this);

	}

	@Override
	public void onConnectHotSpotMessage(String arg0, int arg1) {

	}
	private void inputOrder() {
		
			dialog1.show();
			Window window = dialog1.getWindow();
			window.setContentView(R.layout.order);
			initDialogViews(window);
		   

	}

	private void initDialogViews(Window window) {
		LayoutParams params = window.getAttributes();
		//修改dialog不能获取焦点的属性
		params.flags = LayoutParams.FLAG_DIM_BEHIND;
		window.setAttributes(params);
		final EditText et_phone = (EditText) window
				.findViewById(R.id.id_et_phoneNumber);
		final EditText et_tiem = (EditText) window
				.findViewById(R.id.id_et_time);
		final EditText et_personcount = (EditText) window
				.findViewById(R.id.id_et_personcount);
		final EditText et_text = (EditText) window
				.findViewById(R.id.id_et_text);
		Button btn_commit = (Button) window.findViewById(R.id.id_btn_commit);
		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(et_phone.getText().toString())) {
					android.widget.Toast.makeText(MainActivity.this,
							"预约电话不能为空", android.widget.Toast.LENGTH_SHORT)
							.show();
				} else if (TextUtils.isEmpty(et_personcount.getText()
						.toString())) {
					android.widget.Toast.makeText(MainActivity.this,
							"姓名", android.widget.Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(et_tiem.getText().toString())) {
					android.widget.Toast.makeText(MainActivity.this,
							"预约时间不能为空", android.widget.Toast.LENGTH_SHORT)
							.show();
				} else {
					
					Order order = new Order(
							new Date(System.currentTimeMillis()), et_personcount.getText().toString(), "", "", et_text.getText().toString(),
							"", false,et_phone.getText().toString(), 0);
					order.save(new SaveListener<String>() {

						@Override
						public void done(String id, BmobException e) {
							if (e != null) {
								android.widget.Toast.makeText(
										MainActivity.this, "服务器错误..提交失败",
										android.widget.Toast.LENGTH_SHORT)
										.show();
							}
						}
					});
					dialog1.cancel();
				}
			}
		});
	}

}
