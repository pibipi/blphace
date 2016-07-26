package com.qijitek.database;

import java.io.Serializable;

public class SingleItem implements Serializable{
	private String name;
	private String imgurl;
	private String code;
	private String water;
	private String oil;
	private String light;
	private String itemtype;
	private String qid;
	private String alias;
	private String brand;
	private String methods;
	private String feature;
	private String price;
	private String spec;
	private boolean isSelect;
	
	
/**
 * 选择对比化妆品的Dialog
 * @param name
 * @param imgurl
 * @param code
 * @param itemtype
 * @param qid
 * @param isSelector
 */
	
public SingleItem(String name, String imgurl, String code, String itemtype,
			String qid, boolean isSelector) {
		super();
		this.name = name;
		this.imgurl = imgurl;
		this.code = code;
		this.itemtype = itemtype;
		this.qid = qid;
		this.isSelect = isSelect;
	}

/**
 * 扫码上传对象
 * @param name
 * @param imgurl
 * @param code
 * @param itemtype
 * @param alias
 * @param brand
 * @param methods
 * @param feature
 */
public SingleItem(String name, String imgurl, String code, String itemtype,
			String alias, String brand, String methods, String feature) {
		super();
		this.name = name;
		this.imgurl = imgurl;
		this.code = code;
		this.itemtype = itemtype;
		this.alias = alias;
		this.brand = brand;
		this.methods = methods;
		this.feature = feature;
	}

/**
 * 搜索列表
 * @param name
 * @param imgurl
 * @param code
 * @param itemtype
 * @param alias
 * @param brand
 * @param methods
 * @param feature
 * @param price
 * @param spec
 */
	public SingleItem(String name, String imgurl, String code, String itemtype,
			String alias, String brand, String methods, String feature,
			String price, String spec) {
		super();
		this.name = name;
		this.imgurl = imgurl;
		this.code = code;
		this.itemtype = itemtype;
		this.alias = alias;
		this.brand = brand;
		this.methods = methods;
		this.feature = feature;
		this.price = price;
		this.spec = spec;
	}

/**
 * b21列表
 * @param name
 * @param imgurl
 * @param code
 * @param water
 * @param oil
 * @param light
 * @param itemtype
 * @param qid
 * @param alias
 * @param brand
 * @param methods
 * @param feature
 */
	public SingleItem(String name, String imgurl, String code, String water,
			String oil, String light, String itemtype, String qid,
			String alias, String brand, String methods, String feature) {
		super();
		this.name = name;
		this.imgurl = imgurl;
		this.code = code;
		this.water = water;
		this.oil = oil;
		this.light = light;
		this.itemtype = itemtype;
		this.qid = qid;
		this.alias = alias;
		this.brand = brand;
		this.methods = methods;
		this.feature = feature;
	}


	@Override
public String toString() {
	return "SingleItem [name=" + name + ", imgurl=" + imgurl + ", code=" + code
			+ ", water=" + water + ", oil=" + oil + ", light=" + light
			+ ", itemtype=" + itemtype + ", qid=" + qid + ", alias=" + alias
			+ ", brand=" + brand + ", methods=" + methods + ", feature="
			+ feature + ", price=" + price + ", spec=" + spec + ", isSelect="
			+ isSelect + "]";
}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}



	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getWater() {
		return water;
	}

	public void setWater(String water) {
		this.water = water;
	}

	public String getOil() {
		return oil;
	}

	public void setOil(String oil) {
		this.oil = oil;
	}

	public String getLight() {
		return light;
	}

	public void setLight(String light) {
		this.light = light;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
