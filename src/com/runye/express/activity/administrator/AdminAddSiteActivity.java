package com.runye.express.activity.administrator;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONArray;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.http.HttpUri;
import com.runye.express.http.MyHttpClient;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;
import com.runye.express.widget.LoadingDialog;

/**
 * 
 * @ClassName: AdminAddSiteActivity
 * @Description: 新增站点界面
 * @author LanJie.Chen
 * @date 2014-7-2 下午6:35:29
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AdminAddSiteActivity extends Activity {
	private final String TAG = "AdminAddSiteActivity";
	/** 确定 */
	private Button bt_addConfim;
	/**
	 * 输入框
	 */
	private EditText[] editTexts;
	private String[] values;
	private String siteName;
	private LoadingDialog mLoadingDialog;
	/** 输入框动画 */
	private Animation animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_addsite);
		initUI();
		SysExitUtil.activityList.add(AdminAddSiteActivity.this);
	}

	private void initUI() {
		animation = AnimationUtils.loadAnimation(this, R.anim.edittext_shake);
		bt_addConfim = (Button) findViewById(R.id.activity_admin_addsite_confim);
		bt_addConfim.setOnClickListener(new MyButtonListener());
		int[] ids = new int[] { R.id.activity_admin_addsite_siteName, R.id.activity_admin_addsite_Lng1,
				R.id.activity_admin_addsite_Lat1, R.id.activity_admin_addsite_Lng2, R.id.activity_admin_addsite_Lat2,
				R.id.activity_admin_addsite_Lng3, R.id.activity_admin_addsite_Lat3, };
		editTexts = new EditText[ids.length];
		values = new String[ids.length];
		for (int i = 0; i < ids.length; i++) {
			editTexts[i] = (EditText) findViewById(ids[i]);
		}
	}

	/**
	 * 
	 * @Description: 增加站长
	 * @return void
	 */
	private void addSite() {
		if (checkInput()) {
			/**
			 * "range": [{"lng": 120.0000, "lat": 32.0000}, { "lng": 112.0000,
			 * "lat": 30.000 }, {"lng": 130.0000,"lat": 35.000}]}"
			 */
			com.alibaba.fastjson.JSONObject jsonObject1 = new com.alibaba.fastjson.JSONObject();
			jsonObject1.put("lng", values[0]);
			jsonObject1.put("lat", values[1]);
			com.alibaba.fastjson.JSONObject jsonObject2 = new com.alibaba.fastjson.JSONObject();
			jsonObject2.put("lng", values[2]);
			jsonObject2.put("lat", values[3]);
			com.alibaba.fastjson.JSONObject jsonObject3 = new com.alibaba.fastjson.JSONObject();
			jsonObject3.put("lng", values[4]);
			jsonObject3.put("lat", values[5]);
			JSONArray array = new JSONArray();
			array.add(jsonObject1);
			array.add(jsonObject2);
			array.add(jsonObject3);
			LogUtil.d(TAG, array.toString());
			// 检查无误，开始向服务器发送请求
			RequestParams params = new RequestParams();
			params.put("name", siteName);
			params.put("range", array.toString());
			MyHttpClient.postSites(HttpUri.SITE, params, new JsonHttpResponseHandler() {
				@Override
				public void onStart() {
					mLoadingDialog = new LoadingDialog(AdminAddSiteActivity.this, "处理中");
					mLoadingDialog.show();
					super.onStart();
				}

				@Override
				public void onSuccess(int statusCode, JSONObject response) {
					super.onSuccess(statusCode, response);
					LogUtil.d(TAG, "增加站点成功" + response.toString());
					mLoadingDialog.dismiss();
					ToastUtil.showShortToast(AdminAddSiteActivity.this, "添加成功");
				}

				@Override
				public void onFailure(Throwable e, JSONObject errorResponse) {
					super.onFailure(e, errorResponse);
					LogUtil.d(TAG, "增加站点失败" + errorResponse.toString());
					ToastUtil.showShortToast(AdminAddSiteActivity.this, "添加失败，请重试");
					mLoadingDialog.dismiss();
				}
			});
		}
	}

	/**
	 * 
	 * @Description: 检查输入合法性
	 * @return boolean
	 */
	private boolean checkInput() {
		for (int i = 0; i < editTexts.length; i++) {
			values[i] = editTexts[i].getText().toString();
			LogUtil.d(TAG, values[i]);
			if (values[i].equals("")) {
				editTexts[i].startAnimation(animation);
				editTexts[i].requestFocus();
				editTexts[i].setError("未填写" + editTexts[i].getHint());
				return false;
			}
		}
		return true;
	}

	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			addSite();
		}
	}
}
