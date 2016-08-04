package com.qijitek.blphace;

import java.util.ArrayList;
import java.util.List;

import com.qijitek.utils.SharedpreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	private ViewPager pager;
	private RelativeLayout start;
	private List<View> list;
	private ImageView img1;
	private ImageView img2;
	private ImageView img3;
	private ImageView img4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		init();
	}

	private void init() {
		img1 = (ImageView) findViewById(R.id.iv1);
		img2 = (ImageView) findViewById(R.id.iv2);
		img3 = (ImageView) findViewById(R.id.iv3);
		img4 = (ImageView) findViewById(R.id.iv4);
		pager = (ViewPager) findViewById(R.id.pager);
		start = (RelativeLayout) findViewById(R.id.start);
		start.setOnClickListener(this);
		list = new ArrayList<View>();
		ImageView iv1 = new ImageView(getApplicationContext());
		iv1.setBackgroundResource(R.drawable.guide1);
		ImageView iv2 = new ImageView(getApplicationContext());
		iv2.setBackgroundResource(R.drawable.guide2);
		ImageView iv3 = new ImageView(getApplicationContext());
		iv3.setBackgroundResource(R.drawable.guide3);
		ImageView iv4 = new ImageView(getApplicationContext());
		iv4.setBackgroundResource(R.drawable.guide4);
		list.add(iv1);
		list.add(iv2);
		list.add(iv3);
		list.add(iv4);
		pager.setAdapter(new MyPagerAdapter());
		pager.setOnPageChangeListener(this);
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start:
			final boolean isLogin = new SharedpreferencesUtil(
					getApplicationContext()).getIsLogin();
			if (isLogin) {
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
			} else {
				startActivity(new Intent(GuideActivity.this,
						LoginActivity.class));
			}
			this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		if (arg0 == 0) {
			img1.setBackgroundResource(R.drawable.guide_point_color);
			img2.setBackgroundResource(R.drawable.guide_point_gray);
			img3.setBackgroundResource(R.drawable.guide_point_gray);
			img4.setBackgroundResource(R.drawable.guide_point_gray);
		} else if (arg0 == 1) {
			img2.setBackgroundResource(R.drawable.guide_point_color);
			img1.setBackgroundResource(R.drawable.guide_point_gray);
			img3.setBackgroundResource(R.drawable.guide_point_gray);
			img4.setBackgroundResource(R.drawable.guide_point_gray);
		} else if (arg0 == 2) {
			img3.setBackgroundResource(R.drawable.guide_point_color);
			img2.setBackgroundResource(R.drawable.guide_point_gray);
			img1.setBackgroundResource(R.drawable.guide_point_gray);
			img4.setBackgroundResource(R.drawable.guide_point_gray);
		} else if (arg0 == 3) {
			img4.setBackgroundResource(R.drawable.guide_point_color);
			img2.setBackgroundResource(R.drawable.guide_point_gray);
			img3.setBackgroundResource(R.drawable.guide_point_gray);
			img1.setBackgroundResource(R.drawable.guide_point_gray);
			start.setVisibility(View.VISIBLE);
		}
	}
}
