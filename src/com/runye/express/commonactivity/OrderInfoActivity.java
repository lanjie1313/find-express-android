package com.runye.express.commonactivity;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_infos);
		initUI();
	}

	private void initUI() {

		orderList = getOrderInfo();
		mListView = (ListView) findViewById(R.id.activity_order_infos_listview);
		bt_confim = (Button) findViewById(R.id.activity_order_infos_confim);
		bt_map = (Button) findViewById(R.id.activity_order_infos_map);
		boolean isSelect = getIntent().getBooleanExtra("ISSELECT", false);
		bt_select = (Button) findViewById(R.id.activity_order_infos_select);
		if (isSelect) {
			bt_select.setVisibility(View.VISIBLE);
			bt_select.setOnClickListener(new MyButtonListener());
		}
		bt_confim.setOnClickListener(new MyButtonListener());
		bt_map.setOnClickListener(new MyButtonListener());
		OrderInfoAdapter adapter = new OrderInfoAdapter(OrderInfoActivity.this,
				orderList);
		mListView.setAdapter(adapter);

	}

	private List<OrderModeBean> getOrderInfo() {
		List<OrderModeBean> list = new ArrayList<OrderModeBean>();
		for (int i = 0; i < 20; i++) {
			OrderModeBean bean = new OrderModeBean();
			bean.setGoodsName("name" + i);
			bean.setGoodsNumber(i + "");
			bean.setGoodsPrice(i + "");
			list.add(bean);

		}
		return list;
	}

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
				ToastUtil.showShortToast(OrderInfoActivity.this, "sss");
				break;
			case R.id.activity_order_infos_map:
				ToastUtil.showShortToast(OrderInfoActivity.this, "xxxx");
				break;
			case R.id.activity_order_infos_select:
				// startActivity(new Intent(OrderInfoActivity.this,
				// SelectCouriersActivity.class));
				mDialog = setDialog();
				mDialog.show();
			default:
				break;
			}
		}
	}
}
