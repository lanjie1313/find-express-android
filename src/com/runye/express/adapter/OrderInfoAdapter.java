package com.runye.express.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.runye.express.activity.common.ImagePagerActivity;
import com.runye.express.android.R;
import com.runye.express.bean.OrderItemsBean;
import com.runye.express.bean.OrderProductsBean;
import com.runye.express.http.HttpUri;
import com.runye.express.utils.LogUtil;
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
	private static final String TAG = "OrderInfoAdapter";
	private final Context mContext;
	private final List<OrderProductsBean> mData;
	private final List<OrderItemsBean> itemsData;
	private final DisplayImageOptions mOptions;
	private final ImageLoadingListener mImageLoadingListener = new AnimateFirstDisplayListener();
	private String[] imageUris;

	public OrderInfoAdapter(Context context, List<OrderProductsBean> data, List<OrderItemsBean> itemsData) {
		this.mContext = context;
		this.mData = data;
		this.itemsData = itemsData;
		mOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_stub)// 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_empty)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_error)// 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 缓存到内存
				.cacheOnDisk(true)// 是否緩存到sd卡上
				.considerExifParams(true).displayer(new RoundedBitmapDisplayer(20)).build();

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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_order_info_listview, null);
			holder.tv_goodsCharge = (TextView) convertView.findViewById(R.id.item_order_info_listview_goodsCharge);
			holder.tv_goodsName = (TextView) convertView.findViewById(R.id.item_order_info_listview_goodsName);
			holder.tv_goodsNumber = (TextView) convertView.findViewById(R.id.item_order_info_listview_goodsNumber);
			holder.iv_goodsImage = (ImageView) convertView.findViewById(R.id.item_order_info_listview_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_goodsCharge.setText(itemsData.get(position).getUnit_price());
		holder.tv_goodsName.setText(mData.get(position).getName());
		holder.tv_goodsNumber.setText(itemsData.get(position).getCount());
		ImageLoader imageLoader = ImageLoader.getInstance();
		// 目前只有一个图片，所以只需要加载getImagesID().get(0)
		imageLoader.displayImage(HttpUri.UPLOADS + mData.get(position).getImagesID().get(0), holder.iv_goodsImage,
				mOptions, mImageLoadingListener);
		holder.iv_goodsImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageUris = new String[mData.size()];
				LogUtil.d(TAG, mData.size() + "个图片");
				for (int i = 0; i < mData.size(); i++) {
					imageUris[i] = HttpUri.UPLOADS + mData.get(i).getImagesID().get(0);
					LogUtil.d(TAG, "所有图片地址：" + imageUris[i]);
				}
				Intent intent = new Intent(mContext, ImagePagerActivity.class);
				intent.putExtra("IMAGEURIS", imageUris);
				intent.putExtra("IMAGEURISPOSITION", position);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	private class ViewHolder {
		/** 商品名称 */
		TextView tv_goodsName;
		/** 商品价格 */
		TextView tv_goodsCharge;
		/** 商品数量 */
		TextView tv_goodsNumber;
		/** 商品图片 */
		ImageView iv_goodsImage;

	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
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