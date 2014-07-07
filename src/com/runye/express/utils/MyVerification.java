package com.runye.express.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @ClassName: MyVerification
 * @Description: 验证手机邮箱格式
 * @author LanJie.Chen
 * @date 2014-7-1 下午3:22:52
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class MyVerification {
	/** 用于判断手机号段是否合法 */
	public static boolean isMobile(String mobiles) {
		Pattern p = Pattern.compile("^[1][3-8]+\\d{9}");
		Matcher m = p.matcher(mobiles);
		System.out.println(m.matches() + "---");
		return m.matches();

	}

	/** 用于判断邮箱地址是否合法 */
	public static boolean isEmail(String emails) {
		Pattern p = Pattern.compile("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?");
		Matcher m = p.matcher(emails);
		System.out.println(m.matches() + "---");
		return m.matches();

	}
}
