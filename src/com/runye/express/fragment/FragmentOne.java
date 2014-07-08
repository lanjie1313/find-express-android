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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.runye.express.activity.common.OrderInfoActivity;
import com.runye.express.adapter.OrderModeAdapter;
import com.runye.express.android.R;
import com.runye.express.bean.OrderModeBean;
import com.runye.express.listview.PullToRefreshView;
import com.runye.express.listview.PullToRefreshView.OnFooterRefreshListener;
import com.runye.express.listview.PullToRefreshView.OnHeaderRefreshListener;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.ToastUtil;

public class FragmentOne extends Fragment implements OnHeaderRefreshListener,
		OnFooterRefreshListener {
	private View mView;
	/** 刷新listview */
	private PullToRefreshView mPullToRefreshView;
	private Spinner spinner;
	private final String TAG = "FragmentOne";
	/** 订单集合 */
	private List<OrderModeBean> mOrderModeList;
	OrderModeAdapter adapter;

	public FragmentOne() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_one, container, false);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(
				R.id.pullToRefreshView1);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		spinner = (Spinner) getActivity().findViewById(
				R.id.activity_admin_list_spinner);
		spinner.setOnItemSelectedListener(new MySpinnerListener());
		// 获取数据1111

		mOrderModeList = getAllOrder("");
		ListView listView = (ListView) mView.findViewById(R.id.list);
		adapter = new OrderModeAdapter(getActivity(), mOrderModeList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new MyListItemListener());

		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 
	 * @Description: 联网获取全部订单
	 * @return void
	 */
	private List<OrderModeBean> getAllOrder(String status) {
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
			bean.setStatus("待分配订单");
			list.add(bean);
		}
		return list;
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

	class MyListItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView textView = (TextView) view
					.findViewById(R.id.item_order_listview_orderNumber);
			String orderNumber = textView.getText().toString();
			ToastUtil.showShortToast(getActivity(), "位置" + position + "/n"
					+ "编号：" + orderNumber);
			startActivity(new Intent(getActivity(), OrderInfoActivity.class));
		}
	}

	class MySpinnerListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			String siteName = parent.getItemAtPosition(position).toString();
			LogUtil.i(TAG, siteName);
			// 切换站点，数据重新拉取
			// OrderModeBean bean = new OrderModeBean();
			// bean.setNumber(11 + "");
			// bean.setTime("2014年7月4日14:32:51");
			// bean.setShopName("美特好");
			// bean.setAddress("华顿实业8层");
			// bean.setRating(3 + "");
			// bean.setCharge("" + 11);
			// bean.setCouriersName("sss");
			// bean.setCouriersNumber(11 + "");
			// mOrderModeList.add(bean);
			// adapter.notifyDataSetChanged();

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

}
