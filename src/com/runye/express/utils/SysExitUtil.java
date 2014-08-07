package com.runye.express.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 
 * @ClassName: SysExitUtil
 * @Description: activity管理器
 * @author LanJie.Chen
 * @date 2014-7-8 上午10:00:54
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class SysExitUtil {
	// 建立一个public static的list用来放activity
	public static List<Activity> activityList = new ArrayList<Activity>();

	// finish所有list中的activity
	public static void exit() {
		int siz = activityList.size();
		for (int i = 0; i < siz; i++) {
			if (activityList.get(i) != null) {
				activityList.get(i).finish();
			}
		}
		System.exit(0);
	}
}
