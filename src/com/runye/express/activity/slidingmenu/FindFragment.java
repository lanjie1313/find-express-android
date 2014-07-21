package com.runye.express.activity.slidingmenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.runye.express.activity.master.MasterBaseActivity;
import com.runye.express.android.R;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;

/**
 * 主页
 * 
 * @author LanJie.Chen
 * 
 */
public class FindFragment extends Fragment {
	/**
	 * 0 、待分配订单 1、 已分配订单 2、当日处理订单 3、 以往处理订单 4、查看评价
	 * */
	private LinearLayout[] layouts;
	private int[] ids;
	private final String TAG = "MasterMainActivity";

	public FindFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_master_main, container, false);
		return rootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
		SysExitUtil.activityList.add(getActivity());
	}

	private void initUI() {
		ids = new int[] { R.id.activity_master_main_noAllocation, R.id.activity_master_main_yesAllocation,
				R.id.activity_master_main_compeleted, R.id.activity_master_main_previous,
				R.id.activity_master_main_evaluate };
		layouts = new LinearLayout[ids.length];
		for (int i = 0; i < ids.length; i++) {
			layouts[i] = (LinearLayout) getActivity().findViewById(ids[i]);
			layouts[i].setOnClickListener(new MyLayoutListener());
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// Builder alertDialog = new AlertDialog.Builder(getActivity());
	// alertDialog.setMessage("确定退出？");
	// alertDialog.setPositiveButton("确定", new
	// android.content.DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// SysExitUtil.exit();
	// }
	// });
	// alertDialog.setNegativeButton("取消", new
	// android.content.DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// }
	// });
	// alertDialog.create();
	// alertDialog.show();
	//
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	private class MyLayoutListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_master_main_noAllocation:
				LogUtil.d(TAG, "待分配订单 ");
				Intent noAllocation = new Intent(getActivity(), MasterBaseActivity.class);
				noAllocation.putExtra("STATUS", "new");
				startActivity(noAllocation);
				break;
			case R.id.activity_master_main_yesAllocation:
				LogUtil.d(TAG, " 已分配订单");
				Intent yesAllocation = new Intent(getActivity(), MasterBaseActivity.class);
				yesAllocation.putExtra("STATUS", "confirmed");
				startActivity(yesAllocation);
				break;
			case R.id.activity_master_main_compeleted:
				LogUtil.d(TAG, "当日处理订单");
				Intent compeleted = new Intent(getActivity(), MasterBaseActivity.class);
				compeleted.putExtra("STATUS", "delivering");
				startActivity(compeleted);
				break;
			case R.id.activity_master_main_previous:
				LogUtil.d(TAG, "以往处理订单");
				Intent previous = new Intent(getActivity(), MasterBaseActivity.class);
				previous.putExtra("STATUS", "complete");
				startActivity(previous);
				break;
			case R.id.activity_master_main_evaluate:
				LogUtil.d(TAG, "查看评价");
				Intent evaluate = new Intent(getActivity(), MasterBaseActivity.class);
				evaluate.putExtra("STATUS", "cancelled");
				startActivity(evaluate);
				break;
			default:
				break;
			}

		}
	}
}
