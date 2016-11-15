package com.qijitek.blphace;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qijitek.database.SingleItem;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

public class ItemDetailActivity extends Activity implements OnClickListener {
	private Handler mHandler;
	private Button add;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private TextView alias;
	private TextView brand;
	private TextView methods;
	private TextView name;
	private ImageView img;
	// private ProgersssDialog dialog;
	private SingleItem si;

	private String code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		init();
	}

	private void init() {
		mHandler = new Handler() {
		};
		// dialog = new ProgersssDialog(ItemDetailActivity.this, "");
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		button5 = (Button) findViewById(R.id.button5);
		alias = (TextView) findViewById(R.id.alias);
		brand = (TextView) findViewById(R.id.brand);
		methods = (TextView) findViewById(R.id.methods);
		name = (TextView) findViewById(R.id.name);
		img = (ImageView) findViewById(R.id.img);
		add = (Button) findViewById(R.id.add);
		add.setOnClickListener(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		code = getIntent().getStringExtra("code");
		try {
			JSONObject jsonObject = new JSONObject(getIntent().getStringExtra(
					"json"));
			JSONObject data = jsonObject.optJSONObject("data");
			final String product_name = data.optString("product_name");
			String price_str = data.optString("price");
			String spec_str = data.optString("spec");
			final String images_str = MyUtils.getValue(data, "images");
			final String alias_str = data.optString("alias");
			final String brand_str = data.optString("brand");
			final String methods_str = data.optString("methods");
			final JSONArray feature = (JSONArray) data.opt("feature");
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					name.setText(product_name);
					System.out.println("images_str.equals");
					if (!images_str.equals("")) {
						System.out.println("!images_str.equals");
						Picasso.with(getApplicationContext()).load(images_str)
								.into(img);
					}
					if (!alias_str.equals("")) {
						alias.setText(alias_str);
					} else {
						alias.setText("无");
					}
					if (!brand_str.equals("")) {
						brand.setText(brand_str);
					} else {
						brand.setText("无");
					}
					if (!methods_str.equals("")) {
						methods.setText(methods_str);
					} else {
						methods.setText("无");
					}
					String f0;
					String f1;
					String f2;
					String f3;
					String f4;
					String feature_txt = "";
					try {
						if (feature.length() >= 5) {
							f0 = feature.getString(0);
							f1 = feature.getString(1);
							f2 = feature.getString(2);
							f3 = feature.getString(3);
							f4 = feature.getString(4);
							feature_txt = f0 + "@" + f1 + "@" + f2 + "@" + f3
									+ "@" + f4;
						} else if (feature.length() < 5 && feature.length() > 0) {
							feature_txt = feature.getString(0);
							for (int i = 1; i < feature.length(); i++) {
								feature_txt = "@" + feature.getString(i);
							}
						} else {
							feature_txt = MyUtils.getNullFeature();
						}
						String[] ms = feature_txt.split("@");
						if (ms.length == 5) {
							button1.setText(ms[0]);
							button2.setText(ms[1]);
							button3.setText(ms[2]);
							button4.setText(ms[3]);
							button5.setText(ms[4]);
						} else if (ms.length == 4) {
							button1.setText(ms[0]);
							button2.setText(ms[1]);
							button3.setText(ms[2]);
							button4.setText(ms[3]);
							button5.setVisibility(View.INVISIBLE);
						} else if (ms.length == 3) {
							button1.setText(ms[0]);
							button2.setText(ms[1]);
							button3.setText(ms[2]);
							button4.setVisibility(View.INVISIBLE);
							button5.setVisibility(View.INVISIBLE);
						} else if (ms.length == 2) {
							button1.setText(ms[0]);
							button2.setText(ms[1]);
							button3.setVisibility(View.INVISIBLE);
							button4.setVisibility(View.INVISIBLE);
							button5.setVisibility(View.INVISIBLE);
						} else if (ms.length == 1) {
							button1.setText(ms[0]);
							button2.setVisibility(View.INVISIBLE);
							button3.setVisibility(View.INVISIBLE);
							button4.setVisibility(View.INVISIBLE);
							button5.setVisibility(View.INVISIBLE);
						}
						// button1.setText(f0);
						// button2.setText(f1);
						// button3.setText(f2);
						// button4.setText(f3);
						// button5.setText(f4);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					si = new SingleItem(product_name, images_str, code,
							new SharedpreferencesUtil(getApplicationContext())
									.getItemtype(), alias_str, brand_str,
							methods_str, feature_txt);
					System.out.println(si.toString());
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			final String baseurl = "http://api.qijitek.com/uploadTestlist/";
			final List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("userid",
					new SharedpreferencesUtil(getApplicationContext())
							.getUserid()));
			params.add(new BasicNameValuePair("itemtype", si.getItemtype()));
			params.add(new BasicNameValuePair("code", si.getCode().trim()));
			params.add(new BasicNameValuePair("name", si.getName().trim()));
			params.add(new BasicNameValuePair("imgurl", si.getImgurl().trim()));
			params.add(new BasicNameValuePair("feature", si.getFeature().trim()));
			params.add(new BasicNameValuePair("alias", si.getAlias().trim()));
			params.add(new BasicNameValuePair("brand", si.getBrand().trim()));
			params.add(new BasicNameValuePair("methods", si.getMethods().trim()));
			params.add(new BasicNameValuePair("time", System
					.currentTimeMillis() + ""));

			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						MyUtils.getJson2(baseurl, params);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								ItemDetailActivity.this.finish();
								Toast.makeText(getApplicationContext(), "添加成功",
										0).show();
							}
						});
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),
										"请检查网络连接", 0).show();
								ItemDetailActivity.this.finish();
							}
						});
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();

			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
}
