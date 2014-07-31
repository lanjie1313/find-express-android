package com.runye.express.activity.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.runye.express.activity.administrator.AdminSiteActivity;
import com.runye.express.activity.slidingmenu.MainActivity;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.http.HttpUri;
import com.runye.express.http.MyHttpClient;
import com.runye.express.service.UpdateService;
import com.runye.express.utils.LoadingDialog;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.NetWork;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: Login
 * @Description: 登陆
 * @author LanJie.Chen
 * @date 2014-7-1 下午3:06:44
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class LoginActivity extends Activity {
	private final String TAG = "LoginActivity";
	/** 登陆按钮 */
	private Button bt_login;
	/** 注册按钮 */
	private Button bt_register;
	/** 用户名 */
	private EditText et_userName;
	/** 密码 */
	private EditText et_passWord;
	/** 身份选择 */
	private TextView tv_identity;
	/** 保存信息 */
	private CheckBox cb_remberInfo;
	/**
	 * 登陆身份选择 admin sitemanager postman
	 * */
	private String identity;
	/** 用户名 */
	private String userName;
	/** 密码 */
	private String passWord;
	/** 获取到的token */
	private String access_token;
	/** 获取到的token类型 */
	private String token_type;
	/** 加载框 */
	private LoadingDialog mLoadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SysExitUtil.activityList.add(LoginActivity.this);
		initUI();
		// checkVersion();

	}

	private void initUI() {
		// 如果用户名密码都有，直接进入主页面
		if (MyApplication.getInstance().isRember()) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		bt_login = (Button) findViewById(R.id.activity_login);
		bt_register = (Button) findViewById(R.id.activity_login_register);
		et_userName = (EditText) findViewById(R.id.activity_login_userName);
		et_passWord = (EditText) findViewById(R.id.activity_login_passWord);
		cb_remberInfo = (CheckBox) findViewById(R.id.activity_login_remberPwd);
		cb_remberInfo.setOnCheckedChangeListener(new MyCheckChanged());
		tv_identity = (TextView) findViewById(R.id.activity_login_identity);
		tv_identity.setOnClickListener(new MyOnClickListener());
		bt_login.setOnClickListener(new MyOnClickListener());
		bt_register.setOnClickListener(new MyOnClickListener());
		boolean isRegister = getIntent().getBooleanExtra("ISREGISTER", false);
		// 不是注册页面
		if (isRegister == false) {
			tv_identity.setText(getResources().getStringArray(R.array.login_identity)[0]);

		}
		// 注册页面跳转
		if (isRegister) {
			et_userName.setText(getIntent().getStringExtra("username"));
			et_passWord.setText("");
			// 默认是管理员
			tv_identity.setText(getResources().getStringArray(R.array.login_identity)[0]);
			cb_remberInfo.setChecked(false);
		}

		// 如果用户名改变，清空密码
		et_userName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				et_passWord.setText("");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	/**
	 * 
	 * @Description: 检测新版本
	 * @return void
	 */
	Intent updateIntent;

	private void checkVersion() {
		if (NetWork.isNetworkConnected(LoginActivity.this)) {
			LogUtil.d(TAG, "开始检测更新\n");
			if (MyApplication.getInstance().localVersion < MyApplication.getInstance().serverVersion) {
				LogUtil.d(TAG, "有新版本，开始更新\n" + "本地version:" + MyApplication.getInstance().localVersion
						+ "\n服务器version:" + MyApplication.getInstance().serverVersion);
				// 发现新版本，提示用户更新
				AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
				alert.setTitle("软件升级").setMessage("发现新版本,建议立即更新使用.")
						.setPositiveButton("更新", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 开启更新服务UpdateService
								updateIntent = new Intent(LoginActivity.this, UpdateService.class);
								startService(updateIntent);
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				alert.create().show();

			} else {
				LogUtil.d(TAG, "没有新版本，无需更新");
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 * @Description: 登陆
	 * @return void
	 */
	private void doLogin() {
		if (checkInput()) {
			// 记住了信息
			if (cb_remberInfo.isChecked()) {
				MyApplication.getInstance().setUserName(et_userName.getText().toString());
				MyApplication.getInstance().setIdentity(tv_identity.getText().toString());
				MyApplication.getInstance().setPassword(et_passWord.getText().toString());
				MyApplication.getInstance().setRember(true);
			} else if (cb_remberInfo.isChecked() == false) {
				MyApplication.getInstance().setRember(false);
			}
			if (MyApplication.getInstance().isADMIN()) {

				startActivity(new Intent(LoginActivity.this, AdminSiteActivity.class));
			}
			LogUtil.d(TAG, "我的身份：" + identity);
			LogUtil.d(TAG, "用户名：" + userName);
			LogUtil.d(TAG, "密码：" + passWord);
			RequestParams params = new RequestParams();
			params.put("grant_type", "password");
			params.put("username", identity + "/" + userName);
			params.put("password", passWord);
			LogUtil.d(TAG, "请求地址：" + HttpUri.LOGIN);
			mLoadingDialog = new LoadingDialog(LoginActivity.this, "正在登录");
			mLoadingDialog.show();
			MyHttpClient.postLogin(HttpUri.LOGIN, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, org.json.JSONObject response) {
					super.onSuccess(statusCode, response);
					// 获取token信息，并保存到全局，方便调用
					JSONObject object = (JSONObject) JSON.parse(response.toString());
					access_token = object.getString("access_token");
					token_type = object.getString("token_type");
					LogUtil.d(TAG, "获取到的access_token：" + access_token);
					LogUtil.d(TAG, "获取到的token_typ：" + token_type);
					MyHttpClient.getUerInfo(HttpUri.USERINFO, access_token, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, org.json.JSONObject response) {
							super.onSuccess(statusCode, response);
							LogUtil.d(TAG, response.toString());
							// 将用户信息写入xml
							MyApplication.getInstance().setAccess_token(access_token);
							MyApplication.getInstance().setToken_type(token_type);
							MyApplication.getInstance().setemail(response.optString("email"));
							MyApplication.getInstance().setId(response.optString("_id"));
							MyApplication.getInstance().setNickName(response.optString("nickName"));
							MyApplication.getInstance().setPhone_num(response.optString("phone_num"));
							MyApplication.getInstance().setSiteId(response.optString("siteId"));
							MyApplication.getInstance().setStatus(response.optString("status"));
							MyApplication.getInstance().setVerifyStatus(response.optString("verifyStatus"));
							mLoadingDialog.dismiss();
							startActivity(new Intent(LoginActivity.this, MainActivity.class));
						}
					});

				}

				@Override
				public void onFailure(Throwable e, org.json.JSONObject errorResponse) {
					super.onFailure(e, errorResponse);
					mLoadingDialog.dismiss();
					ToastUtil.showShortToast(LoginActivity.this, "用户名或者密码错误");
				}
			});

		} else {
			ToastUtil.showShortToast(LoginActivity.this, "请填写账号和密码");
		}
	}

	/**
	 * 
	 * @Description: 注册
	 * @return void
	 */
	private void doRegister() {
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);
	}

	/**
	 * 
	 * @Description: 选择登陆身份
	 * @return void
	 */
	private void selectIdentity() {
		Dialog dialog = null;
		Builder builder = new android.app.AlertDialog.Builder(this);
		// 设置对话框的图标
		builder.setIcon(R.drawable.ic_launcher);
		// 设置对话框的标题
		builder.setTitle("我是：");
		builder.setItems(R.array.login_identity, new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String identity = getResources().getStringArray(R.array.login_identity)[which];
				ToastUtil.showLongToast(LoginActivity.this, "我是" + identity);
				tv_identity.setText(identity);
			}
		});
		// 创建一个列表对话框
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 
	 * @Description: 检查输入合法性
	 * @return boolean
	 */
	private boolean checkInput() {
		userName = et_userName.getText().toString().trim();
		passWord = et_passWord.getText().toString().trim();
		String str = tv_identity.getText().toString();
		if (!userName.equals("") && !passWord.equals("")) {
			if (str.equals(getResources().getStringArray(R.array.login_identity)[0])) {
				MyApplication.getInstance().setADMIN(true);
				MyApplication.getInstance().setCOURIERS(false);
				MyApplication.getInstance().setMASTER(false);
				identity = "admin";
			}
			if (str.equals(getResources().getStringArray(R.array.login_identity)[1])) {
				identity = "sitemanager";
				MyApplication.getInstance().setMASTER(true);
				MyApplication.getInstance().setADMIN(false);
				MyApplication.getInstance().setCOURIERS(false);
			}
			if (str.equals(getResources().getStringArray(R.array.login_identity)[2])) {
				identity = "postman";
				MyApplication.getInstance().setCOURIERS(true);
				MyApplication.getInstance().setMASTER(false);
				MyApplication.getInstance().setADMIN(false);
			}
			return true;
		}
		return false;
	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_login:
				if (NetWork.isNetworkConnected(LoginActivity.this)) {

					doLogin();
				} else {
					ToastUtil.showShortToast(LoginActivity.this, "请检查网络配置");
				}
				break;
			case R.id.activity_login_register:
				if (NetWork.isNetworkConnected(LoginActivity.this)) {

					doRegister();
				} else {
					ToastUtil.showShortToast(LoginActivity.this, "请检查网络配置");
				}

				break;
			case R.id.activity_login_identity:
				selectIdentity();
				break;
			default:
				break;
			}

		}
	}

	class MyCheckChanged implements android.widget.CompoundButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
			} else {

			}
		}

	}

}
