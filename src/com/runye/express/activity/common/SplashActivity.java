package com.runye.express.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.runye.express.android.R;

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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		// 设置延迟，播放登陆界面
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				RedirectMainActivity();
			}
		}, 3000);
	}

	/**
	 * 跳转
	 */
	private void RedirectMainActivity() {
		Intent i = new Intent();
		i.setClass(SplashActivity.this, LoginActivity.class);
		startActivity(i);
		SplashActivity.this.finish();
	}
}
