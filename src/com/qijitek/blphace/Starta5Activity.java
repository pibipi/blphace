package com.qijitek.blphace;

import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.qijitek.blphace.Fragment_Test1.ScanThread;
import com.qijitek.service.BluetoothLeService;
import com.qijitek.utils.SharedpreferencesUtil;

public class Starta5Activity extends android.support.v4.app.FragmentActivity
		implements OnCheckedChangeListener, OnClickListener {
	private FragmentManager fragmentManager;
	private RadioGroup radioGroup;
	private ViewPager pager;
	private List<Fragment> fragments;
	private RadioButton bt1;
	private RadioButton bt2;
	//
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothLeService mBluetoothLeService;
	private ServiceConnection mServiceConnection;
	private SharedpreferencesUtil sharedpreferencesUtil;
	private String mDeviceAddress;
	//
	private final static int BLESTART = 1;
	private boolean state_flag = true;
	protected static final String TAG = "Starta5Activity";
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starta5);
		init();
		initBle();
		changeFragment(new Fragment_Test1(), true);
	}

	private void init() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 5:
//TODO 
					break;
				case 6:

					break;

				default:
					break;
				}
			}

		};
		sharedpreferencesUtil = new SharedpreferencesUtil(
				getApplicationContext());
		mDeviceAddress = sharedpreferencesUtil.getMyDeviceMac();
		fragmentManager = getSupportFragmentManager();
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(this);
		bt1 = (RadioButton) findViewById(R.id.bt1);
		bt2 = (RadioButton) findViewById(R.id.bt2);
		pager = (ViewPager) findViewById(R.id.pager);
		// initViewPager();
	}

	public void changeFragment(Fragment fragment, boolean isInit) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container, fragment);
		if (!isInit) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	// @SuppressWarnings("deprecation")
	// private void initViewPager() {
	// fragments = new ArrayList<Fragment>();
	// fragments.add(new Fragment_Test1());
	// fragments.add(new Fragment_Test2());
	// pager.setAdapter(new MyPagerAdapter(fragmentManager, fragments));
	// pager.setCurrentItem(0);
	// pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	//
	// @Override
	// public void onPageSelected(int index) {
	// System.out.println(index + "index");
	// switch (index) {
	// case 0:
	// bt1.setChecked(true);
	// break;
	// case 1:
	// bt2.setChecked(true);
	// break;
	// default:
	// break;
	// }
	// }
	//
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// }
	//
	// @Override
	// public void onPageScrollStateChanged(int arg0) {
	//
	// }
	// });
	// }
	//
	// class MyPagerAdapter extends FragmentPagerAdapter {
	// private List<Fragment> list = new ArrayList<Fragment>();
	//
	// public MyPagerAdapter(FragmentManager fm) {
	// super(fm);
	// }
	//
	// public MyPagerAdapter(FragmentManager fm, List<Fragment> list) {
	// super(fm);
	// this.list = list;
	// }
	//
	// @Override
	// public Fragment getItem(int index) {
	// return list.get(index);
	// // if (index == 0) {
	// // return new Fragment_Test1();
	// // } else if (index == 1) {
	// // return new Fragment_Test2();
	// // } else {
	// // return new Fragment_Test1();
	// // }
	// }
	//
	// @Override
	// public int getCount() {
	// return list.size();
	// }
	//
	// }
	//
	// @Override
	// public void onCheckedChanged(RadioGroup group, int checkedId) {
	// switch (checkedId) {
	// case R.id.bt1:
	// pager.setCurrentItem(0);
	// break;
	// case R.id.bt2:
	// pager.setCurrentItem(1);
	// break;
	// default:
	// break;
	// }
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化BLE：检测是否支持蓝牙，检测蓝牙是否已经打开。
	 */
	private void initBle() {
		// 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(getApplicationContext(), "不支持BLE蓝牙", 1).show();
			System.out.println("不支持ble蓝牙");
		} else
			System.out.println("支持ble蓝牙");
		// 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();
		// 检查设备上是否支持蓝牙
		if (bluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(),
					"error_bluetooth_not_supported", Toast.LENGTH_SHORT).show();
		} // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, BLESTART);
		} else {
			if (!mDeviceAddress.equals("")) {
				startService(new Intent(Starta5Activity.this,
						BluetoothLeService.class));
				connectService();
			}
		}

	}

	private void connectService() {
		mServiceConnection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName componentName,
					IBinder service) {
				mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
						.getService();
				if (!mBluetoothLeService.initialize()) {
					Log.e(TAG, "Unable to initialize Bluetooth");
					finish();
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {
				mBluetoothLeService = null;
			}
		};
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BLESTART:
			System.out.println(resultCode + "resultCode");
			if (resultCode == 0) {
				this.finish();
				// 拒绝打开
			} else if (resultCode == -1) {
				// 同意打开蓝牙
				if (!mDeviceAddress.equals("")) {
					startService(new Intent(Starta5Activity.this,
							BluetoothLeService.class));
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.bt1:
			changeFragment(new Fragment_Test1(), true);
			break;
		case R.id.bt2:
			changeFragment(new Fragment_Test2(), true);
			break;

		default:
			break;
		}
	}

	class ScanThread extends Thread {

		@Override
		public void run() {
			while (state_flag) {
				boolean state = sharedpreferencesUtil.getConnectState();
				Log.e(TAG, "ScanThread" + state);
				Message msg = new Message();
				if (state) {
					msg.what = 5;
				} else {
					msg.what = 6;
				}
				mHandler.sendMessage(msg);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void onPause() {
		state_flag = false;
		Log.e(TAG, "on pause");
		super.onPause();
	}

	@Override
	public void onResume() {
		state_flag = true;
		new ScanThread().start();
		Log.e(TAG, "on resume");
		super.onResume();
	}
}
