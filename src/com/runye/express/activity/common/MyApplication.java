package com.runye.express.activity.common;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MyApplication extends Application {

	private static MyApplication mInstance = null;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;
	/** 本地安装版本 */
	public int localVersion = 0;
	/** 服务器版本 */
	public int serverVersion = 2;
	/** 安装目录 */
	public String downloadDir = "FindExpress/";
	/** 是否管理员 */
	private boolean ISADMIN = false;
	/** 是否站长 */
	private boolean ISMASTER = false;
	/** 是否快递员 */
	private boolean ISCOURIERS = false;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = new MyApplication();
		initEngineManager(this);
		initImageLoader(getApplicationContext());
		try {
			PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
			localVersion = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(new MyGeneralListener())) {
			Toast.makeText(MyApplication.getInstance().getApplicationContext(), "BMapManager  初始化错误!",
					Toast.LENGTH_LONG).show();
		}
	}

	public static MyApplication getInstance() {
		return mInstance;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(MyApplication.getInstance().getApplicationContext(), "您的网络出错啦！", Toast.LENGTH_LONG)
						.show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(MyApplication.getInstance().getApplicationContext(), "输入正确的检索条件！", Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onGetPermissionState(int arg0) {

		}

	}

	/**
	 * 
	 * @Description: 配置imageloader
	 * @param context
	 * @return void
	 */
	private void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).diskCache(new UnlimitedDiscCache(cacheDir))
				.denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

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

}
