/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qijitek.service;

import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.qijitek.constant.NomalConstant;
import com.qijitek.utils.SharedpreferencesUtil;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeService extends Service {
	private final static String TAG = BluetoothLeService.class.getSimpleName();

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress;
	public BluetoothGatt mBluetoothGatt;
	private int mConnectionState = STATE_DISCONNECTED;

	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;

	private boolean bind_mode = false;
	private Handler mHandler = new Handler();

	public final static String ACTION_GATT_CONNECTED = "com.example.bldemo.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bldemo.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bldemo.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bldemo.ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "com.example.bldemo.EXTRA_DATA";
	public final static String BYTE_DATA = "com.example.bldemo.BYTE_DATA";
	public final static String DATA_UUID = "com.example.bldemo.DATA_UUID";

	public final static String EXTRA_UUID = "com.example.bldemo.EXTRA_UUID";
	public final static String EXTRA_STATUS = "com.example.bldemo.EXTRA_STATUS";

	private volatile boolean mBusy = false; // Write/read pending response

	public final static UUID UUID_BLE_CHARACTERISTIC_F1 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F1);
	public final static UUID UUID_BLE_CHARACTERISTIC_F2 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F2);
	public final static UUID UUID_BLE_CHARACTERISTIC_F3 = UUID
			.fromString(NomalConstant.BLE_CHARACTERISTIC_F3);
	public final static UUID UUID_BLE_SERVICE = UUID
			.fromString(NomalConstant.BLE_SERVICE);

	// Implements callback methods for GATT events that the app cares about. For
	// example,
	// connection change and services discovered.
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			String intentAction;
			SharedpreferencesUtil sharedpreferencesUtil = new SharedpreferencesUtil(
					getApplicationContext());
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				sharedpreferencesUtil.setConnectState(true);
				intentAction = ACTION_GATT_CONNECTED;
				mConnectionState = STATE_CONNECTED;
				broadcastUpdate(intentAction);
				Log.i(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.i(TAG, "Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				sharedpreferencesUtil.setConnectState(false);
				intentAction = ACTION_GATT_DISCONNECTED;
				mConnectionState = STATE_DISCONNECTED;
				Log.i(TAG, "Disconnected from GATT server.");
				broadcastUpdate(intentAction);
				mBluetoothGatt.disconnect();
				close();
				initialize();
				if (mBluetoothAdapter.isEnabled()) {
					connect_tuli();
				}
			}
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// TODO Auto-generated method stub
			Log.e(TAG, "我是onDescriptorWrite");
			mBusy = false;
			super.onDescriptorWrite(gatt, descriptor, status);
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
				displayGattServices(getSupportedGattServices());
			} else {
				Log.w(TAG, "onServicesDiscovered received: " + status);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				final BluetoothGattCharacteristic characteristic, int status) {
			System.out.println("im here onCharacteristicRead");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				// if
				// (characteristic.getUuid().equals(OADActivity.BALABALA_OAD_CHARACTERISTIC2))
				// {
				// System.out.println("read BALABALA_OAD_CHARACTERISTIC2");
				// } else
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
				System.out.println("succ");
			}
			if (UUID_BLE_CHARACTERISTIC_F3.equals(characteristic.getUuid())) {
				System.out.println("收到F3");
				bind_mode = true;
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			} else if (UUID_BLE_CHARACTERISTIC_F2.equals(characteristic
					.getUuid())) {
				System.out.println("收到F2，准备写入");
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						byte[] b = new byte[1];
						b[0] = 2;
						writeCharacteristic(b, characteristic.getUuid()
								.toString());
					}
				}, 300);
			}
			mBusy = false;
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			// byte[] b=characteristic.getValue();
			// System.out.println(b[0]+"--"+b[1]+"--"+b[2]);
			if (characteristic.getUuid().equals(UUID_BLE_CHARACTERISTIC_F3)) {
				System.out.println("F2写入01");
			} else if (characteristic.getUuid().equals(
					UUID_BLE_CHARACTERISTIC_F2)) {
				System.out.println("F2写入02");
			}
			;
			Log.e(TAG, "onCharacteristicWrite" + status
					+ BluetoothGatt.GATT_SUCCESS + "``"
					+ characteristic.getUuid().toString());
			super.onCharacteristicWrite(gatt, characteristic, status);
			mBusy = false;
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			System.out.println("im here");
			if (UUID_BLE_CHARACTERISTIC_F1.equals(characteristic.getUuid())) {
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
				// } else if
				// (OADActivity.BALABALA_OAD_CHARACTERISTIC1.equals(characteristic.getUuid()))
				// {
				// broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			} else if (UUID_BLE_CHARACTERISTIC_F3.equals(characteristic
					.getUuid())) {
				System.out.println("收到F3");
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
			mBusy = false;
		}

	};

	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
		mBusy = false;
	}

	private void broadcastUpdate(final String action,
			final BluetoothGattCharacteristic characteristic) {
		final Intent intent = new Intent(action);
		final byte[] data = characteristic.getValue();
		intent.putExtra(BYTE_DATA, data);
		intent.putExtra(DATA_UUID, characteristic.getUuid().toString());
		if (data != null && data.length > 0) {
			final StringBuilder stringBuilder = new StringBuilder(data.length);
			for (byte byteChar : data)
				stringBuilder.append(String.format("%02X ", byteChar));
			String ff = stringBuilder.toString();
			Log.e(TAG, "service read CHARACTERISTIC   " + ff.toString());
			intent.putExtra(EXTRA_DATA, ff);

			sendBroadcast(intent);
		}
		mBusy = false;
	}

	@Override
	public void onCreate() {
		Log.e(TAG, "service oncreate````````````````````````");
		initialize();
		connect_tuli();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "service ondestroy````````````````````````");
		// if (!bind_mode) {
		disconnect();
		close();
		// }bind_mode=false;
		new SharedpreferencesUtil(getApplicationContext())
				.setConnectState(false);
		super.onDestroy();
	}

	public void connect_tuli() {
		String address = new SharedpreferencesUtil(getApplicationContext())
				.getMyDeviceMac();
		Log.e(TAG, address);
		if (!address.equals("")) {
			connect(address);
		}
	}

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
					// readCharacteristic(bluetoothGattCharacteristic);
					setCharacteristicNotification(bluetoothGattCharacteristic,
							true);
					Log.e(TAG, "找到了bluetoothGattCharacteristic1");
					// break;

				}
				if (UUID_BLE_CHARACTERISTIC_F2
						.equals(bluetoothGattCharacteristic.getUuid())
						&& !new SharedpreferencesUtil(getApplicationContext())
								.getMyDeviceMac().equals("")) {
					readCharacteristic(bluetoothGattCharacteristic);
					// mHandler.postDelayed(new Runnable() {
					//
					// @Override
					// public void run() {
					// byte[] b = new byte[1];
					// b[0] = 1;
					// writeCharacteristic(b, bluetoothGattCharacteristic
					// .getUuid().toString());
					// }
					// }, 300);

					Log.e(TAG, "准备读取bluetoothGattCharacteristic2");

				}
			}
		}
	}

	private void broadcastUpdate(final String action,
			final BluetoothGattCharacteristic characteristic, final int status) {
		final Intent intent = new Intent(action);
		intent.putExtra(EXTRA_UUID, characteristic.getUuid().toString());
		intent.putExtra(EXTRA_DATA, characteristic.getValue());
		intent.putExtra(EXTRA_STATUS, status);
		sendBroadcast(intent);
		mBusy = false;
	}

	/**
	 * 将byte[] b 写入为String uuid的characteristic中
	 * 
	 * @param b
	 *            写入的值
	 * @param uuid
	 *            characteristic的uuid
	 */
	public void writeCharacteristic(byte[] b, String uuid) {
		mBusy = true;
		BluetoothGattService ms = mBluetoothGatt.getService(UUID_BLE_SERVICE);
		if (ms != null) {
			BluetoothGattCharacteristic characteristic = ms
					.getCharacteristic(UUID.fromString(uuid));
			characteristic.setValue(b);
			mBluetoothGatt.writeCharacteristic(characteristic);
		}
	}

	public boolean writeOADCharacteristic(
			BluetoothGattCharacteristic characteristic) {
		if (!checkGatt())
			return false;

		mBusy = true;
		return mBluetoothGatt.writeCharacteristic(characteristic);
	}

	public boolean writeCharacteristic(
			BluetoothGattCharacteristic characteristic, byte b) {
		if (!checkGatt())
			return false;
		mBusy = true;
		byte[] val = new byte[1];
		val[0] = b;
		characteristic.setValue(val);
		// System.out.println(mBluetoothGatt.writeCharacteristic(characteristic));
		return mBluetoothGatt.writeCharacteristic(characteristic);
	}

	public class LocalBinder extends Binder {
		public BluetoothLeService getService() {
			return BluetoothLeService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.e(TAG, "bind");
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// After using a given device, you should make sure that
		// BluetoothGatt.close() is called
		// such that resources are cleaned up properly. In this particular
		// example, close() is
		// invoked when the UI is disconnected from the Service.
		// mBluetoothGatt=null;
		// close();
		Log.e(TAG, "unbind");
		return super.onUnbind(intent);
	}

	private final IBinder mBinder = new LocalBinder();

	/**
	 * Initializes a reference to the local Bluetooth adapter.
	 * 
	 * @return Return true if the initialization is successful.
	 */
	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	private boolean checkGatt() {
		if (mBluetoothAdapter == null) {
			// Log.w(TAG, "BluetoothAdapter not initialized");
			return false;
		}
		if (mBluetoothGatt == null) {
			// Log.w(TAG, "BluetoothGatt not initialized");
			return false;
		}

		if (mBusy) {
			// Log.w(TAG, "LeService busy");
			return false;
		}
		return true;

	}

	// /**
	// * OAD......
	// * Enables or disables notification on a give characteristic.
	// *
	// * @param characteristic
	// * Characteristic to act on.
	// * @param enabled
	// * If true, enable notification. False otherwise.
	// */
	// public boolean
	// setOADCharacteristicNotification(BluetoothGattCharacteristic
	// characteristic, boolean enable) {
	// if (!checkGatt()) {
	// System.out.println("没有进来吧");
	// return false;
	// }
	//
	// boolean ok = false;
	// if (mBluetoothGatt.setCharacteristicNotification(characteristic, enable))
	// {
	//
	// BluetoothGattDescriptor clientConfig = characteristic
	// .getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
	// if (clientConfig != null) {
	//
	// if (enable) {
	// Log.i(TAG, "Enable notification: " +
	// characteristic.getUuid().toString());
	// ok =
	// clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
	// } else {
	// Log.i(TAG, "Disable notification: " +
	// characteristic.getUuid().toString());
	// ok =
	// clientConfig.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
	// }
	//
	// if (ok) {
	// mBusy = true;
	// ok = mBluetoothGatt.writeDescriptor(clientConfig);
	// Log.i(TAG, "writeDescriptor: " + characteristic.getUuid().toString());
	// }
	// }
	// }
	// return ok;
	// }

	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 * 
	 * @param address
	 *            The device address of the destination device.
	 * 
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			// initialize();
			return false;
		}

		// // Previously connected device. Try to reconnect.
		// if (mBluetoothDeviceAddress != null
		// && address.equals(mBluetoothDeviceAddress)
		// && mBluetoothGatt != null) {
		// Log.d(TAG,
		// "Trying to use an existing mBluetoothGatt for connection.");
		// if (mBluetoothGatt.connect()) {
		// mConnectionState = STATE_CONNECTING;
		// return true;
		// } else {
		// return false;
		// }
		// }
		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);

		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		return true;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 * 
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}

	/**
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
		Log.e(TAG, "wozai setCharacteristicNotification");
		List<BluetoothGattDescriptor> descriptors = characteristic
				.getDescriptors();
		// Log.e(TAG, descriptors.size() + "");
		// for (BluetoothGattDescriptor dp : descriptors) {
		// dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		// mBluetoothGatt.writeDescriptor(dp);
		// Log.e(TAG, "正在设置notification" + i + "");
		// Log.e(TAG, "正在设置notification" + dp.getUuid().toString() + "");
		// }

	}

	/**
	 * Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 * 
	 * @return A {@code List} of supported services.
	 */
	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;

		return mBluetoothGatt.getServices();
	}

	public void dismissProgressDialog(ProgressDialog p) {
		p.dismiss();
	}

	public boolean waitIdle(int timeout) {
		timeout /= 10;
		while (--timeout > 0) {
			if (mBusy)
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			else
				break;
		}

		return timeout > 0;
	}

	public boolean getConnState(String address) {
		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		// int state = mBluetoothGatt.getConnectionState(device);
		int state2 = mBluetoothManager.getConnectionState(device,
				BluetoothProfile.GATT);
		if (state2 == BluetoothProfile.STATE_CONNECTED) {
			System.out.println("STATE_CONNECTED");
			return true;
		} else if (state2 == BluetoothProfile.STATE_DISCONNECTED) {
			System.out.println("STATE_DISCONNECTED");
			return false;
		} else if (state2 == BluetoothProfile.STATE_CONNECTING) {
			System.out.println("STATE_CONNECTING");
		} else if (state2 == BluetoothProfile.STATE_DISCONNECTING) {
			System.out.println("STATE_DISCONNECTING");
		}
		return false;

	}

	public BluetoothAdapter getBluetoothAdapter() {
		return this.mBluetoothAdapter;
	}

	public BluetoothGattCallback getBluetoothGattCallback() {
		return this.mGattCallback;
	}
}
