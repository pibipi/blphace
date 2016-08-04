package com.qijitek.blphace;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qijitek.constant.NomalConstant;
import com.qijitek.database.CompareData;
import com.qijitek.database.FaceData;
import com.qijitek.database.SingleItem;
import com.qijitek.service.BluetoothLeService;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.qijitek.view.RoundProgressBar;
import com.squareup.picasso.Picasso;

public class CompareResultActivity extends Activity implements OnClickListener {
	private final static String TAG = CompareResultActivity.class
			.getSimpleName();
	private RoundProgressBar water_progress1;
	private RoundProgressBar water_progress2;
	private RoundProgressBar oil_progress1;
	private RoundProgressBar oil_progress2;
	private RoundProgressBar light_progress1;
	private RoundProgressBar light_progress2;
	private BluetoothLeService mBluetoothLeService;
	private ServiceConnection mServiceConnection;
	private BluetoothAdapter bluetoothAdapter;
	private String mDeviceAddress;
	private boolean state_flag = true;
	public final static UUID UUID_BLE_CHARACTERISTIC_F1 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F1);
	public final static UUID UUID_BLE_CHARACTERISTIC_F2 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F2);
	public final static UUID UUID_BLE_CHARACTERISTIC_F3 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F3);
	public final static UUID UUID_BLE_SERVICE = UUID
			.fromString(NomalConstant.BLE_SERVICE);
	private SharedpreferencesUtil sharedpreferencesUtil;
	private Handler mHandler;
	private int times = 1;
	//
	private SingleItem si;
	private SingleItem si2;
	//
	private TextView name1;
	private TextView name2;
	private ImageView img1;
	private ImageView img2;
	//
	private TextView p1;
	private TextView p2;
	private Button submit;
	//
	private AlertDialog reset_Dialog;
	private boolean isSave = false;
	boolean checklist = false;
	private ImageView state_icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compare_result_activity);
		init();
		checklist = getIntent().getBooleanExtra("checklist", false);
		if (checklist) {
			CompareData cd = (CompareData) getIntent().getExtras()
					.getSerializable("data");
			initCheck(cd);
		} else {
			si = (SingleItem) getIntent().getExtras().getSerializable("si");
			si2 = (SingleItem) getIntent().getExtras().getSerializable("si2");
			init_preData();
			initBle();
		}
	}

	private void initCheck(final CompareData cd) {

		name1.setText(cd.getName1());
		name2.setText(cd.getName2());
		if (!cd.getImgurl1().equals("")) {
			Picasso.with(getApplicationContext()).load(cd.getImgurl1())
					.into(img1);
		}
		if (!cd.getImgurl2().equals("")) {
			Picasso.with(getApplicationContext()).load(cd.getImgurl2())
					.into(img2);
		}
		submit.setVisibility(View.INVISIBLE);
		p1.setVisibility(View.GONE);
		p2.setVisibility(View.GONE);
		// water_progress1.setVisibility(View.VISIBLE);
		// water_progress2.setVisibility(View.VISIBLE);
		// oil_progress1.setVisibility(View.VISIBLE);
		// oil_progress2.setVisibility(View.VISIBLE);
		// light_progress1.setVisibility(View.VISIBLE);
		// light_progress2.setVisibility(View.VISIBLE);
		showP1();
		showP2();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				showProgress(Integer.valueOf(cd.getWater1()),
						Integer.valueOf(cd.getOil1()),
						Integer.valueOf(cd.getLight1()), 1);
				showProgress(Integer.valueOf(cd.getWater2()),
						Integer.valueOf(cd.getOil2()),
						Integer.valueOf(cd.getLight2()), 2);
			}
		}, 300);
		// water_progress1.setProgress(Integer.valueOf(cd.getWater1()));
		// water_progress2.setProgress(Integer.valueOf(cd.getWater2()));
		// oil_progress1.setProgress(Integer.valueOf(cd.getOil1()));
		// oil_progress2.setProgress(Integer.valueOf(cd.getOil2()));
		// light_progress1.setProgress(Integer.valueOf(cd.getLight1()));
		// light_progress2.setProgress(Integer.valueOf(cd.getLight2()));
	}

	private void showP1() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);
		p1.setVisibility(View.GONE);
		p2.setText("测试中...");
		water_progress1.startAnimation(alphaAnimation);
		oil_progress1.startAnimation(alphaAnimation);
		light_progress1.startAnimation(alphaAnimation);
	}

	private void showP2() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);
		p2.setVisibility(View.GONE);
		water_progress2.startAnimation(alphaAnimation);
		oil_progress2.startAnimation(alphaAnimation);
		light_progress2.startAnimation(alphaAnimation);
	}

	private void init() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					final FaceData fd = (FaceData) msg.obj;
					si.setWater(fd.getWater() + "");
					si.setOil(fd.getOil() + "");
					si.setLight(fd.getLight() + "");
					showP1();
					postDelayed(new Runnable() {

						@Override
						public void run() {
							showProgress(fd.getWater(), fd.getOil(),
									fd.getLight(), 1);
						}
					}, 300);
					break;
				case 2:
					final FaceData fd2 = (FaceData) msg.obj;
					si2.setWater(fd2.getWater() + "");
					si2.setOil(fd2.getOil() + "");
					si2.setLight(fd2.getLight() + "");
					showP2();
					postDelayed(new Runnable() {

						@Override
						public void run() {
							showProgress(fd2.getWater(), fd2.getOil(),
									fd2.getLight(), 2);
						}
					}, 300);
					break;
				case 5:
					state_icon.setBackgroundResource(R.drawable.ic_launcher);
					break;
				case 6:
					state_icon
							.setBackgroundResource(R.drawable.ic_launcher_black);
					break;

				default:
					break;
				}
			}

		};
		state_icon = (ImageView) findViewById(R.id.state_icon);
		submit = (Button) findViewById(R.id.submit);
		reset_Dialog = new AlertDialog.Builder(CompareResultActivity.this)
				.create();
		sharedpreferencesUtil = new SharedpreferencesUtil(
				getApplicationContext());
		mDeviceAddress = sharedpreferencesUtil.getMyDeviceMac();
		water_progress1 = (RoundProgressBar) findViewById(R.id.water_progress1);
		water_progress2 = (RoundProgressBar) findViewById(R.id.water_progress2);
		oil_progress1 = (RoundProgressBar) findViewById(R.id.oil_progress1);
		oil_progress2 = (RoundProgressBar) findViewById(R.id.oil_progress2);
		light_progress1 = (RoundProgressBar) findViewById(R.id.light_progress1);
		light_progress2 = (RoundProgressBar) findViewById(R.id.light_progress2);
		name1 = (TextView) findViewById(R.id.name1);
		name2 = (TextView) findViewById(R.id.name2);
		p1 = (TextView) findViewById(R.id.p1);
		p2 = (TextView) findViewById(R.id.p2);
		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);

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
				startService(new Intent(CompareResultActivity.this,
						BluetoothLeService.class));
				// connectService();
			}
		}

	}

	private void init_preData() {
		name1.setText(si.getName());
		name2.setText(si2.getName());
		if (!si.getImgurl().equals("")) {
			Picasso.with(getApplicationContext()).load(si.getImgurl())
					.into(img1);
		}
		if (!si2.getImgurl().equals("")) {
			Picasso.with(getApplicationContext()).load(si2.getImgurl())
					.into(img2);
		}
		water_progress1.setProgress(0);
		water_progress2.setProgress(0);
		oil_progress1.setProgress(0);
		oil_progress2.setProgress(0);
		light_progress1.setProgress(0);
		light_progress2.setProgress(0);
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

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				Log.e(TAG, "connected");
				Toast.makeText(getApplicationContext(), "设备已连接", 1).show();
				p1.setText("测试中...");
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
					if (times == 1 || times == 2) {
						Message msg = new Message();
						msg.what = times;
						System.out.println(times + "times");
						msg.obj = fd;
						mHandler.sendMessage(msg);
						times += 1;
					}
					// mHandler.post(new Runnable() {
					//
					// @Override
					// public void run() {
					// showProgress(fd.getWater(), fd.getOil(),
					// fd.getLight(), times);
					// }
					//
					// });
					Log.e(TAG,
							fd.getWater() + "``" + fd.getOil() + "``"
									+ fd.getLight() + "``" + fd.getAverage());

				}

			}
		}
	};

	private void showProgress(int water, int oil, int light, int times) {
		System.out.println(times);
		new WaterThread(water, times).start();
		new OilThread(oil, times).start();
		new LightThread(light, times).start();
	}

	class WaterThread extends Thread {
		private int water;
		private int times;

		public WaterThread(int water, int times) {
			super();
			this.water = water;
			this.times = times;
		}

		@Override
		public void run() {
			super.run();
			for (int i = 0; i <= water; i++) {
				if (times == 1) {
					water_progress1.setProgress(i);
				} else if (times == 2) {
					water_progress2.setProgress(i);
				}
				try {
					sleep(1700 / water);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	class OilThread extends Thread {
		private int oil;
		private int times;

		public OilThread(int oil, int times) {
			super();
			this.oil = oil;
			this.times = times;
		}

		@Override
		public void run() {
			super.run();
			for (int i = 0; i <= oil; i++) {
				if (times == 1) {
					oil_progress1.setProgress(i);
				} else if (times == 2) {
					oil_progress2.setProgress(i);
				}
				try {
					sleep(1700 / oil);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	class LightThread extends Thread {
		private int light;
		private int times;

		public LightThread(int light, int times) {
			super();
			this.light = light;
			this.times = times;
		}

		@Override
		public void run() {
			super.run();
			for (int i = 0; i <= light; i++) {
				if (times == 1) {
					light_progress1.setProgress(i);
				} else if (times == 2) {
					light_progress2.setProgress(i);
				}
				try {
					sleep(1700 / light);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

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
	public void onPause() {
		state_flag = false;
		unregisterReceiver(mGattUpdateReceiver);
		Log.e(TAG, "on pause");
		super.onPause();
	}

	@Override
	public void onResume() {
		state_flag = true;
		mDeviceAddress = new SharedpreferencesUtil(getApplicationContext())
				.getMyDeviceMac();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		new ScanThread().start();
		Log.e(TAG, "on resume");
		super.onResume();
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			if (!isSave && times > 2) {
				showDialog();
			} else {
				finish();
			}
			break;
		case R.id.submit:
			saveResult();
			break;
		default:
			break;
		}
	}

	private void saveResult() {
		if (times <= 2) {
			Toast.makeText(CompareResultActivity.this, "请先完成测试", 0).show();
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						String baseUrl = "http://api.qijitek.com/uploadCompareTestResult/";

						List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
						params.add(new BasicNameValuePair("userid",
								new SharedpreferencesUtil(
										getApplicationContext()).getUserid()));
						params.add(new BasicNameValuePair("time", System
								.currentTimeMillis() + ""));
						params.add(new BasicNameValuePair("itemtype",
								sharedpreferencesUtil.getItemtype()));
						params.add(new BasicNameValuePair("qid1", si.getQid()));
						params.add(new BasicNameValuePair("code1", si.getCode()));
						params.add(new BasicNameValuePair("name1", si.getName()));
						params.add(new BasicNameValuePair("imgurl1", si
								.getImgurl()));
						params.add(new BasicNameValuePair("water1", si
								.getWater()));
						params.add(new BasicNameValuePair("oil1", si.getOil()));
						params.add(new BasicNameValuePair("light1", si
								.getLight()));
						params.add(new BasicNameValuePair("qid2", si2.getQid()));
						params.add(new BasicNameValuePair("code2", si2
								.getCode()));
						params.add(new BasicNameValuePair("name2", si2
								.getName()));
						params.add(new BasicNameValuePair("imgurl2", si2
								.getImgurl()));
						params.add(new BasicNameValuePair("water2", si2
								.getWater()));
						params.add(new BasicNameValuePair("oil2", si2.getOil()));
						params.add(new BasicNameValuePair("light2", si2
								.getLight()));

						JSONObject jsonObject = MyUtils.getJson2(baseUrl,
								params);
						isSave = true;
						CompareResultActivity.this.finish();
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(CompareResultActivity.this,
										"保存成功", 0).show();
								finish();
							}
						});

					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(CompareResultActivity.this,
										"请检查网络连接", 0).show();
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	private void showDialog() {
		reset_Dialog.show();

		Window window = reset_Dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		// window.setWindowAnimations(R.style.mystyle); // 添加动画

		reset_Dialog.getWindow().setContentView(R.layout.dialog_not_save);
		reset_Dialog.getWindow().findViewById(R.id.yes)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						saveResult();
						reset_Dialog.dismiss();
					}
				});
		reset_Dialog.getWindow().findViewById(R.id.no)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						reset_Dialog.dismiss();
						CompareResultActivity.this.finish();
					}
				});
		reset_Dialog.setCancelable(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isSave && times > 2) {
				showDialog();
			}
		}

		return super.onKeyDown(keyCode, event);
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
				startService(new Intent(CompareResultActivity.this,
						BluetoothLeService.class));
			}
			break;
		default:
			break;
		}
	}
}
