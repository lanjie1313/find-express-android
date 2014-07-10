package com.runye.express.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 
 * @ClassName: AssembleAddress
 * @Description: 地址拼装
 * @author LanJie.Chen
 * @date 2014-7-9 下午1:31:30
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class AssembleAddress {
	/**
	 * 
	 */
	/** ?...&.. */
	public static String questionMark(String path, Map<String, String> params) {
		// StringBuilder是用来组拼请求地址和参数
		StringBuilder sb = new StringBuilder();
		sb.append(path).append("?");
		if (params != null && params.size() != 0) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				// 如果请求参数中有中文，需要进行URLEncoder编码 gbk/utf8
				try {
					sb.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
}
