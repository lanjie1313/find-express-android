package com.runye.express.http;

import com.runye.express.async.AsyncHttpClient;
import com.runye.express.async.AsyncHttpResponseHandler;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;

public class MyHttpClient {
	private static AsyncHttpClient client = new AsyncHttpClient();

	/**
	 * 
	 * @Description: 获取站点
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @return void
	 */
	public static void getSite(String url,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	/**
	 * 
	 * @Description: 注册
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @return void
	 */
	public static void postRegister(String url, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}

	public static void delete(String url, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		client.delete(url, responseHandler);
	}

	public static void patch(String url, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		client.patch(url, params, responseHandler);
	}
}
