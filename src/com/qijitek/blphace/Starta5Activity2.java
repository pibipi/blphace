package com.qijitek.blphace;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qijitek.constant.NomalConstant;
import com.qijitek.database.FaceData;
import com.qijitek.service.BluetoothLeService;
import com.qijitek.utils.SharedpreferencesUtil;

public class Starta5Activity2 extends Activity implements OnClickListener {
	private final static String TAG = Fragment_Test2.class.getSimpleName();
	private ArrayList<FaceData> list;
	private SharedpreferencesUtil sharedpreferencesUtil;
	private Handler mHandler;
	private boolean state_flag = true;
	private BluetoothLeService mBluetoothLeService;
	private ServiceConnection mServiceConnection;
	private BluetoothAdapter bluetoothAdapter;
	private String mDeviceAddress;
	private RelativeLayout tips_rel;
	private TextView tips;
	//
	public final static UUID UUID_BLE_CHARACTERISTIC_F1 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F1);
	public final static UUID UUID_BLE_CHARACTERISTIC_F2 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F2);
	public final static UUID UUID_BLE_CHARACTERISTIC_F3 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F3);
	public final static UUID UUID_BLE_SERVICE = UUID
			.fromString(NomalConstant.BLE_SERVICE);
	//
	private ImageView multiply_img;
	private FaceData averageData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starta52);
		init();
		initBle();
	}

	private void init() {
		sharedpreferencesUtil = new SharedpreferencesUtil(
				getApplicationContext());
		mDeviceAddress = new SharedpreferencesUtil(getApplicationContext())
				.getMyDeviceMac();
		averageData = new FaceData();
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 5:
					System.out.println("mDeviceAddress"+mDeviceAddress);
					if (!mDeviceAddress.equals("")) {
						tips.setText("已连接");
						tips_rel.setClickable(false);
					}
					break;
				case 6:
					if (!mDeviceAddress.equals("")) {
						tips.setText("未连接");
						tips_rel.setClickable(false);
					}
					break;
				case 7:
					tips.setText("绑定设备");
					tips_rel.setClickable(true);
					break;
				default:
					break;
				}
			}

		};
		tips_rel = (RelativeLayout) findViewById(R.id.tips_rel);
		tips_rel.setOnClickListener(this);
		tips = (TextView) findViewById(R.id.tips);
		list = new ArrayList<FaceData>();
		multiply_img = (ImageView) findViewById(R.id.multiply_img);
		//
		if (mDeviceAddress.equals("")) {
			mHandler.sendEmptyMessage(7);
		} else {
			tips.setText("未连接");
			mHandler.sendEmptyMessage(6);
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

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				Log.e(TAG, "connected");
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				System.out.println("disconnect");
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				System.out
						.println("intent.getStringExtra(BluetoothLeService.DATA_UUID)"
								+ intent.getStringExtra(
										BluetoothLeService.DATA_UUID)
										.toString());
				if (intent.getStringExtra(BluetoothLeService.DATA_UUID).equals(
						UUID_BLE_CHARACTERISTIC_F1.toString())) {
					byte[] b = intent
							.getByteArrayExtra(BluetoothLeService.BYTE_DATA);
					System.out.println("Multiply" + "对了F1```" + b.length + "``"
							+ b[0]);
					final FaceData fd = byte2data(b);
					if (list.size() <= 5) {
						list.add(fd);
						System.out.println("list size" + list.size());
						switch (list.size()) {
						case 1:
							multiply_img
									.setImageResource(R.drawable.multiply_test2);
							break;
						case 2:
							multiply_img
									.setImageResource(R.drawable.multiply_test3);
							break;
						case 3:
							multiply_img
									.setImageResource(R.drawable.multiply_test4);
							break;
						case 4:
							multiply_img
									.setImageResource(R.drawable.multiply_test5);
							break;
						case 5:
							multiply_img
									.setImageResource(R.drawable.multiply_test1);
							handdata(list, fd);
							break;
						default:
							break;
						}
					}
					Log.e(TAG,
							fd.getWater() + "``" + fd.getOil() + "``"
									+ fd.getLight() + "``" + fd.getAverage());

				}

			}
		}
	};

	private void handdata(ArrayList<FaceData> list2, FaceData fd) {
		list2average(list2);
		Intent intent2 = new Intent(Starta5Activity2.this,
				SingleResultActivity2.class);
		intent2.putExtra("water", averageData.getWater());
		intent2.putExtra("oil", averageData.getOil());
		intent2.putExtra("light", averageData.getLight());
		intent2.putExtra("average", averageData.getAverage());
		list.clear();
		startActivity(intent2);
	}

	private void list2average(ArrayList<FaceData> list) {
		int water = (list.get(0).getWater() + list.get(1).getWater()
				+ list.get(2).getWater() + list.get(3).getWater() + list.get(4)
				.getWater()) / 5;
		int oil = (list.get(0).getOil() + list.get(1).getOil()
				+ list.get(2).getOil() + list.get(3).getOil() + list.get(4)
				.getOil()) / 5;
		int light = (list.get(0).getLight() + list.get(1).getLight()
				+ list.get(2).getLight() + list.get(3).getLight() + list.get(4)
				.getLight()) / 5;
		// int average = (list.get(0).getAverage() + list.get(1).getAverage()
		// + list.get(2).getAverage() + list.get(3).getAverage() + list
		// .get(4).getAverage()) / 5;

		int ave_water = suan(new int[] { list.get(0).getWater(),
				list.get(1).getWater(), list.get(2).getWater(),
				list.get(3).getWater(), list.get(4).getWater() });
		int ave_oil = suan(new int[] { list.get(0).getOil(),
				list.get(1).getOil(), list.get(2).getOil(),
				list.get(3).getOil(), list.get(4).getOil() });
		int ave_light = suan(new int[] { list.get(0).getLight(),
				list.get(1).getLight(), list.get(2).getLight(),
				list.get(3).getLight(), list.get(4).getLight() });
		int average = (ave_water + ave_oil + ave_light) / 3;
		if (average >= 99) {
			average = 99;
		} else if (average <= 1) {
			average = 1;
		}
		System.out.println("water" + water + "oil" + oil + "light" + light
				+ "average" + average);
		averageData.setWater(water);
		averageData.setOil(oil);
		averageData.setLight(light);
		averageData.setUniform(average);
		averageData.setAverage(average);
	}

	private int suan(int[] array) {
		double ave = 0;
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i] + "");
			ave += array[i];
		}
		ave /= array.length;

		double sum = 0;
		for (int i = 0; i < array.length; i++)
			// sum += (ave - Math.abs((array[i] - ave) / ave));
			sum += ((ave - Math.abs(array[i] - ave)) / ave);
		sum /= array.length;
		sum *= 100;
		Log.e("齐集均方差", sum + "");
		return (int) sum;
		// DecimalFormat format = new DecimalFormat("##.####");
		// String _sum = format.format(sum);

	}

	private FaceData byte2data(byte[] b) {
		// 原始数据
		int light = (int) ((int) b[1] & 0xff) * 256 + (int) ((int) b[0] & 0xff);
		int res = (int) ((int) b[5] & 0xff) * 256 + (int) ((int) b[4] & 0xff);
		int water = (int) ((int) b[3] & 0xff) * 256 + (int) ((int) b[2] & 0xff);
		// color
		int oil = (int) ((int) b[7] & 0xff) * 256 + (int) ((int) b[6] & 0xff);
		int blue = (int) ((int) b[9] & 0xff) * 256 + (int) ((int) b[8] & 0xff);
		int green = (int) ((int) b[11] & 0xff) * 256
				+ (int) ((int) b[10] & 0xff);
		int red = (int) ((int) b[13] & 0xff) * 256 + (int) ((int) b[12] & 0xff);
		System.out.println(res + "``" + water + "``" + oil + "``" + light
				+ "``");
		// 处理后的数据
		// 处理water
		int _water;
		if (water >= 250) {
			_water = 200 / 3 + water / 30;
		} else {
			_water = water * 3 / 10;
		}
		if (_water > 99) {
			_water = 99;
		} else if (_water < 1) {
			_water = 1;
		}

		// 处理light
		int _light = 0;
		if (light >= 500) {
			_light = light / 50 + 80;
		} else if (light >= 300 && light < 500) {
			_light = light / 10 + 40;
		} else if (light >= 200 && light < 300) {
			_light = light * 3 / 10 - 20;
		} else if (light < 200) {
			_light = light / 5;
		}

		if (_light > 99) {
			_light = 99;
		} else if (_light < 1) {
			_light = 1;
		}

		// 处理oil
		int _oil;
		_oil = (int) ((_water - 10 + new Random().nextInt(21)) / 5f + 30 - 10 + new Random()
				.nextInt(21));

		if (_oil <= 10) {
			_oil = 10;
		} else if (_oil >= 90) {
			_oil = 90;
		}

		int _average = (int) (_water * 0.4f + _light * 0.5f + _oil * 0.1f);
		FaceData fd = new FaceData(_water, _oil, _light, _average);
		return fd;

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
			startActivityForResult(enableBtIntent, 1);
		} else {
			if (!mDeviceAddress.equals("")) {
				startService(new Intent(Starta5Activity2.this,
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
		case 1:
			System.out.println(resultCode + "resultCode");
			if (resultCode == 0) {
				this.finish();
				// 拒绝打开
			} else if (resultCode == -1) {
				// 同意打开蓝牙
				if (!mDeviceAddress.equals("")) {
					startService(new Intent(Starta5Activity2.this,
							BluetoothLeService.class));
				} else {
					mHandler.sendEmptyMessage(7);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onPause() {
		state_flag = false;
		unregisterReceiver(mGattUpdateReceiver);
		Log.e(TAG, "on pause");
		super.onPause();
	}

	@Override
	public void onResume() {
		mDeviceAddress = new SharedpreferencesUtil(getApplicationContext())
		.getMyDeviceMac();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		state_flag = true;
		new ScanThread().start();
		Log.e(TAG, "on resume");
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tips_rel:
			startActivity(new Intent(Starta5Activity2.this,
					Bind_Device_Activity.class));
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
}
