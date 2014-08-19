package com.androidApp.mozartinpocket.database;

import android.content.Context;
import android.database.sqlite.*;

public class DataBaseHelper extends SQLiteOpenHelper{
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "mozartinpocket.db";
	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db){
//		db.execSQL("DROP TABLE IF EXISTS music");  
		db.execSQL("CREATE TABLE IF NOT EXISTS music(music_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name VARCHAR(100),"+
				"author_username VARCHAR(100),"+
				"date VARCHAR(10),"+
				"description TEXT,"+
				"save_path VARCHAR(100)"+
                ")");
//		db.execSQL("DROP TABLE IF EXISTS user"); 
		db.execSQL("CREATE TABLE IF NOT EXISTS user(user_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"username VARCHAR(100),"+
				"password VARCHAR(12),"+
				"name VARCHAR(100),"+
				"age INTEGER,"+
				"gender VARCHAR(6),"+
				"email  VARCHAR(100),"+
				"interests_tag VARCHAR(200),"+
				"my_music_style VARCHAR(100),"+
				"photo_filename VARCHAR(100)"+
                ")");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		

	}
}
