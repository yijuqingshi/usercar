package com.kuxiao.usercar.utils;

import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BaiduNaviManager.TTSPlayStateListener;

public class MyTTSUtils implements BNOuterTTSPlayerCallback,TTSPlayStateListener{

	
	private static final String TAG = MyTTSUtils.class.getSimpleName();
	
	public MyTTSUtils()
	{
	}

	//BNOuterTTSPlayerCallback»Øµ÷
	@Override
	public int getTTSState() {
		return 0;
	}

	@Override
	public void initTTSPlayer() {
		
	}

	@Override
	public void pauseTTS() {
		
	}

	@Override
	public void phoneCalling() {
		
	}

	@Override
	public void phoneHangUp() {
		
	}
	

	@Override
	public int playTTSText(String arg0, int arg1) {
		
		return 0;
	}

	@Override
	public void releaseTTSPlayer() {
		
	}

	@Override
	public void resumeTTS() {
		
	}

	@Override
	public void stopTTS() {
		
	}
	
	
	//TTSPlayStateListenerTTS²¥·Å¼àÌýÆ÷

	@Override
	public void playEnd() {
		
	}

	@Override
	public void playStart() {
   		
	}
	

}
