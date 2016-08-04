package com.qijitek.blphace;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	private Button bt1;
	private Button bt2;
	private Button bt3;
	private Button bt4;
	private RelativeLayout tips;
	private Handler mHandler;
	private String last_versionName = "";
	private AlertDialog reset_Dialog;
	private NotificationManager mNotificationManager;
	private long mExitTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		initSlidingMenu(savedInstanceState);
		new Thread(new Runnable() {
			@Override
			public void run() {
				init_update();
			}
		}).start();
		if (new SharedpreferencesUtil(getApplicationContext()).getIsFirst1()) {
			showTips2();
			showTips1();
			new SharedpreferencesUtil(getApplicationContext())
					.saveIsFirst1(false);
		}
	}

	private void init_update() {
		try {
			last_versionName = MyUtils.getVersionName(getApplicationContext());
			System.out.println(last_versionName + "last_versionName");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		try {
			boolean need_update = false;
			JSONObject jsonObject = MyUtils
					.getJson("http://api.qijitek.com/getVersion");
			String new_versionName = jsonObject.optString("msg").toString();
			if (new_versionName.equals(last_versionName)) {
				need_update = false;
			} else {
				need_update = true;
				mHandler.sendEmptyMessage(1);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		reset_Dialog = new AlertDialog.Builder(MainActivity.this).create();
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					showDialog();
					break;
				case 666:
					int progress = msg.arg1;
					if (progress > 0 && progress < 100) {
						initNotify(progress, 100, "昕颜正在下载更新...");
					} else {
						// initNotify(0, 0, "下载完成，点击安装");
						mNotificationManager.cancelAll();
					}
					break;
				default:
					break;
				}
			}

		};
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

	private void showTips2() {
		final AlertDialog tips1_Dialog = new AlertDialog.Builder(
				MainActivity.this, R.style.dialog).create();
		tips1_Dialog.show();

		Window window = tips1_Dialog.getWindow();
		// window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		// window.setWindowAnimations(R.style.dialog); // 添加动画
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = tips1_Dialog.getWindow()
				.getAttributes();
		lp.width = (int) (display.getWidth() - 2); // 设置宽度
		lp.height = (int) (display.getHeight() - 50); // 设置宽度
		tips1_Dialog.getWindow().setAttributes(lp);

		tips1_Dialog.getWindow().setContentView(R.layout.dialog_tips_main2);
		tips1_Dialog.setCancelable(true);
		tips1_Dialog.getWindow().findViewById(R.id.t1)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						tips1_Dialog.dismiss();
					}
				});
	}

	private void showTips1() {
		final AlertDialog tips1_Dialog = new AlertDialog.Builder(
				MainActivity.this, R.style.dialog).create();
		tips1_Dialog.show();

		Window window = tips1_Dialog.getWindow();
		// window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		// window.setWindowAnimations(R.style.dialog); // 添加动画
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = tips1_Dialog.getWindow()
				.getAttributes();
		lp.width = (int) (display.getWidth() - 2); // 设置宽度
		lp.height = (int) (display.getHeight() - 50); // 设置宽度
		tips1_Dialog.getWindow().setAttributes(lp);

		tips1_Dialog.getWindow().setContentView(R.layout.dialog_tips_main1);
		tips1_Dialog.setCancelable(true);
		tips1_Dialog.getWindow().findViewById(R.id.t1)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						tips1_Dialog.dismiss();
					}
				});
	}

	private void showDialog() {
		reset_Dialog.show();

		Window window = reset_Dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		// window.setWindowAnimations(R.style.mystyle); // 添加动画

		reset_Dialog.getWindow().setContentView(R.layout.dialog_update);
		reset_Dialog.getWindow().findViewById(R.id.yes)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(getApplicationContext(), "后台下载中...", 0)
								.show();
						new Thread(new Runnable() {

							@Override
							public void run() {
								MyUtils.downloadApk(getApplicationContext(),
										mHandler);
							}
						}).start();
						reset_Dialog.dismiss();
					}
				});
		reset_Dialog.getWindow().findViewById(R.id.no)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						reset_Dialog.dismiss();
					}
				});
		reset_Dialog.setCancelable(false);
	}

	private void initNotify(int progress, int max, String title) {
		final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this);
		mBuilder.setContentTitle(title)
				// 设置通知栏标题
				// .setContentIntent(getDefalutIntent(Notification.FLAG_NO_CLEAR))
				// 设置通知栏点击意图
				.setTicker("Alphace正在下载更新...")
				// 通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())
				// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
				.setPriority(Notification.PRIORITY_MAX)
				// 设置该通知优先级
				// .setAutoCancel(false)
				// 设置这个标志当用户单击面板就可以让通知将自动取消
				.setOngoing(false)
				// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setProgress(max, progress, false)
				.setSmallIcon(R.drawable.ic_launcher);// 设置通知小ICON
		if (progress != 0) {
			mBuilder.setContentText(progress + "%");
		} else {
			mBuilder.setContentText("");
			mBuilder.setAutoCancel(true);

		}
		Notification notification = mBuilder.build();
		notification.flags = Notification.FLAG_NO_CLEAR;
		mNotificationManager.notify(1, notification);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
