package com.kuxiao.usercar.test;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;

import com.kuxiao.usercar.db.PathContentProvider;
import com.kuxiao.usercar.db.PathDao;
import com.kuxiao.usercar.db.PointContentProvider;
import com.kuxiao.usercar.db.PointDao;

public class PointContentProviderTest extends AndroidTestCase {

	private String TAG = "PointContentProviderTest";
	
	public void insertPoint() {
		// 获取ContentResolver对象
		ContentResolver contentResolver = getContext().getContentResolver();
		ContentValues values = new ContentValues();
		values.put(PointDao.POINT_ADDRESS, "深圳市下沙八坊");
		values.put(PointDao.POINT_NAME, "腾讯大厦");
		values.put(PointDao.POINT_LATITUDE, 1.257458);
		values.put(PointDao.POINT_LONGITUDE, 1.257458);
		contentResolver.insert(PointContentProvider.URI_LOCATION_ALL, values);

	}

	public void queryPoint() {
		// 获取ContentResolver对象
		ContentResolver contentResolver = getContext().getContentResolver();
		Cursor cursor = contentResolver.query(
				PointContentProvider.URI_LOCATION_ALL, null, null, null, null);
		Log.d(TAG, "------>" + "执行了查询了，没有进入循环体");
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndex(PointDao.POINT_NAME));
			String address = cursor.getString(cursor
					.getColumnIndex(PointDao.POINT_ADDRESS));
			Log.d(TAG, "------>" + name);
			Log.d(TAG, "------>" + address);
		}
	}
	public void insertPath() {
		// 获取ContentResolver对象
		ContentResolver contentResolver = getContext().getContentResolver();
		ContentValues values = new ContentValues();
		values.put(PathDao.PATH_START_NAME, "我的位置");
		values.put(PathDao.PATH_START_ADDRESS, "深圳市下沙八坊");
		values.put(PathDao.PATH_START_LATITUDE, 1.25211);
		values.put(PathDao.PATH_START_LONGITUDE, 1.68555);
		values.put(PathDao.PATH_END_NAME, "腾讯大厦");
		values.put(PathDao.PATH_END_ADDRESS, "深圳市下沙八坊");
		values.put(PathDao.PATH_END_LATITUDE, 1.454515);
		values.put(PathDao.PATH_END_LONGITUDE, 1.257458);
		contentResolver.insert(PathContentProvider.URI_PATH_ALL, values);

	}

}
