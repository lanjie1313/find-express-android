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
	// private final String TAG = "OrderModeBean";
	private String id;
	private String number;
	private String user;
	private String status;
	private String notes;
	private String os_notes;
	private String last_modified;
	private String v;
	private String serial_number;
	private boolean evaluated;
	private boolean display;
	private String discount;
	private String shippingType;
	private String shipping;
	private boolean immediate_delivery;
	private String creation_date;
	private String total;
	private String subtotal;
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
	private Double location_lng;
	private Double location_lat;

	/** items */
	@SuppressWarnings("unused")
	private String items;
	/**
	 * merchant
	 */
	private String merchant;

	private List<OrderItemsBean> itemList;

	public List<OrderItemsBean> getItemList() {
		return itemList;
	}

	public void setItems(String items) {
		this.itemList = new ArrayList<OrderItemsBean>();
		this.itemList.addAll(JSON.parseArray(items.toString().replaceAll("\"_id\"", "\"id\""), OrderItemsBean.class));
		this.items = items;
	}

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

	/** 再解析 */
	public void setRecipient(String recipient) {
		// 解析recipient
		JSONObject recipientObject = JSON.parseObject(recipient);
		this.recipient_city = recipientObject.getString("city");
		this.recipient_district = recipientObject.getString("district");
		this.recipient_street = recipientObject.getString("street");
		this.recipient_street2 = recipientObject.getString("street2");
		this.recipient_name = recipientObject.getString("name");
		this.recipient_phone_num = recipientObject.getString("phone_num");
		// 解析location
		JSONObject locationObject = recipientObject.getJSONObject("location");
		this.location_lng = locationObject.getDouble("lng");
		this.location_lat = locationObject.getDouble("lat");
		this.recipient = recipient;
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
}