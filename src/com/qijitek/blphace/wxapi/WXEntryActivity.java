package com.qijitek.blphace.wxapi;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.qijitek.blphace.MainActivity;
import com.qijitek.blphace.R;
import com.qijitek.constant.NomalConstant;
import com.qijitek.utils.MyUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private static String TAG = "WXEntryActivity";
	private IWXAPI api;
	public static BaseResp mResp = null;
	// 是否有新的认证请求
	public static boolean hasNewAuth = false;
	private Handler mh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate<-----------------------------");
		setContentView(R.layout.activity_login);
		api = WXAPIFactory.createWXAPI(this, NomalConstant.AppID, false);
		api.handleIntent(getIntent(), this);
		mh = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 2:
					Intent intent = new Intent(WXEntryActivity.this,
							MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}

		};
	}

	@Override
	public void onReq(BaseReq arg0) {
		Log.e(TAG, "onReq``");
	}

	@Override
	public void onResp(BaseResp arg0) {
		if (arg0.errCode != 0) {
			finish();
			return;
		}
		switch (arg0.getType()) {
		case 1:
			String code = ((SendAuth.Resp) arg0).code;
			code_get(code);
			Toast.makeText(getApplicationContext(), "登陆成功", 0).show();
			break;
		case 2:
			Toast.makeText(getApplicationContext(), "分享成功", 0).show();
			finish();
			break;
		default:
			break;
		}

		Log.e(TAG, "onResp``");
	}

	public void code_get(final String code) {
		new Thread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				String access_token;
				String refresh_token;
				String openid;
				String nickname;
				String url1 = "https://api.weixin.qq.com/sns/oauth2/access_token?"
						+ "appid="
						+ NomalConstant.AppID
						+ "&secret="
						+ NomalConstant.AppSecret
						+ "&code="
						+ code
						+ "&grant_type=authorization_code";
				try {
					JSONObject jsonObj1 = MyUtils.getJson(url1);
					access_token = jsonObj1.getString("access_token");
					openid = jsonObj1.getString("openid");
					refresh_token = jsonObj1.getString("refresh_token");
					System.out.println(jsonObj1.toString());

					//
					String url3 = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="
							+ NomalConstant.AppID
							+ "&grant_type=refresh_token&refresh_token="
							+ refresh_token;
					JSONObject jsonObj3 = MyUtils.getJson(url3);
					System.out.println(jsonObj3.toString());
					access_token = jsonObj3.getString("access_token");
					//
					String url4 = "https://api.weixin.qq.com/sns/auth?access_token="
							+ access_token + "&openid=" + openid;
					JSONObject jsonObj4 = MyUtils.getJson(url4);
					System.out.println(jsonObj4.toString());
					//
					String url2 = "https://api.weixin.qq.com/sns/userinfo?access_token="
							+ access_token + "&openid=" + openid;
					JSONObject jsonObj2 = MyUtils.initSSLWithHttpClinet(url2);
					System.out.println(jsonObj2.toString());
					// TODO jsonObj2为用户信息

					Message msg = new Message();
					msg.what = 2;
					mh.sendMessage(msg);

					// System.out.println(url2);
					// JSONObject jsonObj2 = MyUtils.getJson(url2);
					// nickname = jsonObj2.getString("nickname");
					// System.out.println(jsonObj2.toString());
					// System.out.println(nickname);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}