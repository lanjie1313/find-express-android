package com.runye.express.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.runye.express.android.R;
import com.runye.express.bean.SiteBean;

/**
 * 
 * @ClassName: AdminListSiteAdapter
 * @Description: 列表模式站点数据源
 * @author LanJie.Chen
 * @date 2014-7-3 上午9:32:08
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class SiteAdapter extends BaseAdapter {
	Context mContext;
	private final List<SiteBean> mData;

	public SiteAdapter(Context context, List<SiteBean> data) {
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
					R.layout.item_admin_site_listview, null);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.item_admin_site_listview_siteName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_title.setText(mData.get(position).getSiteName());
		return convertView;
	}

	private class ViewHolder {
		TextView tv_title;
	}
}