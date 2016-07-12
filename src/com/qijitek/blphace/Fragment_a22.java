package com.qijitek.blphace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_a22 extends Fragment implements OnClickListener {
	private TextView text_a;
	private TextView text_b;
	private TextView text_c;
	private int score = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_a22, null);
		init(view);
		return view;
	}

	private void init(View view) {
		text_a = (TextView) view.findViewById(R.id.text_a);
		text_b = (TextView) view.findViewById(R.id.text_b);
		text_c = (TextView) view.findViewById(R.id.text_c);
		text_a.setOnClickListener(this);
		text_b.setOnClickListener(this);
		text_c.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_a:
			score = 1;
			text_a.setTextColor(0xffda5b5b);
			text_b.setTextColor(0xff393939);
			text_c.setTextColor(0xff393939);
			break;
		case R.id.text_b:
			score = 3;
			text_a.setTextColor(0xff393939);
			text_b.setTextColor(0xffda5b5b);
			text_c.setTextColor(0xff393939);
			break;
		case R.id.text_c:
			score = 5;
			text_a.setTextColor(0xff393939);
			text_b.setTextColor(0xff393939);
			text_c.setTextColor(0xffda5b5b);
			break;

		default:

			break;
		}
		Intent intent = new Intent();
		intent.setAction(new Starta2Activity().ACTION_CHANGE_PAGER);
		intent.putExtra("pager", 2);
		intent.putExtra("score", score);
		getContext().sendBroadcast(intent);
	}
}
