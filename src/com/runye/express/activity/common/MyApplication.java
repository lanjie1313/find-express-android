package com.runye.express.activity.common;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnNotificationClickListener;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.runye.express.chat.activity.ChatActivity;
import com.runye.express.chat.activity.ChatMainActivity;
import com.runye.express.chat.db.DbOpenHelper;
import com.runye.express.chat.db.UserDao;
import com.runye.express.chat.domain.User;
import com.runye.express.service.CheckVersionService;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.PreferenceUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @ClassName: MyApplication
 * @Description: 全局保存信息
 * @author LanJie.Chen
 * @date 2014-7-23 上午10:43:06
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class MyApplication extends Application {
	private static final String TAG = "MyApplication";
	/** 地图相关 */
	public BMapManager mBMapManager = null;
	/** 地图apikey */
	public boolean m_bKeyRight = true;
	private static MyApplication mInstance = null;
	public static Context applicationContext;
	private Map<String, User> contactList;
	private boolean isLgoinChat = false;
	/** 本地安装版本 */
	public int localVersion = 0;
	/** 服务器版本 */
	public int serverVersion = 1;

	/** 安装目录 */
	public String downloadDir = "FindExpress/";
	/** SD卡缓存路径 */
	public static String mSdcardDataDir;
	/** 是否管理员 */
	private boolean ADMIN = false;
	/** 是否站长 */
	private boolean MASTER = false;
	/** 是否快递员 */
	private boolean COURIERS = false;
	/** 身份 */
	private String identity;
	/** 用户名 */
	private String username;
	/** 密码 */
	private String password;
	/** 用户id */
	private String id;
	/** 站点id */
	private String siteId;
	/** 昵称 */
	private String nickName;
	/** 电话 */
	private String phone_num;
	/** 邮箱 */
	private String email;
	/** 状态 */
	private String status;
	/** 审核 */
	private String verifyStatus;
	/** 登陆token */
	private String access_token;
	/** token类型 */
	private String token_type;
	/** 记住了信息 */
	private boolean isRember;
	Intent service;

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		mInstance = new MyApplication();
		// 百度地图引擎
		initEngineManager(this);
		// 图片缓存
		initImageLoader(this);
		// 版本获取
		getVersion(this);
		// 初始化chatSDK
		initEMChat();
		// 加载缓存路径
		initEnvironment();
		service = new Intent(this, CheckVersionService.class);
		startService(service);

	}

	public static MyApplication getInstance() {
		return mInstance;
	}

	/**
	 * 
	 * @Description: 初始化环信SDK
	 * @return void
	 */
	private void initEMChat() {
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果使用到百度地图相关，这个if判断不能少
		if (processAppName == null || processAppName.equals("")) {
			return;
		}
		// 初始化环信SDK,一定要先调用init()
		EMChat.getInstance().init(applicationContext);
		// debugmode设为true后，就能看到sdk打印的log了
		EMChat.getInstance().setDebugMode(true);

		// 获取到EMChatOptions对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		// 设置收到消息是否有新消息通知，默认为true
		options.setNotificationEnable(PreferenceUtils.getInstance(applicationContext).getSettingMsgNotification());
		// 设置收到消息是否有声音提示，默认为true
		options.setNoticeBySound(PreferenceUtils.getInstance(applicationContext).getSettingMsgSound());
		// 设置收到消息是否震动 默认为true
		options.setNoticedByVibrate(PreferenceUtils.getInstance(applicationContext).getSettingMsgVibrate());
		// 设置语音消息播放是否设置为扬声器播放 默认为true
		options.setUseSpeaker(PreferenceUtils.getInstance(applicationContext).getSettingMsgSpeaker());

		// 设置notification消息点击时，跳转的intent为自定义的intent
		options.setOnNotificationClickListener(new OnNotificationClickListener() {

			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(applicationContext, ChatActivity.class);
				ChatType chatType = message.getChatType();
				if (chatType == ChatType.Chat) { // 单聊信息
					intent.putExtra("userId", message.getFrom());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				} else { // 群聊信息
							// message.getTo()为群聊id
					intent.putExtra("groupId", message.getTo());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				}
				return intent;
			}
		});
		// 设置一个connectionlistener监听账户重复登陆
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
		// options.setNotifyText(new OnMessageNotifyListener() {
		//
		// @Override
		// public String onNewMessageNotify(EMMessage message) {
		// //可以根据message的类型提示不同文字，demo简单的覆盖了原来的提示
		// return "你的好基友" + message.getFrom() + "发来了一条消息哦";
		// }
		//
		// @Override
		// public String onLatestMessageNotify(EMMessage message, int
		// fromUsersNum, int messageNum) {
		// return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
		// }
		// });

		MobclickAgent.onError(applicationContext);
		LogUtil.d(TAG, "聊天服务器初始化成功");
	}

	/**
	 * 
	 * @Description: 获取appName
	 * @param pID
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		// PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					// CharSequence c =
					// pm.getApplicationLabel(pm.getApplicationInfo(info.processName,
					// PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}

	/**
	 * 
	 * @Description: version获取
	 * @return void
	 */
	private void getVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
			localVersion = packageInfo.versionCode;
			LogUtil.d(TAG, "本地版本获取成功" + localVersion);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description: 获取当前缓存路径
	 * @return void
	 */
	private void initEnvironment() {
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory().getPath() + "/find-express/cache/");
			if (!file.exists()) {
				if (file.mkdirs()) {
					mSdcardDataDir = file.getAbsolutePath();
				}
			} else {
				mSdcardDataDir = file.getAbsolutePath();
			}
		}

	}

	/**
	 * 
	 * @Description: 地图引擎加载
	 * @param context
	 * @return void
	 */
	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
			mBMapManager.init(new MyGeneralListener());
			LogUtil.d(TAG, "地图引擎加载成功");
		}

		if (!mBMapManager.init(new MyGeneralListener())) {
			Toast.makeText(MyApplication.getInstance().getApplicationContext(), "BMapManager  初始化错误!",
					Toast.LENGTH_LONG).show();
			LogUtil.d(TAG, "地图引擎加载失败");
		}
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(MyApplication.getInstance().getApplicationContext(), "您的网络出错啦！", Toast.LENGTH_LONG)
						.show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(MyApplication.getInstance().getApplicationContext(), "输入正确的检索条件！", Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onGetPermissionState(int arg0) {

		}

	}

	/**
	 * 
	 * @Description: 配置imageloader
	 * @param context
	 * @return void
	 */
	private void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).diskCache(new UnlimitedDiscCache(cacheDir))
				.denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		LogUtil.d(TAG, "配置imageloader成功");
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout() {
		// 先调用sdk logout，在清理app中自己的数据
		if (MyApplication.getInstance().isLgoinChat()) {

			EMChatManager.getInstance().logout();
			DbOpenHelper.getInstance(applicationContext).closeDB();
			MyApplication.getInstance().setIsLgoinChat(false);
			setContactList(null);
		}
		stopService(service);
		MyApplication.getInstance().setRember(false);
		// 清空xml数据
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		LogUtil.d(TAG, "，配置文件已删除，chat服务器已退出，将不再接受消息");

	}

	/**
	 * 
	 * @ClassName: MyConnectionListener
	 * @Description: chat服务器连接监听
	 */
	class MyConnectionListener implements ConnectionListener {
		@Override
		public void onReConnecting() {
		}

		@Override
		public void onReConnected() {
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				Intent intent = new Intent(applicationContext, ChatMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("conflict", true);
				startActivity(intent);
			}

		}

		@Override
		public void onConnecting(String progress) {

		}

		@Override
		public void onConnected() {
		}
	}

	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		if (getUserName() != null && contactList == null) {
			UserDao dao = new UserDao(applicationContext);
			// 获取本地好友user list到内存,方便以后获取好友list
			contactList = dao.getContactList();
		}
		return contactList;
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		this.contactList = contactList;
	}

	/**
	 * 
	 * @Description: 获取身份
	 * @return String
	 */
	public String getIdentity() {
		if (identity == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			identity = preferences.getString("identity", null);
		}
		return identity;
	}

	/**
	 * 
	 * @Description: 设置身份
	 * @return String
	 */
	public void setIdentity(String identity) {
		if (identity != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString("identity", identity).commit()) {
				LogUtil.d(TAG, "身份类型：" + identity);
				this.identity = identity;
			}
		}
	}

	public boolean isADMIN() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		ADMIN = preferences.getBoolean("ADMIN", false);
		return ADMIN;
	}

	public boolean isMASTER() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		MASTER = preferences.getBoolean("MASTER", false);
		return MASTER;
	}

	public boolean isCOURIERS() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		COURIERS = preferences.getBoolean("COURIERS", false);
		return COURIERS;
	}

	public void setADMIN(boolean ADMIN) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putBoolean("ADMIN", ADMIN).commit()) {
			LogUtil.d(TAG, "ADMIN：" + ADMIN);
			this.ADMIN = ADMIN;
		}
	}

	public void setMASTER(boolean MASTER) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putBoolean("MASTER", MASTER).commit()) {
			LogUtil.d(TAG, "MASTER：" + MASTER);
			this.MASTER = MASTER;
		}

	}

	public void setCOURIERS(boolean COURIERS) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putBoolean("COURIERS", COURIERS).commit()) {
			LogUtil.d(TAG, "COURIERS：" + COURIERS);
			this.COURIERS = COURIERS;
		}
	}

	/**
	 * 
	 * @Description: 获取用户名
	 * @return String
	 */
	public String getUserName() {
		if (username == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			username = preferences.getString("username", null);
		}
		return username;
	}

	/**
	 * 
	 * @Description: 设置用户名
	 * @return String
	 */
	public void setUserName(String username) {
		if (username != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString("username", username).commit()) {
				LogUtil.d(TAG, "用户名：" + username);
				this.username = username;
			}
		}
	}

	/**
	 * 
	 * @Description: 获取密码
	 * @return String
	 */
	public String getPassword() {
		if (password == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			password = preferences.getString("password", null);
		}
		return password;
	}

	/**
	 * 
	 * @Description: 设置面
	 * @return String
	 */
	public void setPassword(String password) {
		if (password != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString("password", password).commit()) {
				LogUtil.d(TAG, "密码：" + password);
				this.password = password;
			}
		}
	}

	/**
	 * 
	 * @Description: 获取id
	 * @return String
	 */
	public String getId() {
		if (id == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			id = preferences.getString("id", null);
		}
		return id;
	}

	/**
	 * 
	 * @Description: 设置id
	 * @return String
	 */
	public void setId(String id) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString("id", id).commit()) {
			LogUtil.d(TAG, "ID:" + id);
			this.id = id;
		}
	}

	/**
	 * 
	 * @Description: 获取站点
	 * @return String
	 */
	public String getSiteId() {
		if (siteId == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			siteId = preferences.getString("siteId", null);
		}
		return siteId;
	}

	/**
	 * 
	 * @Description: 设置站点
	 * @return String
	 */
	public void setSiteId(String siteId) {
		if (siteId != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString("siteId", siteId).commit()) {
				LogUtil.d(TAG, "站点id：" + siteId);
				this.siteId = siteId;
			}
		}
	}

	/**
	 * 
	 * @Description: 获取昵称
	 * @return String
	 */

	public String getNickName() {
		if (nickName == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			nickName = preferences.getString("nickName", null);
		}
		return nickName;
	}

	/**
	 * 
	 * @Description: 设置昵称
	 * @return String
	 */
	public void setNickName(String nickName) {
		if (nickName != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString("nickName", nickName).commit()) {
				LogUtil.d(TAG, "昵称：" + nickName);
				this.nickName = nickName;
			}
		}
	}

	/**
	 * 
	 * @Description: 获取电话
	 * @return String
	 */
	public String getPhone_num() {
		if (phone_num == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			phone_num = preferences.getString("phone_num", null);
		}
		return phone_num;
	}

	/**
	 * 
	 * @Description: 设置电话
	 * @return String
	 */
	public void setPhone_num(String phone_num) {
		if (phone_num != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString("phone_num", phone_num).commit()) {
				LogUtil.d(TAG, "电话：" + phone_num);
				this.phone_num = phone_num;
			}
		}
	}

	/**
	 * 
	 * @Description: 获取邮箱
	 * @return String
	 */
	public String getEmail() {
		if (email == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			email = preferences.getString("email", null);
		}
		return email;
	}

	/**
	 * 
	 * @Description: 设置邮箱
	 * @return String
	 */
	public void setemail(String email) {
		if (email != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString("email", email).commit()) {
				LogUtil.d(TAG, "邮箱：" + email);
				this.email = email;
			}
		}
	}

	/**
	 * 
	 * @Description: 获取状态
	 * @return String
	 */
	public String getStatus() {
		if (status == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			status = preferences.getString("status", null);
		}
		return status;
	}

	/**
	 * 
	 * @Description: 设置状态
	 * @return String
	 */
	public void setStatus(String status) {
		if (status != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString("status", status).commit()) {
				LogUtil.d(TAG, "状态：" + status);
				this.status = status;
			}
		}
	}

	/**
	 * 
	 * @Description: 获取审核状态
	 * @return String
	 */
	public String getVerifyStatus() {
		if (verifyStatus == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			verifyStatus = preferences.getString("verifyStatus", null);
		}
		return verifyStatus;
	}

	/**
	 * 
	 * @Description: 设置审核状态
	 * @return String
	 */
	public void setVerifyStatus(String verifyStatus) {
		if (verifyStatus != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString("verifyStatus", verifyStatus).commit()) {
				LogUtil.d(TAG, "审核状态：" + verifyStatus);
				this.verifyStatus = verifyStatus;
			}
		}
	}

	/**
	 * 
	 * @Description: 获取token
	 * @return String
	 */

	public String getAccess_token() {
		if (access_token == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			access_token = preferences.getString("access_token", null);
		}
		return access_token;
	}

	/**
	 * 
	 * @Description: 设置token
	 * @return String
	 */
	public void setAccess_token(String access_token) {
		if (access_token != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString("access_token", access_token).commit()) {
				LogUtil.d(TAG, "token：" + access_token);
				this.access_token = access_token;
			}
		}
	}

	/**
	 * 
	 * @Description: 获取token类型
	 * @return String
	 */
	public String getToken_type() {
		if (token_type == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			token_type = preferences.getString("token_type", null);
		}
		return token_type;
	}

	/**
	 * 
	 * @Description: 设置token类型
	 * @return String
	 */
	public void setToken_type(String token_type) {
		if (token_type != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString("token_type", token_type).commit()) {
				LogUtil.d(TAG, "token类型：" + token_type);
				this.token_type = token_type;
			}
		}
	}

	public boolean isRember() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		isRember = preferences.getBoolean("isRember", false);
		return isRember;
	}

	public void setRember(boolean isRember) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putBoolean("isRember", isRember).commit()) {
			LogUtil.d(TAG, "是否记住信息：" + isRember);
			this.isRember = isRember;
		}
	}

	/**
	 * 
	 * @Description: 是否成功登陆chat服务器
	 * @return
	 * @return boolean
	 */
	public boolean isLgoinChat() {
		return isLgoinChat;
	}

	/**
	 * 
	 * @Description: 设置是否成功登陆了chat服务器
	 * @param isLgoinChat
	 * @return void
	 */
	public void setIsLgoinChat(boolean isLgoinChat) {
		this.isLgoinChat = isLgoinChat;
	}

}
