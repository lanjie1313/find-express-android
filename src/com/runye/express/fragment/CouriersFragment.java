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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.runye.express.activity.common.MyApplication;
import com.runye.express.activity.couriers.CouriersBaseActivity;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.http.HttpUri;
import com.runye.express.http.MyHttpClient;
import com.runye.express.utils.BadgeView;
import com.runye.express.utils.HomeButton;
import com.runye.express.utils.HomeButton.HomeClickListener;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.NetState;
import com.runye.express.utils.SysExitUtil;

/**
 * 
 * @ClassName: OrderFragment
 * @Description: 订单选择fragment
 * @author LanJie.Chen
 * @date 2014-7-25 下午3:49:30
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class CouriersFragment extends Fragment {
	/**
	 * 0 、new待分配订单 1、confirmed 已分配订单 2、delivering当日处理订单 3、 awaiting_pickup以往处理订单
	 * 4、complete查看评价5、cancelled
	 * */
	private HomeButton[] buttons;
	private int[] ids;
	private final String TAG = "CouriersFragment";
	private BadgeView[] mBadgeView;
	/**
	 * 请求完成标识
	 */
	private int count = 0;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			int nowCompleted = (Integer) msg.obj;
			if (nowCompleted == 4) {
				setLoadingState(false);
				count = 0;
			}
		};
	};

	public CouriersFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_couriers_main, container, false);
		return rootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
		SysExitUtil.activityList.add(getActivity());

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 刷新首页数据
		setLoadingState(true);
		getNewOrder();
		// getCancelledOrder();
		getConfirmedOrder();
		getCompleteOrder();
		getDeliveringOrder();

		NetState receiver = new NetState();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		getActivity().registerReceiver(receiver, filter);
	}

	private void initUI() {
		ids = new int[] { R.id.activity_couriers_main_new, R.id.activity_couriers_main_completed,
				R.id.activity_couriers_main_history, R.id.activity_couriers_main_evaluation };
		buttons = new HomeButton[ids.length];
		mBadgeView = new BadgeView[ids.length];
		for (int i = 0; i < ids.length; i++) {
			buttons[i] = (HomeButton) getActivity().findViewById(ids[i]);
			buttons[i].setOnHomeClick(new MyHomeClickListener());
			mBadgeView[i] = new BadgeView(getActivity(), buttons[i]);
		}
		HomeButton button = (HomeButton) getActivity().findViewById(R.id.activity_couriers_main_more);
		BadgeView badgeView = new BadgeView(getActivity(), button);
	}

	MenuItem mProgressMenu;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_main, menu);
		mProgressMenu = menu.findItem(R.id.actionbar_loading);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionbar_loading:
			// 刷新首页数据
			setLoadingState(true);
			for (int i = 0; i < mBadgeView.length; i++) {
				mBadgeView[i].hide();
				mBadgeView[i].setText("");
			}
			getNewOrder();
			// getCancelledOrder();
			getConfirmedOrder();
			getCompleteOrder();
			getDeliveringOrder();
			break;

		default:
			break;
		}
		return false;
	};

	/**
	 * 
	 * @Description: 设置actionbar-progress的转动状态
	 * @param refreshing
	 * @return void
	 */
	public void setLoadingState(boolean refreshing) {
		if (mProgressMenu != null) {
			if (refreshing) {
				mProgressMenu.setActionView(R.layout.progressbar);
				mProgressMenu.setVisible(true);
			} else {
				// mProgressMenu.setVisible(false);
				mProgressMenu.setActionView(null);
			}
		}
	}

	// #2D2F31
	String[] status = new String[] { "new", "delivering", "complete", "cancelled" };
	String access_token = MyApplication.getInstance().getAccess_token();

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

	// private void getCancelledOrder() {
	// // 根据状态加载订单
	// RequestParams params = new RequestParams();
	// params.put("limit", 1 + "");
	// params.put("status", status[4]);
	// MyHttpClient.getOrders(HttpUri.ORDERS, access_token, params, new
	// JsonHttpResponseHandler() {
	// @Override
	// public void onSuccess(int statusCode, org.json.JSONObject response) {
	// super.onSuccess(statusCode, response);
	// try {
	// int countAll = response.getJSONObject("_metadata").getInt("count");
	// mBadgeView[4].setText(countAll + "");
	// mBadgeView[4].show();
	// LogUtil.d(TAG, "Cancelled:" + countAll);
	// count++;
	// Message message = new Message();
	// message.obj = count;
	// mHandler.sendMessage(message);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// });
	//
	// }

	private class MyHomeClickListener implements HomeClickListener {

		@Override
		public void onclick(View v) {
			switch (v.getId()) {
			case R.id.activity_couriers_main_new:
				LogUtil.d(TAG, "待分配订单 ");
				Intent noAllocation = new Intent(getActivity(), CouriersBaseActivity.class);
				noAllocation.putExtra("STATUS", "new");
				startActivity(noAllocation);
				break;
			case R.id.activity_couriers_main_completed:
				LogUtil.d(TAG, " 已分配订单");
				Intent yesAllocation = new Intent(getActivity(), CouriersBaseActivity.class);
				yesAllocation.putExtra("STATUS", "confirmed");
				startActivity(yesAllocation);
				break;
			case R.id.activity_couriers_main_history:
				LogUtil.d(TAG, "当日处理订单");
				Intent compeleted = new Intent(getActivity(), CouriersBaseActivity.class);
				compeleted.putExtra("STATUS", "delivering");
				startActivity(compeleted);
				break;
			case R.id.activity_couriers_main_evaluation:
				LogUtil.d(TAG, "以往处理订单");
				Intent previous = new Intent(getActivity(), CouriersBaseActivity.class);
				previous.putExtra("STATUS", "complete");
				startActivity(previous);
				break;
			case R.id.activity_couriers_main_more:
				LogUtil.d(TAG, "查看评价");
				Intent evaluate = new Intent(getActivity(), CouriersBaseActivity.class);
				evaluate.putExtra("STATUS", "cancelled");
				startActivity(evaluate);
				break;
			default:
				break;

			}

		}
	}
}
