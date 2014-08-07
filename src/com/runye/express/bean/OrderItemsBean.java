package com.runye.express.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: OrderItems
 * @Description: 订单商品清单信息
 * @author LanJie.Chen
 * @date 2014-7-18 下午3:27:46
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class OrderItemsBean implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
	 */

	private static final long serialVersionUID = 1L;
	/** 商品id */
	private String product;
	/** 商品数量 */
	private String count;
	/** 商品单价 */
	private String unit_price;
	/**  */
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
