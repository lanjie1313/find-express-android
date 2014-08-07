package com.runye.express.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.runye.express.android.R;

public class LoadingDialog extends Dialog {
	String message = "";

	// int width = 0;
	// int height = 0;
	// Context context;

	public LoadingDialog(Context context, String message) {
		super(context, R.style.MyDialogStyle);
		this.message = message;
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		TextView textView = (TextView) findViewById(R.id.loading_message);
		textView.setText(message);
		// // RelativeLayout layout = (RelativeLayout)
		// // findViewById(R.id.loading_layout);
		// // layout.setLayoutParams(new LayoutParams(width, height));
		// Window window = getWindow();
		// WindowManager.LayoutParams params = window.getAttributes();
		// // set width,height by density and gravity
		// float density = getDensity(context);
		// params.width = (int) (width * density);
		// params.height = (int) (height * density);

	}

	// private float getDensity(Context context) {
	// Resources resources = context.getResources();
	// DisplayMetrics dm = resources.getDisplayMetrics();
	// return dm.density;
	// }
}