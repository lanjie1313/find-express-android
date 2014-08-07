package com.runye.express.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.runye.express.activity.app.MyApplication;
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

	public void getAppVersion() {
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
				// 进度发生变化通知调用方
				// send the message to the client
				try {
					Thread.sleep(3000);
					LogUtil.d(TAG, "检查成功");
					MyApplication.getInstance().serverVersion = 1;
					MyApplication.getInstance().isFinshed = true;
					stopSelf();
					LogUtil.d(TAG, "停止服务");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
