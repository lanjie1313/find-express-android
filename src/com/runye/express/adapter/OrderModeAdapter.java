package com.runye.express.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.runye.express.android.R;
import com.runye.express.bean.OrderModeBean;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: AdminOrderListAdapter
 * @Description: 订单数据源
 * @author LanJie.Chen
 * @date 2014-7-3 上午9:32:08
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class OrderModeAdapter extends BaseAdapter {
	Context mContext;
	private final List<OrderModeBean> mData;

	public OrderModeAdapter(Context context, List<OrderModeBean> data) {
		this.mContext = context;
		this.mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_order_listview, null);
			holder.tv_couriersName = (TextView) convertView.findViewById(R.id.item_order_listview_couriresName);
			holder.tv_couriersNumber = (TextView) convertView.findViewById(R.id.item_order_listview_couriresNumber);
			holder.tv_orderCharge = (TextView) convertView.findViewById(R.id.item_order_listview_orderCharge);
			holder.tv_orderNumber = (TextView) convertView.findViewById(R.id.item_order_listview_orderNumber);
			holder.tv_orderShop = (TextView) convertView.findViewById(R.id.item_order_listview_orderShop);
			holder.tv_orderTime = (TextView) convertView.findViewById(R.id.item_order_listview_orderTime);
			holder.tv_deliveryAddress = (TextView) convertView.findViewById(R.id.item_order_listview_deliveryAddress);
			holder.bt_change = (Button) convertView.findViewById(R.id.item_order_listview_status);
			holder.rb_rating = (RatingBar) convertView.findViewById(R.id.item_order_listview_rating);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_orderNumber.setText(mData.get(position).getNumber());
		holder.tv_orderTime.setText(mData.get(position).getCreation_date());
		holder.tv_orderCharge.setText("￥" + mData.get(position).getTotal());
		holder.tv_deliveryAddress.setText(mData.get(position).getRecipient_address());
		// holder.tv_couriersName.setText(mData.get(position).getCouriersName());
		holder.tv_couriersNumber.setText(position + 1 + "");
		// holder.tv_orderCharge.setText(mData.get(position).getCharge());
		// holder.tv_orderShop.setText(mData.get(position).getShopName());
		// holder.tv_orderTime.setText(mData.get(position).getTime());
		holder.bt_change.setOnClickListener(new MyButtonListener());
		// holder.rb_rating.setRating(Float.valueOf(mData.get(position)
		// .getRating()));

		holder.bt_change.setText(mData.get(position).getStatus());
		String status = mData.get(position).getStatus();
		if (status.equals("new")) {
			holder.bt_change.setBackgroundResource(R.drawable.waiting);

		} else if (status.equals("已分配")) {
			holder.bt_change.setBackgroundResource(R.drawable.done);

		} else if (status.equals("ALL")) {
			holder.bt_change.setBackgroundResource(R.drawable.sending);

		} else if (status.equals("已发货")) {
			holder.bt_change.setBackgroundResource(R.drawable.cancle);

		}
		return convertView;
	}

	private class ViewHolder {
		/** 订单编号 */
		TextView tv_orderNumber;
		/** 订单时间 */
		TextView tv_orderTime;
		/** 商户 */
		TextView tv_orderShop;
		/** 收货地址 */
		TextView tv_deliveryAddress;
		/** 快递员编号 */
		TextView tv_couriersNumber;
		/** 快递员名称 */
		TextView tv_couriersName;
		/** 订单金额 */
		TextView tv_orderCharge;
		/** 评分 */
		RatingBar rb_rating;
		/** button */
		Button bt_change;

	}

	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.item_order_listview_status:
				// 具体的我操作
				ToastUtil.showShortToast(mContext, "呵呵");
				break;

			default:
				break;
			}
		}
	}
}