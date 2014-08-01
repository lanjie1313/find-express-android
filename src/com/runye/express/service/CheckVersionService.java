package com.runye.express.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.runye.express.utils.LogUtil;

public class CheckVersionService extends Service {
	// It's the mer of server
	private static final String TAG = "CheckVersionService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.d(TAG, "启动CheckVersionService");
		getAppVersion();
		return super.onStartCommand(intent, flags, startId);
	}

	private void getAppVersion() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// MyHttpClient.getVersion(null, null, new
				// JsonHttpResponseHandler() {
				// @Override
				// public void onSuccess(int statusCode, JSONObject response) {
				// super.onSuccess(statusCode, response);
				// }
				//
				// @Override
				// public void onFailure(Throwable e, JSONObject errorResponse)
				// {
				// super.onFailure(e, errorResponse);
				// }
				// });
				try {
					Thread.sleep(3000);
					LogUtil.d(TAG, "启动获取版本服务");
					Intent intent = new Intent("com.example.communication.RECEIVER");
					// 发送Action为com.example.communication.RECEIVER的广播
					sendBroadcast(intent);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}
}
