package com.runye.express.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.runye.express.utils.LogUtil;

/**
 * 
 * @ClassName: ProductsBean
 * @Description: 商品信息
 * @author LanJie.Chen
 * @date 2014-7-16 上午10:29:47
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class OrderProductsBean implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
	 */

	private static final long serialVersionUID = 1L;
	private final String TAG = "ProductsBean";
	private String id;
	private String v;
	private String brand;
	private String categoryName;
	private String description;
	private String market_price;
	private String merchant;
	private String name;
	private String packing;
	private String price;
	private String stock;
	private String subcategoryName;
	private String subsubcategoryName;
	private String status;
	private String marquees;
	private String images;
	private String sales;

	private boolean has_receipt;
	private boolean has_return;
	private boolean has_warranty;
	private boolean recommend;
	private boolean featured;
	private boolean listed;

	private List<String> imagesID;

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

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMarket_price() {
		return market_price;
	}

	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPacking() {
		return packing;
	}

	public void setPacking(String packing) {
		this.packing = packing;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getSubcategoryName() {
		return subcategoryName;
	}

	public void setSubcategoryName(String subcategoryName) {
		this.subcategoryName = subcategoryName;
	}

	public String getSubsubcategoryName() {
		return subsubcategoryName;
	}

	public void setSubsubcategoryName(String subsubcategoryName) {
		this.subsubcategoryName = subsubcategoryName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMarquees() {
		return marquees;
	}

	public void setMarquees(String marquees) {
		this.marquees = marquees;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		JSONArray array = JSON.parseArray(images);
		imagesID = new ArrayList<String>();
		for (int i = 0; i < array.size(); i++) {
			LogUtil.d(TAG, array.get(i) + "");
		}

		this.images = images;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public boolean isHas_receipt() {
		return has_receipt;
	}

	public void setHas_receipt(boolean has_receipt) {
		this.has_receipt = has_receipt;
	}

	public boolean isHas_return() {
		return has_return;
	}

	public void setHas_return(boolean has_return) {
		this.has_return = has_return;
	}

	public boolean isHas_warranty() {
		return has_warranty;
	}

	public void setHas_warranty(boolean has_warranty) {
		this.has_warranty = has_warranty;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	public boolean isListed() {
		return listed;
	}

	public void setListed(boolean listed) {
		this.listed = listed;
	}

	public List<String> getImagesID() {
		return imagesID;
	}

	public void setImagesID(List<String> imagesID) {
		this.imagesID = imagesID;
	}
}
