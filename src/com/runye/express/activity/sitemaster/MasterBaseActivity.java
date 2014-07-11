package com.runye.express.activity.sitemaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
	private List<OrderModeBean> mList;
	private PullToRefreshView mPullToRefreshView;
	private ListView mListView;
	private String access_token;
	private int skip = 0;
	private final int limit = 10;

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
	OrderModeAdapter adapter = null;
	private int allCount = 0;

	private void getOrders() {
		/**
		 * 根据siteid请求order
		 */
		SharedPreferences preferences = getSharedPreferences("user_info", 1);
		// String siteId = preferences.getString("siteId", "");
		access_token = preferences.getString("access_token", "");
		//
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
				mList.addAll(JSON.parseArray(replaceAfter, OrderModeBean.class));
				LogUtil.d(TAG, "mList的大小========" + mList.size());
				allCount += mList.size();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss     ");
				Date curDate = new Date(System.currentTimeMillis());
				// 获取当前时间
				String str = formatter.format(curDate);
				mPullToRefreshView.onHeaderRefreshComplete("最后更新：" + str);
				mPullToRefreshView.onFooterRefreshComplete();
				adapter.notifyDataSetChanged();

			}

			@Override
			public void onFailure(Throwable e, org.json.JSONObject errorResponse) {
				super.onFailure(e, errorResponse);
				ToastUtil.showShortToast(MasterBaseActivity.this, "请求出错了，请重试");
			}
		});
	}

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
		mList = new ArrayList<OrderModeBean>();
		adapter = new OrderModeAdapter(MasterBaseActivity.this, mList);
		skip = 0;
		LogUtil.d(TAG, "skip:onHeaderRefresh==" + skip);
		getOrders();

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		skip += limit;
		LogUtil.d(TAG, "skip:onFooterRefresh==" + skip);
		getOrders();

	}

}
