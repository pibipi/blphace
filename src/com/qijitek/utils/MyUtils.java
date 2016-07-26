package com.qijitek.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyUtils {
	public static byte[] bitmap2bytearray(Bitmap bitmap) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		return os.toByteArray();
	}

	public static Bitmap bytearray2bitmap(byte[] b) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		return bitmap;

	}

	public static String long2time(long l) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(l);
		Date data = calendar.getTime();
		// SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		String time = format.format(data);
		return time;

	}

	public static String long2date(long l) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(l);
		Date data = calendar.getTime();
		// SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		SimpleDateFormat format = new SimpleDateFormat("MM/dd");
		String time = format.format(data);
		return time;

	}

	public static String long2week(long l) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(l);
		Date data = calendar.getTime();
		// SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		SimpleDateFormat format = new SimpleDateFormat("EEEE");
		String time = format.format(data);
		return time;

	}

	public static JSONObject getJson(String url)
			throws ClientProtocolException, IOException, JSONException {
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObj;
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String strResult = EntityUtils.toString(httpResponse.getEntity());
			jsonObj = new JSONObject(strResult);
			System.out.println("getJson" + jsonObj.toString());
			return jsonObj;
		} else
			System.out.println("getJson null");
		return null;

	}

	/**
	 * https连接
	 * 
	 * @param path
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static JSONObject initSSLWithHttpClinet(String path)
			throws ClientProtocolException, IOException {
		HTTPSTrustManager.allowAllSSL();
		JSONObject jsonObject = null;
		int timeOut = 30 * 1000;
		HttpParams param = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(param, timeOut);
		HttpConnectionParams.setSoTimeout(param, timeOut);
		HttpConnectionParams.setTcpNoDelay(param, true);

		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", TrustAllSSLSocketFactory
				.getDefault(), 443));
		ClientConnectionManager manager = new ThreadSafeClientConnManager(
				param, registry);
		DefaultHttpClient client = new DefaultHttpClient(manager, param);

		HttpGet request = new HttpGet(path);
		// HttpGet request = new HttpGet("https://www.alipay.com/");
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				entity.getContent()));
		StringBuilder result = new StringBuilder();
		String line = "";
		while ((line = reader.readLine()) != null) {
			result.append(line);
			try {
				jsonObject = new JSONObject(line);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Log.e("HTTPS TEST", result.toString());
		return jsonObject;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 当前activity截图
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap captureScreen(Activity activity) {
		activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
		Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
		return bmp;
	}

	/**
	 * 压缩bitmap
	 * 
	 * @param sentBitmap
	 * @param scale
	 * @return
	 */
	public static Bitmap scalebmp(Bitmap sentBitmap, float scale) {
		int width = Math.round(sentBitmap.getWidth() * scale);
		int height = Math.round(sentBitmap.getHeight() * scale);
		sentBitmap = Bitmap
				.createScaledBitmap(sentBitmap, width, height, false);

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
		return bitmap;
	}

	/**
	 * Stack Blur v1.0 from
	 * http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html Java
	 * Author: Mario Klingemann <mario at quasimondo.com>
	 * http://incubator.quasimondo.com
	 * 
	 * created Feburary 29, 2004 Android port : Yahel Bouaziz <yahel at
	 * kayenko.com> http://www.kayenko.com ported april 5th, 2012
	 * 
	 * This is a compromise between Gaussian Blur and Box blur It creates much
	 * better looking blurs than Box Blur, but is 7x faster than my Gaussian
	 * Blur implementation.
	 * 
	 * I called it Stack Blur because this describes best how this filter works
	 * internally: it creates a kind of moving stack of colors whilst scanning
	 * through the image. Thereby it just has to add one new block of color to
	 * the right side of the stack and remove the leftmost color. The remaining
	 * colors on the topmost layer of the stack are either added on or reduced
	 * by one, depending on if they are on the right or on the left side of the
	 * stack.
	 * 
	 * If you are using this algorithm in your code please add the following
	 * line: Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
	 */

	public static Bitmap fastblurscale(Bitmap sentBitmap, float scale,
			int radius) {

		int width = Math.round(sentBitmap.getWidth() * scale);
		int height = Math.round(sentBitmap.getHeight() * scale);
		sentBitmap = Bitmap
				.createScaledBitmap(sentBitmap, width, height, false);

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

		return (bitmap);
	}

	public static String getVersionName(Context context)
			throws NameNotFoundException {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		return packageInfo.versionName;
	}

	public static void downloadApk(Context context, Handler mHandler) {
		String urlStr = "http://api.qijitek.com/downloadApk/";
		String path = "alphace";
		String fileName = "Alphace.apk";
		OutputStream output = null;
		try {
			/*
			 * 通过URL取得HttpURLConnection 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
			 * <uses-permission android:name="android.permission.INTERNET" />
			 */
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 取得inputStream，并将流中的信息写入SDCard

			/*
			 * 写前准备 1.在AndroidMainfest.xml中进行权限配置 <uses-permission
			 * android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
			 * 取得写入SDCard的权限 2.取得SDCard的路径：
			 * Environment.getExternalStorageDirectory() 3.检查要保存的文件上是否已经存在
			 * 4.不存在，新建文件夹，新建文件 5.将input流中的信息写入SDCard 6.关闭流
			 */
			String SDCard = Environment.getExternalStorageDirectory() + "";
			String pathName = SDCard + "/" + path + "/" + fileName;// 文件存储路径

			File file = new File(pathName);
			InputStream input = conn.getInputStream();
			if (file.exists()) {
				file.delete();
				System.out.println("exits");
			}
			String dir = SDCard + "/" + path;
			new File(dir).mkdir();// 新建文件夹
			file.createNewFile();// 新建文件
			output = new FileOutputStream(file);
			// 读取大文件
			byte[] buffer = new byte[1024];
			int len = 0;
			long current = 0;
			long total = conn.getContentLength();
			int old_progress = 0;
			int new_progress = 0;
			while ((len = input.read(buffer)) > 0) {
				output.write(buffer, 0, len);
				current += len;
				System.out.println("current" + current + "total" + total);
				System.out.println(100 * current / total + "");
				new_progress = (int) (100 * current / total);
				if (new_progress != old_progress) {
					Message msg = new Message();
					msg.what = 666;
					msg.arg1 = new_progress;
					mHandler.sendMessage(msg);
				}
				old_progress = new_progress;
			}
			output.flush();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
				installApk(context);
				System.out.println("success");
			} catch (IOException e) {
				System.out.println("fail");
				e.printStackTrace();
			}
		}
	}

	private static void installApk(Context context) {
		String str = "/alphace/Alphace.apk";
		String fileName = Environment.getExternalStorageDirectory() + str;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(fileName)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 验证手机号码
	 * 
	 * @param phoneNumber
	 *            手机号码
	 * @return boolean
	 */
	public static boolean checkPhoneNumber(String phoneNumber) {
		Pattern pattern = Pattern
				.compile("^((13[0-9])|(147)|(17[0-9])|(15[^4,\\D])|(18[0,0-9]))\\d{8}$");
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.matches();
	}

	public static String getOkHttp(String url) throws IOException {
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();

		return response.body().string();
	}

	public static String getSkintypeText(String type) {
		int t = Integer.valueOf(type);
		String str = "";
		switch (t) {
		case 1:
			str = "干性皮肤的mm应选用养分多，滋润型的乳液化妆品，以使肌肤湿润、健康、有活力。定期做面部按摩，提高肌肤温度，改善血液循环，增进肌肤的生理活动，使肌肤光润亮泽。注意饮食营养的平衡（脂肪可稍多一些）。冬季室内受暖气影响，肌肤会变得更加促造，因此室内宜使用加湿器。";
			break;
		case 2:
			str = "混合性皮肤的mm可以用比较滋润的保养品，注意两颊的保湿，但T区要做好清洁工作，最好跟两颊分开护理，用些比较清爽的产品，控制多余油份的产生。一些去油光和去黑头的特效产品只用在T区部位，多吃水果。千万不要嫌护肤程序麻烦哦！";
			break;
		case 3:
			str = "油性皮肤的mm应使用洁净力强的洗面产品，去掉附着毛孔中的污物。用调节皮脂分泌的化妆品护理肌肤，并用清爽的乳液柔和皮肤。不偏食油腻食物，多吃蔬菜、水果和含维生素B的食物。保持心情轻松愉快，多参加体育活动，睡眠要充足。";
			break;
		default:
			break;
		}
		return str;

	}

	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	public static String getValue(JSONObject object, String key) {
		String value = "";
		try {
			if (!(object.get(key) instanceof String)) {
				value = "";
			} else {
				value = object.optString(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (value == null) {
			value = "";
		}
		System.out.println("myutils" + value);
		return value;
	}
}
