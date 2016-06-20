package com.qijitek.blphace;

import java.util.Random;
import java.util.UUID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.qijitek.constant.NomalConstant;
import com.qijitek.database.FaceData;
import com.qijitek.service.BluetoothLeService;
import com.qijitek.utils.SharedpreferencesUtil;

public class Fragment_Test1 extends Fragment implements OnClickListener {
	private final static String TAG = Fragment_Test1.class.getSimpleName();
	private Button bind_device;
	private Handler mHandler;
	private SharedpreferencesUtil sharedpreferencesUtil;
	private boolean state_flag = true;
	private int water = 60, oil = 60, light = 60;
	private int result = 60;
	//
	public final static UUID UUID_BLE_CHARACTERISTIC_F1 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F1);
	public final static UUID UUID_BLE_CHARACTERISTIC_F2 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F2);
	public final static UUID UUID_BLE_CHARACTERISTIC_F3 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F3);
	public final static UUID UUID_BLE_SERVICE = UUID
			.fromString(NomalConstant.BLE_SERVICE);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_test1, null);
		init(view);
		return view;
	}

	private void init(View view) {
		mHandler = new Handler() {
		};
		sharedpreferencesUtil = new SharedpreferencesUtil(getActivity()
				.getApplicationContext());
		bind_device = (Button) view.findViewById(R.id.bind_device);
		bind_device.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bind_device:
			startActivity(new Intent(getActivity(), Bind_Device_Activity.class));
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
					// TODO Auto-generated catch block
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
					// byte[] b = intent
					// .getByteArrayExtra(BluetoothLeService.BYTE_DATA);
					// FaceData fd = byte2data(b);
					// //
					// long time = System.currentTimeMillis();
					// DBHelper dbHelper = new DBHelper(getActivity()
					// .getApplicationContext(), "face", null, 1);
					// long row = dbHelper
					// .insert(fd.getWater(), fd.getOil(), fd.getLight(),
					// fd.getAverage(), fd.getWater(), time);
					// System.out.println("row插入成功" + row);
					// //
					System.out
							.println("intent.getStringExtra(BluetoothLeService.DATA_UUID)"
									+ intent.getStringExtra(
											BluetoothLeService.DATA_UUID)
											.toString());
					if (intent.getStringExtra(BluetoothLeService.DATA_UUID)
							.equals(UUID_BLE_CHARACTERISTIC_F1.toString())) {
						byte[] b = intent
								.getByteArrayExtra(BluetoothLeService.BYTE_DATA);
						System.out.println("Single" + "对了F1```" + b.length
								+ "``" + b[0]);
						FaceData fd = byte2data(b);
						water = fd.getWater();
						oil = fd.getOil();
						light = fd.getLight();
						result = (int) (light * 0.5f + water * 0.3f + 0.2f * oil);

						Log.e(TAG, fd.getWater() + "``" + fd.getOil() + "``"
								+ fd.getLight() + "``" + fd.getAverage());
						Intent i = new Intent(getActivity(),
								SingleResultActivity2.class);
						i.putExtra("water", water);
						i.putExtra("oil", oil);
						i.putExtra("light", light);
						i.putExtra("result", result);
						startActivity(i);
					}
				}

			}
		}
	};

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

	@Override
	public void onPause() {
		state_flag = false;
		getActivity().unregisterReceiver(mGattUpdateReceiver);
		Log.e(TAG, "on pause");
		super.onPause();
	}

	@Override
	public void onResume() {
		getActivity().registerReceiver(mGattUpdateReceiver,
				makeGattUpdateIntentFilter());
		state_flag = true;
//		new ScanThread().start();
		Log.e(TAG, "on resume");
		super.onResume();
	}
}
