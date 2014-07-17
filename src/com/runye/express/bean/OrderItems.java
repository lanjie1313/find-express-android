package com.runye.express.bean;

import java.io.Serializable;

public class OrderItems implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
	 */

	private static final long serialVersionUID = 1L;
	private String product;
	private String count;
	private String unit_price;
	private String id;

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
