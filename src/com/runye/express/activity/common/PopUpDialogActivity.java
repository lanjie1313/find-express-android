package com.runye.express.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.runye.express.android.R;

/**
 * 
 * @ClassName: PopUpDialogActivity
 * @Description: 弹出框
 * @author LanJie.Chen
 * @date 2014-8-6 下午2:03:43
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class PopUpDialogActivity extends Activity implements OnClickListener {
	/** 新的 */
	private LinearLayout layout_new;
	/** 配送中 */
	private LinearLayout layout_deliving;
	/** 已完成 */
	private LinearLayout layout_completed;
	/** 拒绝 */
	private LinearLayout layout_rejected;
	/** 取消 */
	private LinearLayout layout_cancled;
	/** 请求参数 */
	private String[] couriersRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup_dialog);
		initView();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		couriersRequest = getResources().getStringArray(R.array.couriers_request);
		// 得到布局组件对象并设置监听事件
		layout_new = (LinearLayout) findViewById(R.id.activity_popup_dialog_new);
		layout_deliving = (LinearLayout) findViewById(R.id.activity_popup_dialog_deliving);
		layout_completed = (LinearLayout) findViewById(R.id.activity_popup_dialog_completed);
		layout_rejected = (LinearLayout) findViewById(R.id.activity_popup_dialog_rejected);
		layout_cancled = (LinearLayout) findViewById(R.id.activity_popup_dialog_cancled);

		layout_new.setOnClickListener(this);
		layout_deliving.setOnClickListener(this);
		layout_completed.setOnClickListener(this);
		layout_rejected.setOnClickListener(this);
		layout_cancled.setOnClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		overridePendingTransition(0, R.anim.chat_slide_out_to_right);
		return true;
	}

	String title = "";
	String status = "";

	@Override
	public void onClick(View v) {
		if (v == layout_new) {
			title = getResources().getString(R.string.couriers_new);
			status = couriersRequest[0];
		}
		if (v == layout_deliving) {
			title = getResources().getString(R.string.couriers_deliving);
			status = couriersRequest[1];
		}
		if (v == layout_completed) {
			title = getResources().getString(R.string.couriers_completed);
			status = couriersRequest[2];
		}
		if (v == layout_rejected) {
			title = getResources().getString(R.string.couriers_rejected);
			status = couriersRequest[3];
		}
		if (v == layout_cancled) {
			title = getResources().getString(R.string.couriers_cancled);
			status = couriersRequest[4];
		}
		if (!title.equals("") || title != null) {

			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("TITLE", title);
			intent.putExtra("STATUS", status);
			setResult(100, intent);
		}
		finish();
		overridePendingTransition(0, R.anim.chat_slide_out_to_right);
	}
}
