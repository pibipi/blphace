package com.qijitek.database;

import java.io.Serializable;

public class UserAddress implements Serializable{
	private String userid;
	private String name;
	private String age;
	private String sex;
	private String skintype;
	private String phone;
	private String province;
	private String address;

	public UserAddress(String userid, String name, String age, String sex,
			String skintype, String phone, String province, String address) {
		super();
		this.userid = userid;
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.skintype = skintype;
		this.phone = phone;
		this.province = province;
		this.address = address;
	}

	@Override
	public String toString() {
		return "UserAddress [userid=" + userid + ", name=" + name + ", age="
				+ age + ", sex=" + sex + ", skintype=" + skintype + ", phone="
				+ phone + ", province=" + province + ", address=" + address
				+ "]";
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSkintype() {
		return skintype;
	}

	public void setSkintype(String skintype) {
		this.skintype = skintype;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
