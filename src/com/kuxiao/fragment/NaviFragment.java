package com.kuxiao.fragment;

import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.kuxiao.usercar.NaviRuningActivity;
import com.kuxiao.usercar.R;
import com.kuxiao.usercar.db.PathContentProvider;
import com.kuxiao.usercar.db.PathDao;
import com.kuxiao.usercar.ui.BikingRouteOverlay;
import com.kuxiao.usercar.ui.DrivingRouteOverlay;
import com.kuxiao.usercar.ui.WalkingRouteOverlay;

public class NaviFragment extends Fragment implements
		OnGetRoutePlanResultListener, OnClickListener {

	// 地图相关
	private TextureMapView mMapView;
	private BaiduMap mBaiduMap;

	// 路线搜索相关
	private RoutePlanSearch mSearch;
	private int type = 0;
	public static final int TYPE_DRIVING = 0;
	public static final int TYPE_BIKING = 3;
	public static final int TYPE_WALKING = 2;
	public static final int TYPE_BUS = 1;
	private static final String TAG = "NaviFragment";

	private PoiInfo mStartPoint;
	private PoiInfo mEndPoint;

	private Button btn_start;

	private MyDrivingRouteOverlay myDrivingRouteOverlay = null;
	private MyWalkingRouteOverlay myWalkingRouteOverlay = null;
	private MyBikingRouteOverlay myBikingRouteOverlay = null;
	
	

	public NaviFragment(PoiInfo start, PoiInfo end) {
		this.mStartPoint = start;
		this.mEndPoint = end;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.navifragment, container, false);
		mMapView = (TextureMapView) view.findViewById(R.id.id_nav_mapview);
		btn_start = (Button) view.findViewById(R.id.id_btn_start);
		mBaiduMap = mMapView.getMap();
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
		btn_start.setOnClickListener(this);
		startRoutePlan(type);
		myDrivingRouteOverlay = new MyDrivingRouteOverlay(mBaiduMap);
		myWalkingRouteOverlay = new MyWalkingRouteOverlay(mBaiduMap);
		myBikingRouteOverlay = new MyBikingRouteOverlay(mBaiduMap);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	public void startRoutePlan(int type) {
		if (mStartPoint == null || mEndPoint == null) {
			Log.d(TAG, "起点位置有空指针...");
		}
		switch (type) {
		case TYPE_BIKING:
			startBikingRoutePlan();
			break;
		case TYPE_BUS:

			break;
		case TYPE_DRIVING:
			startDrivierRoutePlan();
			break;
		case TYPE_WALKING:
			startWalkingRoutePlan();
			break;
		}
	}
	
	 /**
     * 骑行规划
     */
	private void startBikingRoutePlan() {
		BikingRoutePlanOption mBikingRoutePlanOption = new BikingRoutePlanOption();
		mBikingRoutePlanOption.from(
				PlanNode.withLocation(mStartPoint.location)).to(
				PlanNode.withLocation(mEndPoint.location));
		mSearch.bikingSearch(mBikingRoutePlanOption);
	}
	
	
    /**
     * 步行规划
     */
	private void startWalkingRoutePlan() {
		WalkingRoutePlanOption mWalkingRoutePlanOption = new WalkingRoutePlanOption();
		mWalkingRoutePlanOption.from(
				PlanNode.withLocation(mStartPoint.location)).to(
				PlanNode.withLocation(mEndPoint.location));
		mSearch.walkingSearch(mWalkingRoutePlanOption);
	}

	/**
	 * 驾车规划
	 */
	private void startDrivierRoutePlan() {
		DrivingRoutePlanOption mDrivingRoutePlanOption = new DrivingRoutePlanOption();
		mDrivingRoutePlanOption.currentCity(mStartPoint.city)
				.from(PlanNode.withLocation(mStartPoint.location))
				.to(PlanNode.withLocation(mEndPoint.location));
		mSearch.drivingSearch(mDrivingRoutePlanOption);
	}
	
	

	
	
	

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		if (mSearch != null) {
			mSearch.destroy();
		}
		super.onDestroy();
	}

	@Override
	public void onGetBikingRouteResult(BikingRouteResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Log.i(TAG, "骑行搜索结果返回有错误....");
			return;
		}
		
		List<BikingRouteLine> mBikingRouteLines = result.getRouteLines();
		Log.d(TAG, "规划好的骑行路线有" + mBikingRouteLines.size() + "条");
		if(mBikingRouteLines != null && mBikingRouteLines.size() != 0)
		{
			mBaiduMap.clear();
			mBaiduMap.setOnMarkerClickListener(myBikingRouteOverlay);
			myBikingRouteOverlay.setData(mBikingRouteLines.get(0));
			myBikingRouteOverlay.addToMap();
			myBikingRouteOverlay.zoomToSpan();
			Log.d(TAG, "显示第一条骑行规划路线的效果");
			
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Log.i(TAG, "驾车搜索结果返回有错误....");
			return;
		}

		List<DrivingRouteLine> mDrivingRouteLine = result.getRouteLines();
		Log.d(TAG, "规划好的驾车路线有" + mDrivingRouteLine.size() + "条");
		if (mDrivingRouteLine != null && mDrivingRouteLine.size() != 0) {
			mBaiduMap.clear();
			mBaiduMap.setOnMarkerClickListener(myDrivingRouteOverlay);
			myDrivingRouteOverlay.setData(mDrivingRouteLine.get(0));
			myDrivingRouteOverlay.addToMap();
			myDrivingRouteOverlay.zoomToSpan();
			Log.d(TAG, "显示第一条驾车规划路线的效果");
		}
	}

	@Override
	public void onGetIndoorRouteResult(IndoorRouteResult result) {

	}

	@Override
	public void onGetMassTransitRouteResult(MassTransitRouteResult result) {

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Log.i(TAG, "步行结果返回有错误....");
			return;
		}
		
		List<WalkingRouteLine> mWalkingRouteLine = result.getRouteLines();
		Log.d(TAG, "规划好的步行路线有" + mWalkingRouteLine.size() + "条");
		if (mWalkingRouteLine != null && mWalkingRouteLine.size() != 0) {
			mBaiduMap.clear();
			mBaiduMap.setOnMarkerClickListener(myDrivingRouteOverlay);
			myWalkingRouteOverlay.setData(mWalkingRouteLine.get(0));
			myWalkingRouteOverlay.addToMap();
			myWalkingRouteOverlay.zoomToSpan();
			Log.d(TAG, "显示第一条步行规划路线的效果");
		}
		
		
		
	}

	@Override
	public void onClick(View v) {

		Intent intent = new Intent(getActivity(), NaviRuningActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("start", mStartPoint);
		bundle.putParcelable("end", mEndPoint);
		intent.putExtras(bundle);
		startActivity(intent);
		ContentValues values = new ContentValues();
		values.put(PathDao.PATH_START_ADDRESS, mStartPoint.address);
		values.put(PathDao.PATH_START_NAME, mStartPoint.name);
		values.put(PathDao.PATH_START_LATITUDE, mStartPoint.location.latitude);
		values.put(PathDao.PATH_START_LONGITUDE, mStartPoint.location.longitude);
		values.put(PathDao.PATH_END_ADDRESS, mEndPoint.address);
		values.put(PathDao.PATH_END_NAME, mEndPoint.name);
		values.put(PathDao.PATH_END_LATITUDE, mEndPoint.location.latitude);
		values.put(PathDao.PATH_END_LONGITUDE, mEndPoint.location.longitude);
		getActivity().getContentResolver().insert(
				PathContentProvider.URI_PATH_ALL, values);

	}

	
	/**
	 * 这里复写父类方法修改对应的路线覆盖物
	 * @author Administrator
	 *
	 */
	class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

	}
	
	
	class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

	}
	
	class MyBikingRouteOverlay extends BikingRouteOverlay {

		public MyBikingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

	}
	
	
	
	
	

}
