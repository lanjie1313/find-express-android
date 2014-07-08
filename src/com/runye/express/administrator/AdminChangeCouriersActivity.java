package com.runye.express.administrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.runye.express.adapter.CouriersAdapter;
import com.runye.express.android.R;
import com.runye.express.listview.PullToRefreshView;
import com.runye.express.listview.PullToRefreshView.OnFooterRefreshListener;
import com.runye.express.listview.PullToRefreshView.OnHeaderRefreshListener;
import com.runye.express.utils.SysExitUtil;

/**
 * 
 * @ClassName: AdminChangeCouriersActivity
 * @Description: 修改快递员
 * @author LanJie.Chen
 * @date 2014-7-3 上午10:45:18
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AdminChangeCouriersActivity extends Activity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	/** 刷新listview */
	private PullToRefreshView mPullToRefreshView;
	private ListView mListView;
	/** 新增快递员 */
	private Button bt_addCouriers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_change_couriers);
		initUI();
		SysExitUtil.activityList.add(AdminChangeCouriersActivity.this);
	}

	private void initUI() {
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.activity_admin_change_couriers_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mListView = (ListView) findViewById(R.id.activity_admin_change_couriers_listView);
		List<String> mList = new ArrayList<String>();
		mList = getCouriers();
		mListView.setAdapter(new CouriersAdapter(
				AdminChangeCouriersActivity.this, mList));
		bt_addCouriers = (Button) findViewById(R.id.activity_admin_change_couriers_addcouriers);
		bt_addCouriers.setOnClickListener(new MyButtonListener());

	}

	/**
	 * 
	 * @Description: 联网取站点下的快递员
	 * @return List<String>
	 */
	private List<String> getCouriers() {
		String[] dataObjects = new String[] { "快递员1", "快递员2", "快递员3", "快递员4",
				"快递员5", "快递员6", "快递员7", "快递员8", "快递员9" };
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < dataObjects.length; i++) {
			list.add(dataObjects[i]);
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
		}
	}

}
