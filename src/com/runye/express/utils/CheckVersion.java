package com.runye.express.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.runye.express.activity.app.MyApplication;
import com.runye.express.service.DownLoadService;

public class CheckVersion {

	private static final String TAG = "CheckVersion";

	/**
	 * 
	 * @Description: 检查是否需要更新
	 * @return void
	 */

	public static void checkVersion(final Context mContext) {
		if (NetWorkUtil.isNetworkConnected(mContext)) {
			LogUtil.d(TAG, "开始检测更新\n");
			if (MyApplication.getInstance().localVersion < MyApplication.getInstance().serverVersion) {
				LogUtil.d(TAG, "有新版本，开始更新\n" + "本地version:" + MyApplication.getInstance().localVersion
						+ "\n服务器version:" + MyApplication.getInstance().serverVersion);
				// 发现新版本，提示用户更新
				AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
				alert.setTitle("软件升级").setMessage("发现新版本,建议立即更新使用.")
						.setPositiveButton("更新", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 开启更新服务UpdateService
								Intent updateIntent = new Intent(mContext, DownLoadService.class);
								mContext.startService(updateIntent);
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				alert.create().show();

			} else {
				LogUtil.d(TAG, "没有新版本，无需更新");
			}
		}

	}
}
