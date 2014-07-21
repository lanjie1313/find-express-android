package com.runye.express.activity.common;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.runye.express.adapter.CouriersAdapter;
import com.runye.express.adapter.OrderInfoAdapter;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.bean.CouriersModeBean;
import com.runye.express.bean.MerchantModeBean;
import com.runye.express.bean.OrderModeBean;
import com.runye.express.bean.OrderProductsBean;
import com.runye.express.http.HttpUri;
import com.runye.express.http.MyHttpClient;
import com.runye.express.listview.ListViewHeight;
import com.runye.express.utils.LoadingDialog;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;

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
	protected static final String TAG = "OrderInfoActivity";
	private OrderModeBean mOrderModeBean;
	/** 快递员集合 */
	private List<CouriersModeBean> mCouriersList;
	/** 订单商品集合 */
	private List<OrderProductsBean> mProductsList;
	/** 快递员 */
	private CouriersAdapter mCouriersAdapter;
	/** 订单信息 */
	private OrderInfoAdapter mOrderInfoAdapter;
	/** 订单商品list */
	private ListView mListView;
	/** 地图 */
	private Button bt_map;
	/** 分配 */
	private Button bt_select;
	/** 选择快递员对话框 */
	private Dialog mDialog;
	/** 底部菜单栏 */
	private LinearLayout bottomLayout;
	/** 店铺地址 */
	private TextView tv_shopAdress;
	/** 店铺名称 */
	private TextView tv_shopName;
	/** 店铺名称 */
	private TextView tv_shopPhone;
	/** 留言地址 */
	private TextView tv_message;
	private TextView tv_time;
	/** 收费信息 */
	private TextView tv_goodsNumber;
	private TextView tv_shipping;
	private TextView tv_subtotal;
	private TextView tv_total;
	private TextView tv_orderNumber;
	/** 买家信息 */
	private TextView tv_buyAddress;
	private TextView tv_buyName;
	private TextView tv_buyPhone;
	private LoadingDialog mLoadingDialog;
	private boolean task1;
	private boolean task2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_infos);
		SysExitUtil.activityList.add(OrderInfoActivity.this);
		initUI();
	}

	private void initUI() {
		mLoadingDialog = new LoadingDialog(OrderInfoActivity.this, "数据获取中");
		mLoadingDialog.show();
		// 获取上个页面装载完成的数据
		mOrderModeBean = (OrderModeBean) getIntent().getSerializableExtra("ORDERINFO");
		bt_map = (Button) findViewById(R.id.activity_order_infos_map);
		bt_map.setOnClickListener(new MyButtonListener());
		/***
		 * 获取商品
		 */
		getOrderInfo();
		/**
		 * 获取商户
		 */
		getMerchantInfo();
		new Thread(new CloseDialog()).start();
		//
		mListView = (ListView) findViewById(R.id.activity_order_infos_listview);
		mOrderInfoAdapter = new OrderInfoAdapter(OrderInfoActivity.this, mProductsList, mOrderModeBean.getItemList());
		mListView.setAdapter(mOrderInfoAdapter);

		// 商家信息
		tv_shopAdress = (TextView) findViewById(R.id.activity_order_infos_shopAdress);
		tv_shopName = (TextView) findViewById(R.id.activity_order_infos_shopName);
		tv_message = (TextView) findViewById(R.id.activity_order_infos_message);
		tv_shopPhone = (TextView) findViewById(R.id.activity_order_infos_shopPhone);
		tv_time = (TextView) findViewById(R.id.activity_order_infos_time);
		tv_time.setText(mOrderModeBean.getCreation_date());
		// 留言信息
		if (mOrderModeBean.getNotes() == null || mOrderModeBean.getNotes().equals("")) {
			tv_message.setText("无");
		} else {

			tv_message.setText(mOrderModeBean.getNotes());
		}
		// 商品信息
		tv_goodsNumber = (TextView) findViewById(R.id.activity_order_infos_goodsNumber);
		tv_shipping = (TextView) findViewById(R.id.activity_order_infos_freight);
		tv_subtotal = (TextView) findViewById(R.id.activity_order_infos_subtotal);
		tv_total = (TextView) findViewById(R.id.activity_order_infos_total);
		tv_orderNumber = (TextView) findViewById(R.id.activity_order_infos_orderNumber);
		tv_goodsNumber.setText(mOrderModeBean.getItemList().size() + "");
		tv_shipping.setText(mOrderModeBean.getShipping());
		tv_subtotal.setText(mOrderModeBean.getSubtotal());
		tv_total.setText(mOrderModeBean.getTotal());
		tv_orderNumber.setText(mOrderModeBean.getNumber());
		// 买家信息
		tv_buyAddress = (TextView) findViewById(R.id.activity_order_infos_buyAddress);
		tv_buyName = (TextView) findViewById(R.id.activity_order_infos_buyerName);
		tv_buyPhone = (TextView) findViewById(R.id.activity_order_infos_buyPhone);
		tv_buyAddress.setText(mOrderModeBean.getRecipient_address());
		tv_buyName.setText(mOrderModeBean.getRecipient_name());
		tv_buyPhone.setText(mOrderModeBean.getRecipient_phone_num());
		tv_buyPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		tv_buyPhone.setOnClickListener(new MyTextViewListener(mOrderModeBean.getRecipient_phone_num()));// 拨打电话
		// 是否显示选择快递员按钮
		String status = mOrderModeBean.getStatus();
		bottomLayout = (LinearLayout) findViewById(R.id.activity_order_infos_bottomLayout);
		if (status.equals("new")) {
			bottomLayout.setVisibility(View.VISIBLE);
			// 此处判断是否为管理员或者站长，true表示可以选择快递员
			bt_select = (Button) findViewById(R.id.activity_order_infos_select);
			bt_select.setOnClickListener(new MyButtonListener());

		}

	}

	/**
	 * 
	 * @Description: 根据订单号查询订单详细信息
	 * @return List<OrderModeBean>
	 */
	private void getOrderInfo() {
		mProductsList = new ArrayList<OrderProductsBean>();
		int size = mOrderModeBean.getItemList().size();
		for (int i = 0; i < size; i++) {
			String id = mOrderModeBean.getItemList().get(i).getProduct();
			LogUtil.d(TAG, "product==id" + id);
			MyHttpClient.getProducts(id, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, JSONObject response) {
					super.onSuccess(statusCode, response);
					LogUtil.d(TAG, "获取商品信息成功" + response.toString());
					mProductsList.add(JSON.parseObject(
							response.toString().replaceAll("\"_id\"", "\"id\"").replaceAll("\"__v\"", "\"v\""),
							OrderProductsBean.class));
					mOrderInfoAdapter.notifyDataSetChanged();
					// 差点忘了计算高度
					ListViewHeight.setListViewHeightBasedOnChildren(mListView);
					task1 = true;
				}

				@Override
				public void onFailure(Throwable e, JSONObject errorResponse) {
					super.onFailure(e, errorResponse);
				}
			});
		}
	}

	// private class MyListViewLisener implements on
	/**
	 * 
	 * @ClassName: closeDialog
	 * @Description: 控制dialog的消失时间
	 */
	private class CloseDialog implements Runnable {
		boolean isRun = true;

		@Override
		public void run() {
			while (isRun) {
				if (task1 == true && task2 == true) {
					mLoadingDialog.dismiss();
					task1 = false;
					task2 = false;
					isRun = false;
				}
			}
		}
	}

	/**
	 * 
	 * @Description: 根据商户id查询商户信息
	 * @return void
	 */
	private void getMerchantInfo() {
		MyHttpClient.getMerchant(mOrderModeBean.getMerchant(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, org.json.JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.d(TAG, "获取商户信息成功" + response.toString());
				MerchantModeBean bean = new MerchantModeBean();
				bean = JSON.parseObject(response.toString(), MerchantModeBean.class);
				tv_shopAdress.setText(bean.getAddress());
				tv_shopPhone.setText(bean.getPhone_num());
				tv_shopPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
				tv_shopPhone.setOnClickListener(new MyTextViewListener(bean.getPhone_num()));
				tv_shopName.setText(bean.getName());
				task2 = true;
			}

			@Override
			public void onFailure(Throwable e, org.json.JSONObject errorResponse) {
				super.onFailure(e, errorResponse);
				LogUtil.d(TAG, "获取商户信息失败" + errorResponse.toString());
				mLoadingDialog.dismiss();
			}
		});
	}

	/**
	 * 
	 * @Description: 联网取站点下的快递员
	 * @return List<String>
	 */
	private void getCouriers() {
		mCouriersList = new ArrayList<CouriersModeBean>();
		mCouriersAdapter = new CouriersAdapter(OrderInfoActivity.this, mCouriersList);
		mLoadingDialog.show();
		RequestParams params = new RequestParams();
		// params.put("siteId", siteBean.getId());
		MyHttpClient.getSiteCouriers(HttpUri.COURIERS, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.d(TAG, "快递员获取成功：" + response.toString());
				// 根据Bean类的到每一个json数组的项
				String replaceAfter = "";
				try {
					replaceAfter = response.getJSONArray("records").toString().replaceAll("\"__v\"", "\"v\"")
							.replaceAll("\"_id\"", "\"id\"");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mCouriersList.addAll(JSON.parseArray(replaceAfter, CouriersModeBean.class));
				mCouriersAdapter.notifyDataSetChanged();
				mLoadingDialog.dismiss();
				mDialog = setDialog();
				mDialog.show();
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				super.onFailure(e, errorResponse);
				LogUtil.d(TAG, "快递员获取失败：" + errorResponse.toString());
				mLoadingDialog.dismiss();
			}
		});
	}

	/**
	 * 
	 * @Description: couriers列表对话框
	 * @return
	 * @return Dialog
	 */
	private Dialog setDialog() {
		Dialog dialog = new Dialog(OrderInfoActivity.this);
		View view = LayoutInflater.from(OrderInfoActivity.this).inflate(R.layout.select_couriers_dialog, null);
		ListView listView = (ListView) view.findViewById(R.id.select_couriers_dialog_listview);
		listView.setAdapter(mCouriersAdapter);
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
		alertDialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mDialog.dismiss();
				// 分配订单
				distribution();
			}
		});
		alertDialog.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.create();
		alertDialog.show();

	}

	/**
	 * 
	 * @Description: 分配订单
	 * @return void
	 */
	private void distribution() {
		// 联网分配
		mLoadingDialog = new LoadingDialog(OrderInfoActivity.this, "订单分配中");
		mLoadingDialog.show();
		bt_select.setText("分配成功");
		bt_select.setClickable(false);
	}

	private class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_order_infos_map:
				// admin or master scan couriers couriers scan user location
				// if (MapApplication.getInstance().isISCOURIERS()) {
				//
				// ToastUtil.showShortToast(OrderInfoActivity.this,
				// "user location");
				// } else {
				// ToastUtil.showShortToast(OrderInfoActivity.this,
				// "couriers location");
				// }
				// 传递用户的经纬度
				Intent intent = new Intent(OrderInfoActivity.this, LocationActivity.class);
				int lng = (int) (mOrderModeBean.getLocation_lng() * 1e6);
				int lat = (int) (mOrderModeBean.getLocation_lat() * 1e6);
				LogUtil.d(TAG, "lng:" + mOrderModeBean.getLocation_lng());
				LogUtil.d(TAG, "lat:" + mOrderModeBean.getLocation_lat());
				LogUtil.d(TAG, "传递到地图的lng:" + lng);
				LogUtil.d(TAG, "传递到地图的lat:" + lat);
				intent.putExtra("USER_LNG", lng);
				intent.putExtra("USER_LAT", lat);
				startActivity(intent);
				break;
			case R.id.activity_order_infos_select:
				getCouriers();
			default:
				break;
			}
		}
	}

	/**
	 * textview的点击监听器
	 */
	private class MyTextViewListener implements OnClickListener {
		String number = "";

		public MyTextViewListener(String number) {
			this.number = number;
		}

		@Override
		public void onClick(View v) {
			Builder alertDialog = new AlertDialog.Builder(OrderInfoActivity.this);
			alertDialog.setMessage("确定拨打？");
			alertDialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 用intent启动拨打电话
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
					startActivity(intent);
				}
			});
			alertDialog.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			alertDialog.create();
			alertDialog.show();

		}
	}

	private class MyListListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			TextView textView = (TextView) view.findViewById(R.id.item_couriers_listview_name);
			String message = textView.getText().toString();
			setConfimDialog(message);
		}
	}
}
