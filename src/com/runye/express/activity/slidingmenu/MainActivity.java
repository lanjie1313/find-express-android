package com.runye.express.activity.slidingmenu;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.runye.express.activity.slidingmenu.ManagerFragment.SlidingMenuListOnItemClickListener;
import com.runye.express.android.R;
import com.runye.express.utils.SysExitUtil;

public class MainActivity extends SlidingActivity implements SlidingMenuListOnItemClickListener {
	// public class MainActivity extends SlidingFragmentActivity {

	private SlidingMenu mSlidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("主页");
		setContentView(R.layout.frame_content);
		SysExitUtil.activityList.add(this);
		// 1、 set the Behind View
		setBehindContentView(R.layout.frame_menu);
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		// mSlidingMenu.setMenu(R.layout.frame_menu); //设置左侧菜单的布局文件
		// mSlidingMenu.setSecondaryMenu(R.layout.frame_menu); 设置右侧菜单的布局文件

		// mSlidingMenu.setShadowWidth(5);
		// mSlidingMenu.setBehindOffset(100);
		mSlidingMenu.setShadowDrawable(R.drawable.drawer_shadow);// 设置阴影图片
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width); // 设置阴影图片的宽度
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset); // SlidingMenu划出时主页面显示的剩余宽度
		mSlidingMenu.setFadeDegree(0.35f);
		// 设置SlidingMenu 的手势模式
		// TOUCHMODE_FULLSCREEN 全屏模式，在整个content页面中，滑动，可以打开SlidingMenu
		// TOUCHMODE_MARGIN
		// 边缘模式，在content页面中，如果想打开SlidingMenu,你需要在屏幕边缘滑动才可以打开SlidingMenu
		// TOUCHMODE_NONE 不能通过手势打开SlidingMenu
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		// 设置 SlidingMenu 内容
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		ManagerFragment menuFragment = new ManagerFragment();
		fragmentTransaction.replace(R.id.menu, menuFragment);
		fragmentTransaction.replace(R.id.content_frame, new FindFragment());
		fragmentTransaction.commit();

		// 使用左上方icon可点，这样在onOptionsItemSelected里面才可以监听到R.id.home
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Builder alertDialog = new AlertDialog.Builder(this);
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
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			toggle();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle(); // 动态判断自动关闭或开启SlidingMenu
			// getSlidingMenu().showMenu();//显示SlidingMenu
			// getSlidingMenu().showContent();//显示内容
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void selectItem(int position, String title) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new FindFragment();
			break;
		default:
			break;
		}
		switchContent(fragment);
		setTitle(title);
	}

	public void switchContent(Fragment fragment) {
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			mSlidingMenu.showContent();
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

}
