package com.runye.express.bean;

/**
 * 
 * @ClassName: SiteBean
 * @Description: 站点
 * @author LanJie.Chen
 * @date 2014-7-4 下午2:59:22
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class SiteBean {
	/** 名称 */
	private String name;

	/** id */
	private String id;
	private String v;
	/** 范围 */
	/**
	 * range": [{"lng": 120.0000, "lat": 32.0000}, { "lng": 112.0000, "lat
	 * ": 30.000 }, {"lng": 120.0000,"lat": 32.000}]
	 */
	private String range;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

}
