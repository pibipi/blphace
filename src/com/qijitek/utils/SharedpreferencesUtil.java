package com.qijitek.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedpreferencesUtil {
	private Context context;
	private String NAME = "FACEFACEFACE";
	private String MyDeviceMac = "MYDEVICEMAC";
	private String ConnectState = "CONNECTSTATE";
	private String AUTOBLE = "AUTOBLE";
	private String LASTSINGLERESULT = "LASTSINGLERESULT";

	public SharedpreferencesUtil(Context context) {
		this.context = context;
	}

	/**
	 * 存储用户绑定成功的设备的mac地址
	 * 
	 * @author kist
	 * @param mac
	 */
	public void saveMyDeviceMac(String mac) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(MyDeviceMac, mac);
		editor.commit();
	}

	/**
	 * 从sharedPreferences里把绑定的设备地址拿出来
	 * 
	 * @author kist
	 * @return 返回保存的设备mac地址
	 */
	public String getMyDeviceMac() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		String mac = preferences.getString(MyDeviceMac, "");
		return mac;

	}

	/**
	 * 保存当前连接状态
	 * 
	 * @author kist
	 * @param state
	 */
	public void setConnectState(boolean state) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(ConnectState, state);
		editor.commit();
	}

	/**
	 * 取出当前登录状态
	 * 
	 * @author kist
	 * @return
	 */
	public boolean getConnectState() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		boolean state = preferences.getBoolean(ConnectState, false);
		return state;
	}

	/**
	 * 保存是否自动打开蓝牙
	 * 
	 * @param state
	 */
	public void setAutoBle(boolean state) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(AUTOBLE, state);
		editor.commit();
	}

	/**
	 * 获取是否自动打开蓝牙
	 * 
	 */
	public boolean getAutoBle() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		boolean state = preferences.getBoolean(AUTOBLE, false);
		return state;
	}

	/**
	 * 
	 */
	public void setLastSingleResult(String[] values) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		String str = "";
		String regularEx = "#";
		if (values != null && values.length > 0) {
			for (String value : values) {
				str += value;
				str += regularEx;
			}
			editor.putString(LASTSINGLERESULT, str);

		}
		editor.commit();
	}

	public String[] getLastSingleResult() {
		String regularEx = "#";
		String[] str = null;
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		String values;
		values = preferences.getString(LASTSINGLERESULT, "60#60#60#60");
		str = values.split(regularEx);
		return str;

	}

	public void saveSumScore(int sum) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("SumScore", sum);
		editor.commit();
	}

	public int getSumScore() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		int sum = preferences.getInt("SumScore", 8);
		return sum;
	}

	public void saveIsLogin(boolean isLogin) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isLogin", isLogin);
		editor.commit();
	}

	public boolean getIsLogin() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		boolean isLogin = preferences.getBoolean("isLogin", false);
		return isLogin;
	}

	public void saveUserid(String userid) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("userid", userid);
		editor.commit();
	}

	public String getUserid() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		String userid = preferences.getString("userid", "");
		return userid;
	}

	/**
	 * 保存肌肤类型 1为干性，2为混合型，3为油
	 * 
	 * @param type
	 */
	public void saveSkintype(String type) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("type", type);
		editor.commit();
	}

	public String getSkintype() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		String type = preferences.getString("type", "0");
		return type;
	}

	public String getSkintypeStr() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		String type = preferences.getString("type", "0");
		int i = Integer.valueOf(type);
		String str = "";
		switch (i) {
		case 1:
			str = "干性";
			break;
		case 2:
			str = "混合性";
			break;
		case 3:
			str = "油性";
			break;

		default:
			break;
		}
		return str;
	}

	public void saveIsTest(boolean isTest) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isTest", isTest);
		editor.commit();
	}

	public boolean getIsTest() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Boolean isTesting = preferences.getBoolean("isTest", false);
		return isTesting;
	}

	public void saveIsTesting(boolean isTesting) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isTesting", isTesting);
		editor.commit();
	}

	public boolean getIsTesting() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Boolean isTesting = preferences.getBoolean("isTesting", true);
		return isTesting;
	}

	public void saveIsFirst1(boolean isFirst) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirst1", isFirst);
		editor.commit();
	}

	public boolean getIsFirst1() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Boolean isFirst = preferences.getBoolean("isFirst1", true);
		return isFirst;
	}

	public void saveIsFirst2(boolean isFirst) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirst2", isFirst);
		editor.commit();
	}

	public boolean getIsFirst2() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Boolean isFirst = preferences.getBoolean("isFirst2", true);
		return isFirst;
	}

	public void saveIsFirst3(boolean isFirst) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirst3", isFirst);
		editor.commit();
	}

	public boolean getIsFirst3() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Boolean isFirst = preferences.getBoolean("isFirst3", true);
		return isFirst;
	}

	public void saveItemtype(String itemtype) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("itemtype", itemtype);
		editor.commit();
	}

	public String getItemtype() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		String itemtype = preferences.getString("itemtype", "");
		return itemtype;
	}
}
