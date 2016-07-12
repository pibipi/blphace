package com.qijitek.blphace;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;

public class AdviceCallbackActivity extends Activity implements OnClickListener {
	private ImageView advice_callback_back;
	private EditText content_txt;
	private EditText phone_txt;
	private Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice_callback);
		init();
	}

	private void init() {
		advice_callback_back = (ImageView) findViewById(R.id.advice_callback_back);
		advice_callback_back.setOnClickListener(this);
		content_txt = (EditText) findViewById(R.id.content_txt);
		phone_txt = (EditText) findViewById(R.id.phone_txt);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.advice_callback_back:
			finish();
			break;
		case R.id.submit:
			new Thread(new Runnable() {
				@Override
				public void run() {
					String url = "http://api.qijitek.com/userAdvice/?userid="
							+ new SharedpreferencesUtil(getApplicationContext())
									.getUserid() + "&phone="
							+ phone_txt.getText().toString().trim()
							+ "&content="
							+ content_txt.getText().toString().trim();
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
			Toast.makeText(getApplicationContext(), "提交成功", 0).show();
			break;
		default:
			break;
		}
	}
}