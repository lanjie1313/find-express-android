package com.runye.express.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.runye.express.activity.app.MyApplication;
import com.runye.express.android.R;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;

/**
 * 
 * @ClassName: SplashActivity
 * @Description: 启动界面
 * @author LanJie.Chen
 * @date 2014-7-2 下午6:51:36
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class SplashActivity extends Activity {
	private static final String TAG = "SplashActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysExitUtil.activityList.add(SplashActivity.this);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		// 设置延迟，播放登陆界面
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				RedirectMainActivity();
			}
		}, 2000);
	}

	/**
	 * 跳转
	 */
	private void RedirectMainActivity() {
		// 如果用户名密码都有，直接进入主页面
		if (MyApplication.getInstance().isRember()) {
			LogUtil.d(TAG, "有账号密码，无需登录");
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
		} else {
			LogUtil.d(TAG, "无账号密码，需登录");
			Intent i = new Intent();
			i.setClass(SplashActivity.this, LoginActivity.class);
			startActivity(i);
		}
		SplashActivity.this.finish();
	}
}
