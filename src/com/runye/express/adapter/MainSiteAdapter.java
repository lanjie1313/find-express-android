package com.runye.express.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.runye.express.android.R;

/**
 * 
 * @ClassName: AdminMainSiteAdapter
 * @Description: 管理员首页站点数据源
 * @author LanJie.Chen
 * @date 2014-7-3 上午9:32:29
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class MainSiteAdapter extends BaseAdapter {

	private final Context mContext;
	private final String[] mTitles;
	private final LayoutInflater mInflater;
	private int selectIndex = -1;

	public MainSiteAdapter(Context context, String[] titles) {
		this.mContext = context;
		this.mTitles = titles;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mTitles.length;
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
			convertView = mInflater.inflate(R.layout.item_admin_main_listview,
					null);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.item_admin_main_listview_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_title.setText(mTitles[position]);
		if (position == selectIndex) {
			convertView.setSelected(true);
		} else {
			convertView.setSelected(false);
		}

		return convertView;
	}

	public void setSelectIndex(int i) {
		selectIndex = i;
	}

	private class ViewHolder {
		TextView tv_title;
	}

}
