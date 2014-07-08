package com.runye.express.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FragmentTwo extends Fragment implements OnHeaderRefreshListener,
		OnFooterRefreshListener {

	private View mView;
	/** 刷新listview */
	private PullToRefreshView mPullToRefreshView;
	/** 订单集合 */
	private List<OrderModeBean> mOrderModeList;
	OrderModeAdapter adapter;

	public FragmentTwo() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_two, container, false);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(
				R.id.pullToRefreshView2);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mOrderModeList = getWaitingOrder();
		ListView listView = (ListView) mView.findViewById(R.id.list2);
		OrderModeAdapter adapter = new OrderModeAdapter(getActivity(),
				mOrderModeList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new MyListLisytener());
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 
	 * @Description: 联网获取待分配订单
	 * @return void
	 */
	private List<OrderModeBean> getWaitingOrder() {
		List<OrderModeBean> list = new ArrayList<OrderModeBean>();
		for (int i = 0; i < 10; i++) {
			OrderModeBean bean = new OrderModeBean();
			bean.setNumber(i + "");
			bean.setTime("2014年7月4日14:32:51");
			bean.setShopName("美特好");
			bean.setAddress("华顿实业8层");
			bean.setRating(3 + "");
			bean.setCharge("" + i);
			bean.setCouriersName("sss");
			bean.setCouriersNumber(i + "");
			bean.setStatus("待分配");
			list.add(bean);
		}
		return list;
	}

	class MyListLisytener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
			OrderModeBean bean = mOrderModeList.get(position);
			Bundle bundle = new Bundle();
			bundle.putParcelable("ORDERINFO", bean);
			intent.putExtras(bundle);
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
