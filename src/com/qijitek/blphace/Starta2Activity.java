package com.qijitek.blphace;

import java.util.ArrayList;
import java.util.List;

import com.qijitek.blphace.Socialc1Activity.MyPagerAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Starta2Activity extends android.support.v4.app.FragmentActivity
		implements OnClickListener {
	private Button next;
	private ViewPager pager;
	private List<Fragment> fragments;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starta2);
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(this);

		fragmentManager = getSupportFragmentManager();
		fragments = new ArrayList<Fragment>();
		pager = (ViewPager) findViewById(R.id.pager);
		fragments.add(new Fragment_a21());
		fragments.add(new Fragment_a22());
		fragments.add(new Fragment_a23());
		fragments.add(new Fragment_a24());
		pager.setAdapter(new MyPagerAdapter(fragmentManager, fragments));
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == fragments.size() - 1) {
					next.setVisibility(View.VISIBLE);
				} else {
					next.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	class MyPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> list = new ArrayList<Fragment>();

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public MyPagerAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			this.list = list;
		}

		@Override
		public Fragment getItem(int index) {
			return list.get(index);
		}

		@Override
		public int getCount() {
			return list.size();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next:
			startActivity(new Intent(Starta2Activity.this,
					Starta3Activity.class));
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
}
