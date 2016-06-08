package com.qijitek.blphace;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Startc1Activity extends android.support.v4.app.FragmentActivity
		implements OnCheckedChangeListener {
	private FragmentManager fragmentManager;
	private RadioGroup radioGroup;
	private ViewPager pager;
	private List<Fragment> fragments;
	private RadioButton bt1;
	private RadioButton bt2;
	private RadioButton bt3;
	private RadioButton bt4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startc1);
		init();
	}

	private void init() {
		fragmentManager = getSupportFragmentManager();
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(this);
		bt1 = (RadioButton) findViewById(R.id.bt1);
		bt2 = (RadioButton) findViewById(R.id.bt2);
		bt3 = (RadioButton) findViewById(R.id.bt3);
		pager = (ViewPager) findViewById(R.id.pager);
		initViewPager();
	}

	@SuppressWarnings("deprecation")
	private void initViewPager() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new Fragment_c11());
		fragments.add(new Fragment_c11());
		fragments.add(new Fragment_c11());
		pager.setAdapter(new MyPagerAdapter(fragmentManager, fragments));
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				System.out.println(index + "index");
				switch (index) {
				case 0:
					bt1.setChecked(true);
					break;
				case 1:
					bt2.setChecked(true);
					break;
				case 2:
					bt3.setChecked(true);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

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
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.bt1:
			pager.setCurrentItem(0);
			break;
		case R.id.bt2:
			pager.setCurrentItem(1);
			break;
		case R.id.bt3:
			pager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}
}
