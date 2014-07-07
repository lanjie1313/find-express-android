package com.runye.express.utils;

import android.util.Log;

public class LogUtil {
	/**
	 * 日志打印工具类，如果软件处于调试状态，允许打印相关日志，否则不允许， 这也是很多api中设置setDebugMode的原因吧
	 * 
	 * @author Tao
	 * 
	 */

	private static boolean isDebug = true;

	public static void i(String tag, String message) {
		if (isDebug) {
			Log.i(tag, message);
		}

	}

	public static void e(String tag, String message) {
		if (isDebug) {
			Log.e(tag, message);
		}

	}

	public static void d(String tag, String message) {
		if (isDebug) {
			Log.d(tag, message);
		}

	}

}
