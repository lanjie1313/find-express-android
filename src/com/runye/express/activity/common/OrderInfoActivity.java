package com.runye.express.activity.common;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.runye.express.adapter.OrderInfoAdapter;
import com.runye.express.adapter.SelectCouriersAdapter;
import com.runye.express.android.R;
import com.runye.express.bean.CouriersModeBean;
import com.runye.express.bean.OrderModeBean;
import com.runye.express.map.MapApplication;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: OrderInfoActivity
 * @Description: 订单详情页面
 * @author LanJie.Chen
 * @date 2014-7-4 下午5:15:23
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class OrderInfoActivity extends Activity {
	private OrderModeBean mOrderModeBean;
	private List<OrderModeBean> orderList;
	private List<CouriersModeBean> couriersList;
	private ListView mListView;
	/** 地图 */
	private Button bt_map;
	/** 确定 */
	private Button bt_confim;
	private Button bt_select;
	/** 选择快递员对话框 */
	Dialog mDialog;
	/** 收货地址 */
	private TextView tv_deliveryAddress;
	/** 店铺地址 */
	private TextView tv_shopAdress;
	/** 店铺名称 */
	private TextView tv_shopName;
	/** 留言地址 */
	private TextView tv_message;
	private TextView tv_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_infos);
		SysExitUtil.activityList.add(OrderInfoActivity.this);
		initUI();
	}

	private void initUI() {
		// 获取上个页面装载完成的数据
		mOrderModeBean = getIntent().getParcelableExtra("ORDERINFO");
		mListView = (ListView) findViewById(R.id.activity_order_infos_listview);
		bt_confim = (Button) findViewById(R.id.activity_order_infos_confim);
		bt_map = (Button) findViewById(R.id.activity_order_infos_map);
		// 此处判断是否为管理员或者站长，true表示可以选择快递员
		bt_select = (Button) findViewById(R.id.activity_order_infos_select);
		if (mOrderModeBean.getStatus().equals("待分配订单")) {
			bt_select.setVisibility(View.VISIBLE);
			bt_select.setOnClickListener(new MyButtonListener());
		}
		// 快递员发货
		if (mOrderModeBean.getStatus().equals("待配送订单")) {
			bt_confim.setText("发货");
		}
		bt_confim.setOnClickListener(new MyButtonListener());
		bt_map.setOnClickListener(new MyButtonListener());
		orderList = getOrderInfo();
		OrderInfoAdapter adapter = new OrderInfoAdapter(OrderInfoActivity.this,
				orderList);
		mListView.setAdapter(adapter);

		tv_deliveryAddress = (TextView) findViewById(R.id.activity_order_infos_deliveryAdress);
		tv_shopAdress = (TextView) findViewById(R.id.activity_order_infos_shopAdress);
		tv_shopName = (TextView) findViewById(R.id.activity_order_infos_shopName);
		tv_message = (TextView) findViewById(R.id.activity_order_infos_message);
		tv_time = (TextView) findViewById(R.id.activity_order_infos_time);
		tv_time.setText(mOrderModeBean.getCreation_date());
		tv_shopAdress.setText(mOrderModeBean.getMerchant());
		tv_shopName.setText(mOrderModeBean.getStatus());
		tv_message.setText(mOrderModeBean.getNotes());
	}

	/**
	 * 
	 * @Description: 根据订单号查询订单详细信息
	 * @return List<OrderModeBean>
	 */
	private List<OrderModeBean> getOrderInfo() {
		List<OrderModeBean> list = new ArrayList<OrderModeBean>();
		for (int i = 0; i < 3; i++) {
			// OrderModeBean bean = new OrderModeBean();
			// bean.setCreation_date("2014年7月10日12:59:25");
			// bean.setNumber("" + i);
			// bean.setDiscount(discount)("" + i);
			// list.add(bean);
		}
		return list;
	}

	/**
	 * 
	 * @Description: 获取快递员信息
	 * @return
	 * @return List<CouriersModeBean>
	 */
	private List<CouriersModeBean> getCouriersInfo() {
		List<CouriersModeBean> list = new ArrayList<CouriersModeBean>();
		for (int i = 0; i < 20; i++) {
			CouriersModeBean bean = new CouriersModeBean();
			bean.setNumber(i + "");
			bean.setName("快递员" + i);
			bean.setPhone("1823402720" + i);
			bean.setStatus("空闲");
			list.add(bean);
		}
		return list;
	}

	/**
	 * 
	 * @Description: couriers列表对话框
	 * @return
	 * @return Dialog
	 */
	private Dialog setDialog() {
		couriersList = getCouriersInfo();
		Dialog dialog = new Dialog(OrderInfoActivity.this);
		View view = LayoutInflater.from(OrderInfoActivity.this).inflate(
				R.layout.select_couriers_dialog, null);
		ListView listView = (ListView) view
				.findViewById(R.id.select_couriers_dialog_listview);
		SelectCouriersAdapter adapter = new SelectCouriersAdapter(
				OrderInfoActivity.this, couriersList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new MyListListener());
		dialog.setContentView(view);
		dialog.setTitle("选择快递员");
		return dialog;
	}

	/**
	 * 
	 * @Description: 选择确认订单
	 * @param message
	 * @return void
	 */
	private void setConfimDialog(final String message) {
		Builder alertDialog = new AlertDialog.Builder(OrderInfoActivity.this);
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDialog.dismiss();
						bt_select.setText(message);
					}
				});
		alertDialog.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		alertDialog.create();
		alertDialog.show();

	}

	private class MyListListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView textView = (TextView) view
					.findViewById(R.id.item_select_couriers_dialog_name);
			String message = textView.getText().toString();
			setConfimDialog(message);
		}
	}

	private class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_order_infos_confim:
				if (MapApplication.getInstance().isISCOURIERS()) {

					ToastUtil.showShortToast(OrderInfoActivity.this,
							"i am couriers，i send");
				} else {

					ToastUtil.showShortToast(OrderInfoActivity.this,
							"i am master or admin,i manage");
				}
				break;
			case R.id.activity_order_infos_map:
				// admin or master scan couriers couriers scan user location
				if (MapApplication.getInstance().isISCOURIERS()) {

					ToastUtil.showShortToast(OrderInfoActivity.this,
							"user location");
				} else {
					ToastUtil.showShortToast(OrderInfoActivity.this,
							"couriers location");
				}
				break;
			case R.id.activity_order_infos_select:
				mDialog = setDialog();
				mDialog.show();
			default:
				break;
			}
		}
	}
}
