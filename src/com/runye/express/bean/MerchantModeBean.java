package com.runye.express.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MerchantModeBean {

	private String avatar;
	private String banner;
	private String company_name;
	private String email;
	private String expiration_date;
	private String firm_name;
	private String license_img;
	private String license_location;
	private String marketing_code;
	private String multipleShop_id;
	private String name;
	private String phone_num;
	private String urgency_phone_num;
	private String status;
	private String description;
	private String summary;
	private String creation_date;

	private boolean display;
	private boolean hasProducts;
	private boolean isMultipleShop;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getExpiration_date() {
		return expiration_date;
	}

	public void setExpiration_date(String expiration_date) {
		this.expiration_date = expiration_date;
	}

	public String getFirm_name() {
		return firm_name;
	}

	public void setFirm_name(String firm_name) {
		this.firm_name = firm_name;
	}

	public String getLicense_img() {
		return license_img;
	}

	public void setLicense_img(String license_img) {
		this.license_img = license_img;
	}

	public String getLicense_location() {
		return license_location;
	}

	public void setLicense_location(String license_location) {
		this.license_location = license_location;
	}

	public String getMarketing_code() {
		return marketing_code;
	}

	public void setMarketing_code(String marketing_code) {
		this.marketing_code = marketing_code;
	}

	public String getMultipleShop_id() {
		return multipleShop_id;
	}

	public void setMultipleShop_id(String multipleShop_id) {
		this.multipleShop_id = multipleShop_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}

	public String getUrgency_phone_num() {
		return urgency_phone_num;
	}

	public void setUrgency_phone_num(String urgency_phone_num) {
		this.urgency_phone_num = urgency_phone_num;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean isHasProducts() {
		return hasProducts;
	}

	public void setHasProducts(boolean hasProducts) {
		this.hasProducts = hasProducts;
	}

	public boolean isMultipleShop() {
		return isMultipleShop;
	}

	public void setMultipleShop(boolean isMultipleShop) {
		this.isMultipleShop = isMultipleShop;
	}

	public boolean isReceiveOrder() {
		return receiveOrder;
	}

	public void setReceiveOrder(boolean receiveOrder) {
		this.receiveOrder = receiveOrder;
	}

	public boolean isShippingFeeFree() {
		return shippingFeeFree;
	}

	public void setShippingFeeFree(boolean shippingFeeFree) {
		this.shippingFeeFree = shippingFeeFree;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		JSONObject jo = JSON.parseObject(address);
		this.address_city = jo.getString("city");
		this.address_district = jo.getString("district");
		this.address_street2 = jo.getString("street2");
		this.address = jo.getString("city") + jo.getString("district") + jo.getString("street2");
	}

	public String getAddress_city() {
		return address_city;
	}

	public void setAddress_city(String address_city) {
		this.address_city = address_city;
	}

	public String getAddress_district() {
		return address_district;
	}

	public void setAddress_district(String address_district) {
		this.address_district = address_district;
	}

	public String getAddress_street2() {
		return address_street2;
	}

	public void setAddress_street2(String address_street2) {
		this.address_street2 = address_street2;
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

	private boolean receiveOrder;
	private boolean shippingFeeFree;
	private boolean featured;

	/** 地址 */
	private String address;
	private String address_city;
	private String address_district;
	private String address_street2;

	/** 经纬度 */

	private String location;
	private String location_lng;
	private String location_lat;

}
