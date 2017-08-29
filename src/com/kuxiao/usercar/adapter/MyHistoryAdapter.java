package com.kuxiao.usercar.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kuxiao.usercar.R;
import com.kuxiao.usercar.bean.UserPath;

public class MyHistoryAdapter extends BaseAdapter {

	private Context mContext;

	private List<UserPath> paths;

	private LayoutInflater inflater;

	public MyHistoryAdapter(Context mContext, List<UserPath> paths) {
		this.mContext = mContext;
		this.paths = paths;
		inflater = LayoutInflater.from(this.mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return paths.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return paths.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_car, parent, false);
			holder = new ViewHolder();
			holder.tv_end = (TextView) convertView
					.findViewById(R.id.id_tv_item_car_end);
			holder.tv_start = (TextView) convertView
					.findViewById(R.id.id_tv_item_car_start);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_start.setText(paths.get(position).getStartAddress());
		holder.tv_end.setText(paths.get(position).getEndAddress());
		return convertView;
	}

	class ViewHolder {
		TextView tv_start, tv_end;
	}

}
