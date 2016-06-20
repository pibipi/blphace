package com.qijitek.blphace;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Manageb2Activity extends android.support.v4.app.FragmentActivity
		implements OnCheckedChangeListener, OnClickListener {
	private FragmentManager fragmentManager;
	private RadioGroup radioGroup;
	private ViewPager pager;
	private List<Fragment> fragments;
	private RadioButton bt1;
	private RadioButton bt2;
	private ImageView add_product;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manageb2);
		init();
	}

	private void init() {
		add_product = (ImageView) findViewById(R.id.add_product);
		add_product.setOnClickListener(this);
		fragmentManager = getSupportFragmentManager();
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(this);
		bt1 = (RadioButton) findViewById(R.id.bt1);
		bt2 = (RadioButton) findViewById(R.id.bt2);
		pager = (ViewPager) findViewById(R.id.pager);
		initViewPager();
	}

	@SuppressWarnings("deprecation")
	private void initViewPager() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new Fragment_b21());
		fragments.add(new Fragment_b22());
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
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_product:
			showDialog();
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	private void showDialog() {
		AlertDialog add_Dialog = new AlertDialog.Builder(Manageb2Activity.this)
				.create();
		add_Dialog.show();

		Window window = add_Dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		// window.setWindowAnimations(R.style.mystyle); // 添加动画

		add_Dialog.getWindow().setContentView(R.layout.dialog_add_product);
		add_Dialog.setCancelable(true);
		ImageView add1 = (ImageView) add_Dialog.getWindow().findViewById(
				R.id.add1);
		add1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Manageb2Activity.this,
						MipcaActivityCapture.class));
			}
		});
	}

}
