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

	public static String getWaterDetail(int water) {
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

	public static String getLightDetail(int light) {
		if (light >= 1 && light <= 50) {
			return "呜呜偏黑，尽量避免出门、在室内活动哦。外出时，佩戴好遮阳帽、遮阳伞、尽量选择穿长袖哦~每天饮水量要充足。日常饮食中，多食富含维生素A的食物和新鲜蔬菜、水果。出行时，要在脸上擦上SPF30以上的防晒护肤品。";
		} else if (light >= 51 && light <= 70) {
			return "相对偏白呢，再接再厉！晴天出门还是要涂防晒霜哦，阴天也要注意哑巴太阳做好防晒！平时多运动并用酸奶敷脸，可以适当补充VCVE，二者结合有助于击退黑色素哦！争取变成白炽灯！";
		} else if (light >= 71 && light <= 99) {
			return "你已经很白啦，但是夏季或者如果长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。";
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

	public static String getOilDetail(int oil) {
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
