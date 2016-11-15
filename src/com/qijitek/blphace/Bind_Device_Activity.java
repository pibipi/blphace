package com.qijitek.blphace;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qijitek.constant.NomalConstant;
import com.qijitek.database.RssiData;
import com.qijitek.service.BluetoothLeService;
import com.qijitek.utils.SharedpreferencesUtil;
import com.umeng.analytics.MobclickAgent;

public class Bind_Device_Activity extends Activity implements OnClickListener {

	public final static UUID UUID_BLE_CHARACTERISTIC_F1 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F1);
	public final static UUID UUID_BLE_CHARACTERISTIC_F2 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F2);
	public final static UUID UUID_BLE_CHARACTERISTIC_F3 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F3);
	public final static UUID UUID_BLE_SERVICE = UUID
			.fromString(NomalConstant.BLE_SERVICE);

	private final static String TAG = Bind_Device_Activity.class
			.getSimpleName();
	private ImageView bind_device_anim_back;
	private Button not_bind;
	private Button search_again;
	private Button start_bind;
	private Animation rotateAnimation;
	private ImageView search_img;
	private ImageView search_state;
	private AnimationSet animationSet;
	private TextView tips;
	//
	private BluetoothAdapter bluetoothAdapter;
	private boolean mScanning = false;
	private Handler mHandler;
	private BluetoothLeService mBluetoothLeService;
	private boolean mConnected = false;
	private SharedpreferencesUtil sharedpreferencesUtil;
	private String mDeviceAddress;
	private Context mContext;
	//
	private ArrayList<RssiData> list_bind_temp;
	private ArrayList<BluetoothDevice> black_bind_temp;
	private BluetoothDevice current_device;
	private RssiData maxDevice;
	private AlertDialog reset_Dialog;

	// msg.what
	private static final int BIND_SUCCESS = 1;
	private static final int ADDRESS_NULL = 2;
	//
	private ServiceConnection mServiceConnection;
	private final static int BLESTART = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_device);
		init();
		search_again.setVisibility(View.INVISIBLE);
		initBle();
		bind_device_anim_back.setOnClickListener(this);
		not_bind.setOnClickListener(this);

		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		startService(new Intent(Bind_Device_Activity.this,
				BluetoothLeService.class));
		connectService();
		MobclickAgent.onEvent(mContext, "toBind");
	}

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				tips.setText("设备已连接，请点击设备上按钮完成绑定");
				// TODO
				search_state.setImageResource(R.drawable.search_succeed);
				search_img.clearAnimation();
				mConnected = true;
				sharedpreferencesUtil.setConnectState(mConnected);
				Log.e(TAG, "connected");
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				if (maxDevice != null) {
					connect(maxDevice.getAddress());
				}
				// search_img.clearAnimation();
				mConnected = false;
				sharedpreferencesUtil.setConnectState(mConnected);
				System.out.println("disconnect");
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				// Show all the supported services and characteristics on the
				// user interface.
				displayGattServices(mBluetoothLeService
						.getSupportedGattServices());
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				System.out
						.println("intent.getStringExtra(BluetoothLeService.DATA_UUID)"
								+ intent.getStringExtra(
										BluetoothLeService.DATA_UUID)
										.toString());
				// 收到用户摇晃后F3返回的notify 确认绑定
				if (intent.getStringExtra(BluetoothLeService.DATA_UUID).equals(
						UUID_BLE_CHARACTERISTIC_F3.toString())) {
					byte[] b = intent
							.getByteArrayExtra(BluetoothLeService.BYTE_DATA);
					System.out.println("对了F3```" + b.length + "``" + b[0]);
					Message msg = new Message();
					msg.what = BIND_SUCCESS;
					mHandler.sendMessage(msg);
				}
			}
		}
	};

	private void displayGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices == null)
			return;

		for (BluetoothGattService gattService : gattServices) {
			String uuid = gattService.getUuid().toString();

			System.out.println("找到一个服务" + uuid);
			List<BluetoothGattCharacteristic> characteristics = gattService
					.getCharacteristics();
			for (final BluetoothGattCharacteristic bluetoothGattCharacteristic : characteristics) {

				if (UUID_BLE_CHARACTERISTIC_F1
						.equals(bluetoothGattCharacteristic.getUuid())) {
					mBluetoothLeService
							.readCharacteristic(bluetoothGattCharacteristic);
					mBluetoothLeService.setCharacteristicNotification(
							bluetoothGattCharacteristic, true);
					Log.e(TAG, "准备读取bluetoothGattCharacteristic1");

				}
				if (UUID_BLE_CHARACTERISTIC_F2
						.equals(bluetoothGattCharacteristic.getUuid())) {
					mBluetoothLeService
							.readCharacteristic(bluetoothGattCharacteristic);
					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							search_again.setVisibility(View.VISIBLE);
							byte[] b = new byte[1];
							b[0] = 1;
							mBluetoothLeService.writeCharacteristic(b,
									bluetoothGattCharacteristic.getUuid()
											.toString());
						}
					}, 300);
					Log.e(TAG, "准备读取bluetoothGattCharacteristic2");

				}
				if (UUID_BLE_CHARACTERISTIC_F3
						.equals(bluetoothGattCharacteristic.getUuid())) {
					mBluetoothLeService
							.readCharacteristic(bluetoothGattCharacteristic);
					mBluetoothLeService.setCharacteristicNotification(
							bluetoothGattCharacteristic, true);
					Log.e(TAG, "准备读取bluetoothGattCharacteristic3");

				}
				Log.e(TAG, "找到一个CHARACTERISTIC  uuid "
						+ bluetoothGattCharacteristic.getUuid().toString());
			}

		}
	}

	private class MyLescanCallback implements BluetoothAdapter.LeScanCallback {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				final byte[] scanRecord) {

			System.out.println(device.getName() + "``" + device.getAddress());
			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if ((device.getName() != null)
							&& (device.getName()
									.contains(NomalConstant.DEVICE_NAME))) {
						RssiData d = new RssiData(device.getName(),
								device.getAddress(), rssi);
						add_temp(d);
					}
				}
			});

			// }
			// }).start();

			// if ((device.getName() != null)
			// && (device.getName().contains("Colomo"))
			// && not_in_black(device)) {
			// black_bind_temp.add(device);
			// current_device = device;
			// scanLeDevices(false);
			// connect(device.getAddress());
			// }
		}

	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				final byte[] scanRecord) {
			if (device.getName() == null) {
				return;
			}
			System.out.println(device.getName() + "``" + device.getAddress());
			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if ((device.getName() != null)
							&& (device.getName()
									.contains(NomalConstant.DEVICE_NAME))) {
						RssiData d = new RssiData(device.getName(),
								device.getAddress(), rssi);
						add_temp(d);
					}
				}
			});

			// }
			// }).start();

			// if ((device.getName() != null)
			// && (device.getName().contains("Colomo"))
			// && not_in_black(device)) {
			// black_bind_temp.add(device);
			// current_device = device;
			// scanLeDevices(false);
			// connect(device.getAddress());
			// }
		}
	};

	private boolean not_in_black(BluetoothDevice device) {
		if (black_bind_temp.size() <= 0) {
			return true;
		} else {
			boolean flag = true;
			for (BluetoothDevice bd : black_bind_temp) {
				if (bd.getAddress().equals(device.getAddress())) {
					flag = false;
				}
			}
			return flag;
		}
	}

	private void add_temp(RssiData data) {
		boolean isHere = false;
		System.out.println("list_bind_temp" + list_bind_temp.size());
		for (RssiData d : list_bind_temp) {
			if (d.getAddress().equals(data.getAddress())) {
				isHere = true;
			}
		}
		if (!isHere) {
			list_bind_temp.add(data);
			System.out.println("jiaru");
		}
	}

	private void init() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case BIND_SUCCESS:
					MobclickAgent.onEvent(getApplicationContext(), "bind_succeed");
					System.out.println("绑定成功");
					Toast.makeText(getApplicationContext(), "绑定成功", 0).show();
					sharedpreferencesUtil.saveMyDeviceMac(maxDevice
							.getAddress());
					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							byte[] b = new byte[1];
							b[0] = 2;
							mBluetoothLeService.writeCharacteristic(b,
									NomalConstant.BLE_CHARACTERISTIC_F2);
							// Intent intent = new Intent(
							// Bind_Device_Anim_Activity.this,
							// MainActivity2.class);
							// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							// startActivity(intent);
							finish();
						}
					}, 200);
					break;
				case ADDRESS_NULL:
					search_state.setImageResource(R.drawable.search_failed);
					search_img.clearAnimation();
					search_again.setVisibility(View.VISIBLE);
					if (!Bind_Device_Activity.this.isFinishing()) {
						// Toast.makeText(getApplicationContext(), "未搜索到设备,请重试",
						// 0)
						// .show();
						tips.setText("未搜索到设备,请重试");
					}
					break;
				default:
					break;
				}

				super.handleMessage(msg);
			}

		};
		mContext=getApplicationContext();
		reset_Dialog = new AlertDialog.Builder(Bind_Device_Activity.this)
				.create();
		list_bind_temp = new ArrayList<RssiData>();
		black_bind_temp = new ArrayList<BluetoothDevice>();

		tips = (TextView) findViewById(R.id.tips);
		search_img = (ImageView) findViewById(R.id.search_img);
		search_state = (ImageView) findViewById(R.id.search_state);
		search_again = (Button) findViewById(R.id.search_again);
		sharedpreferencesUtil = new SharedpreferencesUtil(
				getApplicationContext());
		bind_device_anim_back = (ImageView) findViewById(R.id.bind_device_anim_back);
		not_bind = (Button) findViewById(R.id.not_bind);
		start_bind = (Button) findViewById(R.id.start_bind);
		start_bind.setOnClickListener(this);
		search_again.setOnClickListener(this);

		// rotateAnimation=
		// AnimationUtils.loadAnimation(this,R.anim.round_anim);

		rotateAnimation = new RotateAnimation(0f, -360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(3000);
		rotateAnimation.setInterpolator(this,
				android.R.anim.linear_interpolator);
		rotateAnimation.setStartTime(Animation.START_ON_FIRST_FRAME);
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		//
		set_animation(1.7f);

	}

	private void set_animation(float animation_size) {
		// 创建一个AnimationSet对象，参数为Boolean型，
		// true表示使用Animation的interpolator，false则是使用自己的
		animationSet = new AnimationSet(true);
		// 参数1：x轴的初始值
		// 参数2：x轴收缩后的值
		// 参数3：y轴的初始值
		// 参数4：y轴收缩后的值
		// 参数5：确定x轴坐标的类型
		// 参数6：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
		// 参数7：确定y轴坐标的类型
		// 参数8：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
		ScaleAnimation scaleAnimation = new ScaleAnimation(1, animation_size,
				1, animation_size, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(700);
		scaleAnimation.setRepeatCount(-1);
		animationSet.addAnimation(scaleAnimation);

		// 创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		// 设置动画执行的时间
		alphaAnimation.setDuration(700);
		alphaAnimation.setRepeatCount(-1);
		// 将alphaAnimation对象添加到AnimationSet当中
		animationSet.addAnimation(alphaAnimation);
		// 使用ImageView的startAnimation方法执行动画
		animationSet.setFillAfter(true);
		animationSet.setInterpolator(new LinearInterpolator());
		// animationSet.setAnimationListener(new AnimationListener() {
		//
		// public void onAnimationStart(Animation animation) {
		//
		// }
		//
		// public void onAnimationRepeat(Animation animation) {
		//
		// }
		//
		// public void onAnimationEnd(Animation animation) {
		// search_img.startAnimation(animationSet);
		// }
		// });
		// search_img.startAnimation(animationSet);
	}

	/**
	 * 初始化BLE：检测是否支持蓝牙，检测蓝牙是否已经打开。
	 */
	private void initBle() {
		// 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			System.out.println("不支持ble蓝牙");
			finish();
		} else
			System.out.println("支持ble蓝牙");
		// 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();
		// 检查设备上是否支持蓝牙
		if (bluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(),
					"error_bluetooth_not_supported", Toast.LENGTH_SHORT).show();
			finish();
		} // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
		if (!bluetoothAdapter.isEnabled()) {
			if (!bluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, BLESTART);
				// bluetoothAdapter.enable();
				connectService();
			}
		}
	}

	/**
	 * 传入true，开始扫描设备
	 * 
	 * @author kist
	 * @param enable
	 */
	private void scanLeDevices(Boolean enable) {
		if (enable) {

			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					bluetoothAdapter.stopLeScan(mLeScanCallback);
					if (!Bind_Device_Activity.this.isFinishing()) {
						mScanning = false;
						Log.e("stopLeScan", "stopLeScan");
						// if (current_device == null
						// || current_device.getAddress().equals("")) {
						// Message msg = new Message();
						// msg.what = ADDRESS_NULL;
						// mHandler.sendMessage(msg);
						// }
						connect2maxRssi();
					}
				}
			}, 5000);
			mScanning = true;
			bluetoothAdapter.startLeScan(mLeScanCallback);

			Log.e("startLeScan", "startLeScan");

		} else {
			mScanning = false;
			bluetoothAdapter.stopLeScan(mLeScanCallback);
			Log.e("stopLeScan", "stopLeScan");
		}
	}

	private void connect2maxRssi() {
		System.out.println(list_bind_temp.size() + "");
		if (list_bind_temp.size() > 0) {
			maxDevice = list_bind_temp.get(0);
			for (RssiData d : list_bind_temp) {
				if (d.getRssi() > maxDevice.getRssi()) {
					maxDevice = d;
				}
			}
			System.out.println(maxDevice.getAddress()
					+ "maxDevice.getAddress()");
			if (maxDevice.getAddress() != null) {
				connect(maxDevice.getAddress());
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						if (!mConnected && !mScanning) {
							Message msg = new Message();
							msg.what = ADDRESS_NULL;
							mHandler.sendMessage(msg);
							mBluetoothLeService.disconnect();
						}
					}
				}, 4000);
			}
		} else {
			Message msg = new Message();
			msg.what = ADDRESS_NULL;
			mHandler.sendMessage(msg);
		}
	}

	private void connect(final String mDeviceAddress) {
		if (mBluetoothLeService == null) {
			connectService();
		}
		mBluetoothLeService.connect(mDeviceAddress);
		// final ServiceConnection mServiceConnection = new ServiceConnection()
		// {
		//
		// @Override
		// public void onServiceConnected(ComponentName componentName,
		// IBinder service) {
		// mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
		// .getService();
		// if (!mBluetoothLeService.initialize()) {
		// Log.e(TAG, "Unable to initialize Bluetooth");
		// finish();
		// }
		// // Automatically connects to the device upon successful
		// // start-up
		// // initialization.
		// if (!sharedpreferencesUtil.getConnectState()) {
		// mBluetoothLeService.connect(mDeviceAddress);
		// }
		// }
		//
		// @Override
		// public void onServiceDisconnected(ComponentName componentName) {
		// mBluetoothLeService = null;
		// }
		// };
		// Intent gattServiceIntent = new Intent(this,
		// BluetoothLeService.class);
		// bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

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

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		if (mBluetoothLeService != null) {
			// final boolean result =
			// mBluetoothLeService.connect(mDeviceAddress);
		}
	}

	@Override
	protected void onPause() {
		// if (mConnected) {
		// mBluetoothLeService.disconnect();
		// mBluetoothLeService.close();
		// }
		// unbindService(mServiceConnection);
		unregisterReceiver(mGattUpdateReceiver);
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_bind:
			mBluetoothLeService.disconnect();
			tips.setText("正在搜索设备...");
			search_state.setImageResource(R.drawable.searching);
			search_img.startAnimation(animationSet);
			scanLeDevices(true);
			start_bind.setVisibility(View.INVISIBLE);
			break;
		case R.id.bind_device_anim_back:
			showDialog();
			break;
		case R.id.not_bind:
			showDialog();
			break;
		case R.id.search_again:
			tips.setText("正在搜索设备...");
			// TODO
			search_state.setImageResource(R.drawable.searching);
			search_img.startAnimation(animationSet);
			list_bind_temp.clear();
			maxDevice = null;
			current_device = null;
			mBluetoothLeService.disconnect();
			// mBluetoothLeService.close();
			scanLeDevices(true);
			search_again.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showDialog() {
		reset_Dialog.show();

		Window window = reset_Dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		// window.setWindowAnimations(R.style.mystyle); // 添加动画

		reset_Dialog.getWindow().setContentView(R.layout.dialog_cancle_bind);
		reset_Dialog.getWindow().findViewById(R.id.yes)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mBluetoothLeService.disconnect();
						mBluetoothLeService.close();
						setResult(0, new Intent());
						Bind_Device_Activity.this.finish();
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
				connectService();
			}
			break;
		default:
			break;
		}
	}
}
