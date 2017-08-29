package com.kuxiao.usercar;

import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.kuxiao.usercar.bean.Order;
import com.kuxiao.usercar.bean.UserDrivie;
import com.kuxiao.usercar.commom.MyApplication;

public class UserCarAcitivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "UserCarAcitivity";
	private TextView tv_start;
	private TextView tv_end;
	private Button btn_call;
	private ImageView iv_back;

	public static final int CODE_CAR_START = 4;
	public static final int CODE_CAR_END = 5;
	private PoiInfo mStartPoiInfo = null;
	private PoiInfo mEndPoiInfo = null;

	private AlertDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usercar);
		mMapView = (TextureMapView) findViewById(R.id.id_map_usercar);
		super.init();
		initViews();
		initEvents();
		Log.i(TAG, "onCreate");
		queryDrivie();
		dialog = new AlertDialog.Builder(this).create();

	}

	private void queryDrivie() {

		BmobQuery<UserDrivie> query = new BmobQuery<>();
		query.addWhereNear(
				"location",
				new BmobGeoPoint(MyApplication.mMyLocation.getLongitude(),
						MyApplication.mMyLocation.getLatitude())).setLimit(10);
		query.findObjects(new FindListener<UserDrivie>() {

			@Override
			public void done(List<UserDrivie> arg0, BmobException e) {
				if (e != null) {
					Log(UserCarAcitivity.class, "查询失败..." + e.getMessage());
				} else {
					showCanUserCar(arg0);
				}
			}
		});
	}

	protected void showCanUserCar(List<UserDrivie> cars) {

		Log(UserCarAcitivity.class, "查询成功...有" + cars.size() + "辆车");
		if (cars != null && cars.size() != 0) {
			for (UserDrivie divie : cars) {
				MarkerOptions markerOptions = new MarkerOptions();
				Log(UserCarAcitivity.class, "查询成功到的经纬度为"
						+ divie.getLaction().getLatitude() + " 经度："
						+ divie.getLaction().getLongitude());
				markerOptions
						.position(
								new LatLng(divie.getLaction().getLatitude(),
										divie.getLaction().getLongitude()))
						.rotate((float) (Math.random() * 360))
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.car));
				mBaiduMap.addOverlay(markerOptions);
				Toast(this, "附近有" + cars.size() + "辆车");
			}
		}

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
	protected void initViews() {
		updateDirection(MyApplication.mMyLocation, direction);
		tv_start = (TextView) findViewById(R.id.id_tv_usercar_start);
		tv_end = (TextView) findViewById(R.id.id_tv_usercar_end);
		iv_back = (ImageView) findViewById(R.id.id_iv_usercar_back);
		btn_call = (Button) findViewById(R.id.id_btn_usercar);
		mLocation = MyApplication.mMyLocation;
		updateLoction(mLocation, direction);
		isLoadFinish = true;
	}

	@Override
	protected void initEvents() {
		tv_start.setOnClickListener(this);
		tv_end.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		btn_call.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.id_iv_usercar_back:
			back();
			break;
		case R.id.id_tv_usercar_start:
			Intent intent = new Intent(this, SelectPointActivity.class);
			intent.setAction(SelectPointActivity.ACTION_START);
			startActivityForResult(intent, CODE_CAR_START);
			break;
		case R.id.id_tv_usercar_end:
			Intent intent1 = new Intent(this, SelectPointActivity.class);
			intent1.setAction(SelectPointActivity.ACTION_END);
			startActivityForResult(intent1, CODE_CAR_END);
			break;
		case R.id.id_btn_usercar:
			inputOrder();
			break;
		}
	}

	private void inputOrder() {
		if (TextUtils.isEmpty(tv_start.getText())
				&& tv_start.getText().toString().equals("输入起点")) {
			Toast(this, "起点不能为空..");
		} else if (TextUtils.isEmpty(tv_end.getText())
				&& tv_end.getText().toString().equals("输入起点")) {
			Toast(this, "终点不能为空..");

		} else {
			dialog.show();
			Window window = dialog.getWindow();
			window.setContentView(R.layout.order);
			initDialogViews(window);
		}

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
					android.widget.Toast.makeText(UserCarAcitivity.this,
							"预约电话不能为空", android.widget.Toast.LENGTH_SHORT)
							.show();
				} else if (TextUtils.isEmpty(et_personcount.getText()
						.toString())) {
					android.widget.Toast.makeText(UserCarAcitivity.this,
							"乘车人数", android.widget.Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(et_tiem.getText().toString())) {
					android.widget.Toast.makeText(UserCarAcitivity.this,
							"预约时间不能为空", android.widget.Toast.LENGTH_SHORT)
							.show();
				} else {
					
					Order order = new Order(
							new Date(System.currentTimeMillis()), "00000", tv_start
									.getText().toString(), tv_end.getText()
									.toString(), et_text.getText().toString(),
							"", false,et_phone.getText().toString(), 0);
					order.save(new SaveListener<String>() {

						@Override
						public void done(String id, BmobException e) {
							if (e != null) {
								android.widget.Toast.makeText(
										UserCarAcitivity.this, "服务器错误..提交失败",
										android.widget.Toast.LENGTH_SHORT)
										.show();
							}
						}
					});
					dialog.cancel();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (intent == null)
			return;
		if (resultCode == SelectPointActivity.CODE_RESULT
				&& requestCode == CODE_CAR_START || requestCode == CODE_CAR_END) {
			PoiInfo mPoiInfo = intent.getParcelableExtra("PoiInfo");

			if (mPoiInfo == null) {
				return;
			}
			Log.d("", "收到了位置信息..为：" + mPoiInfo.toString());
			switch (requestCode) {
			case CODE_CAR_START:
				mStartPoiInfo = mPoiInfo;
				tv_start.setText(mStartPoiInfo.name);
				break;
			case CODE_CAR_END:
				mEndPoiInfo = mPoiInfo;
				tv_end.setText(mEndPoiInfo.name);
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

}
