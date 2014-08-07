package com.runye.express.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.runye.express.activity.app.MyApplication;
import com.runye.express.activity.common.OrderInfoActivity;
import com.runye.express.activity.common.PopUpDialogActivity;
import com.runye.express.activity.common.WayBillsInfoActivity;
import com.runye.express.adapter.WayBillsModeAdapter;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.bean.WayBills;
import com.runye.express.http.MyHttpClient;
import com.runye.express.listview.SingleLayoutListView;
import com.runye.express.listview.SingleLayoutListView.OnLoadMoreListener;
import com.runye.express.listview.SingleLayoutListView.OnRefreshListener;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.NetStatusBroadcast;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;
import com.runye.express.widget.BadgeView;
import com.runye.express.widget.MyToast;

/**
 * 
 * @ClassName: OrderFragment
 * @Description: 订单选择fragment
 * @author LanJie.Chen
 * @date 2014-7-25 下午3:49:30
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class CouriersFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
	private final String TAG = "CouriersFragment";
	/**
	 * 请求完成标识
	 */
	/** 刷新listview */
	private SingleLayoutListView mListView;
	private List<WayBills> mOrderData;
	private WayBillsModeAdapter mAdapter;
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
	// @SuppressLint("UseSparseArrays")
	// private final Map<Integer, Integer> mapBefore = new HashMap<Integer,
	// Integer>();
	/** 请求计数器 */
	private int count = 0;
	private int index = 0;
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {

		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.mList = (List<WayBills>) msg.obj;
					mAdapter.notifyDataSetChanged();
				}
				mListView.onRefreshComplete(); // 下拉刷新完成
				break;
			case LOAD_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.mList.addAll((List<WayBills>) msg.obj);
					LogUtil.d(TAG, "mAdapter.mList加载后的大小：" + mAdapter.mList.size());
					mAdapter.notifyDataSetChanged();
				}
				mListView.onLoadMoreComplete(); // 加载更多完成
				if (mAdapter.mList.size() == mapNow.get(count)) {
					// 这里做处理是为了让数据加载完成后再次点击加载更多，才出现提示
					if (index > 0) {
						MyToast.createToast(getActivity(), "没有更多了", false);
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

	public CouriersFragment() {
	}

	/** 标题 */
	private TextView tv_title;
	/** 项目 */
	private ImageView iv_project;
	/** 消息 */
	private ImageView iv_notify;
	/** 菜单 */
	private ImageView iv_menu;
	/** 消息提示 */
	private BadgeView bv_notify;
	/** 请求类型 */
	private String status;
	/** 只有在fragment创建的时候在自动加载 */
	private boolean isFirst = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogUtil.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtil.d(TAG, "onCreateView");
		View rootView = inflater.inflate(R.layout.activity_couriers_main, container, false);
		return rootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		LogUtil.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		initUI();
		SysExitUtil.activityList.add(getActivity());
		NetStatusBroadcast receiver = new NetStatusBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		getActivity().registerReceiver(receiver, filter);

	}

	@Override
	public void onResume() {
		if (isFirst) {
			mListView.pull2RefreshManually(true);
		}
		// 必须放在这里
		if (isFirst == false) {
			LogUtil.d(TAG, "停止了自动刷新");
			mListView.pull2RefreshManually(false);
		}
		super.onResume();

	}

	@Override
	public void onPause() {
		isFirst = false;
		super.onPause();
	}

	private void initUI() {
		isFirst = true;
		status = getResources().getStringArray(R.array.couriers_request)[0];
		// 菜单
		iv_menu = (ImageView) getActivity().findViewById(R.id.activity_couriers_main_menu);
		iv_menu.setOnClickListener(new MyOnclick());
		// 通知
		iv_notify = (ImageView) getActivity().findViewById(R.id.activity_couriers_main_notify);
		iv_notify.setOnClickListener(new MyOnclick());
		bv_notify = new BadgeView(getActivity(), iv_notify);
		bv_notify.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		bv_notify.setText("3");
		bv_notify.show();
		// 项目选择
		iv_project = (ImageView) getActivity().findViewById(R.id.activity_couriers_main_project);
		iv_project.setOnClickListener(new MyOnclick());

		tv_title = (TextView) getActivity().findViewById(R.id.activity_couriers_main_title);
		tv_title.setText(getResources().getString(R.string.couriers_new));
		// 刷新listview
		mListView = (SingleLayoutListView) getActivity().findViewById(R.id.activity_couriers_main_listview);
		mOrderData = new ArrayList<WayBills>();
		mListView.setOnItemClickListener(new MyListLisytener());
		mAdapter = new WayBillsModeAdapter(getActivity(), mOrderData);
		mListView.setAdapter(mAdapter);
		mListView.setCanLoadMore(true);
		mListView.setCanRefresh(true);
		mListView.setAutoLoadMore(true);
		mListView.setMoveToFirstItemAfterRefresh(true);
		mListView.setOnRefreshListener(this);
		mListView.setOnLoadListener(this);
	}

	/**
	 * 
	 * @Description: 这里应该是首次获取和刷新和加载更多
	 * @return void
	 */
	private void getOrders(final int type) {
		/**
		 * 根据siteid请求order
		 */
		/** 商户token */
		String access_token = MyApplication.getInstance().getAccess_token();
		String siteId = MyApplication.getInstance().getSiteId();
		String id = MyApplication.getInstance().getId();
		LogUtil.d(TAG, "请求的token:" + access_token + "\n站点ID：" + siteId + "\nid:" + id + "\n类型：" + status);
		// 根据状态加载订单
		RequestParams params = new RequestParams();
		params.put("status", status);
		params.put("skip", skip + "");
		params.put("limit", limit + "");
		// params.put("site_id", siteId);
		// params.put("postman_id", id);
		MyHttpClient.getOrders("http://api.tyfind.cn:8008/orders", "svM5aSHE2C/eS81HCGoKUxjjwhvF8OhGBCw1be3lCE0=",
				params, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, org.json.JSONObject response) {
						super.onSuccess(statusCode, response);
						// LogUtil.d(TAG, "请求成功：" + response);
						try {
							count++;
							int orderNumber = response.getJSONObject("_metadata").getInt("count");
							mapNow.put(count, orderNumber);
							String result = response.getJSONArray("records").toString().replaceAll("\"__v\"", "\"v\"")
									.replaceAll("\"_id\"", "\"id\"");
							List<WayBills> _list = new ArrayList<WayBills>();
							_list.addAll(JSON.parseArray(result, WayBills.class));
							if (type == 0) { // 下拉刷新
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
						ToastUtil.showShortToast(getActivity(), "请求出错了，请重试");
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
			Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
			// 注意，这个刷新listview返回的position有误，需要减一
			// WayBills bean = mAdapter.mList.get(position - 1);
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("ORDERINFO", bean);
			// intent.putExtras(bundle);
			// startActivity(intent);
			startActivity(new Intent(getActivity(), WayBillsInfoActivity.class));
			boolean stop = false;
			Message message = mHandler.obtainMessage(STOP_REFRSH, stop);
			mHandler.sendMessage(message);
		}
	}

	private class MyOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == iv_menu) {
				SlidingActivity activity = (SlidingActivity) getActivity();
				activity.toggle();
				isFirst = false;
			}
			if (v == iv_project) {
				startActivityForResult(new Intent(getActivity(), PopUpDialogActivity.class), 1);
				getActivity().overridePendingTransition(R.anim.chat_slide_in_from_right, 0);
			}
			if (v == iv_notify) {
				ToastUtil.showShortToast(getActivity(), "开发中");
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 100) {
			tv_title.setText(data.getExtras().getString("TITLE"));
			status = data.getExtras().getString("STATUS");
			mAdapter.mList.clear();
			mAdapter.notifyDataSetChanged();
			mapNow.clear();
			count = 0;
			mListView.pull2RefreshManually(true);
			getOrders(0);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onLoadMore() {
		// 加载更多
		Log.d(TAG, "onLoad");
		skip += limit;
		getOrders(1);

	}

	@Override
	public void onRefresh() {
		// 下拉刷新
		Log.d(TAG, "onRefresh");
		skip = 0;
		getOrders(0);

	}
}
