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
}
