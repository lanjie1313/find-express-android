package com.runye.express.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.runye.express.android.R;
import com.runye.express.bean.OrderModeBean;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: AdminOrderListAdapter
 * @Description: 订单详情数据源
 * @author LanJie.Chen
 * @date 2014-7-3 上午9:32:08
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class OrderInfoAdapter extends BaseAdapter {
	Context mContext;
	private final List<OrderModeBean> mData;

	public OrderInfoAdapter(Context context, List<OrderModeBean> data) {
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
			convertView = View.inflate(mContext,
					R.layout.item_order_info_listview, null);
			holder.tv_goodsCharge = (TextView) convertView
					.findViewById(R.id.item_order_info_listview_goodsCharge);
			holder.tv_goodsName = (TextView) convertView
					.findViewById(R.id.item_order_info_listview_goodsName);
			holder.tv_goodsNumber = (TextView) convertView
					.findViewById(R.id.item_order_info_listview_goodsNumber);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_goodsCharge.setText(mData.get(position).getGoodsPrice());
		holder.tv_goodsName.setText(mData.get(position).getGoodsName());
		holder.tv_goodsNumber.setText(mData.get(position).getGoodsNumber());
		return convertView;
	}

	private class ViewHolder {
		/** 商品名称 */
		TextView tv_goodsName;
		/** 商品价格 */
		TextView tv_goodsCharge;
		/** 商品数量 */
		TextView tv_goodsNumber;

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