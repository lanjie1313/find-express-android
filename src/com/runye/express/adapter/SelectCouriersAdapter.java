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
 * @ClassName: SelectCouriersAdapter
 * @Description: 选择快递员
 * @author LanJie.Chen
 * @date 2014-7-4 下午12:18:20
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class SelectCouriersAdapter extends BaseAdapter {
	Context mContext;
	private final List<CouriersModeBean> mData;

	public SelectCouriersAdapter(Context context, List<CouriersModeBean> data) {
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
					R.layout.item_select_couriers_dialog_listview, null);
			holder.tv_number = (TextView) convertView
					.findViewById(R.id.item_select_couriers_dialog_number);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.item_select_couriers_dialog_name);
			holder.tv_phone = (TextView) convertView
					.findViewById(R.id.item_select_couriers_dialog_phone);
			holder.tv_status = (TextView) convertView
					.findViewById(R.id.item_select_couriers_dialog_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position < 10) {

			holder.tv_number.setText("0" + mData.get(position).getNumber());
		} else {
			holder.tv_number.setText(mData.get(position).getNumber());
		}
		holder.tv_name.setText(mData.get(position).getName());
		holder.tv_phone.setText(mData.get(position).getPhone());
		holder.tv_status.setText(mData.get(position).getStatus());
		return convertView;
	}

	private class ViewHolder {
		/** 编号 */
		TextView tv_number;
		/** 名字 */
		TextView tv_name;
		TextView tv_phone;
		TextView tv_status;
	}

}