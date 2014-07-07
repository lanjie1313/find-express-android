package com.runye.express.webmaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.runye.express.adapter.OrderModeAdapter;
import com.runye.express.android.R;
import com.runye.express.bean.OrderModeBean;
import com.runye.express.commonactivity.OrderInfoActivity;
import com.runye.express.listview.PullToRefreshView;
import com.runye.express.listview.PullToRefreshView.OnFooterRefreshListener;
import com.runye.express.listview.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 
 * @ClassName: MasterBaseActivity
 * @Description: master activity 管理器
 * @author LanJie.Chen
 * @date 2014-7-7 下午4:32:14
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class MasterBaseActivity extends Activity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private List<OrderModeBean> mList;
	private PullToRefreshView mPullToRefreshView;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master_base);
		initUI();
	}

	private void initUI() {
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.activity_master_pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mListView = (ListView) findViewById(R.id.activity_master_listview);
		//
		String status = getIntent().getStringExtra("STATUS");
		mList = getOrderInfo(status);
		OrderModeAdapter adapter = new OrderModeAdapter(this, mList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new MyListLisytener());
	}

	private List<OrderModeBean> getOrderInfo(String status) {
		List<OrderModeBean> list = new ArrayList<OrderModeBean>();
		for (int i = 0; i < 20; i++) {
			OrderModeBean bean = new OrderModeBean();
			bean.setNumber(i + "");
			bean.setTime("2014年7月4日14:32:51");
			bean.setShopName("美特好");
			bean.setAddress("华顿实业8层");
			bean.setRating(3 + "");
			bean.setCharge("" + i);
			bean.setCouriersName("sss");
			bean.setCouriersNumber(i + "");
			bean.setStatus(status);
			list.add(bean);
		}
		return list;
	}

	class MyListLisytener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(MasterBaseActivity.this,
					OrderInfoActivity.class);
			intent.putExtra("ISSELECT", true);
			startActivity(intent);
		}
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
}
