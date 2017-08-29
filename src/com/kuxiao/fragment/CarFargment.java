package com.kuxiao.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kuxiao.usercar.NavigationActivity;
import com.kuxiao.usercar.R;
import com.kuxiao.usercar.adapter.MyPathCursorAdapter;
import com.kuxiao.usercar.db.PathContentProvider;
import com.kuxiao.usercar.db.PathDao;

public class CarFargment extends Fragment {
	private ListView lv_History;

	private static final int LOADLER_ID = 0;

	// private MyHistoryAdapter mAdaper = null;

	private MyPathCursorAdapter mPathCursorAdapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.car, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initViews(view);
		initLoadler();
	}

	public MyPathCursorAdapter getmPathCursorAdapter() {
		return mPathCursorAdapter;
	}

	private void initLoadler() {

		getLoaderManager().initLoader(LOADLER_ID, null,
				new LoaderCallbacks<Cursor>() {

					@Override
					public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

						CursorLoader cursorLoader = new CursorLoader(
								getActivity(),
								PathContentProvider.URI_PATH_ALL, null, null,
								null, PathDao.PATHS_ID + " DESC");
						return cursorLoader;
					}

					@Override
					public void onLoadFinished(Loader<Cursor> loader,
							Cursor cursor) {
						if (loader.getId() == LOADLER_ID) {
							mPathCursorAdapter.swapCursor(cursor);
						}
					}

					@Override
					public void onLoaderReset(Loader<Cursor> arg0) {
						mPathCursorAdapter.swapCursor(null);
					}
				});
	}

	private void initViews(View view) {
		lv_History = (ListView) view.findViewById(R.id.id_lv_history);
		mPathCursorAdapter = new MyPathCursorAdapter(getActivity(), null, false);
		lv_History.setAdapter(mPathCursorAdapter);
		lv_History.setOnItemClickListener((NavigationActivity) getActivity());
	}

}
