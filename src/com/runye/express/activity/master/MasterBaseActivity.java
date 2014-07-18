package com.runye.express.activity.master;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.runye.express.activity.common.OrderInfoActivity;
import com.runye.express.adapter.OrderModeAdapter;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.bean.OrderModeBean;
import com.runye.express.http.HttpUri;
import com.runye.express.http.MyHttpClient;
import com.runye.express.listview.PullToRefreshView;
import com.runye.express.listview.PullToRefreshView.OnFooterRefreshListener;
import com.runye.express.listview.PullToRefreshView.OnHeaderRefreshListener;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: MasterBaseActivity
 * @Description: master activity 管理器
 * @author LanJie.Chen
 * @date 2014-7-7 下午4:32:14
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class MasterBaseActivity extends Activity implements OnHeaderRefreshListener, OnFooterRefreshListener {
	private final String TAG = "MasterBaseActivity";
	/** 刷新listview */
	private PullToRefreshView mPullToRefreshView;
	private ListView mListView;
	private List<OrderModeBean> mList;
	private OrderModeAdapter adapter;
	/** 商户token */
	private String access_token;
	/** 分页 */
	private int skip = 0;
	/** 请求个数 */
	private final int limit = 10;
	/** 请求次数 */
	private int allCount = 0;
	/** 下拉 */
	private final HashMap<Integer, List<OrderModeBean>> map = new HashMap<Integer, List<OrderModeBean>>();
	/** 上拉 */
	private final HashMap<Integer, Integer> map1 = new HashMap<Integer, Integer>();
	/** 是否下拉操作 */
	private boolean isHead;
	/** 是否上拉操作 */
	private boolean isFoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master_base);
		initUI();
		SysExitUtil.activityList.add(MasterBaseActivity.this);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initUI() {
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.activity_master_pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mListView = (ListView) findViewById(R.id.activity_master_listview);
		mList = new ArrayList<OrderModeBean>();
		mListView.setOnItemClickListener(new MyListLisytener());
		mPullToRefreshView.startRefresh();
		getOrders();
		adapter = new OrderModeAdapter(MasterBaseActivity.this, mList);
		mListView.setAdapter(adapter);

	}

	/**
	 * 
	 * @Description: 这里应该是首次获取和刷新和加载更多
	 * @return void
	 */

	private void getOrders() {

		/**
		 * 根据siteid请求order
		 */
		SharedPreferences preferences = getSharedPreferences("user_info", 1);
		// String siteId = preferences.getString("siteId", "");
		access_token = preferences.getString("access_token", "");
		// 根据状态加载订单
		String status = getIntent().getStringExtra("STATUS");
		RequestParams params = new RequestParams();
		params.put("status", status);
		params.put("skip", skip + "");
		params.put("limit", limit + "");
		MyHttpClient.getOrders(HttpUri.ORDERS, access_token, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, org.json.JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.d(TAG, "请求成功：" + response);

				// 根据Bean类的到每一个json数组的项
				String replaceAfter = "";
				try {
					replaceAfter = response.getJSONArray("records").toString().replaceAll("\"__v\"", "\"v\"")
							.replaceAll("\"_id\"", "\"id\"");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// 下拉清空list
				if (isHead) {
					mList.clear();
				}
				mList.addAll(JSON.parseArray(replaceAfter, OrderModeBean.class));
				// 记录新增加的item
				int count = 0;
				map.put(allCount, mList);
				// 当前的list
				List<OrderModeBean> listNow = map.get(allCount);
				// 上一个list
				List<OrderModeBean> listBefor = map.get(allCount - 1);
				// 只保证在下拉刷新时比较
				if (allCount > 0 && isHead) {
					for (int i = 0; i < listBefor.size(); i++) {
						String idBefor = listBefor.get(i).getId();
						String idNow = listNow.get(i).getId();
						if (!idBefor.equals(idNow)) {
							count++;
						}
					}
					if (count > 0) {
						ToastUtil.showShortToast(MasterBaseActivity.this, "新增加了" + count + "个订单");
					} else {
						Toast toast = Toast.makeText(getApplicationContext(), "没有新订单", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 100);
						toast.show();
					}

				}
				LogUtil.d(TAG, "mList刷新后的大小========" + mList.size());
				// 只保证在上拉加载时比较是否加载完
				map1.put(allCount, mList.size());
				if (isFoot) {
					if (map1.get(allCount) == map1.get(allCount - 1)) {
						ToastUtil.showShortToast(MasterBaseActivity.this, "没有更多订单了");
					} else {
						ToastUtil.showShortToast(MasterBaseActivity.this,
								"新加载了" + (map1.get(allCount) - map1.get(allCount - 1)) + "个订单");
					}
				}
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss     ");
				Date curDate = new Date(System.currentTimeMillis());
				// 获取当前时间
				String str = formatter.format(curDate);
				mPullToRefreshView.onHeaderRefreshComplete("最后更新：" + str);
				mPullToRefreshView.onFooterRefreshComplete();
				adapter.notifyDataSetChanged();
				allCount++;
				isHead = false;
				isFoot = false;
			}

			@Override
			public void onFailure(Throwable e, org.json.JSONObject errorResponse) {
				super.onFailure(e, errorResponse);

				ToastUtil.showShortToast(MasterBaseActivity.this, "请求出错了，请重试");
			}
		});
	}

	/**
	 * 
	 * @ClassName: MyListLisytener
	 * @Description: 点击item进入到订单详情页面
	 */
	class MyListLisytener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(MasterBaseActivity.this, OrderInfoActivity.class);
			OrderModeBean bean = mList.get(position);
			Bundle bundle = new Bundle();
			bundle.putSerializable("ORDERINFO", bean);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// 刷新
		isHead = true;
		skip = 0;
		LogUtil.d(TAG, "skip:onHeaderRefresh==" + skip);
		getOrders();

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// 加载
		isFoot = true;
		skip += limit;
		LogUtil.d(TAG, "skip:onFooterRefresh==" + skip);
		getOrders();

	}

}
