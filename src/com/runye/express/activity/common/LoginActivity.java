package com.runye.express.activity.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.runye.express.activity.administrator.AdminMainActivity;
import com.runye.express.activity.couriers.CouriersManActivity;
import com.runye.express.activity.sitemaster.MasterMainActivity;
import com.runye.express.android.R;
import com.runye.express.http.HttpUri;
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
		if (!username.equals("") && !password.equals("")
				&& !identity.equals("") && isRegister == false) {

			et_userName.setText(username);
			et_passWord.setText(password);
			tv_identity.setText(identity);
		}
		if (isRegister) {
			et_userName.setText("");
			et_passWord.setText("");
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

			// 登陆
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new BasicNameValuePair("grant_type", "password"));
			// 判断身份
			paramsList.add(new BasicNameValuePair("username", identity + "/"
					+ userName));
			LogUtil.d(TAG, "我的身份：" + identity);
			LogUtil.d(TAG, "用户名：" + userName);
			LogUtil.d(TAG, "密码：" + passWord);
			paramsList.add(new BasicNameValuePair("password", passWord));
			MyAsyncTaskPost post = new MyAsyncTaskPost(LoginActivity.this,
					paramsList);
			post.execute(HttpUri.LOGIN);

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
		userName = et_userName.getText().toString().trim();
		passWord = et_passWord.getText().toString().trim();
		String str = tv_identity.getText().toString();
		if (!userName.equals("") && !passWord.equals("")) {
			if (str.equals(getResources()
					.getStringArray(R.array.login_identity)[0])) {
				MapApplication.getInstance().setISADMIN(true);
				identity = "admin";
			}
			if (str.equals(getResources()
					.getStringArray(R.array.login_identity)[1])) {
				identity = "sitemanager";
				MapApplication.getInstance().setISMASTER(true);
			}
			if (str.equals(getResources()
					.getStringArray(R.array.login_identity)[2])) {
				identity = "postman";
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
				doLogin();
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

	private class MyAsyncTaskPost extends AsyncTask<String, Integer, String> {
		ProgressDialog proDialog;
		List<NameValuePair> mPairs;

		public MyAsyncTaskPost(Context context, List<NameValuePair> params) {
			proDialog = new ProgressDialog(context, 0);
			proDialog.setCancelable(true);
			proDialog.setMax(100);
			proDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			proDialog.show();
			mPairs = params;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) { // 在后台，下载url网页内容

			try {

				// 第一步，创建HttpPost对象
				HttpPost post = new HttpPost(params[0]);

				// 登陆时
				post.addHeader("Authorization",
						"Basic YW5kcm9pZENsaWVudDpOZXB0dW5l'");
				/* 发出HTTP request */
				post.setEntity(new UrlEncodedFormEntity(mPairs));
				// 第二步，使用execute方法发送HTTP Post请求，并返回HttpResponse对象
				HttpResponse response = new DefaultHttpClient().execute(post);
				LogUtil.d(TAG, "响应码："
						+ response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == 200) { // 判断网络连接是否成功
					// 第三步，使用getEntity方法活得返回结果
					HttpEntity entity = response.getEntity();
					long len = entity.getContentLength(); // 获取url网页内容总大小
					InputStream is = entity.getContent();
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int ch = -1;
					int count = 0; // 统计已下载的url网页内容大小
					while (is != null && (ch = is.read(buffer)) != -1) {
						bos.write(buffer, 0, ch);
						count += ch;
						if (len > 0) {
							float ratio = count / (float) len * 100; // 计算下载url网页内容百分比
							publishProgress((int) ratio);
						}
						Thread.sleep(100);
					}

					String result = new String(bos.toString(HTTP.UTF_8));
					return result;
				} else {
					LogUtil.d(TAG, "Error Response: "
							+ response.getStatusLine().toString());
				}
			} catch (Exception e) {
				LogUtil.d(TAG, "Error: " + e.getMessage().toString());
				e.printStackTrace();
			}

			return "error";
		}

		@Override
		protected void onProgressUpdate(Integer... values) { // 可以与UI控件交互
			proDialog.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			LogUtil.d(TAG, result);
			proDialog.dismiss();
			if (result.equals("error")) {
				ToastUtil.showShortToast(LoginActivity.this, "用户名或者密码错误");
			} else {
				// 获取token信息，并保存到全局，方便调用
				JSONObject object = (JSONObject) JSON.parse(result);
				String access_token = object.getString("access_token");
				String token_type = object.getString("token_type");
				MapApplication.getInstance().setAccess_token(access_token);
				MapApplication.getInstance().setToken_type(token_type);
				LogUtil.d(TAG, "获取到的access_token：" + access_token);
				LogUtil.d(TAG, "获取到的token_typ：" + token_type);
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
			}
		}
	}

}
