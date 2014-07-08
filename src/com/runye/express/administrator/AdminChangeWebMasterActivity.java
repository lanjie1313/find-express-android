package com.runye.express.administrator;

import android.app.Activity;
import android.os.Bundle;

import com.runye.express.android.R;
import com.runye.express.utils.SysExitUtil;

/**
 * 
 * @ClassName: AdminChangeWebMasterActivity
 * @Description: 修改站长
 * @author LanJie.Chen
 * @date 2014-7-3 上午10:45:18
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AdminChangeWebMasterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_change_webmaster);
		SysExitUtil.activityList.add(AdminChangeWebMasterActivity.this);
	}

	private void initUI() {
	}
}
