package com.runye.express.map;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

public class MapApplication extends Application {

	private static MapApplication mInstance = null;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;
	/** 是否管理员 */
	private boolean ISADMIN = false;
	/** 是否站长 */
	private boolean ISMASTER = false;
	/** 是否快递员 */
	private boolean ISCOURIERS = false;

	/** 是否管理员 */
	public boolean isISADMIN() {
		return ISADMIN;
	}

	public void setISADMIN(boolean iSADMIN) {
		ISADMIN = iSADMIN;
	}

	/** 是否站长 */
	public boolean isISMASTER() {
		return ISMASTER;
	}

	public void setISMASTER(boolean iSMASTER) {
		ISMASTER = iSMASTER;
	}

	/** 是否快递员 */
	public boolean isISCOURIERS() {
		return ISCOURIERS;
	}

	public void setISCOURIERS(boolean iSCOURIERS) {
		ISCOURIERS = iSCOURIERS;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = new MapApplication();
		initEngineManager(this);
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(new MyGeneralListener())) {
			Toast.makeText(MapApplication.getInstance().getApplicationContext(), "BMapManager  初始化错误!",
					Toast.LENGTH_LONG).show();
		}
	}

	public static MapApplication getInstance() {
		return mInstance;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(MapApplication.getInstance().getApplicationContext(), "您的网络出错啦！", Toast.LENGTH_LONG)
						.show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(MapApplication.getInstance().getApplicationContext(), "输入正确的检索条件！", Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onGetPermissionState(int arg0) {

		}

	}
}
