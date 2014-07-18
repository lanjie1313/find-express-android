package com.runye.express.activity.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.runye.express.activity.couriers.CouriersManActivity;
import com.runye.express.activity.master.MasterMainActivity;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.http.HttpUri;
import com.runye.express.http.MyHttpClient;
import com.runye.express.map.MapApplication;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SysExitUtil.activityList.add(LoginActivity.this);
		initUI();

	}

	private void initUI() {
		boolean isRegister = getIntent().getBooleanExtra("ISREGISTER", false);
		MapApplication.getInstance().setISADMIN(false);
		MapApplication.getInstance().setISCOURIERS(false);
		MapApplication.getInstance().setISMASTER(false);
		bt_login = (Button) findViewById(R.id.activity_login);
		bt_register = (Button) findViewById(R.id.activity_login_register);
		et_userName = (EditText) findViewById(R.id.activity_login_userName);
		et_passWord = (EditText) findViewById(R.id.activity_login_passWord);
		tv_identity = (TextView) findViewById(R.id.activity_login_identity);
		cb_remberInfo = (CheckBox) findViewById(R.id.activity_login_remberPwd);
		cb_remberInfo.setOnCheckedChangeListener(new MyCheckChanged());
		tv_identity.setOnClickListener(new MyOnClickListener());
		bt_login.setOnClickListener(new MyOnClickListener());
		bt_register.setOnClickListener(new MyOnClickListener());

		SharedPreferences userInfo = getSharedPreferences("user_info", 0);
		String username = userInfo.getString("username", "").trim();
		String password = userInfo.getString("password", "").trim();
		String identity = userInfo.getString("identity", "").trim();
		if (!username.equals("") && !password.equals("") && !identity.equals("") && isRegister == false) {

			et_userName.setText(username);
			et_passWord.setText(password);
			tv_identity.setText(identity);
		}
		if (isRegister) {
			et_userName.setText("");
			et_passWord.setText("");
		}
	}

	LoadingDialog dialog;

	/**
	 * 
	 * @Description: 登陆
	 * @return void
	 */
	private void doLogin() {
		if (checkInput()) {

			if (cb_remberInfo.isChecked()) {
				// 记住信息
				SharedPreferences userInfo = getSharedPreferences("user_info", 0);
				userInfo.edit().putString("identity", tv_identity.getText().toString()).commit();
				userInfo.edit().putString("username", et_userName.getText().toString()).commit();
				userInfo.edit().putString("password", et_passWord.getText().toString()).commit();
				LogUtil.d(TAG, "记住了信息");

			} else {

				LogUtil.d(TAG, "没有记住信息");
			}
			if (MapApplication.getInstance().isISADMIN()) {

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
			dialog = new LoadingDialog(LoginActivity.this, "正在登录");
			dialog.show();
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
							// 将用户信息写入xml
							LogUtil.d(TAG, response.toString());
							SharedPreferences userInfo = getSharedPreferences("user_info", 1);
							userInfo.edit().putString("id", response.optString("_id")).commit();
							userInfo.edit().putString("siteId", response.optString("siteId")).commit();
							userInfo.edit().putString("nickName", response.optString("nickName")).commit();
							userInfo.edit().putString("phone_num", response.optString("phone_num")).commit();
							userInfo.edit().putString("email", response.optString("email")).commit();
							userInfo.edit().putString("status", response.optString("status")).commit();
							userInfo.edit().putString("verifyStatus", response.optString("verifyStatus")).commit();
							userInfo.edit().putString("access_token", access_token).commit();
							userInfo.edit().putString("token_type", token_type).commit();
							dialog.dismiss();
							if (MapApplication.getInstance().isISMASTER()) {
								startActivity(new Intent(LoginActivity.this, MasterMainActivity.class));
							}
							if (MapApplication.getInstance().isISCOURIERS()) {
								startActivity(new Intent(LoginActivity.this, CouriersManActivity.class));
							}
						}
					});

				}

				@Override
				public void onFailure(Throwable e, org.json.JSONObject errorResponse) {
					super.onFailure(e, errorResponse);
					dialog.dismiss();
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
				MapApplication.getInstance().setISADMIN(true);
				MapApplication.getInstance().setISCOURIERS(false);
				MapApplication.getInstance().setISMASTER(false);
				identity = "admin";
			}
			if (str.equals(getResources().getStringArray(R.array.login_identity)[1])) {
				identity = "sitemanager";
				MapApplication.getInstance().setISMASTER(true);
				MapApplication.getInstance().setISADMIN(false);
				MapApplication.getInstance().setISCOURIERS(false);
			}
			if (str.equals(getResources().getStringArray(R.array.login_identity)[2])) {
				identity = "postman";
				MapApplication.getInstance().setISCOURIERS(true);
				MapApplication.getInstance().setISMASTER(false);
				MapApplication.getInstance().setISADMIN(false);
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
