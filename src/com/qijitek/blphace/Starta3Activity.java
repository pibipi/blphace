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
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;

public class Starta3Activity extends Activity implements OnClickListener,
		SpinerAdapter.IOnItemSelectListener {
	private List<String> mListType = new ArrayList<String>(); // 类型列表
	private TextView mTView;
	private SpinerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starta3);
		mTView = (TextView) findViewById(R.id.tv_value);

		// 初始化数据
		mListType.add("雅诗兰黛");
		mListType.add("欧莱雅");
		mListType.add("兰蔻");
		mListType.add("就知道三个");
		mListType.add("还有什么");

		mAdapter = new SpinerAdapter(this, mListType);
		mAdapter.refreshData(mListType, 0);

		// 显示第一条数据
		mTView.setText("雅诗兰黛");

		// 初始化PopWindow
		mSpinerPopWindow = new SpinerPopWindow(this);
		mSpinerPopWindow.setAdatper(mAdapter);
		mSpinerPopWindow.setItemListener(this);
	}

	// 设置PopWindow
	private SpinerPopWindow mSpinerPopWindow;

	private void showSpinWindow() {
		Log.e("", "showSpinWindow");
		mSpinerPopWindow.setWidth(mTView.getWidth());
		mSpinerPopWindow.showAsDropDown(mTView);
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
	@Override
	public void onItemClick(int pos) {
		// TODO Auto-generated method stub
		if (pos >= 0 && pos <= mListType.size()) {
			String value = mListType.get(pos);
			mTView.setText(value.toString());
		}
	}
}
