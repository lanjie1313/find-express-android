package com.runye.express.administrator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.runye.express.android.R;
import com.runye.express.utils.SysExitUtil;

/**
 * 
 * @ClassName: AdminSiteInfoActivity
 * @Description: 站点详细信息
 * @author LanJie.Chen
 * @date 2014-7-3 上午10:45:18
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AdminSiteInfoActivity extends Activity {
	/** 站点名称 */
	private TextView tv_siteName;
	/** 站长名称 */
	private TextView tv_webMaster;
	/** 修改站点 */
	private Button bt_changeSite;
	/** 修改站长 */
	private Button bt_changeMaster;
	/** 修改站长 */
	private Button bt_changeCouriers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_siteinfo);
		initUI();
		SysExitUtil.activityList.add(AdminSiteInfoActivity.this);
		getSiteInfo();
	}

	private void initUI() {
		tv_siteName = (TextView) findViewById(R.id.activity_admin_siteinfo_siteName);
		tv_webMaster = (TextView) findViewById(R.id.activity_admin_siteinfo_webmaster);
		bt_changeCouriers = (Button) findViewById(R.id.activity_admin_siteinfo_changeCouriers);
		bt_changeMaster = (Button) findViewById(R.id.activity_admin_siteinfo_changeMaster);
		bt_changeSite = (Button) findViewById(R.id.activity_admin_siteinfo_changeSite);
		bt_changeCouriers.setOnClickListener(new MyButtonListener());
		bt_changeMaster.setOnClickListener(new MyButtonListener());
		bt_changeSite.setOnClickListener(new MyButtonListener());

	}

	/**
	 * 
	 * @Description: 更具上个界面传递过来的站点查询此站点信息
	 * @return void
	 */
	private void getSiteInfo() {
		String siteName = getIntent().getStringExtra("SITENAME");
		tv_siteName.setText(siteName);
	}

	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_admin_siteinfo_changeCouriers:
				startActivity(new Intent(AdminSiteInfoActivity.this,
						AdminChangeCouriersActivity.class));
				break;
			case R.id.activity_admin_siteinfo_changeMaster:
				startActivity(new Intent(AdminSiteInfoActivity.this,
						AdminChangeWebMasterActivity.class));
				break;
			case R.id.activity_admin_siteinfo_changeSite:
				startActivity(new Intent(AdminSiteInfoActivity.this,
						AdminChangeSiteNameActivity.class));
				break;

			default:
				break;
			}
		}
	}
}
