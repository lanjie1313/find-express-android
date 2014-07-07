package com.runye.express.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.runye.express.android.R;
import com.runye.express.utils.ToastUtil;

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
	private final List<String> mData;

	public CouriersAdapter(Context context, List<String> data) {
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
					R.layout.item_couriers_listview, null);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.item_couriers_listview_title);
			holder.bt_change = (Button) convertView
					.findViewById(R.id.item_couriers_listview_change);
			holder.bt_del = (Button) convertView
					.findViewById(R.id.item_couriers_listview_del);
			holder.tv_number = (TextView) convertView
					.findViewById(R.id.item_couriers_listview_number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.bt_change.setOnClickListener(new MyButtonListener(position));
		holder.bt_del.setOnClickListener(new MyButtonListener(position));
		holder.tv_title.setText(mData.get(position));
		if (position < 10) {

			holder.tv_number.setText("0" + position);
		} else {
			holder.tv_number.setText(position);
		}
		return convertView;
	}

	private class ViewHolder {
		/** 编号 */
		TextView tv_number;
		/** 标题 */
		TextView tv_title;
		/** 修改 */
		Button bt_change;
		/** 删除 */
		Button bt_del;
	}

	class MyButtonListener implements OnClickListener {
		private final int mPosition;

		private MyButtonListener(int position) {
			this.mPosition = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.item_couriers_listview_change:
				ToastUtil.showShortToast(mContext, mData.get(mPosition));
				break;
			case R.id.item_couriers_listview_del:

				break;

			default:
				break;
			}
		}
	}
}