package com.runye.express.service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.runye.express.android.R;
import com.runye.express.utils.FileUtil;
import com.runye.express.utils.LogUtil;

/***
 * s 更新版本
 * 
 * @author zhangjia
 * 
 */
public class UpdateService extends Service {
	private final String TAG = "UpdateService";
	/** 超时 */
	private static final int TIMEOUT = 10 * 1000;
	/** 下载地址 */
	private static final String down_url = "http://www.tyfind.com:8080/find-consumers-android-update/new/find-consumers-android.apk";
	/** 下载成功 */
	private static final int DOWN_OK = 1;
	/** 下载失败 */
	private static final int DOWN_ERROR = 0;
	/***
	 * 创建通知栏
	 */
	RemoteViews mViews;
	/** 应用名称 */
	private String app_name;
	/** 通知管理器 */
	private NotificationManager notificationManager;
	/** 通知 */
	private Notification notification;
	/** 点击通知跳转 */
	private Intent mUpdateIntent;
	/** 等待跳转 */
	private PendingIntent mPendingIntent;
	/** 通知ID */
	private final int notification_id = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.d(TAG, "UpdateService+onStartCommand");
		app_name = getResources().getString(R.string.app_name);
		// 创建文件
		FileUtil.createFile(app_name);

		createNotification();

		createThread();

		return super.onStartCommand(intent, flags, startId);

	}

	/**
	 * 
	 * @Description: 创建通知
	 * @return void
	 */
	public void createNotification() {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		android.app.Notification.Builder builder = new Notification.Builder(this);
		mViews = new RemoteViews(getPackageName(), R.layout.notification_item);
		mViews.setImageViewResource(R.id.notificationImage, R.drawable.ic_launcher);
		mViews.setTextViewText(R.id.notificationTitle, "Find物流版正在下载");
		mViews.setTextViewText(R.id.notificationPercent, "0%");
		mViews.setProgressBar(R.id.notificationProgress, 100, 0, false);
		builder.setContent(mViews);
		mUpdateIntent = new Intent(Intent.ACTION_MAIN);
		mUpdateIntent.addCategory(Intent.CATEGORY_HOME);
		mPendingIntent = PendingIntent.getActivity(this, 0, mUpdateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(mPendingIntent);
		builder.setTicker("开始下载，点击可查看");
		builder.setSmallIcon(R.drawable.ic_launcher).setWhen(System.currentTimeMillis()).setAutoCancel(true);// 设置可以清除
		notification = builder.getNotification();
		notificationManager.notify(notification_id, notification);
	}

	/***
	 * 开线程下载
	 */
	public void createThread() {
		/***
		 * 更新UI
		 */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_OK:
					InstallationAPK();
					break;
				case DOWN_ERROR:
					break;

				default:
					stopSelf();
					break;
				}

			}

		};

		final Message message = new Message();

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					long downloadSize = downloadUpdateFile(down_url, FileUtil.updateFile.toString());
					if (downloadSize > 0) {
						// 下载成功
						message.what = DOWN_OK;
						handler.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
					message.what = DOWN_ERROR;
					handler.sendMessage(message);
				}

			}
		}).start();
	}

	/**
	 * 
	 * @Description: 自动安装
	 * @return void
	 */
	private void InstallationAPK() {
		notificationManager.cancel(notification_id);
		// 停止服务
		stopSelf();
		// 下载完成，点击安装
		Uri uri = Uri.fromFile(FileUtil.updateFile);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		startActivity(intent);

	}

	/***
	 * 下载文件
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateFile(String down_url, String file) throws Exception {
		int down_step = 5;// 提示step
		int totalSize;// 文件总大小
		int downloadCount = 0;// 已经下载好的大小
		int updateCount = 0;// 已经上传的文件大小
		InputStream inputStream;
		OutputStream outputStream;
		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		// 获取下载文件的size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
		byte buffer[] = new byte[1024];
		int readsize = 0;
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;// 时时获取下载到的大小
			/**
			 * 每次增张5%
			 */
			if (updateCount == 0 || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
				updateCount += down_step;
				mViews.setTextViewText(R.id.notificationPercent, updateCount + "%");
				mViews.setProgressBar(R.id.notificationProgress, 100, updateCount, false);
				notificationManager.notify(notification_id, notification);

			}

		}
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		inputStream.close();
		outputStream.close();

		return downloadCount;

	}

}
