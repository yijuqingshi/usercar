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

	Button mBtnPre = null; // ��һ���ڵ�
	Button mBtnNext = null; // ��һ���ڵ�
	int nodeIndex = -1; // �ڵ�����,������ڵ�ʱʹ��
	RouteLine route = null;
	MassTransitRouteLine massroute = null;
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;
	private TextView popupText = null; // ����view

	// ��ͼ��أ�ʹ�ü̳�MapView��MyRouteMapViewĿ������дtouch�¼�ʵ�����ݴ���
	// ���������touch�¼���������̳У�ֱ��ʹ��MapView����
	TextureMapView mMapView = null; // ��ͼView
	BaiduMap mBaidumap = null;
	// �������
	RoutePlanSearch mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��

	WalkingRouteResult nowResultwalk = null;
	BikingRouteResult nowResultbike = null;
	TransitRouteResult nowResultransit = null;
	DrivingRouteResult nowResultdrive = null;
	MassTransitRouteResult nowResultmass = null;

	int nowSearchType = -1; // ��ǰ���еļ��������ж�����ڵ�ʱ���ʹ�á�

	String startNodeStr = "������";
	String endNodeStr = "����";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routeplan);
		initViews();

	}

	private void initViews() {
		CharSequence titleLable = "·�߹滮����";
		setTitle(titleLable);
		// ��ʼ����ͼ
		mMapView = (TextureMapView) findViewById(R.id.map);
		mBaidumap = mMapView.getMap();
		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		// ��ͼ����¼�����
		mBaidumap.setOnMapClickListener(this);
		// ��ʼ������ģ�飬ע���¼�����
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
	}

	/**
	 * ����·�߹滮����ʾ��
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v) {
		// ��������ڵ��·������
		route = null;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		mBaidumap.clear();
		// ����������ť��Ӧ
		// �������յ���Ϣ������tranist search ��˵��������������
		PlanNode stNode = PlanNode.withCityNameAndPlaceName("����", startNodeStr);
		PlanNode enNode = PlanNode.withCityNameAndPlaceName("����", endNodeStr);

		// ʵ��ʹ�����������յ���н�����ȷ���趨

		if (v.getId() == R.id.mass) {
			PlanNode stMassNode = PlanNode
					.withCityNameAndPlaceName("����", "�찲��");
			PlanNode enMassNode = PlanNode.withCityNameAndPlaceName("�Ϻ�",
					"��������");
			mSearch.masstransitSearch(new MassTransitRoutePlanOption().from(
					stMassNode).to(enMassNode));
			nowSearchType = 0;
		} else if (v.getId() == R.id.drive) {
			mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
					.to(enNode));
			nowSearchType = 1;
		} else if (v.getId() == R.id.transit) {
			mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
					.city("����").to(enNode));
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
	 * �л�·��ͼ�꣬ˢ�µ�ͼʹ����Ч ע�⣺ ���յ�ͼ��ʹ�����Ķ���.
	 */
	public void changeRouteIcon(View v) {
		if (routeOverlay == null) {
			return;
		}
		if (useDefaultIcon) {
			((Button) v).setText("�Զ������յ�ͼ��");
			Toast.makeText(this, "��ʹ��ϵͳ���յ�ͼ��", Toast.LENGTH_SHORT).show();

		} else {
			((Button) v).setText("ϵͳ���յ�ͼ��");
			Toast.makeText(this, "��ʹ���Զ������յ�ͼ��", Toast.LENGTH_SHORT).show();

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
			Toast.makeText(RoutePlanText.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT)
					.show();
		}

		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// ���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
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
				Log.d("route result", "�����<0");
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

	// ��·��ѡ���Dialog
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

	// ��ӦDLg�е�List item ���
	interface OnItemInDlgClickListener {
		public void onItemClick(int position);
	}

	// ����RouteOverly
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
	 * ���и�����
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
