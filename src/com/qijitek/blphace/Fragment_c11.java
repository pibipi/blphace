package com.qijitek.blphace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class Fragment_c11 extends Fragment implements OnClickListener {
	private RelativeLayout list_c111;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_c11_2, null);
		init(view);
		return view;
	}

	private void init(View view) {
		list_c111 = (RelativeLayout) view.findViewById(R.id.list_c111);
		list_c111.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.list_c111:
			startActivity(new Intent(getActivity(), Socialc2Activity2.class));
			break;

		default:
			break;
		}
	}
}
