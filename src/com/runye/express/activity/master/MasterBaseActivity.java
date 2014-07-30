package com.runye.express.activity.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.runye.express.activity.common.ComminBaseActivity;
import com.runye.express.activity.common.MyApplication;
import com.runye.express.activity.common.OrderInfoActivity;
import com.runye.express.adapter.OrderModeAdapter;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.bean.OrderModeBean;
import com.runye.express.http.HttpUri;
import com.runye.express.http.MyHttpClient;
import com.runye.express.listview.SingleLayoutListView;
import com.runye.express.listview.SingleLayoutListView.OnLoadMoreListener;
import com.runye.express.listview.SingleLayoutListView.OnRefreshListener;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.MyToast;
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
public class MasterBaseActivity extends ComminBaseActivity {
	private final String TAG = "MasterBaseActivity";
	/** 刷新listview */
	private SingleLayoutListView mListView;
	private List<OrderModeBean> mOrderData;
	private OrderModeAdapter mAdapter;
	/** 商户token */
	private String access_token;
	/** 分页 */
	private int skip = 0;
	/** 请求个数 */
	private final int limit = 10;
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;
	/***/
	private static final int STOP_REFRSH = 12;
	/** 当前请求 */
	@SuppressLint("UseSparseArrays")
	private final Map<Integer, Integer> mapNow = new HashMap<Integer, Integer>();
	/** 上一个请求 */
	@SuppressLint("UseSparseArrays")
	private final Map<Integer, Integer> mapBefore = new HashMap<Integer, Integer>();
	/** 请求计数器 */
	private int count = 0;
	private int index = 0;
	private int index2 = 0;
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {

		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.mList = (List<OrderModeBean>) msg.obj;
					mAdapter.notifyDataSetChanged();
				}
				mListView.onRefreshComplete(); // 下拉刷新完成
				if (count > 1) {
					if (mapNow.get(count).equals(mapBefore.get(count))) {
						MyToast.createToast(MasterBaseActivity.this, "没有新的订单");
					} else {
						MyToast.createToast(MasterBaseActivity.this,
								"有新的订单:" + (mapNow.get(count) - mapBefore.get(count)) + "个");
					}
				}
				break;
			case LOAD_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.mList.addAll((List<OrderModeBean>) msg.obj);
					LogUtil.d(TAG, "mAdapter.mList加载后的大小：" + mAdapter.mList.size());
					mAdapter.notifyDataSetChanged();
				}
				mListView.onLoadMoreComplete(); // 加载更多完成
				if (mAdapter.mList.size() == mapNow.get(count)) {
					// 这里做处理是为了让数据加载完成后再次点击加载更多，才出现提示
					if (index > 0) {
						MyToast.createToast(MasterBaseActivity.this, "没有更多了");
					}
					index++;
				}
				break;
			case STOP_REFRSH:
				mListView.setDoRefreshOnUIChanged((Boolean) msg.obj);
				break;
			}
		};
	};

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
		if (index2 > 0) {
			mListView.setDoRefreshOnUIChanged(false);
		}
		index2++;
	}

	private void initUI() {

		mListView = (SingleLayoutListView) findViewById(R.id.singleLayoutListView);
		mOrderData = new ArrayList<OrderModeBean>();
		mListView.setOnItemClickListener(new MyListLisytener());
		mAdapter = new OrderModeAdapter(MasterBaseActivity.this, mOrderData);
		mListView.setAdapter(mAdapter);
		mListView.setCanLoadMore(true);
		mListView.setCanRefresh(true);
		mListView.setAutoLoadMore(true);
		mListView.setMoveToFirstItemAfterRefresh(true);
		mListView.setDoRefreshOnUIChanged(true);
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO 下拉刷新
				Log.d(TAG, "onRefresh");
				skip = 0;
				getOrders(0);
			}
		});

		mListView.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO 加载更多
				Log.d(TAG, "onLoad");
				skip += limit;
				getOrders(1);
			}
		});

	}

	/**
	 * 
	 * @Description: 这里应该是首次获取和刷新和加载更多
	 * @return void
	 */
	private void getOrders(final int type) {
		// 首先尝试读取缓存
		// String cacheConfigString = ConfigCache.getUrlCache(HttpUri.ORDERS);
		// 根据结果判定是读取缓存，还是重新读取
		// if (cacheConfigString != null) {
		// LogUtil.d(TAG, "listview有缓存,使用缓存");
		// handleResult(cacheConfigString, type);
		// } else {
		// LogUtil.d(TAG, "listview没有缓存,不使用缓存");
		/**
		 * 根据siteid请求order
		 */
		access_token = MyApplication.getInstance().getAccess_token();
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
				try {
					count++;
					int orderNumber = response.getJSONObject("_metadata").getInt("count");
					mapNow.put(count, orderNumber);
					if (count > 1) {

						mapBefore.put(count, mapNow.get(count - 1));
					}
					String result = response.getJSONArray("records").toString().replaceAll("\"__v\"", "\"v\"")
							.replaceAll("\"_id\"", "\"id\"");
					List<OrderModeBean> _list = new ArrayList<OrderModeBean>();
					_list.addAll(JSON.parseArray(result, OrderModeBean.class));
					if (type == 0) { // 下拉刷新
						// Collections.reverse(mList); //逆序
						LogUtil.d(TAG, "第" + count + "次请求订单的总数：" + mapNow.get(count));
						if (count > 1) {

							if (mapNow.get(count).equals(mapBefore.get(count))) {
								LogUtil.d(TAG, "没有新的订单");
							} else {
								LogUtil.d(TAG, "有新的订单:" + (mapNow.get(count) - mapBefore.get(count)));
							}
						}
						Message _Msg = mHandler.obtainMessage(REFRESH_DATA_FINISH, _list);
						mHandler.sendMessage(_Msg);
					} else if (type == 1) {
						Message _Msg = mHandler.obtainMessage(LOAD_DATA_FINISH, _list);
						mHandler.sendMessage(_Msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// 成功下载，则保存到本地作为后面缓存文件
				// ConfigCache.setUrlCache(result, HttpUri.ORDERS);
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
			OrderModeBean bean = mAdapter.mList.get(position);
			Bundle bundle = new Bundle();
			bundle.putSerializable("ORDERINFO", bean);
			intent.putExtras(bundle);
			startActivity(intent);
			boolean stop = false;
			Message message = mHandler.obtainMessage(STOP_REFRSH, stop);
			mHandler.sendMessage(message);
		}
	}

}
