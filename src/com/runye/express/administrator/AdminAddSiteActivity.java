package com.runye.express.administrator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.runye.express.android.R;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: AdminAddSiteActivity
 * @Description: 新增站点界面
 * @author LanJie.Chen
 * @date 2014-7-2 下午6:35:29
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AdminAddSiteActivity extends Activity {
	/** 确定 */
	private Button bt_addConfim;
	/** 站点名 */
	private EditText et_siteName;
	/** 站长名 */
	private EditText et_webMasterName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_addsite);
		initUI();
		SysExitUtil.activityList.add(AdminAddSiteActivity.this);
	}

	private void initUI() {
		bt_addConfim = (Button) findViewById(R.id.activity_admin_addsite_confim);
		et_siteName = (EditText) findViewById(R.id.activity_admin_addsite_site);
		et_webMasterName = (EditText) findViewById(R.id.activity_admin_addsite_webmasterName);

		bt_addConfim.setOnClickListener(new MyButtonListener());
	}

	/**
	 * 
	 * @Description: 增加站长
	 * @return void
	 */
	private void addSite() {
		if (checkInput()) {
			// 检查无误，开始向服务器发送请求
		} else {
			ToastUtil.showLongToast(AdminAddSiteActivity.this, "不能含有空格！");
		}
	}

	/**
	 * 
	 * @Description: 检查输入合法性
	 * @return boolean
	 */
	private boolean checkInput() {
		String str1 = et_siteName.getText().toString().trim();
		String str2 = et_webMasterName.getText().toString().trim();
		if (!str1.equals("") && !str2.equals("")) {
			return true;
		}
		return false;
	}

	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			addSite();
		}
	}
}
