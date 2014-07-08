package com.runye.express.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.runye.express.bean.GoodsInfoBean;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {
	private final String TAG = "MyAsyncTask";
	ProgressDialog proDialog;

	public MyAsyncTask(Context context) {
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
		// 第一步，创建HttpGet对象
		HttpGet get = new HttpGet(params[0]); // url
		// 第二步，使用execute方法发送HTTP GET请求，并返回HttpResponse对象
		HttpResponse response = null;
		try {
			response = new DefaultHttpClient().execute(get);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = "";
		if (response.getStatusLine().getStatusCode() == 200) { // 判断网络连接是否成功
			// 第三步，使用getEntity方法活得返回结果
			HttpEntity entity = response.getEntity();
			// long len = entity.getContentLength(); // 获取url网页内容总大小
			// InputStream is = entity.getContent();
			// ByteArrayOutputStream bos = new ByteArrayOutputStream();
			// byte[] buffer = new byte[1024];
			// int ch = -1;
			// int count = 0; // 统计已下载的url网页内容大小
			// while (is != null && (ch = is.read(buffer)) != -1) {
			// bos.write(buffer, 0, ch);
			// count += ch;
			// if (len > 0) {
			// float ratio = count / (float) len * 100; // 计算下载url网页内容百分比
			// publishProgress((int) ratio);
			// }
			// Thread.sleep(100);
			// }
			// String result = new String(bos.toString("UTF-8"));
			try {
				result = EntityUtils.toString(entity, HTTP.UTF_8);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;

	}

	//
	@Override
	protected void onProgressUpdate(Integer... values) { // 可以与UI控件交互
		proDialog.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(String result) { // 可以与UI控件交互
		System.out.println(result);
		JSON.parseObject(result, GoodsInfoBean.class);
		proDialog.dismiss();
	}
}
