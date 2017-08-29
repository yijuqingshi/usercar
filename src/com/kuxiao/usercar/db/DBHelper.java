package com.kuxiao.usercar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "usercar.db";
	private static int DB_VERSION = 5;

	private static DBHelper mDBHelper = null;
	// Points建表语句
	private static final String CREATE_TABLE_POINT = "create table "
			+ PointDao.TABLE_NAME + "(" + PointDao.POINT_ID
			+ " integer primary key autoincrement," + PointDao.POINT_ADDRESS
			+ " text," + PointDao.POINT_LATITUDE + " double,"
			+ PointDao.POINT_LONGITUDE + " double," + PointDao.POINT_NAME
			+ " text)";
	// Paths建表语句
	private static final String CREATE_TABLE_PATH = "create table "
			+ PathDao.TABLE_NAME + "(" + PathDao.PATHS_ID
			+ " integer primary key autoincrement,"
			+ PathDao.PATH_START_ADDRESS + " text,"
			+ PathDao.PATH_START_LATITUDE + " double,"
			+ PathDao.PATH_START_LONGITUDE + " double,"
			+ PathDao.PATH_START_NAME + " text," 
			+ PathDao.PATH_END_ADDRESS+ " text," 
			+ PathDao.PATH_END_LATITUDE + " double,"
			+ PathDao.PATH_END_LONGITUDE + " double," 
			+ PathDao.PATH_END_NAME + " text)";

	private static final String DROP_TABLE_POINT = "drop table " + "if exists "
			+ PathDao.TABLE_NAME;
	// drop table if exists user_info
	private static final String DROP_TABLE_PATH = "drop table " + "if exists "
			+ PointDao.TABLE_NAME;

	private DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
   
	//单例模式
	public static DBHelper getInstance(Context context) {
		if (mDBHelper == null) {
			synchronized (new Object()) {

				if (mDBHelper == null) {
					mDBHelper = new DBHelper(context.getApplicationContext());
				}
			}
		}
		return mDBHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PATH);
		db.execSQL(CREATE_TABLE_POINT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_PATH);
		db.execSQL(DROP_TABLE_POINT);
		db.execSQL(CREATE_TABLE_PATH);
		db.execSQL(CREATE_TABLE_POINT);
	}

}
