package com.runye.express.utils;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.runye.express.android.R;

public class MyToast {
	public static void createToast(final Activity context, String message) {
		final View view = LayoutInflater.from(context).inflate(R.layout.mytoast, null);
		TextView textView = (TextView) view.findViewById(R.id.mytoast_message);
		textView.setText(message);
		LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		// params.topMargin = 0;
		// params.gravity = Gravity.CENTER;
		context.addContentView(view, params);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				View hiddenView = context.findViewById(R.id.mytoast_layout);
				ViewGroup group = (ViewGroup) hiddenView.getParent();
				group.removeView(hiddenView);
			}
		}, 3000);

	}
}
