package com.qijitek.blphace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.qijitek.utils.SharedpreferencesUtil;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	private Button bt1;
	private Button bt2;
	private Button bt3;
	private Button bt4;
	private RelativeLayout tips;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		initSlidingMenu(savedInstanceState);
	}

	private void init() {
		tips = (RelativeLayout) findViewById(R.id.tips);
		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		bt3 = (Button) findViewById(R.id.bt3);
		bt4 = (Button) findViewById(R.id.bt4);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		// initTips();
	}

	private void initTips() {
		bt1.setClickable(false);
		bt2.setClickable(false);
		bt3.setClickable(false);
		bt4.setClickable(false);
	}

	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_layout);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commit();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidth(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt1:
			if (!new SharedpreferencesUtil(getApplicationContext()).getIsTest()) {
				startActivity(new Intent(MainActivity.this,
						Starta1Activity.class));
			} else {
				startActivity(new Intent(MainActivity.this,
						Starta5Activity2.class));
			}
			break;
		case R.id.bt2:
			startActivity(new Intent(MainActivity.this, Manageb1Activity.class));
			break;
		case R.id.bt3:
			startActivity(new Intent(MainActivity.this, Socialc1Activity2.class));
			break;
		case R.id.bt4:
			startActivity(new Intent(MainActivity.this, Applyd12Activity.class));
			break;
		case R.id.tips:
			tips.setVisibility(View.INVISIBLE);
			bt1.setClickable(true);
			bt2.setClickable(true);
			bt3.setClickable(true);
			bt4.setClickable(true);
			break;
		case R.id.slide_menu:
			toggle();
			break;
		default:
			break;
		}
	}

}
