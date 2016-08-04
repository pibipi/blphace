package com.qijitek.blphace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Inflater;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qijitek.database.SingleItem;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.squareup.picasso.Picasso;

public class Manageb3Activity extends Activity implements OnClickListener {
	private ArrayList<SingleItem> singleItems;
	private SingleItem si;
	private SingleItem si2;
	private MyAdapter myAdapter;
	private Handler mHandler;
	private TextView alias;
	private TextView brand;
	private TextView methods;
	private TextView name;
	private ImageView img;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manageb3);
		si = (SingleItem) getIntent().getSerializableExtra("SingleItem");
		init();
	}

	private void init() {
		mHandler = new Handler() {
		};
		singleItems = new ArrayList<SingleItem>();
		myAdapter = new MyAdapter(getApplicationContext());
		alias = (TextView) findViewById(R.id.alias);
		brand = (TextView) findViewById(R.id.brand);
		methods = (TextView) findViewById(R.id.methods);
		name = (TextView) findViewById(R.id.name);
		img = (ImageView) findViewById(R.id.img);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		button5 = (Button) findViewById(R.id.button5);
		if (!si.getAlias().equals("")) {
			alias.setText(si.getAlias());
		} else {
			alias.setText("无");
		}
		if (!si.getBrand().equals("")) {
			brand.setText(si.getBrand());
		} else {
			brand.setText("无");
		}
		if (!si.getMethods().equals("")) {
			methods.setText(si.getMethods());
		} else {
			methods.setText("无");
		}
		name.setText(si.getName());
		if (!si.getImgurl().equals("")) {
			Picasso.with(getApplicationContext()).load(si.getImgurl())
					.into(img);
		}
		System.out.println(si.getFeature());
		String[] ms = si.getFeature().split("@");
		if (ms.length == 0) {
			ms[0] = MyUtils.getNullFeature();
		}
		if (ms.length == 5) {
			button1.setText(ms[0]);
			button2.setText(ms[1]);
			button3.setText(ms[2]);
			button4.setText(ms[3]);
			button5.setText(ms[4]);
		} else if (ms.length == 4) {
			button1.setText(ms[0]);
			button2.setText(ms[1]);
			button3.setText(ms[2]);
			button4.setText(ms[3]);
			button5.setVisibility(View.INVISIBLE);
		} else if (ms.length == 3) {
			button1.setText(ms[0]);
			button2.setText(ms[1]);
			button3.setText(ms[2]);
			button4.setVisibility(View.INVISIBLE);
			button5.setVisibility(View.INVISIBLE);
		} else if (ms.length == 2) {
			button1.setText(ms[0]);
			button2.setText(ms[1]);
			button3.setVisibility(View.INVISIBLE);
			button4.setVisibility(View.INVISIBLE);
			button5.setVisibility(View.INVISIBLE);
		} else if (ms.length == 1) {
			button1.setText(ms[0]);
			button2.setVisibility(View.INVISIBLE);
			button3.setVisibility(View.INVISIBLE);
			button4.setVisibility(View.INVISIBLE);
			button5.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.start2:
			if (singleItems.size() != 0) {
				showDialog();
			} else {
				Toast.makeText(getApplicationContext(),
						"添加的化妆品数量少于两个，无法开始对比测试", 0).show();
			}
			break;
		case R.id.start1:
			Intent intent = new Intent(Manageb3Activity.this,
					Starta5Activity2.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("si", si);
			intent.putExtras(bundle);
			intent.putExtra("needsave", true);
			startActivity(intent);
			break;
		// case R.id.test:
		// startActivity(new Intent(Manageb3Activity.this,
		// CompareResultActivity.class));
		// break;
		default:
			break;
		}
	}

	private void getSingleItems() {
		String itemtype = new SharedpreferencesUtil(getApplicationContext())
				.getItemtype();
		String userid = new SharedpreferencesUtil(getApplicationContext())
				.getUserid();
		String url = "http://api.qijitek.com/getTestList/?userid=" + userid
				+ "&itemtype=" + itemtype;
		try {
			JSONObject jsonObject = MyUtils.getJson(url);
			JSONArray obj = jsonObject.getJSONArray("obj");
			int length = obj.length();
			for (int i = 0; i < length; i++) {
				JSONObject json = (JSONObject) obj.opt(i);
				String code = json.getString("code");
				String methods = json.getString("methods");
				String qid = json.getString("qid");
				String brand = json.getString("brand");
				String water = json.getString("water");
				String oil = json.getString("oil");
				String imgurl = json.getString("imgurl");
				String name = json.getString("name");
				String light = json.getString("light");
				String feature = json.getString("feature");
				String alias = json.getString("alias");
				// SingleItem si = new SingleItem(name, imgurl, code, water,
				// oil,
				// light, itemtype, qid, alias, brand, methods, feature);
				if (!qid.equals(si.getQid())) {
					SingleItem sss = new SingleItem(name, imgurl, code,
							itemtype, qid, false);
					singleItems.add(sss);
				}
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						myAdapter.notifyDataSetChanged();
					}
				});
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "请检查网络连接", 0)
							.show();
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_start_test, null);
		final AlertDialog add_Dialog = new AlertDialog.Builder(
				Manageb3Activity.this).create();
		add_Dialog.show();

		Window window = add_Dialog.getWindow();
		// window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		// window.setWindowAnimations(R.style.mystyle); // 添加动画

		// add_Dialog.getWindow().setContentView(layout);
		add_Dialog.getWindow().setContentView(R.layout.dialog_start_test);
		add_Dialog.setCancelable(true);

		final ImageView cancle = (ImageView) add_Dialog.getWindow()
				.findViewById(R.id.cancle);
		final ImageView ok = (ImageView) add_Dialog.getWindow().findViewById(
				R.id.ok);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add_Dialog.dismiss();
			}
		});
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (rightChoice()) {
					Intent intent = new Intent(Manageb3Activity.this,
							CompareResultActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("si", si);
					bundle.putSerializable("si2", si2);
					intent.putExtras(bundle);
					if (!new SharedpreferencesUtil(getApplicationContext())
							.getMyDeviceMac().equals("")) {
						startActivity(intent);
					} else {
						Toast.makeText(getApplicationContext(), "请先绑定设备", 0)
								.show();
						startActivity(new Intent(Manageb3Activity.this,
								Bind_Device_Activity.class));
					}
					add_Dialog.dismiss();
				}
			}
		});
		ListView listView = (ListView) window.findViewById(R.id.list);

		// listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		// {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// System.out.println(position);
		// refreshList(position);
		// }
		//
		// });
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				refreshList(position);
				cancle.setVisibility(View.GONE);
				ok.setVisibility(View.VISIBLE);
			}
		});
		listView.setAdapter(myAdapter);
	}

	protected boolean rightChoice() {

		int count = 0;
		for (SingleItem ss : singleItems) {
			if (ss.isSelect()) {
				count++;
				si2 = ss;
			}
		}
		if (count == 1) {
			return true;
		} else {
			Toast.makeText(getApplicationContext(), "请选择一件化妆品进行对比", 0).show();
			return false;
		}
	}

	private class MyAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			super();
			this.context = context;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return singleItems.size();
		}

		@Override
		public Object getItem(int position) {
			return singleItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_b3dialog, null);
				viewHolder = new ViewHolder();
				viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.name);
				viewHolder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkBox);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			SingleItem s = singleItems.get(position);
			viewHolder.name.setText(s.getName());
			if (!s.getImgurl().equals("")) {
				Picasso.with(context).load(s.getImgurl()).into(viewHolder.img);
			}
			viewHolder.checkBox.setChecked(s.isSelect());
			// System.out.println("getview" + position + s.getisSelect());
			return convertView;
		}

	}

	private void refreshList(int position) {
		for (int i = 0; i < singleItems.size(); i++) {
			if (i == position) {
				singleItems.get(position).setSelect(true);
				System.out.println(singleItems.get(position).toString());
			} else {
				singleItems.get(i).setSelect(false);

			}
			System.out.println(singleItems.get(i).isSelect() + "i" + i
					+ "position" + position);
		}
		for (SingleItem ss : singleItems) {
			// System.out.println("again" + ss.toString());
		}
		myAdapter.notifyDataSetChanged();
		System.out.println(singleItems.get(position).toString());
	}

	private static class ViewHolder {
		private ImageView img;
		private TextView name;
		private CheckBox checkBox;
	}

	@Override
	protected void onResume() {
		singleItems.clear();
		super.onResume();
		new Thread(new Runnable() {

			@Override
			public void run() {
				getSingleItems();
			}
		}).start();
	}

}
