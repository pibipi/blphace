package com.qijitek.blphace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Fragment_b21 extends Fragment implements OnClickListener {
	private ImageView list_img1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_b21, null);
		init(view);
		return view;
	}

	private void init(View view) {
		list_img1 = (ImageView) view.findViewById(R.id.list_img1);
		list_img1.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.list_img1:
			startActivity(new Intent(getActivity(), Manageb3Activity.class));
			break;

		default:
			break;
		}
	}
}
