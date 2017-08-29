package com.kuxiao.usercar;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ZiXunActivity extends Activity {

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zixun);
		initViews();
	}
	protected void initViews() {
		mWebView = (WebView) findViewById(R.id.mWebView);
		mWebView.setScrollBarStyle(0);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setAllowFileAccess(true);
		webSettings.setBuiltInZoomControls(true);
		mWebView.loadUrl("http://www.dy3y.net/productad/");
		// ��������
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});
		// ����ǵ���ҳ�ϵ����ӱ������ʱ��
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

	}

}
