package com.kuxiao.usercar.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.kuxiao.usercar.R;
import com.kuxiao.usercar.db.PointDao;

public class MyPointCursorAdapter extends CursorAdapter {

	private static final String TAG = "MyPointCursorAdapter";
	private Context mContext;
	private LayoutInflater inflater;
	private List<PoiInfo> mPoints = null;

	public MyPointCursorAdapter(Context context, Cursor c, boolean f) {
		super(context, c, f);
		this.mContext = context;
		this.inflater = LayoutInflater.from(mContext);
		 mPoints = new LinkedList<PoiInfo>();
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TextView tv_name = (TextView) view
				.findViewById(R.id.id_tv_item_location_name);
		String name = cursor.getString(cursor
				.getColumnIndex(PointDao.POINT_NAME));
		tv_name.setText(name);
		TextView tv_address = (TextView) view
				.findViewById(R.id.id_tv_item_location_address);
		tv_address.setText(cursor.getString(cursor
				.getColumnIndex(PointDao.POINT_ADDRESS)));
		// cursor.close();
       
	}
	
    /**
     * �����������ƶ�Cursor������
     * Ҳ���ܹر�Cursor
     * @param cursor
     * 
     * Ĭ�ϵĶ�Ӧ��ListView�ϵ�˳����
     * 0  ��һ��      ���ݿ��һ��     ���ȱ����
     * 1  �ڶ���     ���ݿ�ڶ���     
     * 2
     * 3
     * 
     */
	private void savePionts(Cursor cursor) {
		
		PoiInfo mPoiInfo = new PoiInfo();
		mPoiInfo.address = cursor.getString(cursor
				.getColumnIndex(PointDao.POINT_ADDRESS));
		mPoiInfo.name = cursor.getString(cursor
				.getColumnIndex(PointDao.POINT_NAME));
		LatLng latLng = new LatLng(cursor.getDouble(cursor
				.getColumnIndex(PointDao.POINT_LATITUDE)),
				cursor.getDouble(cursor
						.getColumnIndex(PointDao.POINT_LONGITUDE)));
		mPoiInfo.location = latLng;
		mPoints.add(mPoiInfo);

	}
	public List<PoiInfo> getPoints() {
		return mPoints;
	}
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// ����View
		View view = inflater.inflate(R.layout.item_history_location, parent,
				false);
		savePionts(cursor);
		return view;
	}

}
