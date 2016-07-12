package com.qijitek.blphace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qijitek.utils.SharedpreferencesUtil;

public class LeftFragment extends Fragment implements OnClickListener {
	RelativeLayout r1;
	RelativeLayout r2;
	RelativeLayout r3;
	RelativeLayout r4;
	RelativeLayout r5;
	private boolean isTest;
	private TextView isTest_txt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.slide_main, null);
		init(view);
		return view;
	}

	private void init(View view) {
		isTest_txt = (TextView) view.findViewById(R.id.isTest_txt);
		r1 = (RelativeLayout) view.findViewById(R.id.r1);
		r2 = (RelativeLayout) view.findViewById(R.id.r2);
		r3 = (RelativeLayout) view.findViewById(R.id.r3);
		r4 = (RelativeLayout) view.findViewById(R.id.r4);
		r5 = (RelativeLayout) view.findViewById(R.id.r5);
		r1.setOnClickListener(this);
		r2.setOnClickListener(this);
		r3.setOnClickListener(this);
		r4.setOnClickListener(this);
		r5.setOnClickListener(this);
		isTest = new SharedpreferencesUtil(getContext()).getIsTest();
		if (!isTest) {
			isTest_txt.setVisibility(View.VISIBLE);
		} else {
			isTest_txt.setVisibility(View.GONE);

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:

			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.r1:
			if (isTest) {
				startActivity(new Intent(getActivity(), MySkinActivity.class));
			} else {
				new SharedpreferencesUtil(getContext()).saveIsTesting(true);
				Intent intent = new Intent(getActivity(), Starta1Activity.class);
				startActivity(intent);
			}
			break;
		case R.id.r2:
			startActivity(new Intent(getActivity(), MyAddressActivity.class));
			break;
		case R.id.r3:

			break;
		case R.id.r4:
			startActivity(new Intent(getActivity(), AboutUsActivity.class));
			break;
		case R.id.r5:
			startActivity(new Intent(getActivity(),
					Personal_Setting_Activity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		isTest = new SharedpreferencesUtil(getContext()).getIsTest();
		System.out.println("istest" + isTest);
		if (!isTest) {
			isTest_txt.setVisibility(View.VISIBLE);
		} else {
			isTest_txt.setVisibility(View.GONE);

		}
		super.onResume();
	}

}
