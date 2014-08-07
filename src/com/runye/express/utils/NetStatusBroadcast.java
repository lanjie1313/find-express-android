package com.runye.express.utils;

import com.runye.express.widget.MyToast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @ClassName: NetState
 * @Description: 监听网络变化工具类
 * @author LanJie.Chen
 * @date 2014-7-31 上午10:46:35
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class NetStatusBroadcast extends BroadcastReceiver {
	private final static String TAG = "NetState";

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (!gprs.isConnected() && !wifi.isConnected()) {
			// 无网络
			MyToast.createToast((Activity) context, "当前网络已断开，点击设置网络", true);
			LogUtil.d(TAG, "当前网络已断开，点击设置网络");
		} else {
			LogUtil.d(TAG, "有网络");
			MyToast.dissToast((Activity) context);
		}
	}

}
