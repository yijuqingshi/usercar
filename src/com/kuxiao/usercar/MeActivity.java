package com.kuxiao.usercar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class MeActivity extends Activity {

	private LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me);
		ll = (LinearLayout) findViewById(R.id.id_ll_me_package);
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(MeActivity.this,
						ZiXunActivity.class);
				startActivity(intent1);
			}
		});
	}

}
