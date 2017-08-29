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

import com.kuxiao.usercar.R;
import com.kuxiao.usercar.bean.UserPath;
import com.kuxiao.usercar.db.PathDao;

public class MyPathCursorAdapter extends CursorAdapter {

	private Context mContext;
	private LayoutInflater inflater;
   
	private List<UserPath> mPaths = null;

	public MyPathCursorAdapter(Context context, Cursor c, boolean f) {
		super(context, c, f);
		this.mContext = context;
		this.inflater = LayoutInflater.from(mContext);
		mPaths = new LinkedList<UserPath>();
	}

	
	public List<UserPath> getmPaths() {
		return mPaths;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TextView tv_start = (TextView) view
				.findViewById(R.id.id_tv_item_car_start);
		tv_start.setText(cursor.getString(cursor
				.getColumnIndex(PathDao.PATH_START_NAME)));
		TextView tv_address = (TextView) view
				.findViewById(R.id.id_tv_item_car_end);
		tv_address.setText(cursor.getString(cursor
				.getColumnIndex(PathDao.PATH_END_NAME)));
		// cursor.close();
		addPaths(cursor);

	}
   /**
    * 保存数据库中的路径
    * @param cursor
    */
	private void addPaths(Cursor cursor) {
		UserPath path = new UserPath();
		path.setEndName(cursor.getString(cursor
				.getColumnIndex(PathDao.PATH_END_NAME)));
		path.setEndAddress(cursor.getString(cursor
				.getColumnIndex(PathDao.PATH_END_ADDRESS)));
		path.setEndLatitude(cursor.getDouble(cursor
				.getColumnIndex(PathDao.PATH_END_LATITUDE)));
		path.setEndLongitude(cursor.getDouble(cursor
				.getColumnIndex(PathDao.PATH_END_LONGITUDE)));
		path.setStartName(cursor.getString(cursor
				.getColumnIndex(PathDao.PATH_START_NAME)));
		path.setStartAddress(cursor.getString(cursor
				.getColumnIndex(PathDao.PATH_START_ADDRESS)));
		path.setStarLatitude(cursor.getDouble(cursor
				.getColumnIndex(PathDao.PATH_START_LATITUDE)));
		path.setStarLongitude(cursor.getDouble(cursor
				.getColumnIndex(PathDao.PATH_START_LONGITUDE)));
		mPaths.add(path);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// 创建View
		View view = inflater.inflate(R.layout.item_car, parent, false);
		return view;
	}

}
