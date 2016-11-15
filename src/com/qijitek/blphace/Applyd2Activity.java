package com.qijitek.blphace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qijitek.database.UserAddress;
import com.qijitek.utils.MyUtils;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

public class Applyd2Activity extends Activity implements OnClickListener {
	private String name = "";
	private String content = "";
	private String imgurl = "";
	private ImageView demo_img;
	private TextView content_txt;
	private int width;
	private int height;
	private int dp_hei;
	private Button apply;
	private Handler mHandler;
	private boolean isUpdate = false;
	private UserAddress userAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applyd2);
		init();
		init_address();
	}

	private void init_address() {
		isUpdate = getIntent().getBooleanExtra("isupdate", false);
		if (isUpdate) {
			userAddress = (UserAddress) getIntent().getExtras().get(
					"useraddress");
		}
	}

	private void init() {
		//
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		height = metrics.heightPixels;
		dp_hei = (int) (180f / (MyUtils.px2dip(getApplicationContext(), height)) * height);
		//
		mHandler = new Handler() {
		};
		apply = (Button) findViewById(R.id.apply);
		apply.setOnClickListener(this);
		if (!getIntent().getStringExtra("isapply").equals("0")) {
			apply.setText("已申请");
			apply.setClickable(false);
		}
		name = getIntent().getStringExtra("name");
		content = getIntent().getStringExtra("content");
		imgurl = getIntent().getStringExtra("imgurl");
		demo_img = (ImageView) findViewById(R.id.demo_img);
		content_txt = (TextView) findViewById(R.id.content_txt);
		Picasso.with(getApplicationContext()).load(imgurl).into(demo_img);
		content_txt.setText("\b\b\b\b" + content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.apply:
			Intent intent = new Intent(Applyd2Activity.this,
					MyAddressActivity.class);
			intent.putExtra("isupdate", isUpdate);
			if (isUpdate) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("useraddress", userAddress);
				intent.putExtras(bundle);
			}
			intent.putExtra("applyFlag", true);
			intent.putExtra("code", getIntent().getStringExtra("code"));
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		apply.setText("已申请");
		apply.setClickable(false);
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
