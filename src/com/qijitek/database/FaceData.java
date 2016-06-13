package com.qijitek.database;

public class FaceData {
	private int water;
	private int oil;
	private int light;
	private int uniform;
	private int average;
	private long time;

	public FaceData() {
		super();
	}

	public FaceData(int water, int oil, int light, int average) {
		super();
		this.water = water;
		this.oil = oil;
		this.light = light;
		this.average = average;
	}

	public FaceData(int water, int oil, int light, int average,long time) {
		super();
		this.water = water;
		this.oil = oil;
		this.light = light;
		this.average = average;
		this.time=time;
	}

	public FaceData(int water, int oil, int light, int uniform, int average,long time) {
		super();
		this.water = water;
		this.oil = oil;
		this.light = light;
		this.uniform = uniform;
		this.average = average;
		this.time=time;
	}

	@Override
	public String toString() {
		return "FaceData [water=" + water + ", oil=" + oil + ", light=" + light + ", uniform=" + uniform + ", average="
				+ average + ", time=" + time + "]";
	}

	public long getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getLight() {
		return light;
	}

	public void setLight(int light) {
		this.light = light;
	}

	public int getUniform() {
		return uniform;
	}

	public void setUniform(int uniform) {
		this.uniform = uniform;
	}


	public int getWater() {
		return water;
	}

	public void setWater(int water) {
		this.water = water;
	}

	public int getOil() {
		return oil;
	}

	public void setOil(int oil) {
		this.oil = oil;
	}

	public int getAverage() {
		return average;
	}

	public void setAverage(int average) {
		this.average = average;
	}

}
