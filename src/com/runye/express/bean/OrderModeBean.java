package com.runye.express.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

public class OrderModeBean implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
	 */

	private static final long serialVersionUID = 1L;
	private final String TAG = "OrderModeBean";
	private String id;
	private String number;
	private String user;

	private String status;
	private String notes;
	private String os_notes;
	private String last_modified;
	private String v;
	private String serial_number;
	private String evaluated;
	private String display;
	private String discount;
	private String shippingType;
	private String shipping;
	private String immediate_delivery;
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
	private String recipient_location;
	/** location */
	private String location;
	private String location_lng;
	private String location_lat;

	/** items */
	private String items;
	private String items_product;
	private String items_count;
	private String items_unit_price;
	private String items_id;
	/**
	 * merchant
	 */
	private String merchant;
	private String merchant_name;
	private String merchant_phone;
	private String merchant_address;
	private List<OrderItems> itemList;

	public List<OrderItems> getItemList() {
		return itemList;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.itemList = new ArrayList<OrderItems>();
		this.itemList.addAll(JSON.parseArray(items.toString().replaceAll("\"_id\"", "\"id\""), OrderItems.class));
		this.items = items;
	}

	public String getMerchant_name() {
		return merchant_name;
	}

	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}

	public String getMerchant_phone() {
		return merchant_phone;
	}

	public void setMerchant_phone(String merchant_phone) {
		this.merchant_phone = merchant_phone;
	}

	public String getMerchant_address() {
		return merchant_address;
	}

	public void setMerchant_address(String merchant_address) {
		this.merchant_address = merchant_address;
	}

	public String getRecipient_location() {
		return recipient_location;
	}

	public void setRecipient_location(String recipient_location) {
		this.recipient_location = recipient_location;
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
		JSONObject jo = JSON.parseObject(recipient);
		this.recipient_city = jo.getString("city");
		this.recipient_district = jo.getString("district");
		this.recipient_street = jo.getString("street");
		this.recipient_street2 = jo.getString("street2");
		this.recipient_name = jo.getString("name");
		this.recipient_phone_num = jo.getString("phone_num");
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation_lng() {
		return location_lng;
	}

	public void setLocation_lng(String location_lng) {
		this.location_lng = location_lng;
	}

	public String getLocation_lat() {
		return location_lat;
	}

	public void setLocation_lat(String location_lat) {
		this.location_lat = location_lat;
	}

	public String getItems_product() {
		return items_product;
	}

	public void setItems_product(String items_product) {
		this.items_product = items_product;
	}

	public String getItems_count() {
		return items_count;
	}

	public void setItems_count(String items_count) {
		this.items_count = items_count;
	}

	public String getItems_unit_price() {
		return items_unit_price;
	}

	public void setItems_unit_price(String items_unit_price) {
		this.items_unit_price = items_unit_price;
	}

	public String getItems_id() {
		return items_id;
	}

	public void setItems_id(String items_id) {
		this.items_id = items_id;
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

	/**
	 * 
	 * @Description: 获取商户信息
	 * @param merchant
	 * @return void
	 */
	public void setMerchant(final String merchant) {
		// MyHttpClient.getMerchant(merchant, new JsonHttpResponseHandler() {
		// @Override
		// public void onSuccess(int statusCode, org.json.JSONObject response) {
		// super.onSuccess(statusCode, response);
		// LogUtil.d(TAG, "获取商户信息成功" + response.toString());
		// JSONObject jo = JSON.parseObject(response.toString());
		// merchant_name = jo.getString("company_name") + "-" +
		// jo.getString("name");
		// LogUtil.d(TAG, merchant_name);
		//
		// }
		//
		// @Override
		// public void onFailure(Throwable e, org.json.JSONObject errorResponse)
		// {
		// super.onFailure(e, errorResponse);
		// LogUtil.d(TAG, "获取商户信息失败" + errorResponse.toString());
		// }
		// });
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

	public String getEvaluated() {
		return evaluated;
	}

	public void setEvaluated(String evaluated) {
		this.evaluated = evaluated;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
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

	public String getImmediate_delivery() {
		return immediate_delivery;
	}

	public void setImmediate_delivery(String immediate_delivery) {
		this.immediate_delivery = immediate_delivery;
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

}