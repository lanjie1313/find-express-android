package com.runye.express.activity.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.runye.express.android.R;
import com.runye.express.utils.LogUtil;

public class ComminBaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();
			overridePendingTransition(0, R.anim.chat_slide_out_to_right);
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			LogUtil.d("", "");
		}
		return super.onKeyDown(keyCode, event);
	}
}
