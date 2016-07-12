package com.qijitek.blphace;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pickerview.OptionsPopupWindow;
import com.pickerview.OptionsPopupWindow.OnOptionsSelectListener;
import com.qijitek.utils.HttpUtils;
import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;

public class MyAddressActivity extends Activity implements OnClickListener {
	private TextView skin_type;
	private TextView sex;
	private TextView province;
	private EditText name;
	private EditText age;
	private EditText phone;
	private EditText address;
	private Button submit;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myaddress);
		init();
	}

	private void init() {
		mHandler = new Handler() {
		};
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
		name = (EditText) findViewById(R.id.name);
		age = (EditText) findViewById(R.id.age);
		phone = (EditText) findViewById(R.id.phone);
		address = (EditText) findViewById(R.id.address);
		skin_type = (TextView) findViewById(R.id.skin_type);
		sex = (TextView) findViewById(R.id.sex);
		province = (TextView) findViewById(R.id.province);
		init_type();
		init_sex();
		init_province();
	}

	private void init_province() {
		String[] ps = { "北京市", "上海市", "天津市", "重庆市", "河北省", "山西省", "辽宁省", "吉林省",
				"黑龙江省", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省",
				"湖南省", "青海省", "广东省", "海南省", "四川省", "贵州省", "云南省", "陕西省", "甘肃省",
				"宁夏回族自治区", "西藏自治区", "广西壮族自治区", "内蒙古自治区", "新疆维吾尔自治区", "香港",
				"澳门", "台湾", "其它" };
		// 选项选择器
		final OptionsPopupWindow pwOptions = new OptionsPopupWindow(this);
		// 选项1
		final ArrayList<String> options1Items = new ArrayList<String>();
		for (int i = 0; i < ps.length; i++) {
			options1Items.add(ps[i]);
		}

		pwOptions.setPicker(options1Items);

		pwOptions.setSelectOptions(0);

		pwOptions.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				// 返回的分别是三个级别的选中位置
				String tx = options1Items.get(options1);
				province.setText(tx);
			}
		});

		// 点击弹出选项选择器
		province.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pwOptions.showAtLocation(province, Gravity.BOTTOM, 0, 0);
			}
		});
	}

	private void init_sex() {
		// 选项选择器
		final OptionsPopupWindow pwOptions = new OptionsPopupWindow(this);
		// 选项1
		final ArrayList<String> options1Items = new ArrayList<String>();
		options1Items.add("男");
		options1Items.add("女");

		pwOptions.setPicker(options1Items);

		pwOptions.setSelectOptions(0);

		pwOptions.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				// 返回的分别是三个级别的选中位置
				String tx = options1Items.get(options1);
				sex.setText(tx);
			}
		});

		// 点击弹出选项选择器
		sex.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pwOptions.showAtLocation(sex, Gravity.BOTTOM, 0, 0);
			}
		});
	}

	private void init_type() {
		// 选项选择器
		final OptionsPopupWindow pwOptions = new OptionsPopupWindow(this);
		// 选项1
		final ArrayList<String> options1Items = new ArrayList<String>();
		options1Items.add("干性肌肤");
		options1Items.add("油性肌肤");
		options1Items.add("混合性肌肤");

		pwOptions.setPicker(options1Items);

		pwOptions.setSelectOptions(0);

		pwOptions.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				// 返回的分别是三个级别的选中位置
				String tx = options1Items.get(options1);
				skin_type.setText(tx);
			}
		});

		// 点击弹出选项选择器
		skin_type.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pwOptions.showAtLocation(skin_type, Gravity.BOTTOM, 0, 0);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
			if (!edit_notnull()) {
				return;
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					String userid = new SharedpreferencesUtil(
							getApplicationContext()).getUserid();
					String age_txt = age.getText().toString().trim();
					String sex_txt = sex.getText().toString().trim();
					String phone_txt = phone.getText().toString().trim();
					String province_txt = province.getText().toString().trim();
					String address_txt = address.getText().toString().trim();
					String name_txt = name.getText().toString().trim();

					String url = "http://api.qijitek.com/updateAddress/?userid="
							+ userid
							+ "&age="
							+ age_txt
							+ "&sex="
							+ sex_txt
							+ "&phone="
							+ phone_txt
							+ "&province="
							+ province_txt
							+ "&address="
							+ address_txt
							+ "&name=" + name_txt;
					try {
						JSONObject jsonObject = MyUtils.getJson(url);
						MyAddressActivity.this.finish();
						if (jsonObject == null) {
							System.out.println("SB le ba");
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),
										"请检查网络连接", 0).show();
							}
						});
						System.out.println("没网");
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.edit:

			break;
		default:
			break;
		}
	}

	private boolean edit_notnull() {
		boolean flag = false;
		// if (!name.getText().toString().trim().equals("")
		// && !sex.getText().toString().trim().equals("")
		// && !age.getText().toString().trim().equals("")
		// && !skin_type.getText().toString().trim().equals("")
		// && !phone.getText().toString().trim().equals("")
		// && !province.getText().toString().trim().equals("")
		// && !address.getText().toString().trim().equals("")) {
		// }
		if (name.getText().toString().trim().equals("")) {
			toastShort("请输入姓名");
		} else if (sex.getText().toString().trim().equals("")) {
			toastShort("请选择性别");
		} else if (age.getText().toString().trim().equals("")) {
			toastShort("请输入年龄");
		} else if (skin_type.getText().toString().trim().equals("")) {
			toastShort("请选择肌肤类型");
		} else if (phone.getText().toString().trim().equals("")) {
			toastShort("请输入收货人手机号");
		} else if (sex.getText().toString().trim().equals("")) {
			toastShort("请输入省份");
		} else if (address.getText().toString().trim().equals("")) {
			toastShort("请输入收货地址");
		} else {
			flag = true;
		}
		return flag;
	}

	private void toastShort(String str) {
		Toast.makeText(getApplicationContext(), str, 0).show();
	}
}
