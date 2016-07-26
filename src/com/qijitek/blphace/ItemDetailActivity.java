package com.qijitek.blphace;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
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

	@Override
	protected void onResume() {
		super.onResume();
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

					alias.setText(alias_str);
					brand.setText(brand_str);
					methods.setText(methods_str);
					String f0;
					String f1;
					String f2;
					String f3;
					String f4;
					String feature_txt = "";
					try {
						f0 = feature.getString(0);
						f1 = feature.getString(1);
						f2 = feature.getString(2);
						f3 = feature.getString(3);
						f4 = feature.getString(4);
						feature_txt = f0 + "@" + f1 + "@" + f2 + "@" + f3 + "@"
								+ f4;
						button1.setText(f0);
						button2.setText(f1);
						button3.setText(f2);
						button4.setText(f3);
						button5.setText(f4);
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
			final String url = "http://api.qijitek.com/uploadTestlist/?userid="
					+ new SharedpreferencesUtil(getApplicationContext())
							.getUserid() + "&itemtype=" + si.getItemtype()
					+ "&code=" + si.getCode() + "&name=" + si.getName()
					+ "&imgurl=" + si.getImgurl() + "&feature="
					+ si.getFeature() + "&alias=" + si.getAlias() + "&brand="
					+ si.getBrand() + "&methods=" + si.getMethods() + "&time="
					+ System.currentTimeMillis();
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						MyUtils.getJson(url);
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
