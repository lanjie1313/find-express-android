package com.runye.express.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName: WayBills
 * @Description: 运单
 * @author LanJie.Chen
 * @date 2014-8-6 下午3:35:55
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class WayBills implements Serializable {
	private String id;
	private String order_id;
	private String order_number;
	private String order_serial_number;
	private String status;
	private String site_id;
	private String postman_id;
	/** 收货信息 */
	private String recipient;
	private String recipient_address;
	private String recipient_name;
	private String recipient_phone_num;
	private String recipient_city;
	private String recipient_district;
	private String recipient_street;
	private String recipient_street2;
	/** 商家 */
	private String shipper;
	private String shipper_name;
	private String shipper_phone_num;
	private String shipper_city;
	private String shipper_district;
	private String shipper_street2;

	public String getId() {
		return id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public String getOrder_number() {
		return order_number;
	}

	public String getOrder_serial_number() {
		return order_serial_number;
	}

	public String getStatus() {
		return status;
	}

	public String getSite_id() {
		return site_id;
	}

	public String getPostman_id() {
		return postman_id;
	}

	public String getRecipient() {
		return recipient;
	}

	public String getRecipient_name() {
		return recipient_name;
	}

	public String getRecipient_phone_num() {
		return recipient_phone_num;
	}

	public String getRecipient_city() {
		return recipient_city;
	}

	public String getRecipient_district() {
		return recipient_district;
	}

	public String getRecipient_street() {
		return recipient_street;
	}

	public String getRecipient_street2() {
		return recipient_street2;
	}

	public String getShipper() {
		return shipper;
	}

	public String getShipper_name() {
		return shipper_name;
	}

	public String getShipper_phone_num() {
		return shipper_phone_num;
	}

	public String getShipper_city() {
		return shipper_city;
	}

	public String getShipper_district() {
		return shipper_district;
	}

	public String getShipper_street2() {
		return shipper_street2;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public void setOrder_serial_number(String order_serial_number) {
		this.order_serial_number = order_serial_number;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public void setPostman_id(String postman_id) {
		this.postman_id = postman_id;
	}

	public void setRecipient(String recipient) {
		// 解析recipient
		JSONObject recipientObject = JSON.parseObject(recipient);
		this.recipient_city = recipientObject.getString("city");
		this.recipient_district = recipientObject.getString("district");
		this.recipient_street = recipientObject.getString("street");
		this.recipient_street2 = recipientObject.getString("street2");
		this.recipient_name = recipientObject.getString("name");
		this.recipient_phone_num = recipientObject.getString("phone_num");
	}

	public void setRecipient_name(String recipient_name) {
		this.recipient_name = recipient_name;
	}

	public void setRecipient_phone_num(String recipient_phone_num) {
		this.recipient_phone_num = recipient_phone_num;
	}

	public void setRecipient_city(String recipient_city) {
		this.recipient_city = recipient_city;
	}

	public void setRecipient_district(String recipient_district) {
		this.recipient_district = recipient_district;
	}

	public void setRecipient_street(String recipient_street) {
		this.recipient_street = recipient_street;
	}

	public void setRecipient_street2(String recipient_street2) {
		this.recipient_street2 = recipient_street2;
	}

	public void setShipper(String shipper) {
		// 解析recipient
		JSONObject recipientObject = JSON.parseObject(shipper);
		this.shipper_city = recipientObject.getString("city");
		this.shipper_district = recipientObject.getString("district");
		this.shipper_street2 = recipientObject.getString("street2");
		this.shipper_name = recipientObject.getString("name");
		this.shipper_phone_num = recipientObject.getString("phone_num");
	}

	public void setShipper_name(String shipper_name) {
		this.shipper_name = shipper_name;
	}

	public void setShipper_phone_num(String shipper_phone_num) {
		this.shipper_phone_num = shipper_phone_num;
	}

	public void setShipper_city(String shipper_city) {
		this.shipper_city = shipper_city;
	}

	public void setShipper_district(String shipper_district) {
		this.shipper_district = shipper_district;
	}

	public void setShipper_street2(String shipper_street2) {
		this.shipper_street2 = shipper_street2;
	}

	public String getRecipient_address() {
		this.recipient_address = this.recipient_city + this.recipient_district + this.recipient_street
				+ this.recipient_street2;
		return recipient_address;
	}

	public void setRecipient_address(String recipient_address) {
		this.recipient_address = recipient_address;
	}
}
