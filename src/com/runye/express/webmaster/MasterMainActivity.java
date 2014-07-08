package com.runye.express.webmaster;

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

/**
 * 
 * @ClassName: MasterMainActivity
 * @Description: 站长主界面
 * @author LanJie.Chen
 * @date 2014-7-7 下午2:29:21
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class MasterMainActivity extends Activity {
	/**
	 * 0 、待分配订单 1、 已分配订单 2、当日处理订单 3、 以往处理订单 4、查看评价
	 * */
	private LinearLayout[] layouts;
	private int[] ids;
	private final String TAG = "MasterMainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master_main);
		initUI();
		SysExitUtil.activityList.add(MasterMainActivity.this);

	}

	private void initUI() {
		ids = new int[] { R.id.activity_master_main_noAllocation,
				R.id.activity_master_main_yesAllocation,
				R.id.activity_master_main_compeleted,
				R.id.activity_master_main_previous,
				R.id.activity_master_main_evaluate };
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
					MasterMainActivity.this);
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

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_master_main_noAllocation:
				LogUtil.d(TAG, "待分配订单 ");
				Intent noAllocation = new Intent(MasterMainActivity.this,
						MasterBaseActivity.class);
				noAllocation.putExtra("STATUS", "待分配订单");
				startActivity(noAllocation);
				break;
			case R.id.activity_master_main_yesAllocation:
				LogUtil.d(TAG, " 已分配订单");
				Intent yesAllocation = new Intent(MasterMainActivity.this,
						MasterBaseActivity.class);
				yesAllocation.putExtra("STATUS", " 已分配订单");
				startActivity(yesAllocation);
				break;
			case R.id.activity_master_main_compeleted:
				LogUtil.d(TAG, "当日处理订单");
				Intent compeleted = new Intent(MasterMainActivity.this,
						MasterBaseActivity.class);
				compeleted.putExtra("STATUS", "当日处理订单");
				startActivity(compeleted);
				break;
			case R.id.activity_master_main_previous:
				LogUtil.d(TAG, "以往处理订单");
				Intent previous = new Intent(MasterMainActivity.this,
						MasterBaseActivity.class);
				previous.putExtra("STATUS", "以往处理订单");
				startActivity(previous);
				break;
			case R.id.activity_master_main_evaluate:
				LogUtil.d(TAG, "查看评价");
				Intent evaluate = new Intent(MasterMainActivity.this,
						MasterBaseActivity.class);
				evaluate.putExtra("STATUS", "查看评价");
				startActivity(evaluate);
				break;
			default:
				break;
			}

		}
	}
}
