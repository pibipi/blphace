package com.qijitek.blphace;

import java.io.IOException;
import java.util.UUID;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qijitek.constant.NomalConstant;
import com.qijitek.database.FaceData;
import com.qijitek.service.BluetoothLeService;
import com.qijitek.utils.GetTypeUtils;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.qijitek.view.MyProgressBar;
import com.umeng.analytics.MobclickAgent;

public class SingleResultActivity2 extends Activity implements OnClickListener {
	private final static String TAG = SingleResultActivity2.class
			.getSimpleName();
	private Handler mHandler;
	//
	private MyProgressBar face_water_progressBar;
	private MyProgressBar face_oil_progressBar;
	private MyProgressBar skin_light_progressBar;
	private MyProgressBar skin_average_progressBar;
	//
	private TextView water_progress;
	private TextView oil_progress;
	private TextView light_progress;
	private TextView average_progress;
	//
	private TextView water_type;
	private TextView oil_type;
	private TextView light_type;
	private TextView average_type;

	private ImageView back;
	private Button share2wechat;

	private int water, oil, light, average, type;
	private int result;
	private AlertDialog reset_Dialog;
	private boolean save_flag = false;
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
	private LinearLayout background;
	private TextView skin_type;
	private ImageView result_img;
	private RelativeLayout relativeLayout5;
	private TextView details_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_result2);
		// registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		init();
		MobclickAgent.onEvent(getApplicationContext(), "SingleResultActivity");
		// type = getIntent().getIntExtra("type", 0);
		// boolean isSingle=getIntent().getBooleanExtra("single", true);
		// if(isSingle){
		// relativeLayout5.setVisibility(View.GONE);
		// }

		water = getIntent().getIntExtra("water", 50);
		oil = getIntent().getIntExtra("oil", 50);
		light = getIntent().getIntExtra("light", 50);
		average = getIntent().getIntExtra("average", 50);
		result = (int) (light * 0.5f + water * 0.3f + 0.1f * oil + 0.1f * average);

		System.out.println(water + "`" + oil + "`" + light + "`" + average
				+ "`" + result);
		final ResultAnimThread raThread = new ResultAnimThread();

		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				raThread.start();
				showProgressBar(water, oil, light, average, result);
			}
		}, 500);
		if (new SharedpreferencesUtil(getApplicationContext()).getIsFirst3()) {
			showTips1();
			new SharedpreferencesUtil(getApplicationContext())
					.saveIsFirst3(false);
		}
	}

	private void init() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					break;
				case 1:
					water_type.setText(GetTypeUtils.getWaterType(water));
					water_progress.setText(msg.arg1 + "");
					break;
				case 2:
					oil_type.setText(GetTypeUtils.getOilType(oil));
					oil_progress.setText(msg.arg1 + "");
					break;
				case 3:
					light_type.setText(GetTypeUtils.getLightType(light));
					light_progress.setText(msg.arg1 + "");
					break;
				case 4:
					average_progress.setText(msg.arg1 + "");
					average_type.setText(GetTypeUtils.getAverageType(average));
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}

		};
		details_txt = (TextView) findViewById(R.id.details_txt);
		relativeLayout5 = (RelativeLayout) findViewById(R.id.relativeLayout5);
		result_img = (ImageView) findViewById(R.id.result_img);
		skin_type = (TextView) findViewById(R.id.skin_type);
		// TODO
		String str = new SharedpreferencesUtil(getApplicationContext())
				.getSkintypeStr();

		if (!str.equals("")) {
			skin_type.setText(str + "肌肤");
			if (str.equals("干性")) {
				result_img.setBackgroundResource(R.drawable.skin_type1);
			} else if (str.equals("混合性")) {
				result_img.setBackgroundResource(R.drawable.skin_type2);
			} else if (str.equals("油性")) {
				result_img.setBackgroundResource(R.drawable.skin_type3);
			}
		} else {
			skin_type.setText("暂无肌肤信息");
			Toast.makeText(getApplicationContext(),
					"请先去<测肤质>更新您的肌肤信息，以便于更好的测试", 1).show();
		}
		background = (LinearLayout) findViewById(R.id.background);
		// Bitmap bmp1 = MyUtils.bytearray2bitmap(getIntent().getByteArrayExtra(
		// "bmp"));
		// Bitmap bmp2 = MyUtils.fastblurscale(bmp1, 0.05f, 15);
		// background.setBackground(new BitmapDrawable(getResources(), bmp2));

		reset_Dialog = new AlertDialog.Builder(SingleResultActivity2.this)
				.create();
		water_type = (TextView) findViewById(R.id.water_type);
		oil_type = (TextView) findViewById(R.id.oil_type);
		light_type = (TextView) findViewById(R.id.light_type);
		average_type = (TextView) findViewById(R.id.average_type);

		water_progress = (TextView) findViewById(R.id.water_progress);
		oil_progress = (TextView) findViewById(R.id.oil_progress);
		light_progress = (TextView) findViewById(R.id.light_progress);
		average_progress = (TextView) findViewById(R.id.average_progress);

		face_water_progressBar = (MyProgressBar) findViewById(R.id.face_water_progressBar);
		face_oil_progressBar = (MyProgressBar) findViewById(R.id.face_oil_progressBar);
		skin_light_progressBar = (MyProgressBar) findViewById(R.id.skin_light_progressBar);
		skin_average_progressBar = (MyProgressBar) findViewById(R.id.skin_average_progressBar);

		back = (ImageView) findViewById(R.id.back);
		share2wechat = (Button) findViewById(R.id.share2wechat);
		share2wechat.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	class ResultAnimThread extends Thread {

		@Override
		public void run() {
			int t = 1000 / result;
			for (int c = 1; c <= result; c++) {
				try {
					sleep(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = 0;
				msg.arg1 = c;
				mHandler.sendMessage(msg);
			}

		}

	}

	private void showProgressBar(final int a, final int b, final int c, int d,
			int e) {
		water = a;
		oil = b;
		light = c;
		average = d;
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = "http://api.qijitek.com/getResultAnalysis/?water="
						+ a + "&oil=" + b + "&light=" + c;
				String url_result = "http://api.qijitek.com/uploadEveryTest/?water="
						+ a
						+ "&oil="
						+ b
						+ "&light="
						+ c
						+ "&userid="
						+ new SharedpreferencesUtil(getApplicationContext())
								.getUserid();
				try {
					JSONObject jsonObject = MyUtils.getJson(url);
					JSONObject jsonObject2 = MyUtils.getJson(url_result);
					final String str = jsonObject.optString("obj");

					mHandler.post(new Runnable() {

						@Override
						public void run() {
							AlphaAnimation alphaAnimation = new AlphaAnimation(
									0f, 1.0f);
							alphaAnimation.setDuration(2000);
							alphaAnimation.setFillAfter(true);
							details_txt.startAnimation(alphaAnimation);
							details_txt.setText(str);
						}
					});
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "请检查网络连接",
									0).show();
							AlphaAnimation alphaAnimation = new AlphaAnimation(
									0f, 1.0f);
							alphaAnimation.setDuration(2000);
							alphaAnimation.setFillAfter(true);
							details_txt.startAnimation(alphaAnimation);
							details_txt.setText("暂无数据");
						}
					});
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// details_txt.setText(GetTypeUtils.getLightDetail(c) + "\n"
		// + GetTypeUtils.getLightDetail(c) + "\n"
		// + GetTypeUtils.getLightDetail(c));
		new WaterThread().start();
		new OilThread().start();
		new SkinThread().start();
		new AverageThread().start();
	}

	class WaterThread extends Thread {
		@Override
		public void run() {
			for (int i = 1; i <= water; i++) {
				face_water_progressBar.setProgress(i);
				Message msg = new Message();
				msg.what = 1;
				msg.arg1 = i;
				mHandler.sendMessage(msg);
				// water_progress.setText(i+"");
				try {
					sleep(1000 / water);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	class OilThread extends Thread {

		@Override
		public void run() {
			for (int j = 1; j <= oil; j++) {
				face_oil_progressBar.setProgress(j);
				Message msg = new Message();
				msg.what = 2;
				msg.arg1 = j;
				mHandler.sendMessage(msg);
				// oil_progress.setText(j);
				try {
					sleep(1000 / oil);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	class SkinThread extends Thread {

		@Override
		public void run() {
			for (int k = 1; k <= light; k++) {
				skin_light_progressBar.setProgress(k);
				Message msg = new Message();
				msg.what = 3;
				msg.arg1 = k;
				mHandler.sendMessage(msg);
				// skin_progress.setText(k);
				try {
					sleep(1000 / light);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	class AverageThread extends Thread {

		@Override
		public void run() {
			for (int m = 5; m <= average; m++) {
				skin_average_progressBar.setProgress(m);
				Message msg = new Message();
				msg.what = 4;
				msg.arg1 = m;
				mHandler.sendMessage(msg);
				// skin_progress.setText(k);
				try {
					sleep(1000 / average);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	class Clickable extends ClickableSpan implements OnClickListener {
		private final View.OnClickListener mListener;

		public Clickable(View.OnClickListener l) {
			mListener = l;
		}

		@Override
		public void onClick(View v) {
			mListener.onClick(v);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			// showDialog();
			break;
		case R.id.share2wechat:
			// TODO
			startActivity(new Intent(SingleResultActivity2.this,
					Socialc1Activity2.class));
			// Toast.makeText(getApplicationContext(), "功能暂未开放", 0).show();
			// share2wechatUrl();
			break;
		default:
			break;
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
					System.out.println("Single" + "对了F1```" + b.length + "``"
							+ b[0]);
					FaceData fd = byte2data(b);
					water = fd.getWater();
					oil = fd.getOil();
					light = fd.getLight();
					result = (int) (light * 0.5f + water * 0.3f + 0.2f * oil);
					// showProgressBar(water, oil, light,average);
					// new ResultAnimThread().start();
					Log.e(TAG,
							fd.getWater() + "``" + fd.getOil() + "``"
									+ fd.getLight() + "``" + fd.getAverage());

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
		int _water = (7 * water + 1420) / 78;
		if (_water <= 20) {
			_water = 20;
		} else if (_water >= 90) {
			_water = 90;
		}
		// 处理light
		int _light = (7 * light) / 40 - 75;
		if (_light <= 30) {
			_light = 30;
		} else if (_light >= 95) {
			_light = 95;
		}
		// 处理oil
		// TODO res为除数，可能为0
		int _oil = ((water * water) / res / 2 + 50);
		if (_oil <= 50) {
			_oil = 50;
		} else if (_oil >= 95) {
			_oil = 95;
		}

		// int _oil = (int) ((10f - (float) oil * 0.1f) * 10f);
		int _average = (int) ((_water + _light + _oil) / 3f);
		FaceData fd = new FaceData(_water, _oil, _light, _average);
		return fd;

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showDialog() {
		// if (save_flag) {
		// finish();
		// return;
		// }
		// reset_Dialog.show();
		//
		// Window window = reset_Dialog.getWindow();
		// window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		// // window.setWindowAnimations(R.style.mystyle); // 添加动画
		//
		// reset_Dialog.getWindow().setContentView(R.layout.dialog_not_save);
		// reset_Dialog.getWindow().findViewById(R.id.yes)
		// .setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// reset_Dialog.dismiss();
		// saveData();
		// SingleResultActivity2.this.finish();
		// }
		// });
		// reset_Dialog.getWindow().findViewById(R.id.no)
		// .setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// SingleResultActivity2.this.finish();
		// reset_Dialog.dismiss();
		// }
		// });
		// reset_Dialog.setCancelable(false);
	}

	private void showTips1() {
		final AlertDialog tips1_Dialog = new AlertDialog.Builder(
				SingleResultActivity2.this, R.style.dialog).create();
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

		tips1_Dialog.getWindow().setContentView(R.layout.dialog_tips_a6);
		tips1_Dialog.setCancelable(true);
		tips1_Dialog.getWindow().findViewById(R.id.t1)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						tips1_Dialog.dismiss();
					}
				});
	}
}
