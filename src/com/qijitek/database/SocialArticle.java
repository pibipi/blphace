package com.qijitek.database;

public class SocialArticle {
	private String title;
	private String summary;
	private String type;
	private String url;
	private String imgurl;

	public SocialArticle(String title, String summary, String type,
			String url, String imgurl) {
		super();
		this.title = title;
		this.summary = summary;
		this.type = type;
		this.url = url;
		this.imgurl = imgurl;
	}

	@Override
	public String toString() {
		return "Social_Article [title=" + title + ", summary=" + summary
				+ ", type=" + type + ", url=" + url + ", imgurl=" + imgurl
				+ "]";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

}
