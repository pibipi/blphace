package com.qijitek.blphace;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.qijitek.constant.WeiboConstant;
import com.qijitek.utils.CountDownButtonHelper;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.qijitek.view.ProgersssDialog;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;

public class LoginActivity extends Activity implements OnClickListener {
	private static final String TAG = "LoginActivity";
	private IWXAPI api;
	private EventHandler eh;
	private Button send;
	private EditText phone;
	private EditText code;
	private Handler mHandler;
	//
	private AuthInfo mAuthInfo;
	private SsoHandler mSsoHandler;
	private Oauth2AccessToken mAccessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		Log.e(TAG, "onCreate");
		initLogin();
		mAuthInfo = new AuthInfo(getApplicationContext(),
				WeiboConstant.APP_KEY, WeiboConstant.REDIRECT_URL,
				WeiboConstant.SCOPE);
		mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);

	}

	private void initLogin() {
		eh = new EventHandler() {

			@Override
			public void afterEvent(int event, int result, Object data) {

				if (result == SMSSDK.RESULT_COMPLETE) {
					// 回调完成
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						System.out.println("succ````````````````");
						mHandler.sendEmptyMessage(2);
						// 提交验证码成功
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						// 获取验证码成功
						mHandler.sendEmptyMessage(1);
					} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
						// 返回支持发送验证码的国家列表
					}
				} else {
					((Throwable) data).printStackTrace();
				}
			}
		};
		SMSSDK.registerEventHandler(eh); // 注册短信回调
	}

	private void init() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					Toast.makeText(getApplicationContext(), "验证码已经发送",
							Toast.LENGTH_SHORT).show();
					break;
				case 2:
					final ProgersssDialog dialog = new ProgersssDialog(
							LoginActivity.this, "");
					new SharedpreferencesUtil(getApplicationContext())
							.saveUserid(phone.getText().toString().trim());
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								JSONObject jsonObject;
								try {
									jsonObject = MyUtils
											.getJson("http://api.qijitek.com/regist/?userid="
													+ phone.getText()
															.toString().trim()
													+ "&login_type=1&platform=1");
									System.out.println(jsonObject.toString()
											+ "jsonObject");
									new SharedpreferencesUtil(
											getApplicationContext())
											.saveIsLogin(true);
									mHandler.postDelayed(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(
													getApplicationContext(),
													"登陆成功", Toast.LENGTH_SHORT)
													.show();
											dialog.dismiss();
											startActivity(new Intent(
													LoginActivity.this,
													MainActivity.class));
											finish();
										}
									}, 1000);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								// String s=
								// MyUtils.getOkHttp("http://api.qijitek.com/regist/?userid="
								// + phone.getText().toString().trim()
								// + "&login_type=1&platform=1");
							} catch (IOException e) {
								mHandler.post(new Runnable() {

									@Override
									public void run() {
										Toast.makeText(getApplicationContext(),
												"请检查网络连接", 0).show();
									}
								});
								e.printStackTrace();
							}
						}
					}).start();

					break;
				case 3:
					final String uid = (String) msg.obj;
					final ProgersssDialog dialog2 = new ProgersssDialog(
							LoginActivity.this, "");
					new SharedpreferencesUtil(getApplicationContext())
							.saveUserid(uid);
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								JSONObject jsonObject;
								try {
									jsonObject = MyUtils
											.getJson("http://api.qijitek.com/regist/?userid="
													+ uid
													+ "&login_type=3&platform=1");
									System.out.println(jsonObject.toString()
											+ "jsonObject");
									new SharedpreferencesUtil(
											getApplicationContext())
											.saveIsLogin(true);
									mHandler.postDelayed(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(
													getApplicationContext(),
													"登陆成功", Toast.LENGTH_SHORT)
													.show();
											dialog2.dismiss();
											startActivity(new Intent(
													LoginActivity.this,
													MainActivity.class));
											finish();
										}
									}, 1000);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								// String s=
								// MyUtils.getOkHttp("http://api.qijitek.com/regist/?userid="
								// + phone.getText().toString().trim()
								// + "&login_type=1&platform=1");
							} catch (IOException e) {
								mHandler.post(new Runnable() {

									@Override
									public void run() {
										Toast.makeText(getApplicationContext(),
												"请检查网络连接", 0).show();
									}
								});
								e.printStackTrace();
							}
						}
					}).start();
					break;
				default:
					break;
				}
			}

		};
		phone = (EditText) findViewById(R.id.phone);
		code = (EditText) findViewById(R.id.code);
		send = (Button) findViewById(R.id.send);
		send.setOnClickListener(this);
		MyApplication app = (MyApplication) getApplication();
		api = (IWXAPI) app.getApi();
		// api = WXAPIFactory.createWXAPI(this, NomalConstant.AppID, true);
		// api.registerApp(NomalConstant.AppID);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			SMSSDK.submitVerificationCode("86", phone.getText().toString()
					.trim(), code.getText().toString().trim());
			// startActivity(new Intent(LoginActivity.this,
			// MainActivity.class));
			// finish();
			break;
		case R.id.wechat_login:
			if (!api.isWXAppInstalled()) {
				Toast.makeText(getApplicationContext(), "请先安装微信应用",
						Toast.LENGTH_SHORT).show();
			} else if (!api.isWXAppSupportAPI()) {
				Toast.makeText(getApplicationContext(), "请先更新微信应用",
						Toast.LENGTH_SHORT).show();
			} else {
				final SendAuth.Req req = new SendAuth.Req();
				req.scope = "snsapi_userinfo";
				req.state = "wechat_sdk_demo_test";
				api.sendReq(req);
				this.finish();
			}
			break;
		case R.id.send:
			code.requestFocus();
			String phone_str = phone.getText().toString().trim();
			if (phone_str.equals("") && !MyUtils.checkPhoneNumber(phone_str)) {
				Toast.makeText(getApplicationContext(), "请输入正确手机号", 0).show();
			} else {
				send.setClickable(false);
				CountDownButtonHelper helper = new CountDownButtonHelper(send,
						"已发送", 60, 1);
				send.setTextColor(0xff727272);
				helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
					@Override
					public void finish() {
						send.setText("发送验证码");
						send.setClickable(true);
						send.setTextColor(0xff393939);
					}
				});
				helper.start();
				//
				// SMSSDK.getSupportedCountries();
				SMSSDK.getVerificationCode("86", phone_str);
			}
			break;
		case R.id.weibo_login:
			// startActivity(new Intent(LoginActivity.this,
			// MainActivity.class));
			// finish();
			mSsoHandler.authorize(new AuthListener());
			break;
		default:
			break;
		}
	}

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			// 从这里获取用户输入的 电话号码信息
			String phoneNum = mAccessToken.getPhoneNum();
			if (mAccessToken.isSessionValid()) {

				// 保存 Token 到 SharedPreferences
				// AccessTokenKeeper.writeAccessToken(LoginActivity.this,
				// mAccessToken);
//				Toast.makeText(LoginActivity.this, "success",
//						Toast.LENGTH_SHORT).show();
				String uid = mAccessToken.getUid();
				System.out.println(uid + "```");
				Message msg = new Message();
				msg.what = 3;
				msg.obj = uid;
				mHandler.sendMessage(msg);
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = "failed";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(LoginActivity.this, "微博授权失败", Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onCancel() {

		}

		@Override
		public void onWeiboException(WeiboException arg0) {

		}
	}

	/**
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 * 
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e(TAG, "onPause");
		SMSSDK.unregisterEventHandler(eh);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

}
