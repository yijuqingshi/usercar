package com.kuxiao.usercar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.navisdk.adapter.BNRouteGuideManager.OnNavigationListener;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviBaseCallbackModel;
import com.baidu.navisdk.adapter.BaiduNaviCommonModule;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.baidu.navisdk.adapter.NaviModuleFactory;
import com.baidu.navisdk.adapter.NaviModuleImpl;

public class NaviRuningActivity extends Activity implements RoutePlanListener {

	private static final String TAG = "NaviRuningActivity";
	// 开始导航相关
	// 通用接口
	BaiduNaviCommonModule mBaiduNaviCommonModele = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        PoiInfo  start =    bundle.getParcelable("start"); 
        PoiInfo  end =    bundle.getParcelable("end"); 
        Log.d(TAG, "开始规划路线....");
        routeplanToNavigation(start, end);
     
	}

	/**
	 * 开始规划路线
	 * 
	 * @param start
	 * @param end
	 */
	public void routeplanToNavigation(PoiInfo start, PoiInfo end) {
		 if (!NavigationActivity.isInitSuccess) {
		 Log.i(TAG, "初始化失败....");
		 return;
		 }
		BNRoutePlanNode sNode = null;
		BNRoutePlanNode eNode = null;
		if (start != null && end != null) {
			sNode = new BNRoutePlanNode(start.location.longitude,
					start.location.latitude, start.name, "",
					CoordinateType.BD09LL);
			eNode = new BNRoutePlanNode(end.location.longitude,
					end.location.latitude, end.name, "", CoordinateType.BD09LL);

			if (sNode != null && eNode != null) {
				List<BNRoutePlanNode> list = new ArrayList<>();
				list.add(sNode);
				list.add(eNode);
				BaiduNaviManager.getInstance().launchNavigator(this, list, 1,
						true, this);
			}
		}else
		{
			Log.d(TAG, "规划路线失败...起点与终点中有null");
		}

	}

	@Override
	public void onJumpToNavigator() {

		   Log.d(TAG, "规划路线成功....");
		mBaiduNaviCommonModele = NaviModuleFactory
				.getNaviModuleManager()
				.getNaviCommonModule(
						NaviModuleImpl.BNaviCommonModuleConstants.ROUTE_GUIDE_MODULE,
						this,
						BNaviBaseCallbackModel.BNaviBaseCallbackConstants.CALLBACK_ROUTEGUIDE_TYPE,
						mNaviCallBack);

		// 如果模块不为NUll，获取View对象
		if (mBaiduNaviCommonModele != null) {
			mBaiduNaviCommonModele.onCreate();
			View view = mBaiduNaviCommonModele.getView();
            setContentView(view);
		}
	}

	@Override
	public void onRoutePlanFailed() {
		Toast.makeText(this, "路线规划失败..", Toast.LENGTH_SHORT).show();
	}

	// 导航回调、
	private MyNaviCallBack mNaviCallBack = new MyNaviCallBack();

	private class MyNaviCallBack implements OnNavigationListener {

		@Override
		public void notifyOtherAction(int arg0, int arg1, int arg2, Object arg3) {

		}

		@Override
		public void onNaviGuideEnd() {

		}

	}

	@Override
	protected void onStart() {
		super.onStart();

		if (mBaiduNaviCommonModele != null) {
			mBaiduNaviCommonModele.onStart();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mBaiduNaviCommonModele != null) {
			mBaiduNaviCommonModele.onResume();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();

		if (mBaiduNaviCommonModele != null) {
			mBaiduNaviCommonModele.onStop();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mBaiduNaviCommonModele != null) {
			mBaiduNaviCommonModele.onPause();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mBaiduNaviCommonModele != null) {
			mBaiduNaviCommonModele.onDestroy();
		}

	}

}
