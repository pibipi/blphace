package com.qijitek.blphace;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qijitek.database.ApplyDemo;
import com.qijitek.database.UserAddress;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.squareup.picasso.Picasso;

@SuppressLint("SimpleDateFormat")
public class Applyd12Activity extends Activity implements OnClickListener,
		OnItemClickListener {
	private ListView applyd1_list;
	private Context context;
	private ArrayList<ApplyDemo> demo_data;
	private MyAdapter myAdapter;
	private Handler mHandler;
	int dp_hei;
	int width;
	int height;
	private boolean isUpdate = false;
	private UserAddress userAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applyd12);
		init();
		applyd1_list.setAdapter(myAdapter);
	}

	private void init() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					myAdapter.notifyDataSetChanged();
					break;

				default:
					break;
				}
			}

		};
		//
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		height = metrics.heightPixels;
		dp_hei = (int) (180f / (MyUtils.px2dip(getApplicationContext(), height)) * height);
		//
		demo_data = new ArrayList<ApplyDemo>();
		applyd1_list = (ListView) findViewById(R.id.applyd1_list);
		applyd1_list.setOnItemClickListener(this);
		context = getApplicationContext();
		myAdapter = new MyAdapter(context);
	}

	private void getDemoData() {
		try {
			JSONObject jsonObject = MyUtils
					.getJson("http://api.qijitek.com/getDemos/?userid="
							+ new SharedpreferencesUtil(getApplicationContext())
									.getUserid());
			demo_data.clear();
			System.out.println(jsonObject.toString());
			JSONArray obj = jsonObject.getJSONArray("obj");
			System.out.println(obj.length());
			int len = obj.length();
			for (int i = 0; i < len; i++) {
				JSONObject json = (JSONObject) obj.opt(i);
				String code = json.getString("code");
				String name = json.getString("name");
				String people = json.getString("people");
				String imageurl = json.getString("imageurl");
				String content = json.getString("content");
				String time = json.getString("time");
				String total = json.getString("total");
				String isapply = json.getString("isapply");
				ApplyDemo ad = new ApplyDemo(code, name, people, imageurl,
						content, time, total, isapply);
				demo_data.add(ad);
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "请检查网络连接", 0)
							.show();
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater layoutInflater;

		public MyAdapter(Context context) {
			super();
			this.layoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return demo_data.size();
		}

		@Override
		public Object getItem(int position) {
			return demo_data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.list_applyd12,
						null);
				viewHolder = new ViewHolder();
				viewHolder.image = (ImageView) convertView
						.findViewById(R.id.image);
				viewHolder.days = (TextView) convertView
						.findViewById(R.id.days);
				viewHolder.total = (TextView) convertView
						.findViewById(R.id.total);
				viewHolder.people = (TextView) convertView
						.findViewById(R.id.people);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			ApplyDemo ad = demo_data.get(position);
			System.out.println("wid" + width + "dp_hei" + dp_hei);
			Picasso.with(context).load(ad.getImageurl())
			// .resize(width, dp_hei)
			// .centerCrop()
					.into(viewHolder.image);
			viewHolder.people.setText("已参加：" + ad.getPeople() + "人");
			viewHolder.total.setText(ad.getTotal());
			//
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy.MM.dd.HH.mm.ss");
			try {
				Date date = format.parse(ad.getTime());
				long dt = date.getTime() - System.currentTimeMillis();
				String dt_str = "";
				System.out.println(System.currentTimeMillis() + "dt" + dt);
				if (dt > 0) {
					dt_str = (int) (dt / 86400000f + 1) + "";
				} else {
					dt_str = "0";
				}
				viewHolder.days.setText(dt_str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//
			return convertView;
		}

	}

	private static class ViewHolder {
		private ImageView image;
		private TextView days;
		private TextView total;
		private TextView people;
		private TextView time;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ApplyDemo ad = demo_data.get(position);
		Intent intent = new Intent(Applyd12Activity.this, Applyd2Activity.class);
		intent.putExtra("name", ad.getName());
		intent.putExtra("content", ad.getContent());
		intent.putExtra("imgurl", ad.getImageurl());
		intent.putExtra("code", ad.getCode());
		intent.putExtra("isapply", ad.getIsapply());
		intent.putExtra("isupdate", isUpdate);
		Bundle bundle = new Bundle();
		bundle.putSerializable("useraddress", userAddress);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void isUpdateAddress() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = "http://api.qijitek.com/getAddress/?userid="
						+ new SharedpreferencesUtil(getApplicationContext())
								.getUserid();
				try {
					JSONObject jsonObject = MyUtils.getJson(url);
					String msg = jsonObject.optString("msg");
					if (msg.equals("已提交")) {
						isUpdate = true;
						JSONArray obj_array = jsonObject.getJSONArray("obj");
						JSONObject obj_json = (JSONObject) obj_array.opt(0);
						userAddress = new UserAddress(
								obj_json.optString("userid"),
								obj_json.optString("name"),
								obj_json.optString("age"),
								obj_json.optString("sex"),
								obj_json.optString("skintype"),
								obj_json.optString("phone"),
								obj_json.optString("province"),
								obj_json.optString("address"));
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "请检查网络连接",
									0).show();
						}
					});
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	@Override
	protected void onResume() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getDemoData();
				isUpdateAddress();
			}
		}).start();
		super.onResume();
	}

}
