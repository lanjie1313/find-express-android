package com.runye.express.activity.administrator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.runye.express.adapter.SiteAdapter;
import com.runye.express.android.R;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.bean.SiteModeBean;
import com.runye.express.http.HttpUri;
import com.runye.express.http.MyHttpClient;
import com.runye.express.utils.LoadingDialog;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: AdminListActivity
 * @Description:站点管理界面
 * @author LanJie.Chen
 * @date 2014-7-2 下午6:33:44
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AdminSiteActivity extends Activity {
	protected static final String TAG = "AdminSiteActivity";
	private ListView listView;
	/** 数据源 */
	private List<SiteModeBean> mList;
	/** 增加站点 */
	private Button bt_addSite;
	/***/
	private SiteAdapter adapter;
	/***/
	private LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_site);
		initUI();
		SysExitUtil.activityList.add(AdminSiteActivity.this);

	}

	private void initUI() {
		listView = (ListView) findViewById(R.id.activity_admin_site_listView);
		mList = new ArrayList<SiteModeBean>();
		adapter = new SiteAdapter(AdminSiteActivity.this, mList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new MyListItemListener());
		bt_addSite = (Button) findViewById(R.id.activity_admin_site_addSite);
		bt_addSite.setOnClickListener(new MyButtonListener());
		getSite();
	}

	/**
	 * 
	 * @Description: 获取站点
	 * @return void
	 */
	private void getSite() {

		dialog = new LoadingDialog(AdminSiteActivity.this, "正在获取站点");
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
				String replaceAfter = "";
				try {
					replaceAfter = response.getJSONArray("records").toString().replaceAll("\"__v\"", "\"v\"")
							.replaceAll("\"_id\"", "\"id\"");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mList.addAll(JSON.parseArray(replaceAfter, SiteModeBean.class));
				adapter.notifyDataSetChanged();
				dialog.dismiss();
				super.onSuccess(statusCode, response);

			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				LogUtil.d(TAG, errorResponse.toString());
				dialog.dismiss();
				ToastUtil.showShortToast(AdminSiteActivity.this, "获取站点失败！请稍后重试");
				super.onFailure(e, errorResponse);
			}

		});
	}

	/**
	 * button点击监听器
	 */
	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(AdminSiteActivity.this, AdminAddSiteActivity.class));
		}
	}

	/**
	 * listview点击事件监听器
	 */
	class MyListItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			SiteModeBean bean = mList.get(position);
			Bundle bundle = new Bundle();
			bundle.putSerializable("SITEBEAN", bean);
			intent.putExtras(bundle);
			intent.setClass(AdminSiteActivity.this, AdminSiteInfoActivity.class);
			startActivity(intent);
		}
	}

}
