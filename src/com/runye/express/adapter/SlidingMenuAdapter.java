package com.runye.express.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.runye.express.android.R;
import com.runye.express.bean.SlidingMenuItemsBean;

/**
 * sliding menu 自定义适配器
 * 
 * @author LanJie.Chen
 * 
 */
public class SlidingMenuAdapter extends BaseAdapter {
	/** 上下文 */
	private final Context context;
	/** sliding menu 集合 */
	private final List<SlidingMenuItemsBean> mItemsList;

	public SlidingMenuAdapter(Context context, List<SlidingMenuItemsBean> mItemsList) {
		this.context = context;
		this.mItemsList = mItemsList;
	}

	@Override
	public int getCount() {
		return mItemsList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemsList.get(position);
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
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.item_slidingmenu_list, null);
			holder.textView_title = (TextView) convertView.findViewById(R.id.title);
			holder.textView_count = (TextView) convertView.findViewById(R.id.counter);
			holder.imageView_image = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageView_image.setImageResource(mItemsList.get(position).getIcon());
		holder.textView_title.setText(mItemsList.get(position).getTitle());
		// 检查是否显示标识
		if (mItemsList.get(position).getCounterVisibility()) {
			holder.textView_count.setText(mItemsList.get(position).getCount());
		} else {
			// 隐藏tips
			holder.textView_count.setVisibility(View.GONE);
		}

		return convertView;
	}

	private final class ViewHolder {
		/** sliding menu 标题 */
		private TextView textView_title;
		/** sliding menu tips数量 */
		private TextView textView_count;
		/** sliding menu 图片 */
		private ImageView imageView_image;
	}
}
