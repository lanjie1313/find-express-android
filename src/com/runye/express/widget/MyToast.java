package com.runye.express.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.runye.express.android.R;
import com.runye.express.utils.LogUtil;

/**
 * 
 * @ClassName: MyToast
 * @Description: 自定义的，类似toastview
 * @author LanJie.Chen
 * @date 2014-7-31 上午10:48:30
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class MyToast {
	private final static String TAG = "MyToast";

	// 点击事件
	public static void createToast(final Activity context, String message, boolean clickable) {
		final View view = LayoutInflater.from(context).inflate(R.layout.mytoast, null);
		TextView textView = (TextView) view.findViewById(R.id.mytoast_message);
		textView.setText(message);
		LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		View hiddenView = context.findViewById(R.id.mytoast_layout);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS); // 系统设置
				context.startActivity(intent);
			}
		});
		view.setClickable(clickable);
		if (hiddenView == null) {
			LogUtil.d(TAG, "创建了");
			context.addContentView(view, params);
		}

		// 判断是否可以点击，如果不能点击，这是自动消失的
		if (clickable == false) {
			// 3秒后自动关闭

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					dissToast(context);
				}
			}, 3000);
		}

	}

	/**
	 * 
	 * @Description: 移除view
	 * @param context
	 * @return void
	 */
	public static void dissToast(final Activity context) {
		View hiddenView = context.findViewById(R.id.mytoast_layout);
		if (hiddenView != null) {
			ViewGroup group = (ViewGroup) hiddenView.getParent();
			group.removeView(hiddenView);
		}
	}
}
