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

}
