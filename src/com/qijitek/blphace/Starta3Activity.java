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

public class Starta3Activity extends Activity implements OnClickListener,
		SpinerAdapter.IOnItemSelectListener {
	private List<String> mListType = new ArrayList<String>(); // 类型列表
	private TextView mTView;
	private SpinerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starta4);
		mTView = (TextView) findViewById(R.id.tv_value);

		// 初始化数据
		mListType.add("雅诗兰黛");
		mListType.add("欧莱雅");
		mListType.add("纯净水");
		mListType.add("蒸馏水");

		mAdapter = new SpinerAdapter(this, mListType);
		mAdapter.refreshData(mListType, 0);

		// 显示第一条数据
		// mTView.setText(names[0]);

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
			startActivity(new Intent(Starta3Activity.this,
					Starta4Activity.class));
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
