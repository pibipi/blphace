package com.qijitek.blphace;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qijitek.service.BluetoothLeService;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;

public class Personal_Setting_Activity extends Activity implements
		OnClickListener {
	protected static final String TAG = "Personal_Setting_Activity";
	private boolean isBind = false;
	private ProgressDialog pd;
	//
	private TextView is_bind;
	private ImageView personal_setting_back;
	private RelativeLayout change_password_layout;
	private RelativeLayout advice_callback_layout;
	private RelativeLayout device_layout;
	private RelativeLayout update;
	private TextView version;
	private SharedpreferencesUtil sharedpreferencesUtil;
	private String mAddress;
	private AlertDialog not_bind_Dialog;
	private Handler mHandler;
	private BluetoothLeService mBluetoothLeService;
	private ServiceConnection mServiceConnection = null;
	private String versionName = "";
	private String latestVersion = "";
	private AlertDialog update_Dialog;
	private NotificationManager mNotificationManager;
	private Button unlogin;
	private AlertDialog log_Dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_setting);
		// pd=ProgressDialog.show(Personal_Setting_Activity.this, "", "");
		// pd.setContentView(R.layout.connecting_progress_dialog2);
		init();
		connectService();
	}

	private void initBind() {
		mAddress = sharedpreferencesUtil.getMyDeviceMac();
		if (mAddress.equals("")) {
			is_bind.setText("未绑定");
		} else {
			is_bind.setText("已绑定");
		}
	}

	private void init() {
		log_Dialog = new AlertDialog.Builder(Personal_Setting_Activity.this)
				.create();
		unlogin = (Button) findViewById(R.id.unlogin);
		unlogin.setOnClickListener(this);
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		update_Dialog = new AlertDialog.Builder(Personal_Setting_Activity.this)
				.create();
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 666:
					int progress = msg.arg1;
					if (progress > 0 && progress < 100) {
						initNotify(progress, 100, "正在下载更新...");
					} else {
						// initNotify(0, 0, "下载完成，点击安装");
						mNotificationManager.cancelAll();
						collapseStatusBar();
					}
					break;

				default:
					break;
				}
			}
		};

		not_bind_Dialog = new AlertDialog.Builder(
				Personal_Setting_Activity.this).create();
		sharedpreferencesUtil = new SharedpreferencesUtil(
				getApplicationContext());
		is_bind = (TextView) findViewById(R.id.is_bind);
		personal_setting_back = (ImageView) findViewById(R.id.personal_setting_back);
		change_password_layout = (RelativeLayout) findViewById(R.id.change_password_layout);
		advice_callback_layout = (RelativeLayout) findViewById(R.id.advice_callback_layout);
		device_layout = (RelativeLayout) findViewById(R.id.device_layout);
		update = (RelativeLayout) findViewById(R.id.update);
		version = (TextView) findViewById(R.id.version);
		try {
			versionName = MyUtils.getVersionName(getApplicationContext());
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		version.setText(versionName);

		personal_setting_back.setOnClickListener(this);
		change_password_layout.setOnClickListener(this);
		advice_callback_layout.setOnClickListener(this);
		device_layout.setOnClickListener(this);
		update.setOnClickListener(this);
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = "http://api.qijitek.com/getVersion/";
				HttpGet httpRequest = new HttpGet(url);
				try {
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						String strResult = EntityUtils.toString(httpResponse
								.getEntity());
						System.out.println(strResult);
						latestVersion = strResult;
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("id````" + e.toString());
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void showDialog() {
		not_bind_Dialog.show();

		Window window = not_bind_Dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		not_bind_Dialog.getWindow().setContentView(R.layout.dialog_cancle_bind);
		not_bind_Dialog.getWindow().findViewById(R.id.yes)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						sharedpreferencesUtil.saveMyDeviceMac("");
						mBluetoothLeService.disconnect();
						Toast.makeText(getApplicationContext(), "已解绑", 0)
								.show();
						is_bind.setText("未绑定");
						not_bind_Dialog.dismiss();
					}
				});
		not_bind_Dialog.getWindow().findViewById(R.id.no)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						not_bind_Dialog.dismiss();
					}
				});
		not_bind_Dialog.setCancelable(false);
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
		isBind = bindService(gattServiceIntent, mServiceConnection,
				BIND_AUTO_CREATE);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personal_setting_back:
			finish();
			break;
		case R.id.change_password_layout:
			break;
		case R.id.advice_callback_layout:
			startActivity(new Intent(Personal_Setting_Activity.this,
					AdviceCallbackActivity.class));
			break;
		case R.id.device_layout:
			mAddress = sharedpreferencesUtil.getMyDeviceMac();
			if (mAddress.equals("")) {
				startActivity(new Intent(Personal_Setting_Activity.this,
						Bind_Device_Activity.class));
			} else {
				showDialog();
			}
			break;
		case R.id.update:
			boolean needUpdate = false;
			if (latestVersion.equals("")) {
				needUpdate = false;
			} else {
				if (latestVersion.equals(versionName)) {
					needUpdate = false;
				} else {
					needUpdate = true;
				}
			}
			if (!needUpdate) {
				showUpdateDialog();
			} else {
				Toast.makeText(getApplicationContext(), "已是最新版本", 0).show();
			}
			break;
		case R.id.unlogin:
			showUnLoginDialog();
			break;
		default:
			break;
		}
	}

	private void initNotify(int progress, int max, String title) {
		final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this);
		mBuilder.setContentTitle(title)
				// 设置通知栏标题
				// .setContentIntent(getDefalutIntent(Notification.FLAG_NO_CLEAR))
				// 设置通知栏点击意图
				.setTicker("正在下载更新...")
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
	protected void onDestroy() {
		if (isBind) {
			unbindService(mServiceConnection);
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		initBind();
		super.onResume();
	}

	private void showUpdateDialog() {
		// update_Dialog.show();
		//
		// Window window = update_Dialog.getWindow();
		// window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		// // window.setWindowAnimations(R.style.mystyle); // 添加动画
		//
		// update_Dialog.getWindow().setContentView(R.layout.dialog_update);
		// update_Dialog.getWindow().findViewById(R.id.yes)
		// .setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(getApplicationContext(), "后台下载中...", 0)
		// .show();
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// MyUtils.downloadApk(getApplicationContext(),
		// mHandler);
		// }
		// }).start();
		// update_Dialog.dismiss();
		// }
		// });
		// update_Dialog.getWindow().findViewById(R.id.no)
		// .setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// update_Dialog.dismiss();
		// }
		// });
		// update_Dialog.setCancelable(false);
	}

	private void collapseStatusBar() {
		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		try {
			Object service = getSystemService("statusbar");
			Class<?> statusbarManager = Class
					.forName("android.app.StatusBarManager");
			Method collapse = null;
			if (service != null) {
				if (currentApiVersion <= 16) {
					collapse = statusbarManager.getMethod("collapse");
				} else {
					collapse = statusbarManager.getMethod("collapsePanels");
				}
				collapse.setAccessible(true);
				collapse.invoke(service);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showUnLoginDialog() {
		log_Dialog.show();

		Window window = log_Dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		// window.setWindowAnimations(R.style.mystyle); // 添加动画

		log_Dialog.getWindow().setContentView(R.layout.dialog_login_out);
		log_Dialog.getWindow().findViewById(R.id.yes)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						new SharedpreferencesUtil(getApplicationContext())
								.saveIsLogin(false);
						Intent intent = new Intent(
								Personal_Setting_Activity.this,
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
						log_Dialog.dismiss();
					}
				});
		log_Dialog.getWindow().findViewById(R.id.no)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						log_Dialog.dismiss();
					}
				});
		log_Dialog.setCancelable(false);
	}
}

// class MyAsyncTask extends AsyncTask<String, Integer, String> {
// @Override
// protected String doInBackground(String... params) {
// try {
// URL url = new URL(params[0]);
// URLConnection conn = url.openConnection();
// count = conn.getContentLength();
// InputStream is = conn.getInputStream();
// OutputStream os = new FileOutputStream(
// Environment.getExternalStorageDirectory()
// + "/singleon.apk");
// byte[] buffer = new byte[1024];
// int len = -1;
// while (!finished) {
// while (!paused && (len = is.read(buffer)) > 0) {
// current += len;
// os.write(buffer, 0, len);
// progress = current * 100 / count;
// // Log.i(TAG, "current = " + current + " count = " +
// // count + " progress = " + progress);
// publishProgress(progress);
// Message msg = new Message();
// mHandler.handleMessage(msg);
// System.out.println("progress" + progress + "count"
// + count);
// }
// }
//
// } catch (MalformedURLException e) {
// e.printStackTrace();
// } catch (IOException e) {
// ;
// e.printStackTrace();
// }
//
// return progress + "";
// }
//
// @Override
// protected void onPostExecute(String result) {
// Log.i(TAG, result + "");
// super.onPostExecute(result);
// }
//
// @Override
// protected void onProgressUpdate(Integer... values) {
//
// if (values[0] == 99) {
// finished = true;
// // manager.cancel(0);
// }
// super.onProgressUpdate(values);
// }
//
// @Override
// protected void onPreExecute() {
// mHandler.handleMessage(new Message());
// super.onPreExecute();
// }
//
// }
// }
