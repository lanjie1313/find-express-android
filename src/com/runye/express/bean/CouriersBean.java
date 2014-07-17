package com.runye.express.bean;

/**
 * 
 * @ClassName: CouriersBean
 * @Description: 快递员信息
 * @author LanJie.Chen
 * @date 2014-7-17 下午5:17:27
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class CouriersBean {

	private String id;
	private String nickName;
	private String phone_num;
	private String email;
	private String siteId;
	private String status;
	private String idverifyStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status.equals("offline")) {

			this.status = "离线";
		}
		if (status.equals("online")) {
			this.status = "在线";
		}
	}

	public String getIdverifyStatus() {
		return idverifyStatus;
	}

	public void setIdverifyStatus(String idverifyStatus) {
		this.idverifyStatus = idverifyStatus;
	}
}
