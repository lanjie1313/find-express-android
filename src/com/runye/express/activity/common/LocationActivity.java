package com.runye.express.activity.common;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.runye.express.android.R;
import com.runye.express.map.BMapUtil;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: LocationActivity
 * @Description: 地图显示类
 * @author LanJie.Chen
 * @date 2014-7-18 下午3:15:49
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class LocationActivity extends ComminBaseActivity {
	/** 用户lng */
	private int mLng;
	/** 用户lat */
	private int mLat;
	/** 构造的点 */
	private GeoPoint mPoint;
	/** map注册 */
	private MyApplication mApplication = null;
	/** mapview */
	private MapView mMapView = null;
	/** 地图控制器 */
	public MapController mMapController = null;
	/** 覆盖物 */
	public PopupOverlay mPopOverlay = null;
	/** 弹出框 */
	private View mViewCache = null;
	/** 弹出框中间 */
	public View mPopInfo = null;
	/** 弹出框左 */
	public View mPopLeft = null;
	/** 弹出框右 */
	public View mPopRight = null;
	/** 弹出框中间文字 */
	public TextView mPopText = null;

	/** 我的位置弹出框 */
	private final View mLocViewCache = null;
	/** 我的位置显示文字 */
	private final TextView mLocPopText = null;

	/** 我的位置弹出框覆盖物 */
	// public PopupOverlay mLocPopOverlay = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		SysExitUtil.activityList.add(LocationActivity.this);
		InitUI();
	}

	private void InitUI() {
		/**
		 * 使用地图sdk前需先初始化BMapManager. BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
		 * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
		 */
		mApplication = (MyApplication) getApplication();
		if (mApplication.mBMapManager == null) {
			mApplication.mBMapManager = new BMapManager(getApplicationContext());
			/**
			 * 如果BMapManager没有初始化则初始化BMapManager
			 */
			mApplication.mBMapManager.init(new MyApplication.MyGeneralListener());
		}
		mLng = getIntent().getIntExtra("USER_LNG", 0);
		mLat = getIntent().getIntExtra("USER_LAT", 0);
		mPoint = new GeoPoint(mLat, mLng);
		mMapView = (MapView) findViewById(R.id.activity_location_mapView);
		mMapController = mMapView.getController();
		mMapController.setZoom(14);
		mMapController.enableClick(true);
		mMapController.animateTo(mPoint);

		//
		// /**
		// * 创建一个loc popupoverlay
		// */
		// mLocViewCache = getLayoutInflater().inflate(R.layout.pop_loc_view,
		// null);
		// mLocPopText = (TextView)
		// mLocViewCache.findViewById(R.id.pop_loc_view_text);
		// mLocPopOverlay = new PopupOverlay(mMapView, new
		// MyPopupClickListener());
		initCarOverlay();

	}

	/** 停车场point点击效果 */
	public MyItemizedOverlay mItemizedOverlay;

	/***
	 * 初始化停车场图层
	 */
	public void initCarOverlay() {
		/**
		 * 准备overlay 数据
		 */
		mItemizedOverlay = new MyItemizedOverlay(getResources().getDrawable(R.drawable.icon_geo), mMapView);
		OverlayItem item = new OverlayItem(mPoint, "", null);
		/**
		 * 设置overlay图标，如不设置，则使用创建ItemizedOverlay时的默认图标.
		 */
		// item.setMarker(null);
		mItemizedOverlay.addItem(item);
		// 将overlay 添加至MapView中
		mMapView.getOverlays().add(mItemizedOverlay);
		mMapView.refresh();

		/**
		 * 向地图添加自定义View.
		 */
		mViewCache = LocationActivity.this.getLayoutInflater().inflate(R.layout.pop_car_view, null);
		mPopInfo = mViewCache.findViewById(R.id.pop_car_view_info);
		mPopLeft = mViewCache.findViewById(R.id.pop_car_view_left);
		mPopText = (TextView) mViewCache.findViewById(R.id.pop_car_view_text);

		/**
		 * 创建一个carpopupoverlay
		 */
		mPopOverlay = new PopupOverlay(mMapView, new MyPopupClickListener());

	}

	/***
	 * 停车场pop点击事件
	 * 
	 * @author LanJie.Chen
	 * 
	 */

	class MyPopupClickListener implements PopupClickListener {

		@Override
		public void onClickedPopup(int index) {
			if (index == 0) {

				ToastUtil.showLongToast(LocationActivity.this, "预约你大爷！");
			} else if (index == 1) {
				ToastUtil.showLongToast(LocationActivity.this, "点我是sb！");
			} else if (index == 2) {
				// // 去这里
				// Intent intent = new Intent(AdministratorMain.this,
				// Navigation.class);
				// intent.putExtra("START_ADR", myAdress);
				// intent.putExtra("START_LAT", (int) (mData.latitude * 1e6));
				// intent.putExtra("START_LON", (int) (mData.longitude * 1e6));
				// intent.putExtra("STOP_ADR", mCurItem.getTitle());
				// intent.putExtra("STOP_LAT",
				// carsList.get(tapIndex).get("LatitudeE6"));
				// intent.putExtra("STOP_LON",
				// carsList.get(tapIndex).get("LongitudeE6"));
				// startActivity(intent);
			}
		}
	}

	private OverlayItem mCurItem = null;

	@SuppressWarnings("rawtypes")
	class MyItemizedOverlay extends ItemizedOverlay {
		public MyItemizedOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index) {
			mCurItem = getItem(index);
			mPopText.setText(mCurItem.getTitle());
			Bitmap[] bitMaps = { BMapUtil.getBitmapFromView(mPopLeft), BMapUtil.getBitmapFromView(mPopInfo), };
			mPopOverlay.showPopup(bitMaps, mCurItem.getPoint(), 32);
			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			if (mPopOverlay != null) {
				mPopOverlay.hidePop();
			}
			return false;
		}
	}

}
