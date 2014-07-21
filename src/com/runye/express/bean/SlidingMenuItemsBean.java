package com.runye.express.bean;

/**
 * sliding menu bean
 * 
 * @author LanJie.Chen
 * 
 */
public class SlidingMenuItemsBean {
	/** items标题 */
	private String title;
	/** items图片 */
	private int icon;
	/** items个数 */
	private String count = "0";
	/** items tips显示标识 */
	private boolean isCounterVisible = false;

	public SlidingMenuItemsBean() {
	}

	/**
	 * 构造方法
	 * 
	 * @param title
	 *            标题
	 * @param icon
	 *            图片
	 */
	public SlidingMenuItemsBean(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	/**
	 * 构造方法
	 * 
	 * @param title
	 *            标题
	 * @param icon
	 *            图片
	 * @param isCounterVisible
	 *            tips标识
	 * @param count
	 *            tips个数
	 */
	public SlidingMenuItemsBean(String title, int icon, boolean isCounterVisible, String count) {
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}

	public String getTitle() {
		return this.title;
	}

	public int getIcon() {
		return this.icon;
	}

	public String getCount() {
		return this.count;
	}

	public boolean getCounterVisibility() {
		return this.isCounterVisible;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public void setCounterVisibility(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}
}
