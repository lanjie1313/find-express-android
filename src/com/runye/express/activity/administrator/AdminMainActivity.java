package com.runye.express.activity.administrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.runye.express.activity.common.MyApplication;
import com.runye.express.adapter.MainSiteAdapter;
import com.runye.express.android.R;
import com.runye.express.listview.HorizontalListView;
import com.runye.express.map.BMapUtil;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: AdministratorMain
 * @Description: 管理员主界面
 * @author LanJie.Chen
 * @date 2014-7-2 上午10:22:14
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AdminMainActivity extends Activity {
	/** listview数据源 */
	private MainSiteAdapter mAdminListViewAdapter;
	private static String[] dataObjects = new String[] { "全站", "A站", "B站", "C站", "D站", "E站", "F站", "G站", "H站" };

	/**
	 * button枚举类 LOCATION 定位 COMPASS 罗盘 FOLLOW 跟随
	 * 
	 * @author LanJie.Chen
	 * 
	 */
	private enum E_BUTTON_TYPE {
		LOC, COMPASS, FOLLOW
	}

	private boolean isLocPop = true;
	/** 枚举类变量 */
	private E_BUTTON_TYPE mCurBtnType;
	/** map注册 */
	private final MyApplication app = null;
	/** mapview */
	private MapView mMapView = null;
	/** 地图控制器 */
	public MapController mMapController = null;
	/** 定位请求按钮 */
	public Button requestLocButton = null;

	private OverlayItem mCurItem = null;

	/** =======我的位置相关ponit========= */
	/** 是否手动触发请求定位 */
	private boolean isRequest = false;
	/** 是否首次定位 */
	private boolean isFirstLoc = true;
	/** 定位服务的客户端。宿主程序在客户端声明此类，并调用 */
	private LocationClient mClient = null;
	/** 用户位置信息 */
	private LocationData mData = null;
	/** 我的位置搜索接口 */
	private MyLocationListenner mLocationListenner = null;
	/** 我的位置弹出框覆盖物 */
	public PopupOverlay mLocPopOverlay = null;
	/** 我的位置实现点击处理 */
	public MyLocOverlay mLocationOverlay = null;
	/** 我的位置弹出框 */
	private View mLocViewCache = null;
	/** 我的位置显示文字 */
	private TextView mLocPopText = null;

	/** =======停车场搜索相关========= */

	/** 停车场搜索引擎 */
	private MKSearch mCarSearch = null;
	/** 停车场搜索接口 */
	private MyCarSearchListener mCarSearchListener = null;
	/** 车场覆盖物 */
	public PopupOverlay mCarPopOverlay = null;
	/** 停车场弹出框 */
	private View mCarViewCache = null;
	/** 停车场弹出框中间 */
	public View mCarPopInfo = null;
	/** 车场弹出框左 */
	public View mCarPopLeft = null;
	/** 车场弹出框右 */
	public View mCarPopRight = null;
	/** 车场弹出框中间文字 */
	public TextView mCarPopText = null;
	/** 停车场集合 */
	private List<HashMap<String, Integer>> carsList = null;
	/** 停车场信息集合 */
	private List<String> pointNames = null;
	/** 停车场point点击效果 */
	public MyCarItemizedOverlay mCarItemizedOverlay;

	/** =============== 反地理搜索接口============ */

	private MKSearch mGeoSearch = null;
	private MyGeoSearchListener mGeoSearchListener = null;
	/** 解析出的我的位置 */
	private String myAdress = null;
	/** 站点管理 */
	private Button bt_list;
	/** 列表模式 */
	private Button bt_site;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_main);
		SysExitUtil.activityList.add(AdminMainActivity.this);
		// /**
		// * 使用地图sdk前需先初始化BMapManager.
		// BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
		// * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
		// */
		// app = (MyApplication) getApplication();
		// if (app.mBMapManager == null) {
		// app.mBMapManager = new BMapManager(getApplicationContext());
		// /**
		// * 如果BMapManager没有初始化则初始化BMapManager
		// */
		// app.mBMapManager.init(new MyApplication.MyGeneralListener());
		// }
		bt_list = (Button) findViewById(R.id.activity_admin_main_list);
		bt_site = (Button) findViewById(R.id.activity_admin_main_site);
		bt_list.setOnClickListener(new MyButtonListener());
		bt_site.setOnClickListener(new MyButtonListener());
		HorizontalListView listview = (HorizontalListView) findViewById(R.id.activity_admin_main_listview);
		mAdminListViewAdapter = new MainSiteAdapter(AdminMainActivity.this, dataObjects);
		listview.setAdapter(mAdminListViewAdapter);
		listview.setOnItemClickListener(new MyListViewListener());

		requestLocButton = (Button) AdminMainActivity.this.findViewById(R.id.activity_admin_main__requestLoc);
		requestLocButton.setOnClickListener(new MyOnClickListener());
		mCurBtnType = E_BUTTON_TYPE.LOC;
		// 百度地图
		mMapView = (MapView) AdminMainActivity.this.findViewById(R.id.activity_admin_main__bmapsView);
		mMapController = mMapView.getController();

		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		// mMapView.setBuiltInZoomControls(true);

		// 我的位置搜索引擎实例化、注册、定位初始化
		mClient = new LocationClient(AdminMainActivity.this);
		mData = new LocationData();
		mLocationListenner = new MyLocationListenner();
		mClient.registerLocationListener(mLocationListenner);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mClient.setLocOption(option);
		mClient.start();
		// 我的位置定位图层初始化
		mLocationOverlay = new MyLocOverlay(mMapView);
		// 设置定位数据
		mLocationOverlay.setData(mData);
		// 添加定位图层
		mMapView.getOverlays().add(mLocationOverlay);
		mLocationOverlay.enableCompass();
		// 修改定位数据后刷新图层生效
		mMapView.refresh();

		/**
		 * 创建一个loc popupoverlay
		 */
		mLocViewCache = getLayoutInflater().inflate(R.layout.pop_loc_view, null);
		mLocPopText = (TextView) mLocViewCache.findViewById(R.id.pop_loc_view_text);
		mLocPopOverlay = new PopupOverlay(mMapView, new MyPopupClickListener());

		// 停车场搜索引擎实例化，注册
		mCarSearch = new MKSearch();
		mCarSearchListener = new MyCarSearchListener(mMapView, this);
		mCarSearch.init(app.mBMapManager, mCarSearchListener);

		// 反地理编码引擎实例化，注册
		mGeoSearch = new MKSearch();
		mGeoSearchListener = new MyGeoSearchListener(mMapView, this);
		mGeoSearch.init(app.mBMapManager, mGeoSearchListener);
	}

	/***
	 * 初始化停车场图层
	 */
	public void initCarOverlay() {
		/**
		 * 准备overlay 数据
		 */
		mCarItemizedOverlay = new MyCarItemizedOverlay(getResources().getDrawable(R.drawable.icon_geo), mMapView);
		GeoPoint[] points = new GeoPoint[carsList.size()];
		OverlayItem[] items = new OverlayItem[carsList.size()];
		for (int i = 0; i < carsList.size(); i++) {
			LogUtil.i("获取到的经度", (carsList.get(i).get("LatitudeE6")) + "");
			LogUtil.i("获取到的纬度", "LongitudeE6" + carsList.get(i).get("LongitudeE6"));

			points[i] = new GeoPoint((carsList.get(i).get("LatitudeE6")), (carsList.get(i).get("LongitudeE6")));
			items[i] = new OverlayItem(points[i], pointNames.get(i), "");
			/**
			 * 设置overlay图标，如不设置，则使用创建ItemizedOverlay时的默认图标.
			 */
			// item.setMarker(null);
			mCarItemizedOverlay.addItem(items[i]);
		}

		LogUtil.i("获取到定位的纬度", "LongitudeE6" + mData.longitude);
		// 将overlay 添加至MapView中
		mMapView.getOverlays().add(mCarItemizedOverlay);
		mMapView.refresh();

		/**
		 * 向地图添加自定义View.
		 */
		mCarViewCache = AdminMainActivity.this.getLayoutInflater().inflate(R.layout.pop_car_view, null);
		mCarPopInfo = mCarViewCache.findViewById(R.id.pop_car_view_info);
		mCarPopLeft = mCarViewCache.findViewById(R.id.pop_car_view_left);
		mCarPopText = (TextView) mCarViewCache.findViewById(R.id.pop_car_view_text);

		/**
		 * 创建一个carpopupoverlay
		 */
		mCarPopOverlay = new PopupOverlay(mMapView, new MyPopupClickListener());

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
				if (isLocPop) {

					ToastUtil.showLongToast(AdminMainActivity.this, "wwwwwwwwww！");
				} else {
					ToastUtil.showLongToast(AdminMainActivity.this, "预约你大爷！");
				}
			} else if (index == 1) {
				ToastUtil.showLongToast(AdminMainActivity.this, "点我是sb！");
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

	/**
	 * 停车场point点击效果，弹出pop
	 * 
	 * @author LanJie.Chen
	 * 
	 */
	private int tapIndex;

	class MyCarItemizedOverlay extends ItemizedOverlay {
		public MyCarItemizedOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index) {
			isLocPop = false;
			mCurItem = getItem(index);
			tapIndex = index;
			mCarPopText.setText(mCurItem.getTitle());
			Bitmap[] bitMaps = { BMapUtil.getBitmapFromView(mCarPopLeft), BMapUtil.getBitmapFromView(mCarPopInfo), };
			mCarPopOverlay.showPopup(bitMaps, mCurItem.getPoint(), 32);
			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			if (mCarPopOverlay != null) {
				mCarPopOverlay.hidePop();
			}
			return false;
		}
	}

	/**
	 * 继承MyLocationOverlay重写dispatchTap实现点击处理
	 * 
	 * @author LanJie.Chen
	 * 
	 */
	public class MyLocOverlay extends MyLocationOverlay {

		public MyLocOverlay(MapView mapView) {
			super(mapView);
		}

		@Override
		protected boolean dispatchTap() {
			isLocPop = true;
			// 处理点击事件,弹出泡泡
			mLocPopText.setBackgroundResource(R.drawable.popup);
			mLocPopText.setText(myAdress);
			mLocPopOverlay.showPopup(BMapUtil.getBitmapFromView(mLocPopText), new GeoPoint(
					(int) (mData.latitude * 1e6), (int) (mData.longitude * 1e6)), 8);
			return true;
		}

	}

	/***
	 * 定位按钮事件监听器
	 * 
	 * @author LanJie.Chen
	 * 
	 */
	public class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (mCurBtnType) {
			case LOC:
				// 手动定位请求
				requestLocClick();
				break;
			case COMPASS:
				mLocationOverlay.setLocationMode(LocationMode.NORMAL);
				requestLocButton.setText("定位");
				mCurBtnType = E_BUTTON_TYPE.LOC;
				break;
			case FOLLOW:
				mLocationOverlay.setLocationMode(LocationMode.COMPASS);
				requestLocButton.setText("罗盘");
				mCurBtnType = E_BUTTON_TYPE.COMPASS;
				break;
			}

		}
	}

	/**
	 * 底部按钮监听器
	 */
	public class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_admin_main_site:
				// 站点管理
				startActivity(new Intent(AdminMainActivity.this, AdminSiteActivity.class));
				break;
			case R.id.activity_admin_main_list:
				// 列表
				startActivity(new Intent(AdminMainActivity.this, AdminListActivity.class));
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 反编码搜索接口
	 * 
	 * @author LanJie.Chen
	 * 
	 */
	public class MyGeoSearchListener implements MKSearchListener {

		public MyGeoSearchListener(MapView mMapView, Context context) {

		}

		@Override
		public void onGetAddrResult(MKAddrInfo res, int error) {
			if (error != 0) {
				String str = String.format("错误号：%d", error);
				Toast.makeText(AdminMainActivity.this, str, Toast.LENGTH_LONG).show();
				return;
			}
			// 地图移动到该点
			mMapView.getController().animateTo(res.geoPt);
			if (res.type == MKAddrInfo.MK_GEOCODE) {
				// 地理编码：通过地址检索坐标点
				String strInfo = String.format("纬度：%f 经度：%f", res.geoPt.getLatitudeE6() / 1e6,
						res.geoPt.getLongitudeE6() / 1e6);
				ToastUtil.showLongToast(AdminMainActivity.this, strInfo);
			}
			if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
				// 反地理编码：通过坐标点检索详细地址及周边poi
				myAdress = res.strAddr;
				ToastUtil.showLongToast(AdminMainActivity.this, myAdress);

			}
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {

		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {

		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {

		}
	}

	/***
	 * 停车场检索服务
	 * 
	 * @author LanJie.Chen
	 * 
	 */
	public class MyCarSearchListener implements MKSearchListener {

		public MyCarSearchListener(MapView mMapView, Context context) {

		}

		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			// 返回地址信息搜索结果
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
			// 返回驾乘路线搜索结果
		}

		@Override
		public void onGetPoiResult(MKPoiResult res, int type, int error) {
			// 错误号可参考MKEvent中的定义
			if (error == MKEvent.ERROR_RESULT_NOT_FOUND) {
				ToastUtil.showLongToast(AdminMainActivity.this, "抱歉，未找到结果");
				return;
			} else if (error != 0 || res == null) {
				ToastUtil.showLongToast(AdminMainActivity.this, "搜索出错啦..");
				return;
			}
			carsList = new ArrayList<HashMap<String, Integer>>();
			pointNames = new ArrayList<String>();
			// 当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
			for (MKPoiInfo info : res.getAllPoi()) {
				if (info.pt != null) {
					HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
					hashMap.put("LatitudeE6", info.pt.getLatitudeE6());
					hashMap.put("LongitudeE6", info.pt.getLongitudeE6());
					pointNames.add(info.address);
					carsList.add(hashMap);
				}
			}

			initCarOverlay();
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
			// 返回公交搜索结果
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
			// 返回步行路线搜索结果
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			// 返回公交车详情信息搜索结果
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
			// 返回联想词信息搜索结果
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type, int error) {
			// 在此处理短串请求返回结果.
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {

		}
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			LogUtil.i("----------", "这是我的MyLocationListenner");
			mData.latitude = location.getLatitude();
			mData.longitude = location.getLongitude();
			// 如果不显示定位精度圈，将accuracy赋值为0即可
			mData.accuracy = location.getRadius();
			// 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
			mData.direction = location.getDerect();
			// 更新定位数据
			mLocationOverlay.setData(mData);
			// 更新图层数据执行刷新后生效
			mMapView.refresh();
			// 是手动触发请求或首次定位时，移动到定位点
			if (isRequest || isFirstLoc) {
				// 移动地图到定位点
				LogUtil.i("----------", "开始移动是地图" + "isRequest=" + isRequest + "isFirstLoc" + isFirstLoc);
				mCarSearch.poiSearchNearBy("停车场", new GeoPoint((int) (mData.latitude * 1e6),
						(int) (mData.longitude * 1e6)), 5000);
				mMapController.animateTo(new GeoPoint((int) (mData.latitude * 1e6), (int) (mData.longitude * 1e6)));
				GeoPoint ptCenter = new GeoPoint((int) (Double.valueOf(mData.latitude) * 1e6),
						(int) (Double.valueOf(mData.longitude) * 1e6));
				// 反Geo搜索
				mGeoSearch.reverseGeocode(ptCenter);
				isRequest = false;
				mLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
				requestLocButton.setText("跟随");
				mCurBtnType = E_BUTTON_TYPE.FOLLOW;
			}
			// 首次定位完成
			isFirstLoc = false;
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	/**
	 * 手动触发一次定位请求
	 */
	public void requestLocClick() {
		isRequest = true;
		LogUtil.i("----------", "手动定位");
		mClient.requestLocation();
		Toast.makeText(AdminMainActivity.this, "正在定位……", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 修改位置图标
	 * 
	 * @param marker
	 */
	public void modifyLocationOverlayIcon(Drawable marker) {
		// 当传入marker为null时，使用默认图标绘制
		mLocationOverlay.setMarker(marker);
		// 修改图层，需要刷新MapView生效
		mMapView.refresh();
	}

	@Override
	public void onDestroy() {
		// 退出时销毁定位
		if (mClient != null)
			mClient.stop();
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Builder alertDialog = new AlertDialog.Builder(AdminMainActivity.this);
			alertDialog.setMessage("确定退出？");
			alertDialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					SysExitUtil.exit();
				}
			});
			alertDialog.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			alertDialog.create();
			alertDialog.show();

		}
		return super.onKeyDown(keyCode, event);
	}

	class MyListViewListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			TextView textView = (TextView) view.findViewById(R.id.item_admin_main_listview_title);
			String s = textView.getText().toString();
			LogUtil.i("====", s);
			ToastUtil.showShortToast(AdminMainActivity.this, s);
			mAdminListViewAdapter.setSelectIndex(position);
			mAdminListViewAdapter.notifyDataSetChanged();
		}
	}
}
