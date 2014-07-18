package com.runye.express.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.runye.express.android.R;
import com.runye.express.bean.CouriersModeBean;

/**
 * 
 * @ClassName: AdminCouriersAdapter
 * @Description: 快递员数据源
 * @author LanJie.Chen
 * @date 2014-7-3 上午9:32:08
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class CouriersAdapter extends BaseAdapter {
	Context mContext;
	private final List<CouriersModeBean> mData;

	public CouriersAdapter(Context context, List<CouriersModeBean> data) {
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
			convertView = View.inflate(mContext, R.layout.item_couriers_listview, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.item_couriers_listview_name);
			holder.tv_number = (TextView) convertView.findViewById(R.id.item_couriers_listview_number);
			holder.tv_phone = (TextView) convertView.findViewById(R.id.item_couriers_listview_phone);
			holder.tv_status = (TextView) convertView.findViewById(R.id.item_couriers_listview_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(mData.get(position).getNickName());
		holder.tv_number.setText(1 + position + "");
		holder.tv_phone.setText(mData.get(position).getPhone_num());
		holder.tv_status.setText(mData.get(position).getStatus());
		return convertView;
	}

	private class ViewHolder {
		/** 编号 */
		TextView tv_number;
		/** 标题 */
		TextView tv_name;
		/** 标题 */
		TextView tv_phone;
		/** 标题 */
		TextView tv_status;
	}

}