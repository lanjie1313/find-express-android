package com.runye.express.activity.administrator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.runye.express.adapter.CouriersAdapter;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.bean.CouriersModeBean;
import com.runye.express.bean.MasterModeBean;
import com.runye.express.bean.SiteModeBean;
import com.runye.express.http.HttpUri;
import com.runye.express.http.MyHttpClient;
import com.runye.express.listview.ListViewHeight;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.widget.LoadingDialog;

/**
 * 
 * @ClassName: AdminSiteInfoActivity
 * @Description: 站点详细信息
 * @author LanJie.Chen
 * @date 2014-7-3 上午10:45:18
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AdminSiteInfoActivity extends Activity {
	protected static final String TAG = "AdminSiteInfoActivity";
	/** 站点名称 */
	private TextView tv_siteName;
	/** 站长名称 */
	private TextView tv_webMaster;
	/** 删除 */
	private Button bt_del;
	/** 修改 */
	private Button bt_change;
	private ListView mListView;
	private CouriersAdapter mAdapter;
	private List<MasterModeBean> mMasterList;
	SiteModeBean siteBean;
	private LoadingDialog mLoadingDialog;
	private List<CouriersModeBean> mCouriersList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_siteinfo);
		initUI();
		SysExitUtil.activityList.add(AdminSiteInfoActivity.this);
	}

	private void initUI() {
		mLoadingDialog = new LoadingDialog(this, "加载中");
		mLoadingDialog.show();
		siteBean = (SiteModeBean) getIntent().getSerializableExtra("SITEBEAN");
		tv_siteName = (TextView) findViewById(R.id.activity_admin_siteinfo_siteName);
		tv_siteName.setText(siteBean.getName());

		tv_webMaster = (TextView) findViewById(R.id.activity_admin_siteinfo_webmaster);
		bt_del = (Button) findViewById(R.id.activity_admin_siteinfo_del);
		bt_change = (Button) findViewById(R.id.activity_admin_siteinfo_change);
		bt_del.setOnClickListener(new MyButtonListener());
		bt_change.setOnClickListener(new MyButtonListener());
		getSiteMaster();
		mMasterList = new ArrayList<MasterModeBean>();
		mCouriersList = new ArrayList<CouriersModeBean>();
		mListView = (ListView) findViewById(R.id.activity_admin_siteinfo_listView);
		mAdapter = new CouriersAdapter(this, mCouriersList);
		mListView.setAdapter(mAdapter);

	}

	/**
	 * 
	 * @Description: 更具上个界面传递过来的站点查询此站长信息
	 * @return void
	 */
	private void getSiteMaster() {

		RequestParams params = new RequestParams();
		params.put("siteId", siteBean.getId());
		LogUtil.d(TAG, "请求网址：" + HttpUri.MASTER + "?siteId=" + siteBean.getId());
		MyHttpClient.getSiteMaster(HttpUri.MASTER, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.d(TAG, "站长获取成功：" + response.toString());
				// 根据Bean类的到每一个json数组的项
				String replaceAfter = "";
				try {
					replaceAfter = response.getJSONArray("records").toString().replaceAll("\"__v\"", "\"v\"")
							.replaceAll("\"_id\"", "\"id\"");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mMasterList.addAll(JSON.parseArray(replaceAfter, MasterModeBean.class));
				tv_webMaster.setText(mMasterList.get(0).getNickName());
				getCouriers();
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				super.onFailure(e, errorResponse);
				LogUtil.d(TAG, "站长获取失败：" + errorResponse.toString());
				mLoadingDialog.dismiss();
			}
		});

	}

	/**
	 * 
	 * @Description: 联网取站点下的快递员
	 * @return List<String>
	 */
	private void getCouriers() {
		RequestParams params = new RequestParams();
		params.put("siteId", siteBean.getId());
		MyHttpClient.getSiteCouriers(HttpUri.COURIERS, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				LogUtil.d(TAG, "快递员获取成功：" + response.toString());
				// 根据Bean类的到每一个json数组的项
				String replaceAfter = "";
				try {
					replaceAfter = response.getJSONArray("records").toString().replaceAll("\"__v\"", "\"v\"")
							.replaceAll("\"_id\"", "\"id\"");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mCouriersList.addAll(JSON.parseArray(replaceAfter, CouriersModeBean.class));
				mAdapter.notifyDataSetChanged();
				ListViewHeight.setListViewHeightBasedOnChildren(mListView);
				mLoadingDialog.dismiss();
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				super.onFailure(e, errorResponse);
				LogUtil.d(TAG, "快递员获取失败：" + errorResponse.toString());
				mLoadingDialog.dismiss();
			}
		});
	}

	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
		}
	}
}
