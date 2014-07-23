package com.runye.express.activity.common;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.bean.SiteModeBean;
import com.runye.express.http.HttpUri;
import com.runye.express.http.MyHttpClient;
import com.runye.express.utils.LoadingDialog;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.MyVerification;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: Register
 * @Description: 注册
 * @author LanJie.Chen
 * @date 2014-7-1 下午3:05:11
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class RegisterActivity extends Activity {
	private final String TAG = "RegisterActivity";
	/**
	 * 0 昵称 1 手机 2 邮箱 3 密码 4 确认密码 5 推广号
	 */
	private EditText[] et_registers;
	private String[] et_values;
	/** 单选控件 */
	private RadioGroup rg_sex;
	/** 站长 */
	private RadioButton rb_master;
	/** 快递员 */
	private RadioButton rb_couriers;
	/** 注册按钮 */
	private Button bt_commit;
	/** 站点选择 */
	private Spinner webSite;
	/** 站点数据源 */
	private String[] sites;
	/** 身份选择 */
	private String choiceIdentity;
	/** 选择的站点 */
	private String choiceSite;
	/** 站点集合 */
	private List<SiteModeBean> mList;
	/** 站点id */
	private String siteId;
	/** 注册uri */
	private String mUri = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initUI();
		SysExitUtil.activityList.add(RegisterActivity.this);
	}

	private void initUI() {
		// 6个et的id
		int[] etID = new int[] { R.id.activity_register_nickname, R.id.activity_register_phone,
				R.id.activity_register_email, R.id.activity_register_password, R.id.activity_register_password2 };
		et_registers = new EditText[etID.length];
		et_values = new String[etID.length];
		for (int i = 0; i < etID.length; i++) {
			et_registers[i] = (EditText) findViewById(etID[i]);

		}

		// RadioGroup
		rg_sex = (RadioGroup) findViewById(R.id.activity_register_radioGroup);
		rb_master = (RadioButton) findViewById(R.id.activity_register_master);
		rb_couriers = (RadioButton) findViewById(R.id.activity_register_couriers);
		rg_sex.setOnCheckedChangeListener(new MyRadioListener());

		// button
		bt_commit = (Button) findViewById(R.id.activity_register_confirm);
		bt_commit.setOnClickListener(new MyButtonListener());
		webSite = (Spinner) findViewById(R.id.activity_register_spinner);
		getSite();

	}

	/**
	 * 
	 * @Description: 获取站点
	 * @return void
	 */
	private void getSite() {

		final LoadingDialog dialog = new LoadingDialog(RegisterActivity.this, "正在获取站点");
		MyHttpClient.getSite(HttpUri.SITE, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				LogUtil.d(TAG, "获取站点中");
				dialog.show();
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.d(TAG, response.toString());
				// 根据Bean类的到每一个json数组的项
				mList = new ArrayList<SiteModeBean>();
				String replaceAfter = "";
				try {
					replaceAfter = response.getJSONArray("records").toString().replaceAll("\"__v\"", "\"v\"")
							.replaceAll("\"_id\"", "\"id\"");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mList.addAll(JSON.parseArray(replaceAfter, SiteModeBean.class));
				sites = new String[mList.size()];
				for (int i = 0; i < mList.size(); i++) {
					sites[i] = mList.get(i).getName();
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this,
						android.R.layout.simple_spinner_item, sites);
				// 设置下拉列表的风格
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				webSite.setAdapter(adapter);
				webSite.setOnItemSelectedListener(new MySpinnerListener());
				dialog.dismiss();
				super.onSuccess(statusCode, response);

			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				LogUtil.d(TAG, errorResponse.toString());
				ToastUtil.showShortToast(RegisterActivity.this, "获取站点失败！请稍后重试");
				super.onFailure(e, errorResponse);
			}

		});
	}

	// private List<E>
	/**
	 * 
	 * @Description: 检查输入完整性
	 * @return boolean
	 */
	private boolean checkInput() {
		for (int i = 0; i < et_values.length; i++) {
			et_values[i] = et_registers[i].getText().toString().trim();
			if (et_values[i].equals("")) {
				ToastUtil.showLongToast(RegisterActivity.this, "还有信息未填写哦！");
				return false;
			}
			LogUtil.d("", et_values[i]);
			// 判断手机号
			if (i == 1) {

				if (MyVerification.isMobile(et_values[1]) == false) {
					ToastUtil.showShortToast(RegisterActivity.this, "请输入正确的手机号");
					return false;
				}
			}
			// 判断邮箱
			if (i == 2) {

				if (MyVerification.isEmail(et_values[2]) == false) {
					ToastUtil.showShortToast(RegisterActivity.this, "请输入正确的邮箱");
					return false;
				}
			}

		}

		if (rb_master.isChecked() == false && rb_couriers.isChecked() == false) {

			ToastUtil.showShortToast(RegisterActivity.this, "请选择身份");
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @Description: 注册方法，联网提交参数
	 * @return void
	 */
	private void doRegister() {
		if (checkInput()) {

			confimRegister();

		} else {

		}
	}

	/**
	 * 
	 * @Description: 注册提示信息
	 * @return void
	 */
	private void confimRegister() {
		Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
		alertDialog.setMessage("即将注册：" + "\n" + "站点：" + choiceSite + "\n" + "身份：" + choiceIdentity);
		alertDialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 联网代码，提交参数 注册
				LogUtil.d(TAG, "名称:" + et_values[0] + "密码" + et_values[3] + "电话" + et_values[1] + "邮箱" + et_values[2]
						+ "站ID" + siteId);
				RequestParams params = new RequestParams();
				params.put("nickName", et_values[0]);
				params.put("phone_num", et_values[1]);
				params.put("email", et_values[2]);
				params.put("password", et_values[3]);
				params.put("siteId", siteId);
				if (choiceIdentity.equals("站长")) {
					mUri = HttpUri.TEST_IP + HttpUri.MASTER;
				} else if (choiceIdentity.equals("快递员")) {
					mUri = HttpUri.TEST_IP + HttpUri.COURIERS;
				}
				MyHttpClient.postRegister(mUri, params, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						ToastUtil.showShortToast(RegisterActivity.this, "注册成功");
						Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
						intent.putExtra("ISREGISTER", true);
						intent.putExtra("username", et_values[1]);
						startActivity(intent);
						finish();

					}

					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						super.onFailure(e, errorResponse);
						ToastUtil.showShortToast(RegisterActivity.this, "当前的手机号或者邮箱已被注册了");
					}
				});
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

	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_register_confirm:
				// 注册
				doRegister();
				break;

			default:
				break;
			}
		}
	}

	class MyRadioListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.activity_register_master:
				rb_master.setChecked(true);
				choiceIdentity = rb_master.getText().toString();
				break;
			case R.id.activity_register_couriers:
				rb_couriers.setChecked(true);
				choiceIdentity = rb_couriers.getText().toString();
				break;
			default:
				break;
			}
		}
	}

	private class MySpinnerListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			choiceSite = mList.get(position).getName();
			siteId = mList.get(position).getId();
			LogUtil.d(TAG, "选择的站点：" + choiceSite);
			LogUtil.d(TAG, "该站点id:" + siteId);

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

}