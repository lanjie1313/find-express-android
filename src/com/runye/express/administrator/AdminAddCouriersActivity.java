package com.runye.express.administrator;

import android.app.Activity;
import android.os.Bundle;

import com.runye.express.android.R;
import com.runye.express.utils.SysExitUtil;

/**
 * 
 * @ClassName: AdminAddCouriersActivity
 * @Description: 新增快递员
 * @author LanJie.Chen
 * @date 2014-7-2 下午6:36:31
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AdminAddCouriersActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_addcouriers);
		SysExitUtil.activityList.add(AdminAddCouriersActivity.this);
	}
}
