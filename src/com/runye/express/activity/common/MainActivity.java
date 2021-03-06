package com.runye.express.activity.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.runye.express.activity.app.MyApplication;
import com.runye.express.android.R;
import com.runye.express.chat.activity.ChatMainActivity;
import com.runye.express.chat.db.UserDao;
import com.runye.express.chat.domain.User;
import com.runye.express.fragment.CouriersFragment;
import com.runye.express.fragment.ManagerFragment;
import com.runye.express.fragment.ManagerFragment.SlidingMenuListOnItemClickListener;
import com.runye.express.fragment.MasterFragment;
import com.runye.express.service.DownLoadService;
import com.runye.express.utils.Constant;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.LoginChatHeaderUtil;
import com.runye.express.utils.ServiceUtil;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;
import com.runye.express.widget.LoadingDialog;

public class MainActivity extends SlidingActivity implements SlidingMenuListOnItemClickListener {

	private static final String TAG = "MainActivity";
	private SlidingMenu mSlidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_content);
		SysExitUtil.activityList.add(MainActivity.this);
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
		if (MyApplication.getInstance().isMASTER()) {
			LogUtil.d(TAG, "初始化站长界面");
			fragmentTransaction.replace(R.id.content_frame, new MasterFragment());
		} else if (MyApplication.getInstance().isCOURIERS()) {
			LogUtil.d(TAG, "初始化快递员界面");
			fragmentTransaction.replace(R.id.content_frame, new CouriersFragment());
		}
		fragmentTransaction.commit();
		// // 使用左上方icon可点，这样在onOptionsItemSelected里面才可以监听到R.id.home
		// getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	LoadingDialog dialog;

	@Override
	public void selectItem(int position, String title) {

		// update the main content by replacing fragments
		// 判断登陆身份
		Fragment fragment = null;
		switch (position) {
		case 0:
			if (MyApplication.getInstance().isMASTER()) {// 站长
				LogUtil.d(TAG, "切换站长界面");
				fragment = new MasterFragment();
			} else if (MyApplication.getInstance().isCOURIERS()) {// 快递员
				LogUtil.d(TAG, "切换快递员界面");
				fragment = new CouriersFragment();
			} else if (MyApplication.getInstance().isADMIN()) {

			}
			break;
		case 1:
			/**
			 * 判断聊天服务器是否加载成功
			 */
			if (MyApplication.getInstance().isLgoinChat()) {
				LogUtil.d(TAG, "chat服务器状态为在线");
				startActivity(new Intent(this, ChatMainActivity.class));
			} else if (MyApplication.getInstance().isLgoinChat() == false) {
				LogUtil.d(TAG, "chat服务器状态为离线");
				loginChatOline();
			}
			break;
		}
		switchContent(fragment);
	}

	private void loginChatOline() {

		dialog = new LoadingDialog(MainActivity.this, "获取聊天内容中");
		dialog.show();
		String username = MyApplication.getInstance().getUserName();
		String password = MyApplication.getInstance().getPassword();
		LogUtil.d(TAG, "username:" + username + "\npassword:" + password);
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login("nx", "q", new EMCallBack() {
			@Override
			public void onSuccess() {
				try {
					// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
					List<String> usernames = EMChatManager.getInstance().getContactUserNames();
					Map<String, User> userlist = new HashMap<String, User>();
					for (String username : usernames) {
						User user = new User();
						user.setUsername(username);
						LoginChatHeaderUtil.setUserHearder(username, user);
						userlist.put(username, user);
					}
					// 添加user"申请与通知"
					User newFriends = new User();
					newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
					newFriends.setNick("申请与通知");
					newFriends.setHeader("");
					userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
					// 添加"群聊"
					User groupUser = new User();
					groupUser.setUsername(Constant.GROUP_USERNAME);
					groupUser.setNick("群聊");
					groupUser.setHeader("");
					userlist.put(Constant.GROUP_USERNAME, groupUser);
					// 存入内存
					MyApplication.getInstance().setContactList(userlist);
					// 存入db
					UserDao dao = new UserDao(MainActivity.this);
					List<User> users = new ArrayList<User>(userlist.values());
					dao.saveContactList(users);
					// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中
					EMGroupManager.getInstance().getGroupsFromServer();
					LogUtil.d(TAG, "chat信息加载成功");
					MyApplication.getInstance().setIsLgoinChat(true);
					dialog.dismiss();
					startActivity(new Intent(MainActivity.this, ChatMainActivity.class));
				} catch (Exception e) {
				}

			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, final String message) {
				dialog.dismiss();
				LogUtil.d(TAG, "chat登陆失败:" + message);
				Thread thread = new Thread(new SampleTask(handler, message));
				thread.start();
			}
		});
	}

	public class SampleTask implements Runnable {
		Handler handler;
		String message;

		public SampleTask(Handler handler, String message) {
			super();
			this.handler = handler;
			this.message = message;
		}

		@Override
		public void run() {
			// 任务完成后通知activity更新UI
			Message msg = new Message();
			msg.what = 1;
			msg.obj = message;
			// message将被添加到主线程的MQ中
			handler.sendMessage(msg);

		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				ToastUtil.showShortToast(MainActivity.this, "获取失败：" + (String) msg.obj);
			}
		};
	};

	public void switchContent(Fragment fragment) {
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			mSlidingMenu.showContent();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setMessage("确定退出？");
			alertDialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (ServiceUtil.isServiceRunning(MainActivity.this, "com.runye.express.service.DownLoadService")) {
						LogUtil.d(TAG, "下载服务还在运行，强制关闭");
						stopService(new Intent(MainActivity.this, DownLoadService.class));
						LogUtil.d(TAG, "关闭成功");
					}
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

}
