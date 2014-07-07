package com.runye.express.bean;

/**
 * 
 * @ClassName: CouriersModeBean
 * @Description: 快递员模式bean
 * @author LanJie.Chen
 * @date 2014-7-4 下午12:22:11
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class CouriersModeBean {
	/** 快递员名称 */
	private String name;
	/** 快递员编号 */
	private String number;
	/** 快递员头像地址 */
	private String image;
	private String phone;
	private String status;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
