package com.runye.express.activity.administrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.runye.express.adapter.CouriersAdapter;
import com.runye.express.android.R;
import com.runye.express.bean.CouriersModeBean;
import com.runye.express.fragment.FragmentFive;
import com.runye.express.fragment.FragmentFour;
import com.runye.express.fragment.FragmentOne;
import com.runye.express.fragment.FragmentThree;
import com.runye.express.fragment.FragmentTwo;
import com.runye.express.fragment.ui.IndicatorFragmentActivity;
import com.runye.express.fragment.ui.TitleIndicator;
import com.runye.express.fragment.ui.ViewPagerCompat;
import com.runye.express.listview.PullToRefreshView;
import com.runye.express.listview.PullToRefreshView.OnFooterRefreshListener;
import com.runye.express.listview.PullToRefreshView.OnHeaderRefreshListener;
import com.runye.express.utils.SysExitUtil;

/**
 * 
 * @ClassName: AdminSiteActivity
 * @Description: 列表模式界面
 * @author LanJie.Chen
 * @date 2014-7-2 下午6:34:04
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AdminListActivity extends IndicatorFragmentActivity implements OnHeaderRefreshListener,
		OnFooterRefreshListener {
	public static final int FRAGMENT_ONE = 0;
	public static final int FRAGMENT_TWO = 1;
	public static final int FRAGMENT_THREE = 2;
	public static final int FRAGMENT_FOUR = 3;
	public static final int FRAGMENT_FIVE = 4;
	/** 地图模式 */
	private Button bt_mapMode;
	/** 订单模式 */
	private Button bt_orderMode;
	/** 快递员模式 */
	private Button bt_couriersMode;
	private Spinner mSpinner;
	// 选项卡控件
	private TitleIndicator mIndicator;
	private ListView mListView;
	private ViewPagerCompat compat;
	/** 刷新listview */
	private PullToRefreshView mPullToRefreshView;
	private List<CouriersModeBean> mCouriersModeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		SysExitUtil.activityList.add(AdminListActivity.this);

	}

	private void initUI() {
		bt_mapMode = (Button) findViewById(R.id.activity_admin_list_mapMode);
		bt_orderMode = (Button) findViewById(R.id.activity_admin_list_orderMode);
		bt_couriersMode = (Button) findViewById(R.id.activity_admin_list_couriersMode);
		mSpinner = (Spinner) findViewById(R.id.activity_admin_list_spinner);
		String[] mItems = getResources().getStringArray(R.array.spinnername);
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
		// 绑定 Adapter到控件
		mSpinner.setAdapter(_Adapter);
		bt_mapMode.setOnClickListener(new MyButtonListener());
		bt_orderMode.setOnClickListener(new MyButtonListener());
		bt_couriersMode.setOnClickListener(new MyButtonListener());

		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.activity_admin_list_pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mIndicator = (TitleIndicator) findViewById(R.id.activity_admin_list_pagerindicator);
		compat = (ViewPagerCompat) findViewById(R.id.activity_admin_list_pager);
		mListView = (ListView) findViewById(R.id.activity_admin_list_listview);
		mCouriersModeList = getCouriers();
		CouriersAdapter adapter = new CouriersAdapter(AdminListActivity.this, mCouriersModeList);
		mListView.setAdapter(adapter);
	}

	/**
	 * 
	 * @Description: 获取快递员
	 * @return
	 * @return List<CouriersModeBean>
	 */
	private List<CouriersModeBean> getCouriers() {
		List<CouriersModeBean> list = new ArrayList<CouriersModeBean>();
		for (int i = 0; i < 10; i++) {
			CouriersModeBean bean = new CouriersModeBean();
			bean.setNickName("asdasdasd" + i);
			list.add(bean);
		}
		return list;
	}

	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabs.add(new TabInfo(FRAGMENT_ONE, getString(R.string.order_all), FragmentOne.class));
		tabs.add(new TabInfo(FRAGMENT_TWO, getString(R.string.order_waiting), FragmentTwo.class));
		tabs.add(new TabInfo(FRAGMENT_THREE, getString(R.string.order_done), FragmentThree.class));
		tabs.add(new TabInfo(FRAGMENT_FOUR, getString(R.string.order_sending), FragmentFour.class));
		tabs.add(new TabInfo(FRAGMENT_FIVE, getString(R.string.order_complete), FragmentFive.class));
		return FRAGMENT_ONE;
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

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss     ");
				Date curDate = new Date(System.currentTimeMillis());
				// 获取当前时间
				String str = formatter.format(curDate);
				mPullToRefreshView.onHeaderRefreshComplete("最后更新：" + str);
			}
		}, 1000);

	}

	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_admin_list_mapMode:
				startActivity(new Intent(AdminListActivity.this, AdminMainActivity.class));
				finish();
				break;
			case R.id.activity_admin_list_orderMode:
				mIndicator.setVisibility(View.VISIBLE);
				compat.setVisibility(View.VISIBLE);
				mPullToRefreshView.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				break;
			case R.id.activity_admin_list_couriersMode:
				compat.setVisibility(View.GONE);
				mIndicator.setVisibility(View.GONE);
				mPullToRefreshView.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	}

}
