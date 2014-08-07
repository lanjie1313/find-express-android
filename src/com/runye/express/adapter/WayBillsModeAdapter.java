package com.runye.express.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.runye.express.android.R;
import com.runye.express.bean.WayBills;

/**
 * 
 * @ClassName: WayBillsModeAdapter
 * @Description: 运单适配器
 * @author LanJie.Chen
 * @date 2014-8-6 下午3:44:50
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class WayBillsModeAdapter extends BaseAdapter {
	Context mContext;
	public List<WayBills> mList;

	public WayBillsModeAdapter(Context context, List<WayBills> data) {
		this.mContext = context;
		this.mList = data;
	}

	@Override
	public int getCount() {
		return mList.size();
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
			convertView = View.inflate(mContext, R.layout.item_waybills_listview, null);
			holder.appointment = (TextView) convertView.findViewById(R.id.item_waybills_appointment);
			holder.buyAddress = (TextView) convertView.findViewById(R.id.item_waybills_buyAddress);
			holder.buyName = (TextView) convertView.findViewById(R.id.item_waybills_buyName);
			holder.buyPhone = (TextView) convertView.findViewById(R.id.item_waybills_buyPhone);
			holder.orderNumber = (TextView) convertView.findViewById(R.id.item_waybills_orderNumber);
			holder.orderTime = (TextView) convertView.findViewById(R.id.item_waybills_orderTime);
			holder.serialNumber = (TextView) convertView.findViewById(R.id.item_waybills_serialNumber);
			holder.shopAddress = (TextView) convertView.findViewById(R.id.item_waybills_shopAddress);
			holder.shopName = (TextView) convertView.findViewById(R.id.item_waybills_shopName);
			holder.shopPhone = (TextView) convertView.findViewById(R.id.item_waybills_shopPhone);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.appointment.setText("立即派送");
		holder.buyAddress.setText("立即派送");
		holder.buyName.setText("立即派送");
		holder.buyPhone.setText("立即派送");
		holder.orderNumber.setText("立即派送");
		holder.orderTime.setText("立即派送");
		holder.serialNumber.setText("立即派送");
		holder.shopAddress.setText("立即派送");
		holder.shopName.setText("立即派送");
		holder.shopPhone.setText("立即派送");
		return convertView;
	}

	static class ViewHolder {

		TextView orderNumber;
		TextView orderTime;
		TextView appointment;
		TextView serialNumber;
		TextView shopName;
		TextView shopPhone;
		TextView shopAddress;
		TextView buyName;
		TextView buyPhone;
		TextView buyAddress;
	}

}