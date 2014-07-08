package com.runye.express.activity.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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

import com.runye.express.activity.administrator.AdminMainActivity;
import com.runye.express.activity.couriers.CouriersManActivity;
import com.runye.express.activity.webmaster.MasterMainActivity;
import com.runye.express.android.R;
import com.runye.express.http.MyAsyncTask;
import com.runye.express.map.MapApplication;
import com.runye.express.utils.LogUtil;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SysExitUtil.activityList.add(LoginActivity.this);
		initUI();

	}

	private void initUI() {
		MapApplication.getInstance().setISADMIN(false);
		MapApplication.getInstance().setISCOURIERS(false);
		MapApplication.getInstance().setISMASTER(false);
		bt_login = (Button) findViewById(R.id.activity_login);
		bt_register = (Button) findViewById(R.id.activity_login_register);
		et_passWord = (EditText) findViewById(R.id.activity_login_userName);
		et_userName = (EditText) findViewById(R.id.activity_login_passWord);
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
		if (!username.equals("") && !password.equals("")
				&& !identity.equals("")) {

			et_userName.setText(username);
			et_passWord.setText(password);
			tv_identity.setText(identity);
		}
	}

	/**
	 * 
	 * @Description: 登陆
	 * @return void
	 */
	private void doLogin() {
		if (checkInput()) {
			if (cb_remberInfo.isChecked()) {
				// 记住信息
				SharedPreferences userInfo = getSharedPreferences("user_info",
						0);
				userInfo.edit()
						.putString("identity", tv_identity.getText().toString())
						.commit();
				userInfo.edit()
						.putString("username", et_userName.getText().toString())
						.commit();
				userInfo.edit()
						.putString("password", et_passWord.getText().toString())
						.commit();

			}
			// 联网登陆代码
			if (MapApplication.getInstance().isISADMIN()) {

				startActivity(new Intent(LoginActivity.this,
						AdminMainActivity.class));
			}
			if (MapApplication.getInstance().isISMASTER()) {
				startActivity(new Intent(LoginActivity.this,
						MasterMainActivity.class));
			}
			if (MapApplication.getInstance().isISCOURIERS()) {
				startActivity(new Intent(LoginActivity.this,
						CouriersManActivity.class));
			}
		} else {
			ToastUtil.showLongToast(LoginActivity.this, "请输入正确的账号或密码！");
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
		builder.setItems(R.array.login_identity,
				new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String identity = getResources().getStringArray(
								R.array.login_identity)[which];
						ToastUtil.showLongToast(LoginActivity.this, "我是"
								+ identity);
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
		String str1 = et_userName.getText().toString().trim();
		String str2 = et_passWord.getText().toString().trim();
		String str3 = tv_identity.getText().toString();
		if (!str1.equals("") && !str2.equals("")) {
			if (str3.equals(getResources().getStringArray(
					R.array.login_identity)[0])) {
				MapApplication.getInstance().setISADMIN(true);
			}
			if (str3.equals(getResources().getStringArray(
					R.array.login_identity)[1])) {
				MapApplication.getInstance().setISMASTER(true);
			}
			if (str3.equals(getResources().getStringArray(
					R.array.login_identity)[2])) {
				MapApplication.getInstance().setISCOURIERS(true);
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
				// doLogin();
				textHttp();
				break;
			case R.id.activity_login_register:
				doRegister();
				break;
			case R.id.activity_login_identity:
				selectIdentity();
				break;
			default:
				break;
			}

		}
	}

	class MyCheckChanged implements
			android.widget.CompoundButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
			} else {

			}
		}

	}

	private void textHttp() {
		params = new HashMap<String, String>();
		params.put("merchant", "5355d0f424c610e42182bfa8");
		params.put("featured", "false");
		params.put("status", "succeed_verify");
		params.put("sort", "sales");
		params.put("limit", "10");
		LogUtil.d("=======", getUri(uir, params));
		MyAsyncTask asyncTask = new MyAsyncTask(LoginActivity.this);
		asyncTask.execute("");
	}

	private Map<String, String> params;
	private final String uir = "http://api.tyfind.com:8888/products";

	private String getUri(String path, Map<String, String> params) {
		// StringBuilder是用来组拼请求地址和参数
		StringBuilder sb = new StringBuilder();
		sb.append(path).append("?");
		if (params != null && params.size() != 0) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				// 如果请求参数中有中文，需要进行URLEncoder编码 gbk/utf8
				try {
					sb.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
}
