package com.runye.express.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.runye.express.utils.ToastUtil;

public class FragmentTwo extends Fragment implements OnHeaderRefreshListener, OnFooterRefreshListener {

	protected static final String TAG = "FragmentTwo";
	private View mView;
	/** 刷新listview */
	private PullToRefreshView mPullToRefreshView;
	/** 订单集合 */
	private List<OrderModeBean> mOrderModeList;
	OrderModeAdapter adapter;
	private int skip = 0;
	private final int limit = 10;

	public FragmentTwo() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_two, container, false);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(R.id.pullToRefreshView2);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mOrderModeList = new ArrayList<OrderModeBean>();
		ListView listView = (ListView) mView.findViewById(R.id.list2);
		adapter = new OrderModeAdapter(getActivity(), mOrderModeList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new MyListLisytener());
		getOrders();
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 
	 * @Description: 联网获取待分配订单
	 * @return void
	 */
	private void getOrders() {
		/**
		 * 根据siteid请求order
		 */
		SharedPreferences preferences = getActivity().getSharedPreferences("user_info", 1);
		// String siteId = preferences.getString("siteId", "");
		String access_token = preferences.getString("access_token", "");
		//
		String status = getActivity().getIntent().getStringExtra("STATUS");
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
				mOrderModeList.addAll(JSON.parseArray(replaceAfter, OrderModeBean.class));
				LogUtil.d(TAG, "mList的大小========" + mOrderModeList.size());
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
				ToastUtil.showShortToast(getActivity(), "请求出错了，请重试");
			}
		});
	}

	class MyListLisytener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
			OrderModeBean bean = mOrderModeList.get(position);
			Bundle bundle = new Bundle();
			// bundle.putSerializable("ORDERINFO", bean);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mOrderModeList = new ArrayList<OrderModeBean>();
		adapter = new OrderModeAdapter(getActivity(), mOrderModeList);
		skip = 0;
		LogUtil.d(TAG, "skip:onHeaderRefresh==" + skip);
		getOrders();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		skip += limit;
		LogUtil.d(TAG, "skip:onFooterRefresh==" + skip);
		getOrders();
	}

}
