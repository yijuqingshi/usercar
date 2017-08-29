package com.kuxiao.usercar.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class PointContentProvider extends ContentProvider {

	// ��Ȩ
	private static final String AUTHORITIER = "com.kuxiao.usercar.db.PointContentProvider";
	// ��������
	public static final int URI_ALL = 1;
	// ��������
	public static final int URI_ONE = 0;

	public static final Uri URI_LOCATION_ALL = Uri.parse("content://com.kuxiao.usercar.db.PointContentProvider/points");
	//content://com.kuxiao.train.contentprovider.UserContentProvider/user_info
	private static UriMatcher mUriMahcher = null;

	private DBHelper mDBhelper = null;

	private SQLiteDatabase db = null;

	// ��ʼ��ƥ��·��
	static {
		mUriMahcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMahcher.addURI(AUTHORITIER, PointDao.TABLE_NAME, URI_ALL);
		mUriMahcher.addURI(AUTHORITIER, PointDao.TABLE_NAME + "/#", URI_ONE);
	}

	@Override
	public boolean onCreate() {
		mDBhelper = DBHelper.getInstance(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		db = mDBhelper.getReadableDatabase();
		switch (mUriMahcher.match(uri)) {
		case URI_ALL:
			cursor = db.query(PointDao.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			//����֪ͨ��uri
			cursor.setNotificationUri(getContext().getContentResolver(), URI_LOCATION_ALL);
			break;
		case URI_ONE:
			long id = ContentUris.parseId(uri);
			String select = "_id=" + id;
			if (selection != null && !selection.equals("")) {
				select += "and" + selection;
			}
			cursor = db.query(PointDao.TABLE_NAME, projection, select,
					selectionArgs, null, null, sortOrder);
			break;
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {

		switch (mUriMahcher.match(uri)) {
		case URI_ALL:
			// ���� Ϊ�� "vnd.android.cursor.dir/+��Ȩ+/����"
			return "vnd.android.cursor.dir/" + AUTHORITIER + "/"
					+ PointDao.TABLE_NAME;
		case URI_ONE:
			// ���� Ϊ�� "vnd.android.cursor.item/+��Ȩ+/����"
			return "vnd.android.cursor.item/" + AUTHORITIER + "/"
					+ PointDao.TABLE_NAME;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri Uri = null;

		if (mUriMahcher.match(uri) != URI_ALL) {
			throw new IllegalArgumentException("wrong URL" + uri);
		}
		db = mDBhelper.getWritableDatabase();
		long id = db.insert(PointDao.TABLE_NAME, null, values);
		if (id > 0) {
			notifyDataSetChanged();
			return Uri = ContentUris.withAppendedId(uri, id);
		}

		return Uri;
	}
    //���ݱ仯ʱ��֪ͨContextResolver
	private void notifyDataSetChanged() {
           getContext().getContentResolver().notifyChange(URI_LOCATION_ALL, null);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		return 0;
	}

}
