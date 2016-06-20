package com.qijitek.database;

public class RssiData {
	private String name;
	private String address;
	private int rssi;

	public RssiData(String name, String address, int rssi) {
		super();
		this.name = name;
		this.address = address;
		this.rssi = rssi;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

}
