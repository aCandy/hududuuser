package com.jizhi.hududu.uclient.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBOpenHelper extends SQLiteOpenHelper {

	
	public static final String TABLE_NAME = "t_dinner";
	public static final String COLUMN_NAME_ID = "_id";
	public static final String COLUMN_NAME_NAME = "name";// 服客姓名
	public static final String COLUMN_NAME_ORDERTOTAL = "ordertotal";// 服客完成订单数
	public static final String COLUMN_NAME_PIC = "pic";// 服客头像
	public static final String COLUMN_NAME_ICON = "icno";  // 服客身份证
	public static final String COLUMN_NAME_AGE = "age";// 服客年龄
	public static final String COLUMN_NAME_AVGRATE = "avgrate";// 服客获得评价平均分
	public static final String COLUMN_NAME_BESTCOOKING_PIC = "bestcooking_pic";
	public static final String COLUMN_NAME_BESTCOOKING_NAME = "bestcooking_name";
	public static final String COLUMN_NAME_LID = "lid";// 服客id
	public static final String COLUMN_NAME_FAR = "far";// 相隔距离
	public static final String COLUMN_NAME_IS_CHOOSE = "choose";//是否选择了该厨师
	
	private static final int DATABASE_VERSION = 1;
	private static DBOpenHelper instance;

	
	private static final String CREATE_DINNER_TABLE_CREATE = "CREATE TABLE "
			+ TABLE_NAME + " ("
			+ COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_NAME_NAME + " TEXT, "
			+ COLUMN_NAME_ORDERTOTAL + " TEXT, "
			+ COLUMN_NAME_PIC + " TEXT, "
			+ COLUMN_NAME_ICON + " TEXT, "
			+ COLUMN_NAME_AGE + " TEXT, "
			+ COLUMN_NAME_AVGRATE + " DECIMAL, "
			+ COLUMN_NAME_BESTCOOKING_NAME + " TEXT, "
			+ COLUMN_NAME_IS_CHOOSE + " TEXT, "
			+ COLUMN_NAME_BESTCOOKING_PIC + " TEXT, "
			+ COLUMN_NAME_FAR + " DOUBLE, "
			+ COLUMN_NAME_LID + " TEXT); ";

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}



	private SQLiteDatabase db;

	private DBOpenHelper(Context context) {
		super(context, "itcast.db", null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DINNER_TABLE_CREATE);
		this.db = db;
	}
	
	
	
	
	public static DBOpenHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DBOpenHelper(context.getApplicationContext());
		}
		return instance;
	}
	
	public void closeDB() {
		if (instance != null) {
			try {
				SQLiteDatabase db = instance.getWritableDatabase();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			instance = null;
		}
	}
}
