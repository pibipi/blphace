package com.qijitek.database;

public class SearchItem {
	private String userid;
	private String itemtype;
	private String name;
	private String pid;
	private String imgurl;
	private String feature;
	private String alias;
	private String brand;
	private String methods;
	private String time;
	private String price;
	private String spec;


/**
 * 搜索列表
 * @param userid
 * @param itemtype
 * @param name
 * @param pid
 * @param imgurl
 * @param price
 * @param spec
 */
	public SearchItem(String userid, String itemtype, String name, String pid,
		String imgurl, String price, String spec) {
	super();
	this.userid = userid;
	this.itemtype = itemtype;
	this.name = name;
	this.pid = pid;
	this.imgurl = imgurl;
	this.price = price;
	this.spec = spec;
}

	@Override
	public String toString() {
		return "SearchItem [userid=" + userid + ", itemtype=" + itemtype
				+ ", name=" + name + ", pid=" + pid + ", imgurl=" + imgurl
				+ ", feature=" + feature + ", alias=" + alias + ", brand="
				+ brand + ", methods=" + methods + ", time=" + time
				+ ", price=" + price + ", spec=" + spec + "]";
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

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
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

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
