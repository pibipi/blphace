package com.qijitek.blphace;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.qijitek.utils.SharedpreferencesUtil;
import com.qijitek.view.NotScollViewPager;
import com.umeng.analytics.MobclickAgent;

public class Starta2Activity extends android.support.v4.app.FragmentActivity
		implements OnClickListener {
	public final static String ACTION_CHANGE_PAGER = "com.qijitek.blphace.ACTION_CHANGE_PAGER";
	private Button next;
	private NotScollViewPager pager;
	private List<Fragment> fragments;
	private FragmentManager fragmentManager;
	private int sum_score = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starta2);
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		new SharedpreferencesUtil(getApplicationContext()).saveSumScore(0);
		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(this);
		fragmentManager = getSupportFragmentManager();
		fragments = new ArrayList<Fragment>();
		pager = (NotScollViewPager) findViewById(R.id.pager);
		fragments.add(new Fragment_a21());
		fragments.add(new Fragment_a22());
		fragments.add(new Fragment_a23());
		fragments.add(new Fragment_a24());
		fragments.add(new Fragment_a25());
		fragments.add(new Fragment_a26());
		fragments.add(new Fragment_a27());
		fragments.add(new Fragment_a28());
		pager.setAdapter(new MyPagerAdapter(fragmentManager, fragments));
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// if (arg0 == fragments.size() - 1) {
				// next.setVisibility(View.VISIBLE);
				// } else {
				// next.setVisibility(View.GONE);
				// }
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

	private static IntentFilter pagerIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CHANGE_PAGER);
		return intentFilter;
	}

	private final BroadcastReceiver pagerReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			int count = intent.getIntExtra("pager", 0);
			int score = intent.getIntExtra("score", 0);
			System.out.println("count" + count);
			sum_score += score;
			if (count == 8) {
				if (new SharedpreferencesUtil(getApplicationContext())
						.getIsTesting()) {
				}
				Starta2Activity.this.finish();
				startActivity(new Intent(Starta2Activity.this,
						Starta3Activity.class));
				System.out.println(sum_score + "sum_score");
				new SharedpreferencesUtil(getApplicationContext())
						.saveSumScore(sum_score);
			} else {
				pager.setCurrentItem(count);
			}
		}
	};

	@Override
	protected void onResume() {
		registerReceiver(pagerReceiver, pagerIntentFilter());
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		unregisterReceiver(pagerReceiver);
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
