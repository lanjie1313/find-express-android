package com.runye.express.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.runye.express.android.R;
import com.runye.express.bean.CouriersModeBean;

/**
 * 
 * @ClassName: AdminCouriersModeAdapter
 * @Description: 快递员模式
 * @author LanJie.Chen
 * @date 2014-7-4 下午12:18:20
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class CouriersModeAdapter extends BaseAdapter {
	Context mContext;
	private final List<CouriersModeBean> mData;

	public CouriersModeAdapter(Context context, List<CouriersModeBean> data) {
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
					R.layout.item_couriers_mode_listview, null);
			holder.tv_number = (TextView) convertView
					.findViewById(R.id.item_couriers_mode_listview_number);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.item_couriers_mode_listview_name);
			holder.iv_image = (ImageView) convertView
					.findViewById(R.id.item_couriers_mode_listview_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position < 10) {

			holder.tv_number.setText("0" + mData.get(position).getNumber());
		} else {
			holder.tv_number.setText(mData.get(position).getNumber());
		}
		// Bitmap bm=BitmapFactory.decodeFile(pathName)
		// holder.iv_image.setImageBitmap(bm)
		holder.tv_name.setText(mData.get(position).getName());
		return convertView;
	}

	private class ViewHolder {
		/** 编号 */
		TextView tv_number;
		/** 名字 */
		TextView tv_name;
		/** 头像 */
		ImageView iv_image;
	}

}