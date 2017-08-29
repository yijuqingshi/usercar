package com.kuxiao.usercar.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kuxiao.usercar.bean.HistoryPoint;

public class PointDao {

	public static final String TABLE_NAME = "points";
	// 位置的地址、经纬度、名称
	public static final String POINT_ADDRESS = "address";
	public static final String POINT_LATITUDE = "latitude";
	public static final String POINT_LONGITUDE = "longitude";
	public static final String POINT_NAME = "name";
	public static final String POINT_ID = "_id";
	
	
	private DBHelper mDBhelper = null;
	
	
	
	public PointDao(Context context)
	{
		 mDBhelper = DBHelper.getInstance(context);
	}
	
	public void savePoint(HistoryPoint point)
	{
		 if(point != null)
		 {
			  SQLiteDatabase db =  mDBhelper.getWritableDatabase();
			  ContentValues cValues = new ContentValues();
			  cValues.put(POINT_ADDRESS, point.getAddress());
			  cValues.put(POINT_NAME, point.getName());
			  cValues.put(POINT_LATITUDE, point.getLatitude());
			  cValues.put(POINT_LONGITUDE, point.getLongitude());
			  db.insert(TABLE_NAME, null, cValues);
			  db.close();
		 }
		
	}
		
}
