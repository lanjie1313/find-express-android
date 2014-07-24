package com.runye.express.activity.slidingmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.HanziToPinyin;
import com.runye.express.activity.common.MyApplication;
import com.runye.express.android.R;
import com.runye.express.chat.Constant;
import com.runye.express.chat.activity.ChatMainActivity;
import com.runye.express.chat.db.UserDao;
import com.runye.express.chat.domain.User;
import com.runye.express.utils.LoadingDialog;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;

public class ChatFragment extends Fragment {

	private static final String TAG = "ChatFragment";

	public ChatFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_splash, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SysExitUtil.activityList.add(getActivity());
	}

	String username;
	String password;
	private LoadingDialog mLoadingDialog;

	private void login() {
		username = MyApplication.getInstance().getUserName();
		password = MyApplication.getInstance().getPassword();
		LogUtil.d(TAG, "username" + username + "\npassword" + password);
		mLoadingDialog = new LoadingDialog(getActivity(), "正在获取列表...");
		mLoadingDialog.show();
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login("nb", "q", new EMCallBack() {

			@Override
			public void onSuccess() {
				LogUtil.d(TAG, "登陆成功");
				try {
					// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
					List<String> usernames = EMChatManager.getInstance().getContactUserNames();
					Map<String, User> userlist = new HashMap<String, User>();
					for (String username : usernames) {
						User user = new User();
						user.setUsername(username);
						setUserHearder(username, user);
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
					UserDao dao = new UserDao(getActivity());
					List<User> users = new ArrayList<User>(userlist.values());
					dao.saveContactList(users);

					// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中
					EMGroupManager.getInstance().getGroupsFromServer();
				} catch (Exception e) {
				}

				if (!getActivity().isFinishing())
					mLoadingDialog.dismiss();
				// 进入主页面
				startActivity(new Intent(getActivity(), ChatMainActivity.class));
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, final String message) {
				mLoadingDialog.dismiss();
				LogUtil.d(TAG, "登陆失败");
			}
		});
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
					.toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}
}
