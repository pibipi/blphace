package com.qijitek.database;

public class ApplyDemo {
	private String code;
	private String name;
	private String people;
	private String imageurl;
	private String content;
	//小样申请截止时间
	private String time;
	//小样总数
	private String total;
	//是否申请过
	private String isapply;


	public ApplyDemo(String code, String name, String people, String imageurl,
			String content, String time, String total, String isapply) {
		super();
		this.code = code;
		this.name = name;
		this.people = people;
		this.imageurl = imageurl;
		this.content = content;
		this.time = time;
		this.total = total;
		this.isapply = isapply;
	}

	@Override
	public String toString() {
		return "ApplyDemo [code=" + code + ", name=" + name + ", people="
				+ people + ", imageurl=" + imageurl + ", content=" + content
				+ ", time=" + time + ", total=" + total + ", isapply="
				+ isapply + "]";
	}

	public String getIsapply() {
		return isapply;
	}

	public void setIsapply(String isapply) {
		this.isapply = isapply;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
