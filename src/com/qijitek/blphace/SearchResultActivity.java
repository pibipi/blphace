package com.qijitek.blphace;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qijitek.constant.NomalConstant;
import com.qijitek.database.SearchItem;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.qijitek.view.ProgersssDialog;
import com.squareup.picasso.Picasso;

public class SearchResultActivity extends Activity implements OnClickListener {
	private RelativeLayout r1;
	private ListView list;
	private ArrayList<SearchItem> searchItems;
	private SharedpreferencesUtil sharedpreferencesUtil;
	private MyAdapter myAdapter;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		getData();
		init();
	}

	private void getData() {
		sharedpreferencesUtil = new SharedpreferencesUtil(
				getApplicationContext());
		searchItems = new ArrayList<SearchItem>();
		try {
			JSONObject jsonObject = new JSONObject(getIntent().getStringExtra(
					"json"));
			JSONObject data = jsonObject.optJSONObject("data");
			JSONArray array = data.optJSONArray("products");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.opt(i);
				SearchItem s = new SearchItem(
						sharedpreferencesUtil.getUserid(),
						sharedpreferencesUtil.getItemtype(),
						object.optString("pname"), object.optString("pid"),
						MyUtils.getValue(object, "imgUrl"),
						object.optString("price"), object.optString("spec"));
				System.out.println(s.toString());
				searchItems.add(s);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		mHandler = new Handler() {
		};
		r1 = (RelativeLayout) findViewById(R.id.r1);
		r1.setOnClickListener(this);
		list = (ListView) findViewById(R.id.list);
		myAdapter = new MyAdapter(getApplicationContext());
		list.setAdapter(myAdapter);
	}

	private class MyAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			super();
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return searchItems.size();
		}

		@Override
		public Object getItem(int position) {
			return searchItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_search_item, null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.spec = (TextView) convertView.findViewById(R.id.spec);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.add = (ImageView) convertView.findViewById(R.id.add);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final SearchItem s = searchItems.get(position);
			holder.name.setText(s.getName());
			String spec = s.getSpec();
			if (!spec.equals("")) {
				holder.spec.setText(s.getSpec());
			} else {
				holder.spec.setText("无");
			}
			String price = s.getPrice();
			if (!price.equals("")) {
				holder.price.setText("¥"+s.getPrice());
			} else {
				holder.price.setText("无");

			}
			if (!s.getImgurl().equals("")) {
				Picasso.with(context)
						.load(s.getImgurl())
						.resize(MyUtils.dip2px(context, 70),
								MyUtils.dip2px(context, 70)).centerCrop()
						.into(holder.img);
			}
			holder.add.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final ProgersssDialog dialog = new ProgersssDialog(
							SearchResultActivity.this, "");
					new Thread(new Runnable() {

						@Override
						public void run() {
							String url = "http://api.qijitek.com/searchJiMiDetail/?pid="
									+ s.getPid()
									+ "&appid="
									+ NomalConstant.QijiID
									+ "&token="
									+ MyUtils.md5(s.getPid()
											+ NomalConstant.QijiSecret);
							url = url.replaceAll(" ", "%20");
							try {
								JSONObject jsonObject = MyUtils.getJson(url
										.trim());
								boolean state = jsonObject
										.optBoolean("success");
								if (state == false) {
									return;
								}
								JSONObject data = jsonObject
										.optJSONObject("data");
								final String alias_str = data
										.optString("alias");
								final String brand_str = data
										.optString("brand");
								final String methods_str = data
										.optString("methods");
								final JSONArray feature = (JSONArray) data
										.opt("feature");
								String f0;
								String f1;
								String f2;
								String f3;
								String f4;
								String feature_txt = "";
								if (feature.length() >= 5) {
									f0 = feature.getString(0);
									f1 = feature.getString(1);
									f2 = feature.getString(2);
									f3 = feature.getString(3);
									f4 = feature.getString(4);
									feature_txt = f0 + "@" + f1 + "@" + f2
											+ "@" + f3 + "@" + f4;
								} else if (feature.length() < 5 && feature.length() > 0) {
									feature_txt = feature.getString(0);
									for (int i = 1; i < feature.length(); i++) {
										feature_txt = "@" + feature.getString(i);
									}
								} else {
									feature_txt = MyUtils.getNullFeature();
								}
								
//								String url20 = "http://api.qijitek.com/uploadTestlist/?userid="
//										+ new SharedpreferencesUtil(
//												getApplicationContext())
//												.getUserid()
//										+ "&itemtype="
//										+ s.getItemtype()
//										+ "&code="
//										+ ""
//										+ "&name="
//										+ s.getName().trim()
//										+ "&imgurl="
//										+ s.getImgurl().trim()
//										+ "&feature="
//										+ feature_txt.trim()
//										+ "&alias="
//										+ alias_str.trim()
//										+ "&brand="
//										+ brand_str.trim()
//										+ "&methods="
//										+ methods_str.trim()
//										+ "&time=" + System.currentTimeMillis();
//								// final String url2 = url20
//								// .replaceAll(" ", "%20");
//								final String url2 = URLEncoder.encode(url20,
//										"utf-8");
//								MyUtils.getJson(url2.trim());
								//
								String baseUrl = "http://api.qijitek.com/uploadTestlist/";
								List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
								params.add(new BasicNameValuePair("userid",
										new SharedpreferencesUtil(
												getApplicationContext())
												.getUserid()));
								params.add(new BasicNameValuePair("itemtype", s
										.getItemtype()));
								params.add(new BasicNameValuePair("code", ""));
								params.add(new BasicNameValuePair("name", s
										.getName().trim()));
								params.add(new BasicNameValuePair("imgurl", s
										.getImgurl().trim()));
								params.add(new BasicNameValuePair("feature",
										feature_txt.trim()));
								params.add(new BasicNameValuePair("alias",
										alias_str.trim()));
								params.add(new BasicNameValuePair("brand",
										brand_str.trim()));
								params.add(new BasicNameValuePair("methods",
										methods_str.trim()));
								params.add(new BasicNameValuePair("time",
										System.currentTimeMillis() + ""));
								MyUtils.getJson2(baseUrl, params);

								mHandler.post(new Runnable() {

									@Override
									public void run() {
										dialog.dismiss();
										Toast.makeText(getApplicationContext(),
												"添加成功", 0).show();
										SearchResultActivity.this.finish();
									}
								});
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								mHandler.post(new Runnable() {

									@Override
									public void run() {
										Toast.makeText(getApplicationContext(),
												"添加失败，请稍后再试", 0).show();
										SearchResultActivity.this.finish();
									}
								});
								e.printStackTrace();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			});
			return convertView;
		}
	}

	private static class ViewHolder {
		private ImageView img;
		private TextView name;
		private TextView spec;
		private TextView price;
		private ImageView add;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.r1:
			finish();
			break;

		default:
			break;
		}
	}
}
