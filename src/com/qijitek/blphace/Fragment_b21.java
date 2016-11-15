package com.qijitek.blphace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qijitek.database.SingleItem;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class Fragment_b21 extends Fragment implements OnClickListener {
	private ListView listView;
	private ArrayList<SingleItem> singleItems;
	private Handler mHandler;
	private Myadapter myadapter;
	private AlertDialog reset_Dialog;
	private SharedpreferencesUtil sharedpreferencesUtil;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_b21, null);
		init(view);
		return view;
	}

	private void init(View view) {
		mHandler = new Handler() {
		};
		sharedpreferencesUtil = new SharedpreferencesUtil(getContext());
		reset_Dialog = new AlertDialog.Builder(getActivity()).create();
		singleItems = new ArrayList<SingleItem>();
		listView = (ListView) view.findViewById(R.id.list);
		// listView.setOnItemLongClickListener(new
		// AdapterView.OnItemLongClickListener() {
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSS");
		// showDialog(position);
		// return true;
		// }
		// });
		myadapter = new Myadapter(getContext());
		listView.setAdapter(myadapter);
	}

	private void getSingleItems() {
		String itemtype = new SharedpreferencesUtil(getContext()).getItemtype();
		String userid = new SharedpreferencesUtil(getContext()).getUserid();
		String baseUrl = "http://api.qijitek.com/getTestList/";
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemtype", itemtype));
		try {
			JSONObject jsonObject = MyUtils.getJson2(baseUrl, params);
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
				SingleItem si = new SingleItem(name, imgurl, code, water, oil,
						light, itemtype, qid, alias, brand, methods, feature);
				singleItems.add(si);
			}
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					myadapter.notifyDataSetChanged();
				}
			});
			Log.e("9ge", singleItems.size() + "");
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

	private class Myadapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context context;

		public Myadapter(Context context) {
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
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_b21, null);
				viewHolder = new ViewHolder();
				viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.name);
				viewHolder.more = (RelativeLayout) convertView
						.findViewById(R.id.more);
				viewHolder.r1 = (RelativeLayout) convertView
						.findViewById(R.id.r1);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final SingleItem si = singleItems.get(position);
			viewHolder.name.setText(si.getName());
			if (!si.getImgurl().equals("")) {
				Picasso.with(context)
						.load(si.getImgurl())
						.resize(MyUtils.dip2px(context, 70),
								MyUtils.dip2px(context, 70)).centerCrop()
						.into(viewHolder.img);
			}
			if (si.getWater().equals("")) {
				viewHolder.more.setVisibility(View.GONE);
			}
			viewHolder.r1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							Manageb3Activity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("SingleItem", si);
					intent.putExtras(bundle);
					startActivity(intent);

				}
			});
			viewHolder.more.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							SingleResultActivity2.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("SingleItem", si);
					intent.putExtras(bundle);
					intent.putExtra("water", Integer.valueOf(si.getWater()));
					intent.putExtra("oil", Integer.valueOf(si.getOil()));
					intent.putExtra("light", Integer.valueOf(si.getLight()));
					intent.putExtra("single", true);
					startActivity(intent);
				}
			});
			viewHolder.r1
					.setOnLongClickListener(new View.OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSS");
							showDialog(position);
							return false;
						}
					});
			viewHolder.more
					.setOnLongClickListener(new View.OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSS");
							showDialog(position);
							return false;
						}
					});
			return convertView;
		}

	}

	private static class ViewHolder {
		private ImageView img;
		private TextView name;
		private RelativeLayout more;
		private RelativeLayout r1;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		singleItems.clear();
		super.onResume();
		new Thread(new Runnable() {

			@Override
			public void run() {
				getSingleItems();
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
								String baseurl = "http://api.qijitek.com/deleteSingleItem/";
								List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
								params.add(new BasicNameValuePair("userid",
										sharedpreferencesUtil.getUserid()));
								params.add(new BasicNameValuePair("itemtype",
										sharedpreferencesUtil.getItemtype()));
								params.add(new BasicNameValuePair("qid",
										singleItems.get(position).getQid()));
								try {
									JSONObject jsonObject = MyUtils.getJson2(
											baseurl, params);
									mHandler.post(new Runnable() {

										@Override
										public void run() {
											singleItems.remove(position);
											myadapter.notifyDataSetChanged();
											Toast.makeText(getContext(),
													"删除成功", 0).show();
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
