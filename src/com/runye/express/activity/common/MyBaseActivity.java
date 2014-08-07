package com.runye.express.activity.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.runye.express.android.R;
import com.runye.express.utils.LogUtil;

/**
 * 
 * @ClassName: ComminBaseActivity
 * @Description: activity基类
 * @author LanJie.Chen
 * @date 2014-8-5 下午2:40:22
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class MyBaseActivity extends Activity {

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
