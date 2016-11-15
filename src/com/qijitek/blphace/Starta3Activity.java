package com.qijitek.blphace;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.qijitek.spinner.SpinerAdapter;
import com.qijitek.spinner.SpinerPopWindow;
import com.qijitek.utils.SharedpreferencesUtil;
import com.umeng.analytics.MobclickAgent;

public class Starta3Activity extends Activity implements OnClickListener {
	private List<String> mListType = new ArrayList<String>(); // 类型列表
	private List<String> mListType2 = new ArrayList<String>(); // 类型列表
	private TextView mTView;
	private SpinerAdapter mAdapter;
	// 设置PopWindow
	private SpinerPopWindow mSpinerPopWindow;

	private TextView mTView2;
	private SpinerAdapter mAdapter2;
	// 设置PopWindow2
	private SpinerPopWindow mSpinerPopWindow2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starta3);
		init_q1();
		init_q2();

	}

	private void init_q2() {
		mTView2 = (TextView) findViewById(R.id.tv_value2);

		// 初始化数据
		mListType2.add("用到空瓶");
		mListType2.add("半途而废");
		mListType2.add("三分钟热度");

		mAdapter2 = new SpinerAdapter(this, mListType2);
		mAdapter2.refreshData(mListType2, 0);

		// 显示第一条数据
		mTView2.setText("用到空瓶");

		// 初始化PopWindow
		mSpinerPopWindow2 = new SpinerPopWindow(this);
		mSpinerPopWindow2.setAdatper(mAdapter2);
		mSpinerPopWindow2
				.setItemListener(new SpinerAdapter.IOnItemSelectListener() {

					@Override
					public void onItemClick(int pos) {
						if (pos >= 0 && pos <= mListType2.size()) {
							String value = mListType2.get(pos);
							mTView2.setText(value.toString());
						}
					}
				});
	}

	private void init_q1() {
		mTView = (TextView) findViewById(R.id.tv_value);

		// 初始化数据
		mListType.add("阿玛尼");
		mListType.add("娇韵诗");
		mListType.add("菲诗小铺");
		mListType.add("雪花秀");
		mListType.add("CPB");
		mListType.add("奥尔滨");
		mListType.add("悦诗风吟");
		mListType.add("兰蔻");
		mListType.add("资生堂");
		mListType.add("YSL");
		mListType.add("雅诗兰黛");

		mAdapter = new SpinerAdapter(this, mListType);
		mAdapter.refreshData(mListType, 0);

		// 显示第一条数据
		mTView.setText("CPB");

		// 初始化PopWindow
		mSpinerPopWindow = new SpinerPopWindow(this);
		mSpinerPopWindow.setAdatper(mAdapter);
		mSpinerPopWindow
				.setItemListener(new SpinerAdapter.IOnItemSelectListener() {

					@Override
					public void onItemClick(int pos) {
						if (pos >= 0 && pos <= mListType.size()) {
							String value = mListType.get(pos);
							mTView.setText(value.toString());
						}
					}
				});
	}

	private void showSpinWindow() {
		Log.e("", "showSpinWindow");
		mSpinerPopWindow.setWidth(mTView.getWidth());
		mSpinerPopWindow.showAsDropDown(mTView);
	}

	private void showSpinWindow2() {
		Log.e("", "showSpinWindow2");
		mSpinerPopWindow2.setWidth(mTView2.getWidth());
		mSpinerPopWindow2.showAsDropDown(mTView2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_dropdown:
			showSpinWindow();
			break;
		case R.id.tv_value:
			showSpinWindow();
			break;
		case R.id.tv_value2:
			showSpinWindow2();
			break;
		case R.id.next:
			// int sum_score = new
			// SharedpreferencesUtil(getApplicationContext())
			// .getSumScore();
			// System.out.println(sum_score + "sum_score");
			// if (sum_score >= 8 && sum_score < 12) {
			// new SharedpreferencesUtil(getApplicationContext())
			// .saveSkintype("1");
			// } else if (sum_score >= 12 && sum_score < 28) {
			// new SharedpreferencesUtil(getApplicationContext())
			// .saveSkintype("2");
			// } else if (sum_score >= 28 && sum_score <= 40) {
			// new SharedpreferencesUtil(getApplicationContext())
			// .saveSkintype("3");
			// }
			if (new SharedpreferencesUtil(getApplicationContext())
					.getIsTesting()) {
			}
			finish();
			startActivity(new Intent(Starta3Activity.this,
					Starta4Activity.class));

			// new
			// SharedpreferencesUtil(getApplicationContext()).saveIsTest(true);
			// new
			// SharedpreferencesUtil(getApplicationContext()).saveIsTesting(false);
			break;
		case R.id.back:
			finish();
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gaochun.adapter.SpinerAdapter.IOnItemSelectListener#onItemClick(int)
	 */
	// @Override
	// public void onItemClick(int pos) {
	// if (pos >= 0 && pos <= mListType.size()) {
	// String value = mListType.get(pos);
	// mTView.setText(value.toString());
	// }
	// }
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
