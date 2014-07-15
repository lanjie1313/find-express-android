package com.runye.express.http;

/**
 * 
 * @ClassName: HttpUri
 * @Description: 请求地址
 * @author LanJie.Chen
 * @date 2014-7-9 下午1:36:06
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class HttpUri {
	/** 正式api */
	public static final String IP = "http://api.tyfind.com:8888/";
	/** 测试api */
	public static final String TEST_IP = "http://api.tyfind.cn:8008/";
	/** 站长注册 */
	public static final String REGISTER_MASTER = "expsitemanagers";
	/** 快递员 */
	public static final String REGISTER_COURIERS = "exppostmans";
	/** 登陆 */
	public static final String LOGIN = TEST_IP + "token";
	/** 站点 */
	public static final String SITE = TEST_IP + "expsites";
	/** 订单 */
	public static final String ORDERS = TEST_IP + "orders";

	/** 用户session */
	public static final String USERINFO = TEST_IP + "session";
	/** merchant */
	public static final String MERCHANT = TEST_IP + "merchants/";
}
