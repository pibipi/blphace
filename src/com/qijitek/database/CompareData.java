package com.qijitek.database;

import java.io.Serializable;

public class CompareData implements Serializable{
	private String time;
	private String name1;
	private String imgurl1;
	private String water1;
	private String oil1;
	private String light1;
	private String name2;
	private String imgurl2;
	private String water2;
	private String oil2;
	private String light2;

	@Override
	public String toString() {
		return "CompareData [time=" + time + ", name1=" + name1 + ", imgurl1="
				+ imgurl1 + ", water1=" + water1 + ", oil1=" + oil1
				+ ", light1=" + light1 + ", name2=" + name2 + ", imgurl2="
				+ imgurl2 + ", water2=" + water2 + ", oil2=" + oil2
				+ ", light2=" + light2 + "]";
	}


	public CompareData(String time, String name1, String imgurl1,
			String water1, String oil1, String light1, String name2,
			String imgurl2, String water2, String oil2, String light2) {
		super();
		this.time = time;
		this.name1 = name1;
		this.imgurl1 = imgurl1;
		this.water1 = water1;
		this.oil1 = oil1;
		this.light1 = light1;
		this.name2 = name2;
		this.imgurl2 = imgurl2;
		this.water2 = water2;
		this.oil2 = oil2;
		this.light2 = light2;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getImgurl1() {
		return imgurl1;
	}

	public void setImgurl1(String imgurl1) {
		this.imgurl1 = imgurl1;
	}

	public String getWater1() {
		return water1;
	}

	public void setWater1(String water1) {
		this.water1 = water1;
	}

	public String getOil1() {
		return oil1;
	}

	public void setOil1(String oil1) {
		this.oil1 = oil1;
	}

	public String getLight1() {
		return light1;
	}

	public void setLight1(String light1) {
		this.light1 = light1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getImgurl2() {
		return imgurl2;
	}

	public void setImgurl2(String imgurl2) {
		this.imgurl2 = imgurl2;
	}

	public String getWater2() {
		return water2;
	}

	public void setWater2(String water2) {
		this.water2 = water2;
	}

	public String getOil2() {
		return oil2;
	}

	public void setOil2(String oil2) {
		this.oil2 = oil2;
	}

	public String getLight2() {
		return light2;
	}

	public void setLight2(String light2) {
		this.light2 = light2;
	}

}
