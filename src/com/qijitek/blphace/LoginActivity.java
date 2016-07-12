package com.qijitek.blphace;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.qijitek.utils.CountDownButtonHelper;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.qijitek.view.ProgersssDialog;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		Log.e(TAG, "onCreate");
		initLogin();
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
					Toast.makeText(getApplicationContext(), "登陆成功",
							Toast.LENGTH_SHORT).show();
					final ProgersssDialog dialog = new ProgersssDialog(
							LoginActivity.this, "验证成功，正在登陆...");
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
			code.setFocusable(true);
			String phone_str = phone.getText().toString().trim();
			if (!phone_str.equals("") && !MyUtils.checkPhoneNumber(phone_str)) {
				Toast.makeText(getApplicationContext(), "请输入正确手机号", 0).show();
			} else {
				send.setClickable(false);
				CountDownButtonHelper helper = new CountDownButtonHelper(send,
						"已发送", 120, 1);
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
		default:
			break;
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
