package com.runye.express.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.runye.express.utils.LogUtil;

/**
 * 
 * @ClassName: OrderModeBean
 * @Description: 按订单模式的订单bean
 * @author LanJie.Chen
 * @date 2014-7-4 下午2:21:38
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */

@SuppressLint("SimpleDateFormat")
public class OrderModeBean implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
	 */

	private static final long serialVersionUID = 1L;
	private final String TAG = "OrderModeBean";
	/** 订单id */
	private String id;
	/** 订单号 */
	private String number;
	/** 买家ID */
	private String user;
	/** 订单状态 */
	private String status;
	/** 买家留言 */
	private String notes;
	/** 客服留言 */
	private String os_notes;
	/** 订单最后修改时间 */
	private String last_modified;
	private String v;
	/** 运单号 */
	private String serial_number;
	/** 评价 */
	private boolean evaluated;
	private boolean display;
	/** 折扣 */
	private String discount;
	/** 运费类型 */
	private String shippingType;
	/** 运费 */
	private String shipping;
	/** 是否立即配送 */
	private boolean immediate_delivery;
	/** 订单创建时间 */
	private String creation_date;
	/** 订单总金额 */
	private String total;
	/** 订单金额 */
	private String subtotal;
	/** 预约配送时间起点 */
	private String scheduled_delivery;
	/** 预约配送时间终点 */
	private String scheduled_delivery_end;
	/** recipient */
	private String recipient;
	private String recipient_address;
	private String recipient_city;
	private String recipient_district;
	private String recipient_street;
	private String recipient_street2;
	private String recipient_name;
	private String recipient_phone_num;
	/** location */
	/** 经度 */
	private Double location_lng;
	/** 纬度 */
	private Double location_lat;

	/** items */
	@SuppressWarnings("unused")
	private String items;
	/**
	 * merchant ID
	 */
	private String merchant;
	/** 订单商品集合 */
	private List<OrderItemsBean> itemList;

	public List<OrderItemsBean> getItemList() {
		return itemList;
	}

	public void setItems(String items) {
		this.itemList = new ArrayList<OrderItemsBean>();
		this.itemList.addAll(JSON.parseArray(items.toString().replaceAll("\"_id\"", "\"id\""), OrderItemsBean.class));
		this.items = items;
	}

	/** 店铺地址 */
	public String getRecipient_address() {
		this.recipient_address = this.recipient_city + this.recipient_district + this.recipient_street
				+ this.recipient_street2;
		return recipient_address;
	}

	public void setRecipient_address(String recipient_address) {
		this.recipient_address = recipient_address;
	}

	public String getRecipient() {

		return recipient;
	}

	/** 再解析 recipient */
	public void setRecipient(String recipient) {

		// 解析recipient
		JSONObject recipientObject = JSON.parseObject(recipient);
		this.recipient_city = recipientObject.getString("city");
		this.recipient_district = recipientObject.getString("district");
		this.recipient_street = recipientObject.getString("street");
		this.recipient_street2 = recipientObject.getString("street2");
		this.recipient_name = recipientObject.getString("name");
		this.recipient_phone_num = recipientObject.getString("phone_num");
		if (recipientObject.containsKey("location")) {
			// 解析location
			JSONObject locationObject = recipientObject.getJSONObject("location");
			this.location_lng = locationObject.getDouble("lng");
			this.location_lat = locationObject.getDouble("lat");
		} else {
			// 默认华顿的地址
			this.location_lng = 112.563597;
			this.location_lat = 37.793202;
		}
		// this.recipient = recipient;
	}

	public String getRecipient_city() {
		return recipient_city;
	}

	public void setRecipient_city(String recipient_city) {
		this.recipient_city = recipient_city;
	}

	public String getRecipient_district() {
		return recipient_district;
	}

	public void setRecipient_district(String recipient_district) {
		this.recipient_district = recipient_district;
	}

	public String getRecipient_street() {
		return recipient_street;
	}

	public void setRecipient_street(String recipient_street) {
		this.recipient_street = recipient_street;
	}

	public String getRecipient_street2() {
		return recipient_street2;
	}

	public void setRecipient_street2(String recipient_street2) {
		this.recipient_street2 = recipient_street2;
	}

	public String getRecipient_name() {
		return recipient_name;
	}

	public void setRecipient_name(String recipient_name) {
		this.recipient_name = recipient_name;
	}

	public String getRecipient_phone_num() {
		return recipient_phone_num;
	}

	public void setRecipient_phone_num(String recipient_phone_num) {
		this.recipient_phone_num = recipient_phone_num;
	}

	public Double getLocation_lng() {
		return location_lng;
	}

	public void setLocation_lng(Double location_lng) {
		this.location_lng = location_lng;
	}

	public Double getLocation_lat() {
		return location_lat;
	}

	public void setLocation_lat(Double location_lat) {
		this.location_lat = location_lat;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OrderModeBean() {
		super();
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getOs_notes() {
		return os_notes;
	}

	public void setOs_notes(String os_notes) {
		this.os_notes = os_notes;
	}

	public String getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(String last_modified) {
		this.last_modified = last_modified;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public String getShipping() {
		return shipping;
	}

	public void setShipping(String shipping) {
		this.shipping = shipping;
	}

	public String getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(String creation_date) {
		// utc时间
		SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date utcDate = null;
		try {
			// 格式化utc时间
			utcDate = utcFormat.parse(creation_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 本地时间
		SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		localFormat.setTimeZone(TimeZone.getDefault());
		this.creation_date = localFormat.format(utcDate.getTime());
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public String getId() {
		return id;
	}

	public boolean isEvaluated() {
		return evaluated;
	}

	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean isImmediate_delivery() {
		return immediate_delivery;
	}

	public void setImmediate_delivery(boolean immediate_delivery) {
		this.immediate_delivery = immediate_delivery;
	}

	public String getScheduled_delivery() {
		return scheduled_delivery;
	}

	public String getScheduled_delivery_end() {
		return scheduled_delivery_end;
	}

	public void setScheduled_delivery(String scheduled_delivery) {
		// utc时间
		SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date utcDate = null;
		try {
			// 格式化utc时间
			utcDate = utcFormat.parse(scheduled_delivery);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 本地时间
		SimpleDateFormat localFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
		localFormat.setTimeZone(TimeZone.getDefault());
		LogUtil.d(TAG, "送餐起始时间：" + localFormat.format(utcDate.getTime()));
		this.scheduled_delivery = localFormat.format(utcDate.getTime());
	}

	public void setScheduled_delivery_end(String scheduled_delivery_end) {
		// utc时间
		SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date utcDate = null;
		try {
			// 格式化utc时间
			utcDate = utcFormat.parse(scheduled_delivery_end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 本地时间
		SimpleDateFormat localFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
		localFormat.setTimeZone(TimeZone.getDefault());
		LogUtil.d(TAG, "送餐终止时间：" + localFormat.format(utcDate.getTime()));
		this.scheduled_delivery_end = localFormat.format(utcDate.getTime());
	}
}