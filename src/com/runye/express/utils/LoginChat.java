package com.runye.express.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.HanziToPinyin;
import com.runye.express.activity.common.MyApplication;
import com.runye.express.chat.Constant;
import com.runye.express.chat.db.UserDao;
import com.runye.express.chat.domain.User;

public class LoginChat {
	/**
	 * 
	 * @Description: 登陆chat聊天服务器
	 * @param TAG
	 * @param context
	 * @return void
	 */
	public static void loginChat(final String TAG, final Context context) {
		String username = MyApplication.getInstance().getUserName();
		String password = MyApplication.getInstance().getPassword();
		LogUtil.d(TAG, "username" + username + "\npassword" + password);
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login("nx", "q", new EMCallBack() {

			@Override
			public void onSuccess() {
				LogUtil.d(TAG, "chat登陆成功");
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
					UserDao dao = new UserDao(context);
					List<User> users = new ArrayList<User>(userlist.values());
					dao.saveContactList(users);
					// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中
					EMGroupManager.getInstance().getGroupsFromServer();
					LogUtil.d(TAG, "chat信息加载成功");
					MyApplication.getInstance().setIsLgoinChat(true);
				} catch (Exception e) {
				}

			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, final String message) {
				LogUtil.d(TAG, "chat登陆失败");
			}
		});
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	private static void setUserHearder(String username, User user) {
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
