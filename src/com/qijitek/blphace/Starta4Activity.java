package com.qijitek.blphace;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;

public class Starta4Activity extends Activity implements OnClickListener {
	private Button next;
	private TextView title;
	private TextView content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starta4);
		init();
		initContent();
	}

	private void initContent() {
		int sum_score = new SharedpreferencesUtil(getApplicationContext())
				.getSumScore();
		System.out.println(sum_score + "sum_score");
		if (sum_score >= 8 && sum_score < 12) {
			new SharedpreferencesUtil(getApplicationContext())
					.saveSkintype("1");
			title.setText("您的肌肤类型是：干性皮肤");
			content.setText(MyUtils.getSkintypeText("1"));
		} else if (sum_score >= 12 && sum_score < 28) {
			new SharedpreferencesUtil(getApplicationContext())
					.saveSkintype("2");
			title.setText("您的肌肤类型是：混合性皮肤");
			content.setText(MyUtils.getSkintypeText("2"));
		} else if (sum_score >= 28 && sum_score <= 40) {
			new SharedpreferencesUtil(getApplicationContext())
					.saveSkintype("3");
			title.setText("您的肌肤类型是：油性皮肤");
			content.setText(MyUtils.getSkintypeText("3"));
		}
	}

	private void init() {
		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);
		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next:
			// if (!new SharedpreferencesUtil(getApplicationContext())
			// .getIsTesting()) {
			// startActivity(new Intent(Starta4Activity.this,
			// Starta5Activity2.class));
			// } else {
			// finish();
			// }
			if (new SharedpreferencesUtil(getApplicationContext()).getIsTest()) {
				finish();
			} else {
				startActivity(new Intent(Starta4Activity.this,
						Starta5Activity2.class));
				finish();
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					String url="http://api.qijitek.com/updateSkintype/?userid="+new SharedpreferencesUtil(getApplicationContext()).getUserid()+"&skintype="+new SharedpreferencesUtil(getApplicationContext()).getSkintype();
					try {
						MyUtils.getJson(url);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			new SharedpreferencesUtil(getApplicationContext()).saveIsTest(true);
			new SharedpreferencesUtil(getApplicationContext())
					.saveIsTesting(false);
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
}
