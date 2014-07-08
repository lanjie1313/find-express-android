package com.runye.express.activity.couriers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.runye.express.android.R;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;

public class CouriersManActivity extends Activity {
	/**
	 * 0 、待配送订单 1、 当日处理订单 2、 以往处理订单 3、查看评价
	 * */
	private LinearLayout[] layouts;
	private int[] ids;
	private final String TAG = "CouriersManActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_couriers_main);
		initUI();
		SysExitUtil.activityList.add(CouriersManActivity.this);
	}

	private void initUI() {
		ids = new int[] { R.id.activity_couriers_main_noAllocation,
				R.id.activity_couriers_main_compeleted,
				R.id.activity_couriers_main_previous,
				R.id.activity_couriers_main_evaluate };
		layouts = new LinearLayout[ids.length];
		for (int i = 0; i < ids.length; i++) {
			layouts[i] = (LinearLayout) findViewById(ids[i]);
			layouts[i].setOnClickListener(new MyLayoutListener());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Builder alertDialog = new AlertDialog.Builder(
					CouriersManActivity.this);
			alertDialog.setMessage("确定退出？");
			alertDialog.setPositiveButton("确定",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SysExitUtil.exit();
						}
					});
			alertDialog.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			alertDialog.create();
			alertDialog.show();

		}
		return super.onKeyDown(keyCode, event);
	}

	private class MyLayoutListener implements OnClickListener {
		// /
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_couriers_main_noAllocation:
				LogUtil.d(TAG, "待配送订单");
				Intent noAllocation = new Intent(CouriersManActivity.this,
						CouriersBaseActivity.class);
				noAllocation.putExtra("STATUS", "待配送订单");
				startActivity(noAllocation);
				break;
			case R.id.activity_couriers_main_compeleted:
				LogUtil.d(TAG, " 当日已完成订单");
				Intent compeleted = new Intent(CouriersManActivity.this,
						CouriersBaseActivity.class);
				compeleted.putExtra("STATUS", " 当日已完成订单");
				startActivity(compeleted);
				break;
			case R.id.activity_couriers_main_previous:
				LogUtil.d(TAG, "以往订单管理");
				Intent previou = new Intent(CouriersManActivity.this,
						CouriersBaseActivity.class);
				previou.putExtra("STATUS", "以往订单管理");
				startActivity(previou);
				break;
			case R.id.activity_couriers_main_evaluate:
				LogUtil.d(TAG, "查看评价");
				Intent evaluate = new Intent(CouriersManActivity.this,
						CouriersBaseActivity.class);
				evaluate.putExtra("STATUS", "查看评价");
				startActivity(evaluate);
				break;
			default:
				break;
			}

		}
	}
}
