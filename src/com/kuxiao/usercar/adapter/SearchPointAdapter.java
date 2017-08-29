package com.kuxiao.usercar.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.kuxiao.usercar.R;

public class SearchPointAdapter extends BaseAdapter {

	private Context mContext;

	private List<PoiInfo> points;

	private LayoutInflater inflater;

	public SearchPointAdapter(Context mContext, List<PoiInfo> points) {
		this.mContext = mContext;
		this.points = points;
		inflater = LayoutInflater.from(this.mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return points.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return points.get(position);
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
			convertView = inflater.inflate(R.layout.search_item, parent, false);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.id_tv_item_search_name);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.id_tv_item_search_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(points.get(position).name);
		holder.tv_address.setText(points.get(position).address);
		return convertView;
	}

	public void setPoints(List<PoiInfo> points) {
		this.points = points;
	}

	public List<PoiInfo> getPoints() {
		return points;
	}

	class ViewHolder {
		TextView tv_name, tv_address;
	}

}
