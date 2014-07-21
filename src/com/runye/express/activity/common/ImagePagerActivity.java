package com.runye.express.activity.common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.runye.express.android.R;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: ImagePagerActivity
 * @Description: 显示商品图片的页面
 * @author LanJie.Chen
 * @date 2014-7-21 下午3:58:10
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class ImagePagerActivity extends Activity {

	private static final String STATE_POSITION = "STATE_POSITION";
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
	private ViewPager mPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_pager);

		Bundle bundle = getIntent().getExtras();
		String[] imageUrls = bundle.getStringArray("IMAGEURIS");
		int pagerPosition = bundle.getInt("IMAGEURISPOSITION", 0);

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		mPager = (ViewPager) findViewById(R.id.activity_image_pager);
		mPager.setAdapter(new ImagePagerAdapter(imageUrls));
		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private final String[] images;
		private final LayoutInflater inflater;

		ImagePagerAdapter(String[] images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.item_pager_image);
			final ProgressBar mProgressBar = (ProgressBar) imageLayout.findViewById(R.id.item_pager_loading);
			mImageLoader.displayImage(images[position], imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					mProgressBar.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
					case IO_ERROR:
						message = "Input/Output error";
						break;
					case DECODING_ERROR:
						message = "Image can't be decoded";
						break;
					case NETWORK_DENIED:
						message = "Downloads are denied";
						break;
					case OUT_OF_MEMORY:
						message = "Out Of Memory error";
						break;
					case UNKNOWN:
						message = "Unknown error";
						break;
					}
					mProgressBar.setVisibility(View.GONE);
					ToastUtil.showShortToast(ImagePagerActivity.this, message);

				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					mProgressBar.setVisibility(View.GONE);
				}
			});

			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}