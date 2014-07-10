package com.runye.express.activity.administrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.runye.express.adapter.SiteAdapter;
import com.runye.express.android.R;
import com.runye.express.bean.SiteBean;
import com.runye.express.listview.PullToRefreshView;
import com.runye.express.listview.PullToRefreshView.OnFooterRefreshListener;
import com.runye.express.listview.PullToRefreshView.OnHeaderRefreshListener;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: AdminListActivity
 * @Description:站点管理界面
 * @author LanJie.Chen
 * @date 2014-7-2 下午6:33:44
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AdminSiteActivity extends Activity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	/** 刷新listview */
	private PullToRefreshView mPullToRefreshView;
	private ListView listView;
	/** 数据源 */
	private List<SiteBean> mList;
	/** 增加站点 */
	private Button bt_addSite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_site);
		initUI();
		SysExitUtil.activityList.add(AdminSiteActivity.this);

	}

	private void initUI() {
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.activity_admin_site_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		listView = (ListView) findViewById(R.id.activity_admin_site_listView);
		mList = getSite();
		listView.setAdapter(new SiteAdapter(AdminSiteActivity.this, mList));
		listView.setOnItemClickListener(new MyListItemListener());

		bt_addSite = (Button) findViewById(R.id.activity_admin_site_addSite);
		bt_addSite.setOnClickListener(new MyButtonListener());
	}

	/**
	 * 
	 * @Description: 联网取站点
	 * @return List<String>
	 */
	private List<SiteBean> getSite() {
		String[] dataObjects = new String[] { "全站", "A站", "B站", "C站", "D站",
				"E站", "F站", "G站", "H站" };
		List<SiteBean> list = new ArrayList<SiteBean>();
		for (int i = 0; i < dataObjects.length; i++) {
			SiteBean bean = new SiteBean();
			bean.setName(dataObjects[i]);
			list.add(bean);
		}
		return list;
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void run() {

				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日   HH:mm:ss     ");
				Date curDate = new Date(System.currentTimeMillis());
				// 获取当前时间
				String str = formatter.format(curDate);
				mPullToRefreshView.onHeaderRefreshComplete("最后更新：" + str);
			}
		}, 1000);

	}

	/**
	 * button点击监听器
	 */
	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(AdminSiteActivity.this,
					AdminAddSiteActivity.class));
		}
	}

	/**
	 * listview点击事件监听器
	 */
	class MyListItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView textView = (TextView) view
					.findViewById(R.id.item_admin_site_listview_siteName);
			String siteName = textView.getText().toString();
			ToastUtil.showShortToast(AdminSiteActivity.this, siteName);
			Intent intent = new Intent();
			intent.putExtra("SITENAME", siteName);
			intent.setClass(AdminSiteActivity.this, AdminSiteInfoActivity.class);
			startActivity(intent);
		}
	}

}
