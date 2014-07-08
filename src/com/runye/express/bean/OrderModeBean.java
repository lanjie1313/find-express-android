package com.runye.express.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @ClassName: OrderModeBean
 * @Description: 按订单模式的订单bean
 * @author LanJie.Chen
 * @date 2014-7-4 下午2:21:38
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class OrderModeBean implements Parcelable {
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
	/** 商品名称 */
	private String goodsName;
	/** 商店地址 */
	private String shopAddress;

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	/** 订单状态 */
	private String status;

	/** 留言 */
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public OrderModeBean() {
	}

	public OrderModeBean(String number, String time, String charge,
			String shopName, String address, String rating,
			String couriersNumber, String couriersName, String goodsName,
			String status, String shopAddress, String message) {
		this.number = number;
		this.time = time;
		this.charge = charge;
		this.shopName = shopName;
		this.address = address;
		this.rating = rating;
		this.couriersNumber = couriersNumber;
		this.couriersName = couriersName;
		this.goodsName = goodsName;
		this.status = status;
		this.shopAddress = shopAddress;
		this.message = message;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// 该方法将类的数据写入外部提供的Parcel中。
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(number);
		dest.writeString(time);
		dest.writeString(charge);
		dest.writeString(shopName);
		dest.writeString(address);
		dest.writeString(rating);
		dest.writeString(couriersNumber);
		dest.writeString(couriersName);
		dest.writeString(goodsName);
		dest.writeString(status);
		dest.writeString(shopAddress);
		dest.writeString(message);
	}

	public static final Parcelable.Creator<OrderModeBean> CREATOR = new Creator<OrderModeBean>() {
		// 实现从source中创建出类的实例的功能
		@Override
		public OrderModeBean createFromParcel(Parcel source) {
			OrderModeBean bean = new OrderModeBean();
			bean.number = source.readString();
			bean.time = source.readString();
			bean.charge = source.readString();
			bean.shopName = source.readString();
			bean.address = source.readString();
			bean.rating = source.readString();
			bean.couriersNumber = source.readString();
			bean.couriersName = source.readString();
			bean.goodsName = source.readString();
			bean.status = source.readString();
			bean.shopAddress = source.readString();
			bean.message = source.readString();
			return bean;
		}

		// 创建一个类型为T，长度为size的数组
		@Override
		public OrderModeBean[] newArray(int size) {
			return new OrderModeBean[size];
		}
	};

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
