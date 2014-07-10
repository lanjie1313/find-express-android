package com.runye.express.activity.sitemaster;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.runye.express.activity.common.OrderInfoActivity;
import com.runye.express.adapter.OrderModeAdapter;
import com.runye.express.android.R;
import com.runye.express.bean.OrderModeBean;
import com.runye.express.http.HttpUri;
import com.runye.express.listview.PullToRefreshView;
import com.runye.express.listview.PullToRefreshView.OnFooterRefreshListener;
import com.runye.express.listview.PullToRefreshView.OnHeaderRefreshListener;
import com.runye.express.map.MapApplication;
import com.runye.express.utils.AssembleAddress;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: MasterBaseActivity
 * @Description: master activity 管理器
 * @author LanJie.Chen
 * @date 2014-7-7 下午4:32:14
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class MasterBaseActivity extends Activity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private final String TAG = "MasterBaseActivity";
	private List<OrderModeBean> mList;
	private PullToRefreshView mPullToRefreshView;
	private ListView mListView;
	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master_base);
		initUI();
		SysExitUtil.activityList.add(MasterBaseActivity.this);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initUI() {
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.activity_master_pullToRefreshView);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mListView = (ListView) findViewById(R.id.activity_master_listview);
		//
		String status = getIntent().getStringExtra("STATUS");
		token = MapApplication.getInstance().getAccess_token();
		mListView.setOnItemClickListener(new MyListLisytener());
		MyAsyncTaskGet get = new MyAsyncTaskGet(this);
		Map<String, String> params = new HashMap<String, String>();
		params.put("status", status);
		String uri = AssembleAddress.questionMark(HttpUri.ORDERS, params);
		LogUtil.d(TAG, "请求URI:" + uri);
		get.execute(uri);
	}

	class MyListLisytener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(MasterBaseActivity.this,
					OrderInfoActivity.class);
			OrderModeBean bean = mList.get(position);
			Bundle bundle = new Bundle();
			// bundle.putSerializable("ORDERINFO", bean);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void run() {

				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日   HH:mm:ss     ");
				Date curDate = new Date(System.currentTimeMillis());
				// 获取当前时间
				String str = formatter.format(curDate);
				mPullToRefreshView.onHeaderRefreshComplete("最后更新：" + str);
			}
		}, 1000);

	}

	class MyAsyncTaskGet extends AsyncTask<String, Integer, String> {
		ProgressDialog proDialog;

		public MyAsyncTaskGet(Context context) {
			proDialog = new ProgressDialog(context, 0);
			proDialog.setCancelable(true);
			proDialog.setMax(100);
			proDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			proDialog.show();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) { // 在后台，下载url网页内容
			try {

				// 第一步，创建HttpGet对象
				HttpGet get = new HttpGet(params[0]);
				get.setHeader("Authorization", "Bearer " + token);
				// 第二步，使用execute方法发送HTTP GET请求，并返回HttpResponse对象
				HttpResponse response = new DefaultHttpClient().execute(get);
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

					String result = new String(bos.toString("UTF-8"));
					return result;
				} else {

					LogUtil.d(TAG, "Error Response:"
							+ response.getStatusLine().toString());
				}
			} catch (Exception e) {
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
			if (result.equals("error")) {
				proDialog.dismiss();
				ToastUtil.showShortToast(MasterBaseActivity.this, "请求出错了，请重试");
			} else {

				LogUtil.d(TAG, "请求成功：" + result);// 可以与UI控件交互
				// 把字符串转为Json对象，这是因为我的json数据首先是json对象
				JSONObject jsonObject = JSON.parseObject(result);
				// 然后是jsonArray，可以根据我的json数据知道
				JSONArray array = jsonObject.getJSONArray("records");
				// 根据Bean类的到每一个json数组的项
				mList = new ArrayList<OrderModeBean>();
				String replaceAfter = array.toString()
						.replaceAll("\"__v\"", "\"v\"")
						.replaceAll("\"_id\"", "\"id\"");//
				mList = JSON.parseArray(replaceAfter, OrderModeBean.class);
				for (int i = 0; i < mList.size(); i++) {
					LogUtil.d(TAG, mList.get(i).getRecipient_address());
				}
				OrderModeAdapter adapter = new OrderModeAdapter(
						MasterBaseActivity.this, mList);
				mListView.setAdapter(adapter);
				proDialog.dismiss();
			}
		}
	}
}
