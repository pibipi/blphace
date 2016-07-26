package com.qijitek.blphace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qijitek.database.CompareData;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.squareup.picasso.Picasso;

public class Fragment_b22 extends Fragment {
	private ListView listView;
	private MyAdapter myAdapter;
	private ArrayList<CompareData> compareDatas;
	private Handler mHandler;
	private AlertDialog reset_Dialog;
	private SharedpreferencesUtil sharedpreferencesUtil;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_b22, null);
		init(view);
		System.out.println("22 oncreate");
		return view;
	}

	private void init(View view) {
		mHandler = new Handler() {
		};
		sharedpreferencesUtil = new SharedpreferencesUtil(getContext());
		reset_Dialog = new AlertDialog.Builder(getActivity()).create();
		compareDatas = new ArrayList<CompareData>();
		listView = (ListView) view.findViewById(R.id.list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CompareData cd = compareDatas.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable("data", cd);
				Intent intent = new Intent(getActivity(),
						CompareResultActivity.class);
				intent.putExtras(bundle);
				intent.putExtra("checklist", true);
				startActivity(intent);
			}
		});
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSS");
				showDialog(position);
				return true;
			}
		});
		myAdapter = new MyAdapter(getContext());
		listView.setAdapter(myAdapter);
	}

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater layoutInflater;
		private Context context;

		public MyAdapter(Context context) {
			super();
			this.context = context;
			this.layoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return compareDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return compareDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.list_b22, null);
				viewHolder = new ViewHolder();
				viewHolder.time = (Button) convertView.findViewById(R.id.time);
				viewHolder.name1 = (TextView) convertView
						.findViewById(R.id.name1);
				viewHolder.name2 = (TextView) convertView
						.findViewById(R.id.name2);
				viewHolder.img1 = (ImageView) convertView
						.findViewById(R.id.img1);
				viewHolder.img2 = (ImageView) convertView
						.findViewById(R.id.img2);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			CompareData cd = compareDatas.get(position);
			viewHolder.name1.setText(cd.getName1());
			viewHolder.name2.setText(cd.getName2());
			if (!cd.getImgurl1().equals("")) {
				Picasso.with(context)
						.load(cd.getImgurl1())
						.resize(MyUtils.dip2px(context, 70),
								MyUtils.dip2px(context, 70)).centerCrop()
						.into(viewHolder.img1);
			}
			if (!cd.getImgurl2().equals("")) {
				Picasso.with(context)
						.load(cd.getImgurl2())
						.resize(MyUtils.dip2px(context, 70),
								MyUtils.dip2px(context, 70)).centerCrop()
						.into(viewHolder.img2);
			}
			long dt = System.currentTimeMillis() - Long.valueOf(cd.getTime());
			String dt_str = "";
			System.out.println(System.currentTimeMillis() + "dt" + dt);
			dt_str = (int) (dt / 86400000f) + "";
			if (dt_str.equals("0")) {
				viewHolder.time.setText("最新");
			} else {
				viewHolder.time.setText(dt_str + "天前");
			}

			return convertView;
		}
	}

	private static class ViewHolder {
		private Button time;
		private ImageView img1;
		private ImageView img2;
		private TextView name1;
		private TextView name2;
	}

	private void getCompareDatas() {
		String itemtype = new SharedpreferencesUtil(getContext()).getItemtype();
		String userid = new SharedpreferencesUtil(getContext()).getUserid();
		String url = "http://api.qijitek.com/getCompareTestList/?userid="
				+ userid + "&itemtype=" + itemtype;
		try {
			JSONObject jsonObject = MyUtils.getJson(url);
			JSONArray obj = jsonObject.getJSONArray("obj");
			int length = obj.length();
			for (int i = 0; i < length; i++) {
				JSONObject json = (JSONObject) obj.opt(i);
				String time = json.getString("time");
				String name1 = json.getString("name1");
				String imgurl1 = json.getString("imgurl1");
				String water1 = json.getString("water1");
				String oil1 = json.getString("oil1");
				String light1 = json.getString("light1");
				String imgurl2 = json.getString("imgurl2");
				String name2 = json.getString("name2");
				String water2 = json.getString("water2");
				String oil2 = json.getString("oil2");
				String light2 = json.getString("light2");
				CompareData cd = new CompareData(time, name1, imgurl1, water1,
						oil1, light1, name2, imgurl2, water2, oil2, light2);
				compareDatas.add(cd);
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
					Toast.makeText(getContext(), "请检查网络连接", 0).show();
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		System.out.println("22 onres");
		compareDatas.clear();
		super.onResume();
		new Thread(new Runnable() {

			@Override
			public void run() {
				getCompareDatas();
			}
		}).start();
	}

	private void showDialog(final int position) {
		reset_Dialog.show();
		Window window = reset_Dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置

		reset_Dialog.getWindow().setContentView(R.layout.dialog_deleteitem);
		reset_Dialog.getWindow().findViewById(R.id.delete)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								String url = "http://api.qijitek.com/deleteCompareItem/?userid="
										+ sharedpreferencesUtil.getUserid()
										+ "&itemtype="
										+ sharedpreferencesUtil.getItemtype()
										+ "&time="
										+ compareDatas.get(position).getTime();
								try {
									JSONObject jsonObject = MyUtils
											.getJson(url);
									mHandler.post(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(getContext(),
													"删除成功", 0).show();
											compareDatas.remove(position);
											myAdapter.notifyDataSetChanged();
										}
									});
								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									mHandler.post(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(getContext(),
													"请检查网络连接", 0).show();
										}
									});
									e.printStackTrace();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}).start();

						reset_Dialog.dismiss();
					}
				});
		reset_Dialog.setCancelable(true);
	}

}
