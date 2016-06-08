package com.qijitek.utils;

import android.widget.ImageView;

import com.qijitek.blphace.R;

/**
 * 通过得分判断皮肤type
 * 
 * @author kist
 * 
 */
public class GetTypeUtils {

	public static String getWaterType(int water) {
		if (water >= 1 && water <= 23) {
			return "很干燥";
		} else if (water >= 24 && water <= 28) {
			return "干燥";
		} else if (water >= 29 && water <= 39) {
			return "缺水";
		} else if (water >= 40 && water <= 43) {
			return "干性";
		} else if (water >= 44 && water <= 50) {
			return "较湿润";
		} else if (water >= 51 && water <= 61) {
			return "湿润";
		} else if (water >= 62 && water <= 99) {
			return "水润";
		} else
			return "";
	}

	public static String getLightType(int light) {
		if (light >= 1 && light <= 40) {
			return "暗沉";
		} else if (light >= 41 && light <= 50) {
			return "偏暗";
		} else if (light >= 51 && light <= 60) {
			return "正常";
		} else if (light >= 61 && light <= 70) {
			return "较白";
		} else if (light >= 71 && light <= 80) {
			return "白皙";
		} else if (light >= 81 && light <= 90) {
			return "洁白";
		} else if (light >= 91 && light <= 99) {
			return "女神";
		} else
			return "";
	}

	public static String getOilType(int oil) {
		if (oil >= 1 && oil <= 40) {
			return "不平衡";
		} else if (oil >= 41 && oil <= 60) {
			return "平衡";
		} else if (oil >= 61 && oil <= 99) {
			return "不平衡";
		} else
			return "";
	}

	public static String getAverageType(int oil) {
		if (oil >= 1 && oil <= 60) {
			return "不均匀";
		} else if (oil >= 61 && oil <= 80) {
			return "均匀";
		} else if (oil >= 81 && oil <= 99) {
			return "很均匀";
		} else
			return "";
	}

	public static void setResultImg(int p, ImageView iv) {
		if (p >= 1 && p <= 2) {
			iv.setBackgroundResource(R.drawable.mark1);
		} else if (p >= 3 && p <= 4) {
			iv.setBackgroundResource(R.drawable.mark2);
		} else if (p >= 5 && p <= 6) {
			iv.setBackgroundResource(R.drawable.mark3);
		} else if (p >= 7 && p <= 8) {
			iv.setBackgroundResource(R.drawable.mark4);
		} else if (p >= 9 && p <= 10) {
			iv.setBackgroundResource(R.drawable.mark5);
		} else if (p >= 11 && p <= 12) {
			iv.setBackgroundResource(R.drawable.mark6);
		} else if (p >= 13 && p <= 14) {
			iv.setBackgroundResource(R.drawable.mark7);
		} else if (p >= 15 && p <= 16) {
			iv.setBackgroundResource(R.drawable.mark8);
		} else if (p >= 17 && p <= 18) {
			iv.setBackgroundResource(R.drawable.mark9);
		} else if (p >= 19 && p <= 20) {
			iv.setBackgroundResource(R.drawable.mark10);
		} else if (p >= 21 && p <= 22) {
			iv.setBackgroundResource(R.drawable.mark11);
		} else if (p >= 23 && p <= 24) {
			iv.setBackgroundResource(R.drawable.mark12);
		} else if (p >= 25 && p <= 26) {
			iv.setBackgroundResource(R.drawable.mark13);
		} else if (p >= 27 && p <= 28) {
			iv.setBackgroundResource(R.drawable.mark14);
		} else if (p >= 29 && p <= 30) {
			iv.setBackgroundResource(R.drawable.mark15);
		} else if (p >= 31 && p <= 67) {
			iv.setBackgroundResource(R.drawable.mark16);
		} else if (p >= 68 && p <= 69) {
			iv.setBackgroundResource(R.drawable.mark17);
		} else if (p >= 70 && p <= 71) {
			iv.setBackgroundResource(R.drawable.mark18);
		} else if (p >= 72 && p <= 73) {
			iv.setBackgroundResource(R.drawable.mark19);
		} else if (p >= 74 && p <= 75) {
			iv.setBackgroundResource(R.drawable.mark20);
		}
		// else if (p >= 74 && p <= 75) {
		// iv.setBackgroundResource(R.drawable.mark21);
		// }
		else if (p >= 76 && p <= 77) {
			iv.setBackgroundResource(R.drawable.mark21);
		} else if (p >= 78 && p <= 79) {
			iv.setBackgroundResource(R.drawable.mark22);
		} else if (p >= 80 && p <= 81) {
			iv.setBackgroundResource(R.drawable.mark23);
		} else if (p >= 82 && p <= 83) {
			iv.setBackgroundResource(R.drawable.mark24);
		} else if (p >= 84 && p <= 85) {
			iv.setBackgroundResource(R.drawable.mark25);
		} else if (p >= 86 && p <= 87) {
			iv.setBackgroundResource(R.drawable.mark26);
		} else if (p >= 88 && p <= 89) {
			iv.setBackgroundResource(R.drawable.mark27);
		} else if (p >= 90 && p <= 91) {
			iv.setBackgroundResource(R.drawable.mark28);
		} else if (p >= 92 && p <= 93) {
			iv.setBackgroundResource(R.drawable.mark29);
		} else if (p >= 94 && p <= 95) {
			iv.setBackgroundResource(R.drawable.mark30);
		} else if (p >= 96 && p <= 97) {
			iv.setBackgroundResource(R.drawable.mark31);
		} else if (p >= 98 && p <= 99) {
			iv.setBackgroundResource(R.drawable.mark32);
		} else {
			System.out.println("p" + p);
			iv.setBackgroundResource(R.drawable.mark32);
		}

	}
}
