package com.runye.express.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;

/**
 * 继承MapView重写onTouchEvent实现泡泡处理操作
 * 
 * @author LanJie.Chen
 * 
 */
public class MyLocationMapView extends MapView {
	public static PopupOverlay locPop = null;// 弹出泡泡图层，点击图标使用

	public MyLocationMapView(Context context) {
		super(context);
	}

	public MyLocationMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLocationMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!super.onTouchEvent(event)) {
			// 消隐泡泡
			if (locPop != null && event.getAction() == MotionEvent.ACTION_UP)
				locPop.hidePop();
		}
		return true;
	}
}