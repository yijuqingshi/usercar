package com.kuxiao.usercar;

import java.util.HashMap;
import java.util.Map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.kuxiao.fragment.BusFragment;
import com.kuxiao.fragment.CarFargment;
import com.kuxiao.fragment.CyclingFragment;
import com.kuxiao.fragment.NaviFragment;
import com.kuxiao.fragment.WalkFragment;
import com.kuxiao.usercar.bean.UserPath;
import com.kuxiao.usercar.utils.FileUtils;
import com.kuxiao.usercar.utils.MyTTSUtils;

public class NavigationActivity extends FragmentActivity implements
		OnClickListener, TextWatcher, NaviInitListener, OnItemClickListener {

	private TextView tv_navigation_car, tv_navigation_bus, tv_navigation_walk,
			tv_navigation_cycling;
	private ImageView iv_back;
	private EditText et_start, et_end;
	private View mView;
	private Map<String, Fragment> fragments = null;
	private WindowManager wm;

	// λ�õ�ѡȡ
	// ��ʼλ�õ���Ϣ
	private PoiInfo mStartPoiInfo = null;
	// ����λ�õ���Ϣ
	private PoiInfo mEndPoiInfo = null;
	public static final int CODE_START = 3;
	public static final int CODE_END = 2;

	private boolean isRunningNavi = false;

	// ���µ�����صĳ�Ա����
	// TTS������������
	private static MyTTSUtils mMyTTS = new MyTTSUtils();;

	private static final String APP_FOLDER_NAME = "usercar";
	// ����TTS��handler
	private static MyHandler mHandler = new MyHandler();
	// Ȩ������
	private final static String authBaseArr[] = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_FINE_LOCATION };
	// �豸Ȩ������
	private final static String authComArr[] = { Manifest.permission.READ_PHONE_STATE };
	private static final String TAG = WelcomeAcitivity.class.getSimpleName();

	public static boolean isInitSuccess = false;

	private int type = 0;
	private NaviFragment mNavifragment = null;

	static class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation);
		// ��ʼ���ؼ�
		initView();
		// ����¼�
		initEvent();

		initNavi();

	}

	private void initEvent() {

		iv_back.setOnClickListener(this);
		tv_navigation_bus.setOnClickListener(this);
		tv_navigation_walk.setOnClickListener(this);
		tv_navigation_car.setOnClickListener(this);
		tv_navigation_cycling.setOnClickListener(this);
		et_start.setOnClickListener(this);
		et_end.setOnClickListener(this);
		et_start.addTextChangedListener(this);
		et_end.addTextChangedListener(this);

	}

	// ��ʼ���ؼ�
	private void initView() {
		tv_navigation_car = (TextView) findViewById(R.id.id_tv_navigation_drive);
		tv_navigation_bus = (TextView) findViewById(R.id.id_tv_navigation_bus);
		tv_navigation_walk = (TextView) findViewById(R.id.id_tv_navigation_walk);
		tv_navigation_cycling = (TextView) findViewById(R.id.id_tv_navigation_cycling);
		iv_back = (ImageView) findViewById(R.id.id_iv_navigation_back);
		mView = findViewById(R.id.id_navigation_index);
		et_start = (EditText) findViewById(R.id.id_et_start);
		et_end = (EditText) findViewById(R.id.id_et_end);
		fragments = new HashMap<String, Fragment>();
		fragments.put("drive", new CarFargment());
		fragments.put("bus", new BusFragment());
		fragments.put("walk", new WalkFragment());
		fragments.put("cycling", new CyclingFragment());
		chageFragment(R.id.id_frame, fragments.get("drive"), type,
				isRunningNavi);
		wm = this.getWindowManager();
		LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) mView
				.getLayoutParams();
		// ��ȡ��Ļ�Ŀ�ȵ�4��֮1
		ll.width = wm.getDefaultDisplay().getWidth() / 4;
		mView.requestLayout();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.id_et_end:
			Intent intent1 = new Intent(this, SelectPointActivity.class);
			intent1.setAction(SelectPointActivity.ACTION_END);
			startActivityForResult(intent1, CODE_END);
			break;
		case R.id.id_et_start:
			Intent intent = new Intent(this, SelectPointActivity.class);
			intent.setAction(SelectPointActivity.ACTION_START);
			startActivityForResult(intent, CODE_START);
			break;
		case R.id.id_tv_navigation_bus:
			type = 1;

			chageFragment(R.id.id_frame, fragments.get("bus"), type,
					isRunningNavi);

			break;
		case R.id.id_tv_navigation_drive:
			type = 0;

			chageFragment(R.id.id_frame, fragments.get("drive"), type,
					isRunningNavi);

			break;
		case R.id.id_tv_navigation_cycling:
			type = 3;

			chageFragment(R.id.id_frame, fragments.get("cycling"), type,
					isRunningNavi);

			break;
		case R.id.id_tv_navigation_walk:
			type = 2;

			chageFragment(R.id.id_frame, fragments.get("walk"), type,
					isRunningNavi);

			break;
		case R.id.id_iv_navigation_back:
			finish();
			break;
		}
	}

	/**
	 * �޸�Fragment�л�
	 */
	private void chageFragment(int layId, Fragment fragment, int index,
			boolean isNavigation) {

		if (isNavigation) {
			mNavifragment.setType(type);
			mNavifragment.startRoutePlan(type);
		} else {
			getSupportFragmentManager().beginTransaction()
					.replace(layId, fragment).commitAllowingStateLoss();
		}
		LinearLayout.LayoutParams LL = (android.widget.LinearLayout.LayoutParams) mView
				.getLayoutParams();
		LL.setMargins(mView.getWidth() * index, 2, 0, 3);
		mView.requestLayout();

	}

	/**
	 * ѡȡλ�ûص�
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (intent == null)
			return;
		if (resultCode == SelectPointActivity.CODE_RESULT
				&& requestCode == CODE_START || requestCode == CODE_END) {
			PoiInfo mPoiInfo = intent.getParcelableExtra("PoiInfo");
			if (mPoiInfo == null) {
				return;
			}
			Log.d("", "�յ���λ����Ϣ..Ϊ��" + mPoiInfo.toString());
			switch (requestCode) {
			case CODE_START:
				mStartPoiInfo = mPoiInfo;
				et_start.setText(mStartPoiInfo.name);
				break;
			case CODE_END:
				mEndPoiInfo = mPoiInfo;
				et_end.setText(mEndPoiInfo.name);
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	/**
	 * �ı������ص�
	 */

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (mStartPoiInfo != null && mEndPoiInfo != null) {
			mNavifragment = new NaviFragment(mStartPoiInfo, mEndPoiInfo);
			mNavifragment.setType(type);
			chageFragment(R.id.id_frame, mNavifragment, type, isRunningNavi);
			isRunningNavi = true;
		}

	}

	/**
	 * ʵ����SDK
	 * 
	 * @throws Exception
	 */
	public void initNavi() {

		// ��ʼʵ����
		if (getSdcradDir() == null && !getSdcradDir().equals("")) {
			isInitSuccess = false;
			Log.d(TAG, "��ʼ��ʧ��,sd��������");
			return;
		}

		// �����Ȩ
		if (!hasBasePhoneAuth() || !hasCompletePhoneAuth()) {
			Log.d(TAG, "��ʼ��ʧ��,�]����Ȩ");
			return;
		}

		BaiduNaviManager.getInstance().init(this, getSdcradDir(),
				APP_FOLDER_NAME, this, mMyTTS, mHandler, mMyTTS);

	}

	// ��ʼ���ص�
	@Override
	public void initFailed() {
		Log.d(TAG, "---->>>��ʼ��ʧ��...");
	}

	@Override
	public void initStart() {
		Log.d(TAG, "---->>>��ʼ����ʼ..");
	}

	@Override
	public void initSuccess() {
		isInitSuccess = true;
		Log.d(TAG, "---->>>��ʼ���ɹ�...");

	}

	@Override
	public void onAuthResult(int status, String arg1) {

		if (0 == status) {
			Log.d(TAG, "��Ȩkey��֤�ɹ�....");
		} else {
			Log.d(TAG, "��Ȩkey��֤ʧ��...." + arg1);
		}

	}

	/**
	 * �����Ȩ
	 * 
	 * @return
	 */
	public boolean hasBasePhoneAuth() {
		PackageManager pm = getPackageManager();
		for (String auth : authBaseArr) {
			int result = pm.checkPermission(auth, this.getPackageName());
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ����豸Ȩ��
	 * 
	 * @return
	 */
	private boolean hasCompletePhoneAuth() {
		// TODO Auto-generated method stub

		PackageManager pm = this.getPackageManager();
		for (String auth : authComArr) {
			if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ��ȡSD��·��
	 * 
	 * @return
	 */
	private String getSdcradDir() {
		if (FileUtils.getInstance().isHasSdCard())
			return FileUtils.getInstance().getSdCardDir();
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CarFargment carFargment = (CarFargment) fragments.get("drive");
		UserPath mUserPath = carFargment.getmPathCursorAdapter().getmPaths()
				.get(position);
		PoiInfo sP = new PoiInfo();
		PoiInfo eP = new PoiInfo();
		sP.address = mUserPath.getStartAddress();
		sP.name = mUserPath.getStartName();
		sP.location = new LatLng(mUserPath.getStarLatitude(),
				mUserPath.getStarLongitude());
		eP.address = mUserPath.getEndAddress();
		eP.name = mUserPath.getEndName();
		eP.location = new LatLng(mUserPath.getEndLatitude(),
				mUserPath.getEndLongitude());
		mNavifragment = new NaviFragment(sP, eP);
		chageFragment(R.id.id_frame, mNavifragment, type, isRunningNavi);
		isRunningNavi = true;
	}

	@Override
	protected void onDestroy() {
		fragments = null;
		mNavifragment = null;
		super.onDestroy();
	}

}
