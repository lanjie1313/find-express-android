package com.runye.express.fragment;

import org.json.JSONException;

import android.app.Fragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runye.express.activity.app.MyApplication;
import com.runye.express.activity.master.MasterBaseActivity;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.http.HttpUri;
import com.runye.express.http.MyHttpClient;
import com.runye.express.utils.CheckVersion;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.NetStatusBroadcast;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.widget.BadgeView;
import com.runye.express.widget.HomeButton;
import com.runye.express.widget.HomeButton.HomeClickListener;

/**
 * 
 * @ClassName: OrderFragment
 * @Description: 订单选择fragment
 * @author LanJie.Chen
 * @date 2014-7-25 下午3:49:30
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class MasterFragment extends Fragment {
	/**
	 * 0 、new待分配订单 1、confirmed 已分配订单 2、delivering当日处理订单 3、 awaiting_pickup以往处理订单
	 * 4、complete查看评价5、cancelled
	 * */
	private HomeButton[] buttons;
	private int[] ids;
	private final String TAG = "MasterFragment";
	private BadgeView[] mBadgeView;
	/**
	 * 请求完成标识
	 */
	private int count = 0;
	private NetStatusBroadcast receiver;
	private final String[] status = new String[] { "site_new", "site_confirmed", "site_reject", "complete", "cancelled" };
	private final String access_token = MyApplication.getInstance().getAccess_token();
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			int nowCompleted = (Integer) msg.obj;
			if (nowCompleted == 5) {
				count = 0;
			}
			if (nowCompleted == 1001) {
				CheckVersion.checkVersion(getActivity());
			}
		};
	};

	public MasterFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_master_main, container, false);
		return rootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
		SysExitUtil.activityList.add(getActivity());
		// 刷新首页数据
		getNewOrder();
		getCancelledOrder();
		getConfirmedOrder();
		getCompleteOrder();
		getDeliveringOrder();
		// 监听网络的广播
		receiver = new NetStatusBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		getActivity().registerReceiver(receiver, filter);

	}

	@Override
	public void onResume() {
		super.onResume();
		new Thread(new MyThread()).start();
	}

	public class MyThread implements Runnable {

		@Override
		public void run() {
			boolean isRun = true;
			while (isRun) {
				LogUtil.d(TAG, "" + MyApplication.getInstance().isFinshed);
				if (MyApplication.getInstance().isFinshed) {
					isRun = false;
					LogUtil.d(TAG, "asdasdasd");
					Message message = new Message();
					message.obj = 1001;
					mHandler.sendMessage(message);
				}

			}
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (receiver != null) {
			LogUtil.d(TAG, "注销了网络状态监听广播");
			getActivity().unregisterReceiver(receiver);
		}
	}

	private void initUI() {
		ids = new int[] { R.id.activity_master_main_new, R.id.activity_master_main_confirmed,
				R.id.activity_master_main_delivery, R.id.activity_master_main_compeleted,
				R.id.activity_master_main_cancle };
		buttons = new HomeButton[ids.length];
		mBadgeView = new BadgeView[ids.length];
		for (int i = 0; i < ids.length; i++) {
			buttons[i] = (HomeButton) getActivity().findViewById(ids[i]);
			buttons[i].setOnHomeClick(new MyHomeClickListener());
			mBadgeView[i] = new BadgeView(getActivity(), buttons[i]);
		}
		HomeButton button = (HomeButton) getActivity().findViewById(R.id.bt);
		BadgeView badgeView = new BadgeView(getActivity(), button);
	}

	private void getNewOrder() {
		// 根据状态加载订单
		RequestParams params = new RequestParams();
		params.put("limit", 1 + "");
		params.put("status", status[0]);
		MyHttpClient.getOrders(HttpUri.ORDERS, access_token, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, org.json.JSONObject response) {
				super.onSuccess(statusCode, response);
				try {
					int countAll = response.getJSONObject("_metadata").getInt("count");
					// #2D2F31
					mBadgeView[0].setText(countAll + "");
					mBadgeView[0].show();
					LogUtil.d(TAG, "New:" + countAll);
					count++;
					Message message = new Message();
					message.obj = count;
					mHandler.sendMessage(message);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	private void getConfirmedOrder() {
		// 根据状态加载订单
		RequestParams params = new RequestParams();
		params.put("limit", 1 + "");
		params.put("status", status[1]);
		MyHttpClient.getOrders(HttpUri.ORDERS, access_token, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, org.json.JSONObject response) {
				super.onSuccess(statusCode, response);
				try {
					int countAll = response.getJSONObject("_metadata").getInt("count");
					mBadgeView[1].setText(countAll + "");
					mBadgeView[1].show();
					LogUtil.d(TAG, "Confirmed:" + countAll);
					count++;
					Message message = new Message();
					message.obj = count;
					mHandler.sendMessage(message);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	private void getDeliveringOrder() {
		// 根据状态加载订单
		RequestParams params = new RequestParams();
		params.put("limit", 1 + "");
		params.put("status", status[2]);
		MyHttpClient.getOrders(HttpUri.ORDERS, access_token, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, org.json.JSONObject response) {
				super.onSuccess(statusCode, response);
				try {
					int countAll = response.getJSONObject("_metadata").getInt("count");
					mBadgeView[2].setText(countAll + "");
					mBadgeView[2].show();
					LogUtil.d(TAG, "Delivering:" + countAll);
					count++;
					Message message = new Message();
					message.obj = count;
					mHandler.sendMessage(message);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	private void getCompleteOrder() {
		// 根据状态加载订单
		RequestParams params = new RequestParams();
		params.put("limit", 1 + "");
		params.put("status", status[3]);
		MyHttpClient.getOrders(HttpUri.ORDERS, access_token, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, org.json.JSONObject response) {
				super.onSuccess(statusCode, response);
				try {
					int countAll = response.getJSONObject("_metadata").getInt("count");
					mBadgeView[3].setText(countAll + "");
					mBadgeView[3].show();
					LogUtil.d(TAG, "Complete:" + countAll);
					count++;
					Message message = new Message();
					message.obj = count;
					mHandler.sendMessage(message);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	private void getCancelledOrder() {
		// 根据状态加载订单
		RequestParams params = new RequestParams();
		params.put("limit", 1 + "");
		params.put("status", status[4]);
		MyHttpClient.getOrders(HttpUri.ORDERS, access_token, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, org.json.JSONObject response) {
				super.onSuccess(statusCode, response);
				try {
					int countAll = response.getJSONObject("_metadata").getInt("count");
					mBadgeView[4].setText(countAll + "");
					mBadgeView[4].show();
					LogUtil.d(TAG, "Cancelled:" + countAll);
					count++;
					Message message = new Message();
					message.obj = count;
					mHandler.sendMessage(message);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

	private class MyHomeClickListener implements HomeClickListener {

		@Override
		public void onclick(View v) {
			switch (v.getId()) {
			case R.id.activity_master_main_new:
				LogUtil.d(TAG, "待分配订单 ");
				Intent noAllocation = new Intent(getActivity(), MasterBaseActivity.class);
				noAllocation.putExtra("STATUS", "new");
				startActivity(noAllocation);
				break;
			case R.id.activity_master_main_confirmed:
				LogUtil.d(TAG, " 已分配订单");
				Intent yesAllocation = new Intent(getActivity(), MasterBaseActivity.class);
				yesAllocation.putExtra("STATUS", "confirmed");
				startActivity(yesAllocation);
				break;
			case R.id.activity_master_main_delivery:
				LogUtil.d(TAG, "当日处理订单");
				Intent compeleted = new Intent(getActivity(), MasterBaseActivity.class);
				compeleted.putExtra("STATUS", "delivering");
				startActivity(compeleted);
				break;
			case R.id.activity_master_main_compeleted:
				LogUtil.d(TAG, "以往处理订单");
				Intent previous = new Intent(getActivity(), MasterBaseActivity.class);
				previous.putExtra("STATUS", "complete");
				startActivity(previous);
				break;
			case R.id.activity_master_main_cancle:
				LogUtil.d(TAG, "查看评价");
				Intent evaluate = new Intent(getActivity(), MasterBaseActivity.class);
				evaluate.putExtra("STATUS", "cancelled");
				startActivity(evaluate);
				break;
			default:
				break;

			}

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
