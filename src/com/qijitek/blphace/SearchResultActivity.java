package com.qijitek.blphace;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qijitek.database.SearchItem;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.squareup.picasso.Picasso;

public class SearchResultActivity extends Activity implements OnClickListener {
	private RelativeLayout r1;
	private ListView list;
	private ArrayList<SearchItem> searchItems;
	private SharedpreferencesUtil sharedpreferencesUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		getData();
		init();
	}

	private void getData() {
		sharedpreferencesUtil = new SharedpreferencesUtil(
				getApplicationContext());
		searchItems = new ArrayList<SearchItem>();
		try {
			JSONObject jsonObject = new JSONObject(getIntent().getStringExtra(
					"json"));
			JSONArray array = jsonObject.optJSONArray("products");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = (JSONObject) array.opt(i);
				SearchItem s = new SearchItem(
						sharedpreferencesUtil.getUserid(),
						sharedpreferencesUtil.getItemtype(),
						object.optString("pname"), object.optString("pid"),
						MyUtils.getValue(object, "imgUrl"),
						object.optString("price"), object.optString("spec"));
				System.out.println(s.toString());
				searchItems.add(s);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		r1 = (RelativeLayout) findViewById(R.id.r1);
		r1.setOnClickListener(this);
		list = (ListView) findViewById(R.id.list);
	}

	private class MyAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			return searchItems.size();
		}

		@Override
		public Object getItem(int position) {
			return searchItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_search_item, null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.spec = (TextView) convertView.findViewById(R.id.spec);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.add = (Button) convertView.findViewById(R.id.add);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			SearchItem s = searchItems.get(position);
			holder.name.setText(s.getName());
			holder.spec.setText(s.getSpec());
			holder.price.setText(s.getPrice());
			if (!s.getImgurl().equals("")) {
				Picasso.with(context)
						.load(s.getImgurl())
						.resize(MyUtils.dip2px(context, 70),
								MyUtils.dip2px(context, 70)).centerCrop()
						.into(holder.img);
			}
			holder.add.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					//TODO
				}
			});
			return convertView;
		}
	}

	private static class ViewHolder {
		private ImageView img;
		private TextView name;
		private TextView spec;
		private TextView price;
		private Button add;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.r1:
			finish();
			break;

		default:
			break;
		}
	}
}
