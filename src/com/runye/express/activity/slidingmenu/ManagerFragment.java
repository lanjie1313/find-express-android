package com.runye.express.activity.slidingmenu;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.runye.express.activity.common.LoginActivity;
import com.runye.express.adapter.SlidingMenuAdapter;
import com.runye.express.android.R;
import com.runye.express.bean.SlidingMenuItemsBean;
import com.runye.express.utils.SysExitUtil;

/**
 * fragment管理器
 * 
 * @author LanJie.Chen
 * 
 */
public class ManagerFragment extends Fragment implements OnItemClickListener, OnClickListener {

	protected static final String TAG = "ManagerFragment";
	/** sliding menu listview */
	private ListView mSlidingMenuList;
	/** sliding menu 标题 */
	private String[] mSlidingMenuTitles;
	/** sliding menu 图片 */
	private TypedArray mSlidingMenuImages;
	/** sliding menu items 数组 */
	private ArrayList<SlidingMenuItemsBean> mSlidingMenuItems;
	/** sliding menu 适配器 */
	private SlidingMenuAdapter mAdapter;
	/** sliding menu 回调接口 */
	private SlidingMenuListOnItemClickListener mCallback;
	/** sliding menu 点击计数器 */
	private int selected = -1;
	/** 根视图加载器 */
	private View view;
	private String count;
	/** 二维码扫描 */
	private TextView textView_fragmentTwoCode;
	private ImageView imageView_headView;
	private String userName;
	/** 显示用户的昵称 */
	private TextView textView_nickName;
	/** 下载的头像 */
	private Bitmap mBitmap;
	String imagePath;

	@Override
	public void onAttach(Activity activity) {
		System.out.println("onAttach---------------------");
		try {
			// 绑定activity
			mCallback = (SlidingMenuListOnItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "必须实现SlidingMenuListOnItemClickListener");
		}
		super.onAttach(activity);
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			textView_nickName.setText((String) msg.obj);
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		count = 50 + "";

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 加载
		view = inflater.inflate(R.layout.fragment_manager, null);
		initView(view);
		return view;
	}

	/**
	 * 创建菜单
	 * 
	 * @param view
	 *            根视图
	 */
	private void initView(View view) {

		SharedPreferences userinfo = getActivity().getSharedPreferences("USERINFO", 0);
		// 取出保存的NAME，取出改字段名的值，不存在则创建默认为空
		userName = userinfo.getString("NAME", ""); // 取出保存的 NAME
		imagePath = userinfo.getString("IMAGEPATH", "");
		textView_nickName = (TextView) view.findViewById(R.id.fragment_manager_nickName);
		textView_fragmentTwoCode = (TextView) view.findViewById(R.id.fragment_manager_twodimensionalcode);
		textView_fragmentTwoCode.setOnClickListener(this);

		imageView_headView = (ImageView) view.findViewById(R.id.fragment_manager_headImageView);
		imageView_headView.setOnClickListener(this);

		mSlidingMenuList = (ListView) view.findViewById(R.id.fragment_menu_listview);
		mSlidingMenuItems = new ArrayList<SlidingMenuItemsBean>();
		mSlidingMenuTitles = getResources().getStringArray(R.array.slidingmenu_titles);
		mSlidingMenuImages = getResources().obtainTypedArray(R.array.slidingmenu_images);

		/**
		 * getResourceId(int index,int defvalue) index 数组索引值，dervalue
		 * 没有定义的资源返回的值
		 */
		mSlidingMenuItems.add(new SlidingMenuItemsBean(mSlidingMenuTitles[0], mSlidingMenuImages.getResourceId(0, -1)));
		mSlidingMenuItems.add(new SlidingMenuItemsBean(mSlidingMenuTitles[1], mSlidingMenuImages.getResourceId(1, -1),
				true, count));
		mSlidingMenuItems.add(new SlidingMenuItemsBean(mSlidingMenuTitles[2], mSlidingMenuImages.getResourceId(2, -1)));
		mSlidingMenuItems.add(new SlidingMenuItemsBean(mSlidingMenuTitles[3], mSlidingMenuImages.getResourceId(3, -1),
				true, "22"));

		// slidingmenu适配器
		mAdapter = new SlidingMenuAdapter(getActivity(), mSlidingMenuItems);
		mSlidingMenuList.setAdapter(mAdapter);
		mSlidingMenuList.setOnItemClickListener(this);

		if (selected != -1) {
			mSlidingMenuList.setItemChecked(selected, true);
			mSlidingMenuList.setSelection(selected);
		} else {
			mSlidingMenuList.setItemChecked(0, true);
			mSlidingMenuList.setSelection(0);
		}
	}

	/*
	 * 切换到不同的功能内容
	 */
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		MainActivity ra = (MainActivity) getActivity();
		ra.switchContent(fragment);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		// update selected item and title, then close the drawer
		mSlidingMenuList.setItemChecked(position, true);
		mSlidingMenuList.setSelection(position);

		if (mCallback != null) {
			mCallback.selectItem(position, mSlidingMenuTitles[position]);
		}
		selected = position;
	}

	/**
	 * sliding menu 回调接口
	 * 
	 * @author LanJie.Chen
	 * 
	 */
	public interface SlidingMenuListOnItemClickListener {

		/**
		 * 选择的items
		 * 
		 * @param position
		 *            位置
		 * @param title
		 *            标题
		 */
		public void selectItem(int position, String title);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_manager_twodimensionalcode:
			startActivity(new Intent(getActivity(), LoginActivity.class));
			SysExitUtil.exit();
			break;

		case R.id.fragment_manager_headImageView:
			break;
		}
	}

}
