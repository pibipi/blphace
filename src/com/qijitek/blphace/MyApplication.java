package com.qijitek.blphace;

import android.app.Application;
import android.content.Intent;
import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;

import com.qijitek.constant.NomalConstant;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MyApplication extends Application {
	public static IWXAPI api;

	@Override
	public void onCreate() {
		super.onCreate();
		api = WXAPIFactory.createWXAPI(this, NomalConstant.AppID, true);
		api.registerApp(NomalConstant.AppID);
		System.out.println("api.registerApp");
		SMSSDK.initSDK(this, "14a84aa542aa0",
				"883c5fe5356a4e513a8d3bc327be17ab");
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		JPushInterface.setLatestNotificationNumber(this, 1);
		JPushInterface.initCrashHandler(this);
	}

	public IWXAPI getApi() {
		return api;
	}

	public void setApi(IWXAPI api) {
		MyApplication.api = api;
	}

}
