package com.alphace.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static String DB_NAME = "face.db";
	private String TABLE_NAME = "face";
	private Context mContext;

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// if (!(TABLE_NAME.equals(""))) {
		String sql = "create table if not exists "
				+ TABLE_NAME
				+ " (id integer primary key AUTOINCREMENT ,type varchar(50) , water varchar(50), oil varchar(50), light varchar(50), uniform varchar(50), average varchar(50),time varchar(50))";
		db.execSQL(sql);
		// }

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	// type=0 护肤前
	// type=1 护肤后
	public long insert(int type, int water, int oil, int light, int uniform,
			int average, long time) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("type", type + "");
		cv.put("water", water + "");
		cv.put("oil", oil + "");
		cv.put("light", light + "");
		cv.put("uniform", uniform + "");
		cv.put("average", average + "");
		cv.put("time", time + "");
		long row = db.insert(TABLE_NAME, null, cv);
		System.out.println(row);
		return row;
	}
}
