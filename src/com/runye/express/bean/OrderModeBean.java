package com.runye.express.bean;

/**
 * 
 * @ClassName: OrderModeBean
 * @Description: 按订单模式的订单bean
 * @author LanJie.Chen
 * @date 2014-7-4 下午2:21:38
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class OrderModeBean {
	/** 订单号 */
	private String number;
	/** 订单时间 */
	private String time;
	/** 订单价格 */
	private String charge;
	/** 店铺名称 */
	private String shopName;
	/** 收货地址 */
	private String address;
	/** 评价 */
	private String rating;
	/** 快递员编号 */
	private String couriersNumber;
	/** 快递员名称 */
	private String couriersName;
	private String goodsName;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	private String goodsPrice;

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	private String goodsNumber;

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getCouriersNumber() {
		return couriersNumber;
	}

	public void setCouriersNumber(String couriersNumber) {
		this.couriersNumber = couriersNumber;
	}

	public String getCouriersName() {
		return couriersName;
	}

	public void setCouriersName(String couriersName) {
		this.couriersName = couriersName;
	}

}
