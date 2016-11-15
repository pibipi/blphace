package com.qijitek.blphace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
import com.qijitek.database.SingleItem;
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
import com.umeng.analytics.MobclickAgent;

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
					mHandler.sendEmptyMessage(4);
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
									initDefaultItems();
									String baseurl = "http://api.qijitek.com/regist/";
									List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
									params.add(new BasicNameValuePair("userid",
											phone.getText().toString().trim()));
									params.add(new BasicNameValuePair(
											"login_type", "1"));
									params.add(new BasicNameValuePair(
											"platform", "1"));
									jsonObject = MyUtils.getJson2(baseurl,
											params);
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
								catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
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
									String baseurl = "http://api.qijitek.com/regist/";
									List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
									params.add(new BasicNameValuePair("userid",
											uid));
									params.add(new BasicNameValuePair(
											"login_type", "3"));
									params.add(new BasicNameValuePair(
											"platform", "1"));
									jsonObject = MyUtils.getJson2(baseurl,
											params);
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
				case 4:
					Toast.makeText(getApplicationContext(), "验证码错误",
							Toast.LENGTH_SHORT).show();
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

	// TODO
	private void initDefaultItems() throws InterruptedException {
		ArrayList<SingleItem> singleItems = new ArrayList<SingleItem>();
		String itemtype = new SharedpreferencesUtil(getApplicationContext())
				.getItemtype();
		String userid = new SharedpreferencesUtil(getApplicationContext())
				.getUserid();
		String baseUrl = "http://api.qijitek.com/getTestList/";
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemtype", itemtype));
		try {
			JSONObject jsonObject = MyUtils.getJson2(baseUrl, params);
			JSONArray obj = jsonObject.getJSONArray("obj");
			int length = obj.length();
			for (int i = 0; i < length; i++) {
				JSONObject json = (JSONObject) obj.opt(i);
				String code = json.getString("code");
				String methods = json.getString("methods");
				String qid = json.getString("qid");
				String brand = json.getString("brand");
				String water = json.getString("water");
				String oil = json.getString("oil");
				String imgurl = json.getString("imgurl");
				String name = json.getString("name");
				String light = json.getString("light");
				String feature = json.getString("feature");
				String alias = json.getString("alias");
				SingleItem si = new SingleItem(name, imgurl, code, water, oil,
						light, itemtype, qid, alias, brand, methods, feature);
				singleItems.add(si);
			}
			Log.e("9ge", singleItems.size() + "");
			if (singleItems.size() == 0) {
				String url1 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=1&code=&name=%E9%9B%85%E8%AF%97%E5%85%B0%E9%BB%9B%E8%82%8C%E9%80%8F%E4%BF%AE%E6%8A%A4%E6%B4%81%E9%9D%A2%E4%B9%B3&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2Fimages%2F20160927113552.jpg&feature=%E6%8E%A7%E6%B2%B9%40%E5%90%AB%E4%B8%80%E5%AE%9A%E9%A6%99%E7%B2%BE%40%E5%8A%A0%E9%80%9F%E5%BE%AE%E5%BE%AA%E7%8E%AF%40%E5%90%AB%E5%A4%A9%E7%84%B6%E4%BF%9D%E6%B9%BF%E5%9B%A0%E5%AD%90%40%E6%9B%BF%E4%BB%A3%E6%80%A7%E9%98%B2%E8%85%90%E5%89%82&alias=&brand=%E9%9B%85%E8%AF%97%E5%85%B0%E9%BB%9B&methods=%E6%B9%BF%E6%89%8B%E6%B9%BF%E8%84%B8%E7%9A%84%E6%83%85%E5%86%B5%E4%B8%8B%E4%BD%BF%E7%94%A8+%E2%80%A2%E5%8F%961-2%E7%B2%92%E5%A6%82%E8%B1%8C%E8%B1%86%E5%A4%A7%E5%B0%8F%E7%9A%84%E9%87%8F%E3%80%81%E6%89%93%E6%B9%BF%E7%9A%84%E8%84%B8%E4%B8%8A%E8%BF%9B%E8%A1%8C%E6%8C%89%E6%91%A9%EF%BC%8C%E5%86%8D%E4%BB%A5%E6%B8%A9%E6%B0%B4%E5%BD%BB%E5%BA%95%E6%B4%81%E5%87%80%EF%BC%9B+%E2%80%A2%E7%9C%BC%E9%83%A8%E5%BB%BA%E8%AE%AE%E4%BD%BF%E7%94%A8%E4%B8%93%E7%94%A8%E7%9A%84%E7%9C%BC%E9%83%A8%E5%8D%B8%E5%A6%86%E6%B6%B2&time="
						+ String.valueOf(System.currentTimeMillis() + 1);
				String url2 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=1&code=&name=%E5%85%B0%E8%94%BB%E6%96%B0%E8%8F%81%E7%BA%AF%E8%87%BB%E9%A2%9C%E6%B4%81%E9%9D%A2%E4%B9%B3&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2Fimages%2F20160823135557.jpg&feature=%E6%B3%95%E5%9B%BD%40%E6%B7%A1%E5%A6%86%E6%88%96%E9%98%B2%E6%99%92%40500-1000%E5%85%83%40%E6%97%A0%E9%9C%80%E5%8C%96%E5%A6%86%E6%A3%89%40%E4%B8%AD%E6%95%88%E5%8D%B8%E5%A6%86&alias=&brand=%E5%85%B0%E8%94%BB&methods=&time="
						+ String.valueOf(System.currentTimeMillis() + 2);
				String url3 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=1&code=&name=%E8%B0%9C%E5%B0%9A%E8%93%9D%E8%8E%93%E8%8C%B6%E4%BF%9D%E6%B9%BF%E6%B4%81%E9%9D%A2%E6%B3%A1%E6%B2%AB+%5B%E6%81%8B%E6%9C%8B%E9%99%90%E9%87%8F%E7%89%88%5D&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2Fimages%2FC5403-1.jpg&feature=%E9%9F%A9%E5%9B%BD%40%E5%85%A8%E5%B9%B4%E3%80%81%E5%85%A8%E6%97%A5%400-100%E5%85%83%40%E9%99%90%E9%87%8F%E7%89%88&alias=&brand=%E8%B0%9C%E5%B0%9A&methods=&time="
						+ String.valueOf(System.currentTimeMillis() + 3);
				String url4 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=2&code=&name=%E5%98%89%E5%A8%9C%E5%AE%9D%E5%A5%A2%E5%8D%8E%E4%BF%9D%E6%B9%BF%E5%8C%96%E5%A6%86%E6%B0%B4%EF%BC%88%E6%B8%85%E7%88%BD%E5%9E%8B%EF%BC%89&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2Fimages%2F20160923133032.jpg&feature=%E8%A7%92%E8%B4%A8%E8%B0%83%E7%90%86%40%E8%BF%87%E6%95%8F%E8%82%A4%E8%B4%A8%E5%8F%AF%E7%94%A8%40%E9%98%B2%E8%85%90%E5%89%82%E5%B0%91%40%E5%90%AB%E4%B8%80%E5%AE%9A%E9%A6%99%E7%B2%BE%40%E5%90%AB%E5%A4%9A%E5%85%83%E9%86%87&alias=&brand=%E5%98%89%E5%A8%9C%E5%AE%9D&methods=&time="
						+ String.valueOf(System.currentTimeMillis() + 4);
				String url5 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=2&code=&name=%E5%B1%88%E8%87%A3%E6%B0%8F%E7%87%95%E7%AA%9D%E6%B2%81%E7%99%BD%E8%87%BB%E9%A2%9C%E6%9F%94%E8%82%A4%E6%B0%B4&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2FproImg%2Fimage_20160115_56990381bbb6f.jpg&feature=%E6%8A%97%E7%82%8E%40%E6%8A%97%E6%B0%A7%E5%8C%96%40%E4%BF%9D%E6%B9%BF%40%E6%97%A0%E8%87%B4%E7%97%98%E9%9A%90%E6%82%A3%40%E8%BF%87%E6%95%8F%E8%82%A4%E8%B4%A8%E5%8F%AF%E7%94%A8&alias=&brand=%E5%B1%88%E8%87%A3%E6%B0%8F&methods=%E6%B4%81%E9%9D%A2%E5%90%8E%EF%BC%8C%E4%BB%A5%E5%8C%96%E5%A6%86%E6%A3%89%E6%B2%BE%E5%8F%96%E9%80%82%E9%87%8F%E6%9C%AC%E5%93%81%E5%9D%87%E5%8C%80%E6%93%A6%E6%8B%AD%E4%BA%8E%E8%84%B8%E9%83%A8%E5%92%8C%E9%A2%88%E9%83%A8%E3%80%82%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9%EF%BC%9A1%E3%80%81%E4%B8%A5%E9%87%8D%E7%81%BC%E4%BC%A4%EF%BC%8C%E6%8D%9F%E4%BC%A4%E5%8F%8A%E6%95%8F%E6%84%9F%E7%9A%AE%E8%82%A4%E5%8B%BF%E7%94%A8%EF%BC%9B2%E3%80%81%E5%A6%82%E4%B8%8D%E6%85%8E%E5%85%A5%E7%9C%BC%EF%BC%8C%E8%AF%B7%E7%94%A8%E5%A4%A7%E9%87%8F%E6%B8%85%E6%B0%B4%E5%86%B2%E6%B4%97%EF%BC%9B3%E3%80%81%E5%A6%82%E8%82%8C%E8%82%A4%E5%87%BA%E7%8E%B0%E4%BB%BB%E4%BD%95%E4%B8%8D%E9%80%82%EF%BC%8C%E8%AF%B7%E7%AB%8B%E5%8D%B3%E5%81%9C%E6%AD%A2%E4%BD%BF%E7%94%A8%E5%B9%B6%E5%B0%B1%E5%8C%BB%E8%AF%8A%E6%B2%BB%E3%80%82&time="
						+ String.valueOf(System.currentTimeMillis() + 5);
				String url6 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=2&code=&name=%E9%9B%85%E8%AF%97%E5%85%B0%E9%BB%9B%E6%9F%94%E4%B8%9D%E7%84%95%E9%87%87%E5%8C%96%E5%A6%86%E6%B0%B4&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2FproImg%2Fimage_20160325_56f4ca0719da7.jpg&feature=%E7%BE%8E%E7%99%BD%40%E6%B8%85%E6%B4%81%40%E4%BF%9D%E6%B9%BF%40%E8%BF%87%E6%95%8F%E8%82%A4%E8%B4%A8%E9%80%89%E7%94%A8%40%E5%90%AB%E4%B8%80%E5%AE%9A%E9%98%B2%E8%85%90%E5%89%82&alias=&brand=%E9%9B%85%E8%AF%97%E5%85%B0%E9%BB%9B&methods=&time="
						+ String.valueOf(System.currentTimeMillis() + 6);
				String url7 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=3&code=&name=%E7%A2%A7%E6%9F%94%E5%80%8D%E6%8A%A4%E9%98%B2%E6%99%92%E4%B9%B3%E6%B6%B2&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2FproImg%2Fimage_20160114_56979fabb768b.jpg&feature=%E9%98%B2%E6%99%92%40%E9%98%B2%E8%85%90%E5%89%82%E5%B0%91%40%E9%A6%99%E7%B2%BE%E5%B0%91%40%E5%90%AB%E5%A4%9A%E5%85%83%E9%86%87%40%E6%97%A5%E6%9C%AC&alias=%E7%A2%A7%E6%9F%94%E9%95%BF%E6%95%88%E6%9F%94%E6%84%9F%E9%98%B2%E6%99%92%E4%B9%B3%E6%B6%B2&brand=%E7%A2%A7%E6%9F%94&methods=%E8%AF%B7%E5%85%85%E5%88%86%E6%91%87%E5%8C%80%E5%90%8E%E5%8F%96%E9%80%82%E9%87%8F%E4%BA%8E%E6%89%8B%E5%BF%83%EF%BC%8C%E6%B6%82%E6%8A%B9%E4%BA%8E%E8%82%8C%E8%82%A4%E3%80%82%E4%B8%BA%E4%BF%9D%E8%AF%81%E6%95%88%E6%9E%9C%EF%BC%8C%E6%B5%81%E6%B1%97%E5%90%8E%E8%AF%B7%E5%85%88%E6%93%A6%E5%B9%B2%EF%BC%8C%E5%86%8D%E6%B6%82%E6%8A%B9%E6%9C%AC%E5%93%81%E3%80%82%E6%B8%85%E6%B4%97%E6%97%B6%EF%BC%8C%E8%84%B8%E9%83%A8%E8%AF%B7%E4%BD%BF%E7%94%A8%E5%8D%B8%E5%A6%86%E4%BA%A7%E5%93%81%E6%B4%97%E5%87%80%EF%BC%8C%E8%BA%AB%E4%BD%93%E9%83%A8%E4%BD%8D%E8%AF%B7%E4%BD%BF%E7%94%A8%E6%B2%90%E6%B5%B4%E9%9C%B2%E7%AD%89%E6%B8%85%E6%B4%81%E7%94%A8%E5%93%81%E5%85%85%E5%88%86%E6%B4%97%E5%87%80%E3%80%82%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9%EF%BC%9A.%E8%82%8C%E8%82%A4%E6%9C%89%E4%BC%A4%E5%8F%A3%E3%80%81%E6%B9%BF%E7%96%B9%E7%AD%89%E5%BC%82%E5%B8%B8%E6%83%85%E5%86%B5%E6%97%B6%E8%AF%B7%E5%8B%BF%E4%BD%BF%E7%94%A8%E3%80%82.%E8%82%8C%E8%82%A4%E6%9C%89%E7%BA%A2%E8%82%BF%E3%80%81%E5%8F%91%E7%97%92%E3%80%81%E5%88%BA%E7%97%9B%E7%AD%89%E5%BC%82%E5%B8%B8%E7%8A%B6%E5%86%B5%E6%97%B6%EF%BC%8C%E8%AF%B7%E5%8B%BF%E4%BD%BF%E7%94%A8%E3%80%82.%E6%B3%A8%E6%84%8F%E4%B8%8D%E8%A6%81%E8%BF%9B%E5%85%A5%E7%9C%BC%E7%9D%9B%EF%BC%8C%E5%A6%82%E4%B8%8D%E6%85%8E%E5%85%A5%E7%9C%BC%EF%BC%8C%E8%AF%B7%E7%AB%8B%E5%8D%B3%E4%BB%A5%E5%A4%A7%E9%87%8F%E6%B8%85%E6%B0%B4%E6%B4%97%E5%87%80%E3%80%82.%E7%94%B1%E4%BA%8E%E5%86%85%E5%90%AB%E6%9F%94%E7%B2%89%EF%BC%8C%E8%AF%B7%E4%B8%8D%E8%A6%81%E5%B0%86%E6%B6%B2%E4%BD%93%E7%9B%B4%E6%8E%A5%E6%8E%A5%E8%A7%A6%E4%BA%8E%E8%A1%A3%E7%89%A9%E3%80%82&time="
						+ String.valueOf(System.currentTimeMillis() + 7);
				String url8 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=3&code=&name=Kiehl%27s%E7%A7%91%E9%A2%9C%E6%B0%8F%E7%B2%BE%E5%87%86%E7%B4%A7%E9%A2%9C%E6%8F%90%E5%8D%87%E7%B2%BE%E5%8D%8E%E4%B9%B3%E6%B6%B2&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2Fimages%2F20160822161121.jpg&feature=%E6%8A%97%E7%9A%B1%40%E6%8A%97%E6%B0%A7%E5%8C%96%40%E4%BF%9D%E6%B9%BF%40%E9%98%B2%E8%85%90%E5%89%82%E5%B0%91%40%E5%90%AB%E9%85%B5%E6%AF%8D%E6%88%90%E5%88%86&alias=&brand=%E7%A7%91%E9%A2%9C%E6%B0%8F&methods=%E6%AF%8F%E5%A4%A9%E6%97%A9%E6%99%9A%EF%BC%8C%E7%B2%BE%E5%8D%8E%E4%B9%8B%E5%90%8E%EF%BC%8C%E4%BB%A5%E6%8C%87%E8%85%B9%E5%8F%96%E9%80%82%E9%87%8F%E9%9D%A2%E9%9C%9C%E5%90%8E%EF%BC%8C%E5%9D%87%E5%8C%80%E6%B6%82%E6%8A%B9%E7%B2%BE%E5%87%86%E7%B4%A7%E9%A2%9C%E6%8F%90%E5%8D%87%E7%B2%BE%E5%8D%8E%E4%B9%B3%E6%B6%B2%E4%BA%8E%E8%84%B8%E9%83%A8%E3%80%81%E9%A2%88%E9%83%A8%E8%82%8C%E8%82%A4&time="
						+ String.valueOf(System.currentTimeMillis() + 8);
				// String url9 =
				// "http://api.qijitek.com/uploadTestlist/?userid="
				// + userid
				// +
				// "&itemtype=3&code=&name=%E6%B0%B4%E5%AF%86%E7%A0%81%E6%B5%B7%E6%B3%89%E6%B7%B1%E9%80%8F%E7%B2%BE%E5%8D%8E%E4%B9%B3%E6%B6%B2&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2Fimages%2FTB1QtmDKpXXXXaBXFXXXXXXXXXX_%21%210-item_pic.jpg_430x430q90.jpg&feature=%40100-200%E5%85%83&alias=&brand=%E6%B0%B4%E5%AF%86%E7%A0%81&methods=&time="
				// + String.valueOf(System.currentTimeMillis() + 9);
				String url9 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=3&code=&name=%E6%B0%B4%E5%AF%86%E7%A0%81%E6%B5%B7%E6%B3%89%E6%B7%B1%E9%80%8F%E7%B2%BE%E5%8D%8E%E4%B9%B3%E6%B6%B2&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2Fimages%2FTB1QtmDKpXXXXaBXFXXXXXXXXXX_%21%210-item_pic.jpg_430x430q90.jpg&feature=%E4%B8%AD%E5%9B%BD%40100-200%E5%85%83&alias=&brand=%E6%B0%B4%E5%AF%86%E7%A0%81&methods=&time="
						+ String.valueOf(System.currentTimeMillis() + 9);
				String url10 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=4&code=&name=%E7%BE%8E%E5%8D%B3%E7%BB%BF%E8%8C%B6%E6%B8%85%E7%9B%88%E7%A5%9B%E7%97%98%E5%94%A7%E5%94%A7%E9%9D%A2%E8%86%9C&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2FproImg%2Fimage_20160329_56fa5f170dcda.jpg&feature=%E5%85%A8%E6%96%B0%E5%94%A7%E5%94%A7%E7%B3%BB%E5%88%97%40%E4%BF%AE%E6%8A%A4%E7%B3%BB%E5%88%97%40%E9%92%88%E5%AF%B9%E5%B9%B4%E8%BD%BB%E8%82%8C%E8%82%A4%40%E6%BB%8B%E6%B6%A6%40%E8%88%92%E6%95%8F&alias=&brand=%E7%BE%8E%E5%8D%B3&methods=&time="
						+ String.valueOf(System.currentTimeMillis() + 10);
				String url11 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=4&code=&name=%E5%B1%88%E8%87%A3%E6%B0%8FWATER360%C2%B0%E7%9F%BF%E6%B3%89%E8%87%BB%E6%B6%A6%E8%88%92%E7%BC%93%E9%9D%A2%E8%86%9C&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2Fimages%2F100597621_1.jpg&feature=%E8%88%92%E6%95%8F%40%E4%BF%AE%E5%A4%8D%40%E4%BF%9D%E6%B9%BF%40%E5%90%AB%E4%B8%80%E5%AE%9A%E9%A6%99%E7%B2%BE%40%E6%9B%BF%E4%BB%A3%E6%80%A7%E9%98%B2%E8%85%90%E5%89%82&alias=&brand=%E5%B1%88%E8%87%A3%E6%B0%8F&methods=&time="
						+ String.valueOf(System.currentTimeMillis() + 11);
				String url12 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=4&code=&name=%E9%9B%AA%E8%8A%B1%E7%A7%80%E6%B8%85%E6%B6%A6%E8%88%92%E7%BC%93%E9%9D%A2%E8%86%9C&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2FproImg%2Fimage_20160115_5698f9ba4b7bc.jpg&feature=%E6%8A%97%E7%82%8E%40%E8%88%92%E6%95%8F%40%E4%BF%9D%E6%B9%BF%40%E9%98%B2%E8%85%90%E5%89%82%E5%B0%91%40%E6%9B%BF%E4%BB%A3%E6%80%A7%E9%98%B2%E8%85%90%E5%89%82&alias=&brand=%E9%9B%AA%E8%8A%B1%E7%A7%80&methods=%E5%A4%8F%E5%AD%A3%E6%88%B7%E5%A4%96%E8%BF%90%E5%8A%A8%E5%90%8E%E6%88%96%E5%B0%B1%E5%AF%9D%E5%89%8D%EF%BC%8C%E5%9C%A8%E6%8A%A4%E8%82%A4%E7%9A%84%E6%9C%80%E5%90%8E%E9%98%B6%E6%AE%B5%E4%BD%BF%E7%94%A8%E3%80%82%E5%8F%96%E9%80%82%E9%87%8F%EF%BC%8C%E9%81%BF%E5%BC%80%E7%9C%BC%E5%91%A8%E5%92%8C%E5%94%87%E9%83%A8%E5%8E%9A%E5%8E%9A%E5%9C%B0%E6%B6%82%E6%8A%B9%E4%BA%8E%E8%84%B8%E9%83%A8%E8%82%8C%E8%82%A4%E3%80%82%E6%B6%82%E6%8A%B9%E5%90%8E%E5%8F%AF%E7%9B%B4%E6%8E%A5%E5%85%A5%E7%9D%A1%EF%BC%9B%E6%88%96%E5%BE%8510%E5%88%86%E9%92%9F%E5%B7%A6%E5%8F%B3%EF%BC%8C%E8%90%A5%E5%85%BB%E6%88%90%E5%88%86%E8%A2%AB%E8%82%8C%E8%82%A4%E5%85%85%E5%88%86%E5%90%B8%E6%94%B6%E5%90%8E%EF%BC%8C%E7%94%A8%E6%B8%A9%E6%B0%B4%E6%B4%97%E5%87%80%E5%8D%B3%E5%8F%AF%E3%80%82%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9%EF%BC%9A1.%E9%81%BF%E5%BC%80%E7%9C%BC%E5%91%A8%E5%92%8C%E5%94%87%E9%83%A8%E3%80%822.%E8%AF%B7%E4%B8%8D%E8%A6%81%E7%94%A8%E4%BA%8E%E4%BC%A4%E5%8F%A3%E6%88%96%E5%85%B6%E4%BB%96%E6%98%93%E8%A2%AB%E6%84%9F%E6%9F%93%E7%9A%84%E5%9C%B0%E6%96%B9%E3%80%823.%E4%BD%BF%E7%94%A8%E4%BA%A7%E5%93%81%E5%90%8E%E7%9A%AE%E8%82%A4%E5%87%BA%E7%8E%B0%E4%BB%A5%E4%B8%8B%E6%83%85%E5%86%B5%E8%AF%B7%E5%81%9C%E6%AD%A2%E4%BD%BF%E7%94%A8%EF%BC%8C%E8%8B%A5%E6%83%85%E5%86%B5%E6%8C%81%E7%BB%AD%E8%AF%B7%E5%92%A8%E8%AF%A2%E7%9A%AE%E8%82%A4%E7%A7%91%E5%8C%BB%E7%94%9F%EF%BC%9A1%EF%BC%89%E4%BD%BF%E7%94%A8%E4%B8%AD%E5%87%BA%E7%8E%B0%E7%BA%A2%E6%96%91%EF%BC%8C%E6%B5%AE%E8%82%BF%EF%BC%8C%E5%8F%91%E7%97%92%EF%BC%8C%E5%88%BA%E6%BF%80%E7%AD%89%E5%BC%82%E5%B8%B8%E6%83%85%E5%86%B5%EF%BC%9B2%EF%BC%89%E7%94%B1%E4%BA%8E%E5%85%89%E7%BA%BF%E7%85%A7%E5%B0%84%E5%87%BA%E7%8E%B0%E4%BB%A5%E4%B8%8A%E5%BC%82%E5%B8%B8%E6%83%85%E5%86%B5%E3%80%824.%E5%AD%98%E7%AE%A1%E7%9A%84%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9%EF%BC%9A1%EF%BC%89%E4%BD%BF%E7%94%A8%E5%90%8E%E8%AF%B7%E7%9B%96%E7%B4%A7%E7%93%B6%E7%9B%96%EF%BC%9B2%EF%BC%89%E8%AF%B7%E5%AD%98%E6%94%BE%E4%BA%8E%E5%84%BF%E7%AB%A5%E6%8E%A5%E8%A7%A6%E4%B8%8D%E5%88%B0%E7%9A%84%E5%9C%B0%E6%96%B9%EF%BC%9B3%EF%BC%89%E8%AF%B7%E4%B8%8D%E8%A6%81%E6%94%BE%E7%BD%AE%E4%BA%8E%E9%AB%98%E6%B8%A9%E3%80%81%E4%BD%8E%E6%B8%A9%E5%8F%8A%E9%98%B3%E5%85%89%E7%9B%B4%E5%B0%84%E5%A4%84%E3%80%82&time="
						+ String.valueOf(System.currentTimeMillis() + 12);
				String url13 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=5&code=&name=%E6%97%A0%E5%8D%B0%E8%89%AF%E5%93%81%E7%84%95%E8%82%A4%E7%9C%BC%E9%83%A8%E6%8A%A4%E7%90%86%E6%B6%B2&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2FproImg%2Fimage_20160325_56f4b95502d33.jpg&feature=%E8%88%92%E6%95%8F%40%E4%BF%AE%E5%A4%8D%40%E4%BF%9D%E6%B9%BF%40%E8%BF%87%E6%95%8F%E8%82%A4%E8%B4%A8%E5%8F%AF%E7%94%A8%40%E5%90%AB%E5%A4%9A%E5%85%83%E9%86%87&alias=&brand=%E6%97%A0%E5%8D%B0%E8%89%AF%E5%93%81&methods=%E4%BD%BF%E7%94%A8%E5%8C%96%E5%A6%86%E6%B0%B4%E3%80%81%E4%B9%B3%E6%B6%B2%E4%B9%8B%E5%90%8E%EF%BC%8C%E5%8F%96%E9%80%82%E9%87%8F%E4%BA%8E%E6%89%8B%E6%8E%8C%EF%BC%8C%E6%B6%82%E6%8A%B9%E5%88%B0%E7%9C%BC%E7%9A%84%E5%91%A8%E5%9B%B4%E3%80%82%E6%84%9F%E5%88%B0%E5%B9%B2%E7%87%A5%E6%97%B6%EF%BC%8C%E5%86%8D%E8%BD%BB%E6%9F%94%E5%9C%B0%E6%B6%82%E6%8A%B9%E3%80%82%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9%EF%BC%9A%E8%82%8C%E8%82%A4%E5%BC%82%E5%B8%B8%E6%97%B6%E3%80%81%E6%88%96%E4%B8%8D%E9%80%82%E5%90%88%E8%82%8C%E8%82%A4%E6%97%B6%E8%AF%B7%E5%8B%BF%E4%BD%BF%E7%94%A8%E3%80%82%E8%AF%AF%E5%85%A5%E7%9C%BC%E7%9D%9B%E6%97%B6%EF%BC%8C%E8%AF%B7%E7%AB%8B%E5%8D%B3%E7%94%A8%E6%B0%B4%E5%86%B2%E6%B4%97%E3%80%82%E5%9B%A0%E5%A4%A9%E7%84%B6%E6%88%90%E5%88%86%E4%BC%9A%E6%9C%89%E9%A2%9C%E8%89%B2%E5%92%8C%E9%A6%99%E5%91%B3%E7%9A%84%E5%B7%AE%E5%BC%82%EF%BC%8C%E4%BD%86%E6%97%A0%E8%B4%A8%E9%87%8F%E9%97%AE%E9%A2%98%E3%80%82&time="
						+ String.valueOf(System.currentTimeMillis() + 13);
				String url14 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=5&code=&name=%E5%80%A9%E7%A2%A7%E7%9C%BC%E9%83%A8%E6%8A%A4%E7%90%86%E6%B0%B4%E5%87%9D%E9%9C%9C&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2FproImg%2Fimage_20160114_569786a05db3b.jpg&feature=%E8%88%92%E6%95%8F%40%E6%8A%97%E6%B0%A7%E5%8C%96%40%E4%BF%9D%E6%B9%BF%40%E6%97%A0%E9%98%B2%E8%85%90%E5%89%82%40%E8%BF%87%E6%95%8F%E8%82%A4%E8%B4%A8%E5%8F%AF%E7%94%A8&alias=&brand=%E5%80%A9%E7%A2%A7&methods=%E4%BB%A5%E6%8C%87%E5%B0%96%E5%8F%96%E9%80%82%E9%87%8F%E6%B6%82%E4%BA%8E%E7%9C%BC%E9%83%A8%E5%8C%BA%E5%9F%9F%EF%BC%8C%E6%97%A9%E6%99%9A%E5%90%84%E4%B8%80%E6%AC%A1%E3%80%82%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9%EF%BC%9A%E5%A6%82%E6%9C%89%E4%B8%8D%E9%80%82%EF%BC%8C%E8%AF%B7%E5%81%9C%E6%AD%A2%E4%BD%BF%E7%94%A8%E3%80%82%E8%AF%B7%E5%8B%BF%E5%85%A5%E7%9C%BC%EF%BC%8C%E9%81%BF%E5%85%8D%E4%B8%8D%E9%80%82%E3%80%82&time="
						+ String.valueOf(System.currentTimeMillis() + 14);
				String url15 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=5&code=&name=%E5%80%A9%E7%A2%A7%E7%9C%BC%E9%83%A8%E6%8A%A4%E7%90%86%E7%B2%BE%E5%8D%8E%E9%9C%B2&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2FproImg%2Fimage_20160114_5697a78489272.jpg&feature=%E7%BE%8E%E5%9B%BD%40200-500%E5%85%83&alias=&brand=%E5%80%A9%E7%A2%A7&methods=&time="
						+ String.valueOf(System.currentTimeMillis() + 15);
				String url16 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=6&code=&name=%E9%9B%85%E5%A7%BF%E6%81%92%E6%97%B6%E7%B4%A7%E8%87%B4%E9%9D%A2%E9%83%A8%E7%B2%BE%E5%8D%8E%E6%B6%B2&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2Fimages%2F1416291398275.png&feature=%E4%BF%AE%E5%A4%8D%40%E6%8A%97%E6%B0%A7%E5%8C%96%40%E4%BF%9D%E6%B9%BF%40%E5%90%AB%E7%A5%9E%E7%BB%8F%E9%85%B0%E8%83%BA%40%E5%90%AB%E9%85%B5%E6%AF%8D%E6%88%90%E5%88%86&alias=&brand=%E9%9B%85%E5%A7%BF&methods=&time="
						+ String.valueOf(System.currentTimeMillis() + 16);
				String url17 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=6&code=&name=%E9%98%BF%E8%8A%99%E9%98%BF%E5%B0%94%E5%8D%91%E6%96%AF%E5%B1%B1%E6%A4%8D%E7%89%A9%E7%8F%8D%E8%90%83%E9%9D%A2%E9%83%A8%E7%B2%BE%E5%8D%8E%E4%B9%B3&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2Fimages%2Fu%3D4254811199%2C428759829%26fm%3D15%26gp%3D0.jpg&feature=%E8%88%92%E6%95%8F%40%E6%8A%97%E6%B0%A7%E5%8C%96%40%E4%BF%9D%E6%B9%BF%40%E6%97%A0%E8%87%B4%E7%97%98%E9%9A%90%E6%82%A3%40%E8%BF%87%E6%95%8F%E8%82%A4%E8%B4%A8%E9%80%89%E7%94%A8&alias=&brand=%E9%98%BF%E8%8A%99&methods=%E6%B4%81%E9%9D%A2%E5%90%8E%EF%BC%8C%E6%93%A6%E5%B9%B2%E9%9D%A2%E9%83%A8%EF%BC%8C%E5%8F%96%E9%80%82%E9%87%8F%E6%9C%AC%E5%93%81%E5%9D%87%E5%8C%80%E6%B6%82%E6%8A%B9%E4%BA%8E%E9%9D%A2%E9%83%A8%E5%8F%8A%E9%A2%88%E9%83%A8%E7%9A%AE%E8%82%A4%EF%BC%8C%E4%B9%8B%E5%90%8E%E5%8F%AF%E4%BD%BF%E7%94%A8%E6%97%A5%E9%9C%9C%E6%88%96%E6%99%9A%E9%9C%9C%E3%80%82%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9%EF%BC%9A%E7%BD%AE%E4%BA%8E%E9%98%B4%E5%87%89%E5%B9%B2%E7%87%A5%E5%A4%84%E4%BF%9D%E5%AD%98%E3%80%82%E9%81%BF%E5%85%8D%E8%A7%A6%E5%8F%8A%E7%9C%BC%E7%9D%9B%E3%80%82%E9%81%BF%E5%85%8D%E5%84%BF%E7%AB%A5%E6%8A%93%E6%8B%BF%E3%80%82&time="
						+ String.valueOf(System.currentTimeMillis() + 17);
				String url18 = "http://api.qijitek.com/uploadTestlist/?userid="
						+ userid
						+ "&itemtype=6&code=&name=Fresh%E9%A6%A5%E8%95%BE%E8%AF%97%E6%B5%B7%E8%8E%93%E9%9D%A2%E9%83%A8%E4%BF%AE%E5%A4%8D%E7%B2%BE%E5%8D%8E%E6%B2%B9&imgurl=http%3A%2F%2F7xo2me.com1.z0.glb.clouddn.com%2Fimages%2F39052712_1411814337943_400x400.jpg&feature=%E6%B5%B7%E8%8E%93%E7%B3%BB%E5%88%97%40%E4%BF%AE%E5%A4%8D%40%E6%8A%97%E6%B0%A7%E5%8C%96%40%E4%BF%9D%E6%B9%BF%40%E8%BF%87%E6%95%8F%E8%82%A4%E8%B4%A8%E5%8F%AF%E7%94%A8&alias=&brand=Fresh&methods=%E5%9C%A8%E6%B8%85%E6%B4%81%E5%90%8E%EF%BC%8C%E5%B0%861%E8%87%B32%E6%BB%B4%E6%9C%AC%E5%93%81%E4%BD%BF%E7%94%A8%E4%BA%8E%E8%84%B8%E9%83%A8%E8%BD%BB%E8%BD%BB%E6%8C%89%E6%91%A9%E3%80%82%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9%EF%BC%9A%E6%97%A0&time="
						+ String.valueOf(System.currentTimeMillis() + 18);
				MyUtils.getJson(url1);

				MyUtils.getJson(url2);

				MyUtils.getJson(url3);

				MyUtils.getJson(url4);

				MyUtils.getJson(url5);

				MyUtils.getJson(url6);

				MyUtils.getJson(url7);

				MyUtils.getJson(url8);

				MyUtils.getJson(url9);

				MyUtils.getJson(url10);

				MyUtils.getJson(url11);

				MyUtils.getJson(url12);

				MyUtils.getJson(url13);

				MyUtils.getJson(url14);

				MyUtils.getJson(url15);

				MyUtils.getJson(url16);

				MyUtils.getJson(url17);

				MyUtils.getJson(url18);

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			// mHandler.post(new Runnable() {
			//
			// @Override
			// public void run() {
			// Toast.makeText(getContext(), "请检查网络连接", 0).show();
			// }
			// });
		} catch (JSONException e) {
			e.printStackTrace();
		}

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
						"已发送", 30, 1);
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
				// Toast.makeText(LoginActivity.this, "success",
				// Toast.LENGTH_SHORT).show();
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
		MobclickAgent.onResume(this);
		SMSSDK.registerEventHandler(eh);
		Log.e(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		Log.e(TAG, "onPause");
		SMSSDK.unregisterEventHandler(eh);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

}
