package com.qijitek.blphace;

import java.io.IOException;
import java.util.Vector;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
import com.qijitek.constant.NomalConstant;
import com.qijitek.utils.MyUtils;
import com.qijitek.view.ProgersssDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private Handler mHandler;
	private ProgersssDialog dialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					Toast.makeText(MipcaActivityCapture.this,
							msg.obj.toString(), 1).show();
					break;

				default:
					break;
				}
			}

		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 处理扫描结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		final String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			bundle.putParcelable("bitmap", barcode);
			resultIntent.putExtras(bundle);
			this.setResult(RESULT_OK, resultIntent);
		}
		// Toast.makeText(getApplicationContext(), resultString, 0).show();
		//
		Log.e("result", resultString + "");
		dialog = new ProgersssDialog(MipcaActivityCapture.this, "");
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					final JSONObject jsonObject = MyUtils
							.getJson("http://openapi.jimi.la/openapi?query="
									+ resultString
									+ "&appid="
									+ NomalConstant.QijiID
									+ "&token="
									+ MyUtils.md5(resultString
											+ NomalConstant.QijiSecret));
					System.out.println("http://openapi.jimi.la/openapi?query="
							+ resultString
							+ "&appid="
							+ NomalConstant.QijiID
							+ "&token="
							+ MyUtils.md5(resultString
									+ NomalConstant.QijiSecret));
					if (jsonObject.optBoolean("success")) {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								Intent intent = new Intent(
										MipcaActivityCapture.this,
										ItemDetailActivity.class);
								intent.putExtra("json", jsonObject.toString()
										+ "");
								intent.putExtra("code", resultString + "");
								startActivity(intent);
								MipcaActivityCapture.this.finish();
							}
						});
					} else {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),
										"查询失败，请稍后再试", 0).show();
								MipcaActivityCapture.this.finish();
							}
						});
					}
					dialog.dismiss();

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "请检查网络连接",
									0).show();
							MipcaActivityCapture.this.finish();
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			Log.e("my", ioe.toString() + "");
			return;
		} catch (RuntimeException e) {
			Log.e("my", e.toString() + "");
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);

				mediaPlayer.prepare();
				// mediaPlayer.prepareAsync();
			} catch (IOException e) {

				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}