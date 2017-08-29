package com.kuxiao.usercar.test;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.kuxiao.usercar.R;
import com.kuxiao.usercar.ui.DrivingRouteOverlay;
import com.kuxiao.usercar.ui.OverlayManager;
import com.kuxiao.usercar.ui.RouteLineAdapter;
import com.kuxiao.usercar.ui.WalkingRouteOverlay;

public class RoutePlanText extends Activity implements OnMapClickListener,
		OnGetRoutePlanResultListener {

	Button mBtnPre = null; // 上一个节点
	Button mBtnNext = null; // 下一个节点
	int nodeIndex = -1; // 节点索引,供浏览节点时使用
	RouteLine route = null;
	MassTransitRouteLine massroute = null;
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;
	private TextView popupText = null; // 泡泡view

	// 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	TextureMapView mMapView = null; // 地图View
	BaiduMap mBaidumap = null;
	// 搜索相关
	RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	WalkingRouteResult nowResultwalk = null;
	BikingRouteResult nowResultbike = null;
	TransitRouteResult nowResultransit = null;
	DrivingRouteResult nowResultdrive = null;
	MassTransitRouteResult nowResultmass = null;

	int nowSearchType = -1; // 当前进行的检索，供判断浏览节点时结果使用。

	String startNodeStr = "西二旗";
	String endNodeStr = "龙泽";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routeplan);
		initViews();

	}

	private void initViews() {
		CharSequence titleLable = "路线规划功能";
		setTitle(titleLable);
		// 初始化地图
		mMapView = (TextureMapView) findViewById(R.id.map);
		mBaidumap = mMapView.getMap();
		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		// 地图点击事件处理
		mBaidumap.setOnMapClickListener(this);
		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
	}

	/**
	 * 发起路线规划搜索示例
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v) {
		// 重置浏览节点的路线数据
		route = null;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		mBaidumap.clear();
		// 处理搜索按钮响应
		// 设置起终点信息，对于tranist search 来说，城市名无意义
		PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", startNodeStr);
		PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", endNodeStr);

		// 实际使用中请对起点终点城市进行正确的设定

		if (v.getId() == R.id.mass) {
			PlanNode stMassNode = PlanNode
					.withCityNameAndPlaceName("北京", "天安门");
			PlanNode enMassNode = PlanNode.withCityNameAndPlaceName("上海",
					"东方明珠");
			mSearch.masstransitSearch(new MassTransitRoutePlanOption().from(
					stMassNode).to(enMassNode));
			nowSearchType = 0;
		} else if (v.getId() == R.id.drive) {
			mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
					.to(enNode));
			nowSearchType = 1;
		} else if (v.getId() == R.id.transit) {
			mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
					.city("北京").to(enNode));
			nowSearchType = 2;
		} else if (v.getId() == R.id.walk) {
			mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode)
					.to(enNode));
			nowSearchType = 3;
		} else if (v.getId() == R.id.bike) {
			mSearch.bikingSearch((new BikingRoutePlanOption()).from(stNode).to(
					enNode));
			nowSearchType = 4;
		}
	}

	/**
	 * 切换路线图标，刷新地图使其生效 注意： 起终点图标使用中心对齐.
	 */
	public void changeRouteIcon(View v) {
		if (routeOverlay == null) {
			return;
		}
		if (useDefaultIcon) {
			((Button) v).setText("自定义起终点图标");
			Toast.makeText(this, "将使用系统起终点图标", Toast.LENGTH_SHORT).show();

		} else {
			((Button) v).setText("系统起终点图标");
			Toast.makeText(this, "将使用自定义起终点图标", Toast.LENGTH_SHORT).show();

		}
		useDefaultIcon = !useDefaultIcon;
		routeOverlay.removeFromMap();
		routeOverlay.addToMap();
	}

	@Override
	public void onGetBikingRouteResult(BikingRouteResult result) {

	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {

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

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePlanText.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}

		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}

		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);

			if (result.getRouteLines().size() > 1) {
				nowResultwalk = result;

				MyTransitDlg myTransitDlg = new MyTransitDlg(
						RoutePlanText.this, result.getRouteLines(),
						RouteLineAdapter.Type.WALKING_ROUTE);
				myTransitDlg
						.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
							public void onItemClick(int position) {
								route = nowResultwalk.getRouteLines().get(
										position);
								WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(
										mBaidumap);
								mBaidumap.setOnMarkerClickListener(overlay);
								// routeOverlay = overlay;
								overlay.setData(nowResultwalk.getRouteLines()
										.get(position));
								overlay.addToMap();
								overlay.zoomToSpan();
							}

						});
				myTransitDlg.show();

			} else if (result.getRouteLines().size() == 1) {
				route = result.getRouteLines().get(0);
				// WalkingRouteOverlay overlay = new
				// MyDrivingRouteOverlay(mBaidumap);
				WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(
						mBaidumap);
				routeOverlay = overlay;
				mBaidumap.setOnMarkerClickListener(overlay);
				overlay.setData(result.getRouteLines().get(0));
				overlay.addToMap();
				overlay.zoomToSpan();
				mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			} else {
				Log.d("route result", "结果数<0");
				return;
			}

		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mSearch != null) {
			mSearch.destroy();
		}
		mMapView.onDestroy();
		super.onDestroy();
	}

	// 供路线选择的Dialog
	class MyTransitDlg extends Dialog {

		private List<? extends RouteLine> mtransitRouteLines;
		private ListView transitRouteList;
		private RouteLineAdapter mTransitAdapter;

		OnItemInDlgClickListener onItemInDlgClickListener;

		public MyTransitDlg(Context context, int theme) {
			super(context, theme);
		}

		public MyTransitDlg(Context context,
				List<? extends RouteLine> transitRouteLines,
				RouteLineAdapter.Type type) {
			this(context, 0);
			mtransitRouteLines = transitRouteLines;
			mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines,
					type);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_transit_dialog);

			transitRouteList = (ListView) findViewById(R.id.transitList);
			transitRouteList.setAdapter(mTransitAdapter);

			transitRouteList
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							onItemInDlgClickListener.onItemClick(position);
							mBtnPre.setVisibility(View.VISIBLE);
							mBtnNext.setVisibility(View.VISIBLE);
							dismiss();

						}
					});
		}

		public void setOnItemInDlgClickLinster(
				OnItemInDlgClickListener itemListener) {
			onItemInDlgClickListener = itemListener;
		}

	}

	// 响应DLg中的List item 点击
	interface OnItemInDlgClickListener {
		public void onItemClick(int position);
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	/**
	 * 步行覆盖物
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	@Override
	public void onMapClick(LatLng arg0) {

	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}

}
