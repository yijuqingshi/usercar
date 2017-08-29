package com.kuxiao.usercar;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.kuxiao.usercar.adapter.MyPointCursorAdapter;
import com.kuxiao.usercar.adapter.SearchPointAdapter;
import com.kuxiao.usercar.commom.MyApplication;
import com.kuxiao.usercar.db.PointContentProvider;
import com.kuxiao.usercar.db.PointDao;

public class SelectPointActivity extends FragmentActivity implements
		OnClickListener, OnGetPoiSearchResultListener {

	public static final String TAG = "SelectPointActivity";

	private ImageView iv_back;

	private Button btn_search;

	private EditText et_point;

	private ListView mListView;

	private static final int LOADER_ID = 1;

	public static final String ACTION_START = "ACTION_START";
	public static final String ACTION_END = "ACTION_END";

	private MyPointCursorAdapter mMyPointCursorAdapter = null;

	private PoiSearch mPoiSearch = null;

	private AlertDialog dialog = null;

	private SearchPointAdapter mPointAdapter;

	private Dialog dialog2 = null;

	public static final int CODE_RESULT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_start);
		initView();
		initEvent();
	}

	// 初始化事件
	private void initEvent() {
		iv_back.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent inetent = getIntent();
				PoiInfo mPoiInfo = mMyPointCursorAdapter.getPoints().get(
						position);
				inetent.putExtra("PoiInfo", mPoiInfo);
				setResult(CODE_RESULT, inetent);
			      	finish();
			}
		});

	}

	// 初始化组件
	private void initView() {

		iv_back = (ImageView) findViewById(R.id.id_iv_location_back);
		btn_search = (Button) findViewById(R.id.id_btn_location);
		et_point = (EditText) findViewById(R.id.id_et_location_search);
		mListView = (ListView) findViewById(R.id.id_lv_history_location);
		switch (getIntent().getAction()) {
		case ACTION_END:
			et_point.setHint("输入终点");
			break;
		case ACTION_START:
			et_point.setHint("输入起点");
			break;
		}
		// 初始化Loadler();
		initLoadler();
		mMyPointCursorAdapter = new MyPointCursorAdapter(this, null, false);
		mListView.setAdapter(mMyPointCursorAdapter);
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		dialog = new AlertDialog.Builder(this).create();
		mPointAdapter = new SearchPointAdapter(this, new ArrayList<PoiInfo>());
		dialog2 = new Dialog(this);
		dialog2.setTitle("正在搜索...请稍等");

	}

	private void initLoadler() {
		getSupportLoaderManager().initLoader(LOADER_ID, null,
				new LoaderCallbacks<Cursor>() {

					@Override
					public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
						CursorLoader cursorLoader = new CursorLoader(
								SelectPointActivity.this,
								PointContentProvider.URI_LOCATION_ALL, null,
								// 注意空格
								null, null, PointDao.POINT_ID + " DESC");

						return cursorLoader;
					}

					@Override
					public void onLoadFinished(Loader<Cursor> loader,
							Cursor cursor) {
						if (loader.getId() == LOADER_ID) {
							mMyPointCursorAdapter.swapCursor(cursor);

						}
					}

					@Override
					public void onLoaderReset(Loader<Cursor> loader) {
						mMyPointCursorAdapter.swapCursor(null);
					}
				});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.id_iv_location_back:
			finish();
			break;
		case R.id.id_btn_location:
			getLocation();
			break;

		}
	}

	/**
	 * 获取位置
	 */
	private void getLocation() {

		String keyword = et_point.getText().toString().trim();
		if (TextUtils.isEmpty(keyword)) {
			Toast.makeText(this, "请输入您要搜索的位置", Toast.LENGTH_SHORT).show();
			return;
		}
		dialog2.show();
		PoiCitySearchOption pcso = new PoiCitySearchOption();
		pcso.city(MyApplication.mMyLocation.getCity()).keyword(keyword).pageNum(1);
		mPoiSearch.searchInCity(pcso);

	}

	/**
	 * 位置搜索监听
	 */
	@Override
	public void onGetPoiDetailResult(PoiDetailResult mPoiDetailResult) {

	}

	@Override
	public void onGetPoiIndoorResult(PoiIndoorResult mPoiIndoorResult) {

	}

	@Override
	public void onGetPoiResult(PoiResult mPoiResult) {
		dialog2.cancel();
		if (mPoiResult == null || mPoiResult.getAllPoi() == null) {
			Toast.makeText(this, "没有搜索到结果...请重新输入", Toast.LENGTH_SHORT).show();
			return;
		}
		showSearchResult(mPoiResult);
	}

	/**
	 * 查询结果显示
	 * 
	 * @param mPoiResult
	 */
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

				PoiInfo mPoiInfo = mPointAdapter.getPoints().get(position);
				Intent intent = getIntent();
				intent.putExtra("PoiInfo", mPoiInfo);
				setResult(CODE_RESULT, intent);
				insertDatabase(mPoiInfo);
				dialog.cancel();
				finish();

			}

			private void insertDatabase(PoiInfo mPoiInfo) {
				ContentValues values = new ContentValues();
				values.put(PointDao.POINT_ADDRESS, mPoiInfo.address);
				values.put(PointDao.POINT_NAME, mPoiInfo.name);
				values.put(PointDao.POINT_LATITUDE, mPoiInfo.location.latitude);
				values.put(PointDao.POINT_LONGITUDE,
						mPoiInfo.location.longitude);
				getContentResolver().insert(
						PointContentProvider.URI_LOCATION_ALL, values);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPoiSearch.destroy();
	}

}
