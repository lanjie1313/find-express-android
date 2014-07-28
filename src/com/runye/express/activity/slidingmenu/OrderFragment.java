package com.runye.express.activity.slidingmenu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runye.express.activity.master.MasterBaseActivity;
import com.runye.express.android.R;
import com.runye.express.utils.HomeButton;
import com.runye.express.utils.HomeButton.HomeClickListener;
import com.runye.express.utils.LogUtil;
import com.runye.express.utils.SysExitUtil;

/**
 * 
 * @ClassName: OrderFragment
 * @Description: 订单选择fragment
 * @author LanJie.Chen
 * @date 2014-7-25 下午3:49:30
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class OrderFragment extends Fragment {
	/**
	 * 0 、待分配订单 1、 已分配订单 2、当日处理订单 3、 以往处理订单 4、查看评价
	 * */
	private HomeButton[] buttons;
	private int[] ids;
	private final String TAG = "OrderFragment";

	public OrderFragment() {
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
		buttons = new HomeButton[ids.length];
		for (int i = 0; i < ids.length; i++) {
			buttons[i] = (HomeButton) getActivity().findViewById(ids[i]);
			buttons[i].setOnHomeClick(new MyHomeClickListener());
		}
	}

	private class MyHomeClickListener implements HomeClickListener {

		@Override
		public void onclick(View v) {
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
