package com.runye.express.http;

import com.runye.express.async.AsyncHttpClient;
import com.runye.express.async.AsyncHttpResponseHandler;
import com.runye.express.async.JsonHttpResponseHandler;
import com.runye.express.async.RequestParams;
import com.runye.express.utils.LogUtil;

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
	public static void getSite(String url, AsyncHttpResponseHandler responseHandler) {
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
	public static void postRegister(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}

	/**
	 * 
	 * @Description: 登陆
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @return void
	 */
	public static void postLogin(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
		client.addHeader("Authorization", "Basic YW5kcm9pZENsaWVudDpOZXB0dW5l'");
		client.post(url, params, responseHandler);
	}

	/**
	 * 
	 * @Description: 获取用户信息
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @param access_token
	 * @return void
	 */
	public static void getUerInfo(String url, String access_token, JsonHttpResponseHandler responseHandler) {
		client.addHeader("Authorization", "Bearer " + access_token);
		LogUtil.d("传递的token:", access_token);
		client.get(url, responseHandler);
	}

	/**
	 * 
	 * @Description: 获取订单
	 * @param url
	 * @param access_token
	 * @param responseHandler
	 * @return void
	 */
	public static void getOrders(String url, String access_token, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		client.addHeader("Authorization", "Bearer " + access_token);
		LogUtil.d("传递的token:", access_token);
		client.get(url, params, responseHandler);
	}

	public static void delete(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
		client.delete(url, responseHandler);
	}

	public static void patch(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
		client.patch(url, params, responseHandler);
	}
}
