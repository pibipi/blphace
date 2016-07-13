package com.qijitek.blphace;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.qijitek.database.SocialArticle;
import com.qijitek.utils.MyUtils;
import com.squareup.picasso.Picasso;

public class Socialc1Activity2 extends Activity implements OnItemClickListener,
		OnClickListener {
	private ListView socialc1_list;
	private ArrayList<SocialArticle> article_data;
	private MyAdapter myAdapter;
	private Handler mHandler;
	private int width;
	private int height;
	private int dp_hei;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_socialc12);
		init();
		socialc1_list.setAdapter(myAdapter);
		socialc1_list.setOnItemClickListener(this);
	}

	private void init() {
		//
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		height = metrics.heightPixels;
		dp_hei = (int) (180f / (MyUtils.px2dip(getApplicationContext(), height)) * height);
		System.out.println(height);
		System.out.println((MyUtils.px2dip(getApplicationContext(), height)));
		System.out.println(dp_hei);
		//
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
		article_data = new ArrayList<SocialArticle>();
		socialc1_list = (ListView) findViewById(R.id.socialc1_list);
		myAdapter = new MyAdapter(getApplicationContext());
		new Thread(new Runnable() {

			@Override
			public void run() {
				getArticleData();
			}
		}).start();

	}

	private void getArticleData() {
		try {
			JSONObject jsonObject = MyUtils
					.getJson("http://api.qijitek.com/getArticles/");
			System.out.println(jsonObject.toString());
			JSONArray obj = jsonObject.getJSONArray("obj");
			System.out.println(obj.length());
			int len = obj.length();
			for (int i = 0; i < len; i++) {
				JSONObject json = (JSONObject) obj.opt(i);
				String url = json.getString("url");
				String imgurl = json.getString("imageurl");
				String summary = json.getString("summary");
				String type = json.getString("type");
				String title = json.getString("title");
				System.out.println("url" + url + "imgurl" + imgurl + "title"
						+ title);
				SocialArticle sa = new SocialArticle(title, summary, type, url,
						imgurl);
				article_data.add(sa);
				mHandler.sendEmptyMessage(1);
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
		private Context context;

		public MyAdapter(Context context) {
			super();
			this.layoutInflater = LayoutInflater.from(context);
			this.context = context;
		}

		@Override
		public int getCount() {
			return article_data.size();
		}

		@Override
		public Object getItem(int position) {
			return article_data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.list_socialc1,
						null);
				viewHolder = new ViewHolder();
				viewHolder.image = (ImageView) convertView
						.findViewById(R.id.image);
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.title);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			SocialArticle sa = article_data.get(position);
			viewHolder.title.setText(sa.getTitle());
			Picasso.with(context).load(sa.getImgurl()).resize(1062, 585)
					.centerCrop().into(viewHolder.image);
			System.out.println("img wid" + viewHolder.image.getHeight());
			return convertView;
		}
	}

	private static class ViewHolder {
		private ImageView image;
		private TextView title;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SocialArticle sa = article_data.get(position);
		Intent intent = new Intent(Socialc1Activity2.this,
				Socialc2Activity2.class);
		intent.putExtra("url", sa.getUrl());
		startActivity(intent);
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
}
