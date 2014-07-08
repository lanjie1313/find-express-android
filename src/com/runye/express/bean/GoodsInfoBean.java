package com.runye.express.bean;

import java.io.Serializable;
import java.util.Map;

import android.annotation.SuppressLint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@SuppressLint("UseSparseArrays")
public class GoodsInfoBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private String images;
	/** 商品数量 */
	private String stock;
	/** 商品原价 */
	private String price;
	/** Find价格 */
	private String market_price;
	/** 商品所属商户的Id */
	private String merchant;
	/** 商品的信息描述 */
	private String description;
	/** 商品的名字 */
	private String name;
	/** 商品的净含量 */
	private String volume;
	/** 商品的品牌 */
	private String brand;
	/** 商品包装方式 */
	private String packing;
	/** 商品的生产日期 */
	private String expiry_date;
	/** 商品是否有保修 */
	private boolean has_warranty;
	/** 商品是否有发票 */
	private boolean has_receipt;
	/** 获取商品数量 */
	private boolean has_return;
	/** 记录选中操作 */
	private boolean isSelect;
	/** 商品一级菜单的名字 */
	private String categoryName;
	/** 商品二级菜单的名字 */
	private String subcategoryName;
	/** 商品三级菜单的名字 */
	private String subsubcategoryName;
	/** 商品是否上架 */
	private boolean listed;
	/** 此商品是否为本店特色 */
	private boolean featured;
	/** 商品的图片ID */
	private String images;
	/** 商品的图片地址数组 */
	private String[] images_array;
	/** 商品的id */
	private String id;
	/** 暂时不知道这是干啥的 */
	private String v;
	private Map<Integer, String> map_productsID;

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	/** 获取商品id */
	public String getId() {
		return id;
	}

	/** 设置商品id */
	public void setId(String id) {
		this.id = id;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	/** 获取商品数量 */
	public String getStock() {
		return stock;
	}

	/** 设置商品数量 */
	public void setStock(String stock) {
		this.stock = stock;
	}

	/** 获取商品原价 */
	public String getPrice() {
		return price;
	}

	/** 设置商品原价 */
	public void setPrice(String price) {
		this.price = price;
	}

	/** 获取商品现在卖的价格Find价格 */
	public String getMarket_price() {
		return market_price;
	}

	/** 设置商品现在卖的价格 Find价格 */
	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}

	/** 获取商品所属的商人 */
	public String getMerchant() {
		return merchant;
	}

	/** 设置商品所属的商人 */
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	/** 获取商品的详细描述 */
	public String getDescription() {
		return description;
	}

	/** 设置商品的详细描述 */
	public void setDescription(String description) {
		this.description = description;
	}

	/** 获取商品的名字 */
	public String getName() {
		return name;
	}

	/** 得到商品的名字 */
	public void setName(String name) {
		this.name = name;
	}

	/** 获取商品的净含量 */
	public String getVolume() {
		return volume;
	}

	/** 设置商品的净含量 */
	public void setVolume(String volume) {
		this.volume = volume;
	}

	/** 获取商品品牌 */
	public String getBrand() {
		return brand;
	}

	/** 设置商品的品牌 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/** 获取商品的包装方式 */
	public String getPacking() {
		return packing;
	}

	/** 设置商品的包装方式 */
	public void setPacking(String packing) {
		this.packing = packing;
	}

	/** 获取商品是否有保修 */
	public boolean isHas_warranty() {
		return has_warranty;
	}

	/** 设置商品是否有保修 */
	public void setHas_warranty(boolean has_warranty) {
		this.has_warranty = has_warranty;
	}

	/** 获取商品是否有发票 */
	public boolean isHas_receipt() {
		return has_receipt;
	}

	/** 设置商品是否有发票 */
	public void setHas_receipt(boolean has_receipt) {
		this.has_receipt = has_receipt;
	}

	/** 获取商品是否有退换货承诺 */
	public boolean isHas_return() {
		return has_return;
	}

	/** 设置商品是否有退换货承诺 */
	public void setHas_return(boolean has_return) {
		this.has_return = has_return;
	}

	/** 获取商品的一级菜单的名字 */
	public String getCategoryName() {
		return categoryName;
	}

	/** 设置商品的一级菜单的名字 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/** 获取商品二级菜单的名字 */
	public String getSubcategoryName() {
		return subcategoryName;
	}

	/** 设置商品二级菜单的名字 */
	public void setSubcategoryName(String subcategoryName) {
		this.subcategoryName = subcategoryName;
	}

	/** 获取商品三级菜单的名字 */
	public String getSubsubcategoryName() {
		return subsubcategoryName;
	}

	/** 设置商品三级菜单的名字 */
	public void setSubsubcategoryName(String subsubcategoryName) {
		this.subsubcategoryName = subsubcategoryName;
	}

	/** 获取商品是否上架 */
	public boolean isListed() {
		return listed;
	}

	/** 设置商品是否上架 */
	public void setListed(boolean listed) {
		this.listed = listed;
	}

	/** 获取商品是否为本店特色 */
	public boolean isFeatured() {
		return featured;
	}

	/** 设置商品是否为本店特色 */
	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	/** 获取商品的生产日期 */
	public String getExpiry_date() {
		return this.expiry_date;
	}

	/** 设置商品的生产日期 */
	public void setExpiry_date(String expiry_date) {
		if (null == expiry_date)
			this.expiry_date = "";
		else if (expiry_date.trim().length() == 0)
			this.expiry_date = "";
		else if (!expiry_date.contains("month") || !expiry_date.contains("day")
				|| !expiry_date.contains("year"))
			this.expiry_date = "";
		else {
			JSONObject jb = (JSONObject) JSON.parse(expiry_date);
			String month = jb.getString("month");
			String year = jb.getString("year");
			String day = jb.getString("day");
			String split = "-";
			StringBuffer sb = new StringBuffer();
			this.expiry_date = sb.append(year).append(split).append(month)
					.append(split).append(day).toString();
		}
	}

	//
	// /** 获取商品图片ID原始信息 */
	// public String getImages() {
	// return images;
	// }

	// /** 设置商品图片ID原始信息 */
	// public void setImages(String images) {
	// this.map_productsID = new HashMap<Integer, String>();
	// if (null == images) {
	// this.images_array = new String[] { "" };
	// } else if (images.equals("[]")) {
	// this.images_array = new String[] { "" };
	// } else {
	// images = images.replace("[", "").replace("]", "").replace("\"", "");
	// if (images.contains(",")) {
	// this.images_array = images.split(",");
	// System.out.println("商品有" + images_array.length + "张图片");
	// for (int i = 0; i < this.images_array.length; i++) {
	// this.map_productsID.put(i, this.images_array[i]);
	// this.images_array[i] = URLPathUtil.UPLOADIMGURL + "/"
	// + this.images_array[i];
	// System.out.println("商品第" + i + "张图片的地址："
	// + this.images_array[i]);
	// }
	// } else {
	// System.out.println("此商品只有一张图片");
	// this.map_productsID.put(0, images);
	// this.images_array = new String[] { URLPathUtil.UPLOADIMGURL
	// + "/" + images };
	// System.out.println("商品的图片地址：" + this.images_array[0]);
	// }
	// }
	// this.images = images;
	// }
	//
	// /** 获取商品图片地址数组 */
	// public String[] getImages_array() {
	// return images_array;
	// }
	//
	// /** 设置商品图片地址数组 */
	// public void setImages_array(String[] images_array) {
	// this.images_array = images_array;
	// }

	/** 获取保存有商品图片ID的Map */
	public Map<Integer, String> getMap_productsID() {
		return map_productsID;
	}

	/** 设置保存商品图片ID的Map */
	public void setMap_productsID(Map<Integer, String> map_productsID) {
		this.map_productsID = map_productsID;
	}
}
