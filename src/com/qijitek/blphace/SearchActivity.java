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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.qijitek.constant.NomalConstant;
import com.qijitek.database.SingleItem;
import com.qijitek.utils.MyUtils;
import com.qijitek.view.ProgersssDialog;
import com.umeng.analytics.MobclickAgent;

public class SearchActivity extends Activity implements OnClickListener {
	private ListView list;
	private ArrayList<SingleItem> singleItems;
	private Handler mHandler;
	private EditText query;
	private Button search;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;
	private Button button7;
	private Button button8;
	private ProgersssDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
	}

	private void init() {
		mHandler = new Handler() {
		};
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		button5 = (Button) findViewById(R.id.button5);
		button6 = (Button) findViewById(R.id.button6);
		button7 = (Button) findViewById(R.id.button7);
		button8 = (Button) findViewById(R.id.button8);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);
		button7.setOnClickListener(this);
		button8.setOnClickListener(this);
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(this);
		query = (EditText) findViewById(R.id.query);

		list = (ListView) findViewById(R.id.list);
		singleItems = new ArrayList<SingleItem>();
		getDefaultItemList();
	}

	protected void getDefaultItemList() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					JSONObject jsonObject = MyUtils
							.getJson("http://api.qijitek.com/getDefaultSearchItem/");
					JSONObject obj = (JSONObject) jsonObject.opt("obj");
					final JSONArray list = (JSONArray) obj.get("list");
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							button1.setText(list.optString(0));
							button2.setText(list.optString(1));
							button3.setText(list.optString(2));
							button4.setText(list.optString(3));
							button5.setText(list.optString(4));
							button6.setText(list.optString(5));
							button7.setText(list.optString(6));
							button8.setText(list.optString(7));

						}
					});
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "请检查网络连接",
									0).show();
							SearchActivity.this.finish();
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	protected void getSearchList(final String query_txt) {
		if (query_txt.equals("")) {
			Toast.makeText(getApplicationContext(), "请输入关键字", 0).show();
			return;
		}
		dialog = new ProgersssDialog(SearchActivity.this, "");
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String baseurl = "http://openapi.jimi.la/openapi/";

					List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
					params.add(new BasicNameValuePair("query", query_txt));
					params.add(new BasicNameValuePair("appid",
							NomalConstant.QijiID));
					params.add(new BasicNameValuePair("token", MyUtils
							.md5(query_txt + NomalConstant.QijiSecret)));

					JSONObject jsonObject = MyUtils.getJson2(baseurl, params);
					if (jsonObject.optBoolean("success")) {
						Intent intent = new Intent(SearchActivity.this,
								SearchResultActivity.class);
						intent.putExtra("json", jsonObject.toString() + "");
						startActivity(intent);
					} else {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),
										"搜索失败，请稍后再试", 0).show();
								SearchActivity.this.finish();
							}
						});
					}
					dialog.dismiss();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "请检查网络连接",
									0).show();
							SearchActivity.this.finish();
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			getSearchList(query.getText().toString().trim());
			break;
		case R.id.button1:
			getSearchList(button1.getText().toString().trim());
			break;
		case R.id.button2:
			getSearchList(button2.getText().toString().trim());
			break;
		case R.id.button3:
			getSearchList(button3.getText().toString().trim());
			break;
		case R.id.button4:
			getSearchList(button4.getText().toString().trim());
			break;
		case R.id.button5:
			getSearchList(button5.getText().toString().trim());
			break;
		case R.id.button6:
			getSearchList(button6.getText().toString().trim());
			break;
		case R.id.button7:
			getSearchList(button7.getText().toString().trim());
			break;
		case R.id.button8:
			getSearchList(button8.getText().toString().trim());
			break;
		default:
			break;
		}
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
