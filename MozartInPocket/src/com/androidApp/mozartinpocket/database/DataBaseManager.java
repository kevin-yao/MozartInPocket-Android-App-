package com.androidApp.mozartinpocket.database;

import java.util.ArrayList;

import com.mozartinpocket.entities.Music;
import com.mozartinpocket.entities.User;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DataBaseManager {
	private SQLiteDatabase db;
	private DataBaseHelper dbHelper;

	public DataBaseManager(Context context){
		dbHelper = new DataBaseHelper(context);
	}

	public void open() throws SQLException{
		db=dbHelper.getWritableDatabase();
	}

	public void close(){
		dbHelper.close();
	}

	public void addUser(User user){
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM user WHERE username=?", new String[]{user.getUsername()});
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		if(count>0){
			deleteUser(user.getUsername());
		}
		db.execSQL("INSERT INTO user(username, password, name, age, gender,"
				+ "email, interests_tag, my_music_style, photo_filename) VALUES(?,"
				+ "?,?,?,?,?,?,?,?)", new Object[]{user.getUsername(), user.getPassword(), user.getName(), 
						user.getAge(), user.getGender(), user.getEmail(),user.getInterestsTag(), user.getMyMusicStyle(), 
						user.getPhotoFilename()});
	}

	public User getUser(String username){
		User user = new User();
		String sql="SELECT * FROM user WHERE username=?";
		Cursor cursor = db.rawQuery(sql,new String[]{username});
		if (cursor.moveToNext()){
		user.setUsername(username);
		user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
		user.setName(cursor.getString(cursor.getColumnIndex("name")));
		user.setAge(cursor.getInt(cursor.getColumnIndex("age")));
		user.setGender(cursor.getString(cursor.getColumnIndex("gender")));
		user.setEmail(cursor.getString(cursor.getColumnIndex("email")));	
		user.setInterestsTag(cursor.getString(cursor.getColumnIndex("interests_tag")));
		user.setMyMusicStyle(cursor.getString(cursor.getColumnIndex("my_music_style")));
		user.setPhotoFilename(cursor.getString(cursor.getColumnIndex("photo_filename")));
		return user;
		}
		return null;
	}

	public void deleteUser(String username){
		String sql="DELETE FROM user WHERE username=?";
		db.execSQL(sql,new String[]{username});
	}

	public void addMusic(Music music){
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM user WHERE name=?", new String[]{music.getName()});
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		if(count>0){
			deleteUser(music.getName());
		}
		db.execSQL("INSERT INTO music(name, author_username, date, description, save_path"
				+ ") VALUES(?,?,?,?,?)", new Object[]{music.getName(),music.getAuthorUsername(), 
						music.getDate(), music.getDescription(), music.getSavePath()});
	}

	public Music getMusic(String musicName){
		Music music = new Music();
		String sql="SELECT * FROM music WHERE name=?";
		Cursor cursor = db.rawQuery(sql,new String[]{musicName});
		if(cursor.moveToNext()){
		music.setName(musicName);
		music.setAuthorUsername((cursor.getString(cursor.getColumnIndex("author_username"))));
		music.setDate((cursor.getString(cursor.getColumnIndex("date"))));
		music.setDescription((cursor.getString(cursor.getColumnIndex("description"))));
		music.setSavePath(cursor.getString(cursor.getColumnIndex("save_path")));	
		return music;
		}
		return null;
	}
	
	public void deleteMusic(String musicName){
		String sql="DELETE FROM music WHERE name=?";
		db.execSQL(sql,new String[]{musicName});
	}

	public ArrayList<Music> getMusicList(String authorUsername){
		ArrayList<Music> musicList = new ArrayList<Music>();
		String sql="SELECT * FROM music WHERE author_username=?";
		Cursor cursor = db.rawQuery(sql,new String[]{authorUsername});
		if(cursor.getCount()==0){
		   return null;
		}	
		while(cursor.moveToNext())
		{
			Music music = new Music();
			music.setName((cursor.getString(cursor.getColumnIndex("name"))));
			music.setAuthorUsername((cursor.getString(cursor.getColumnIndex("author_username"))));
			music.setDate((cursor.getString(cursor.getColumnIndex("date"))));
			music.setDescription((cursor.getString(cursor.getColumnIndex("description"))));
			music.setSavePath(cursor.getString(cursor.getColumnIndex("save_path")));	
			musicList.add(music);
		}
		return musicList;
	}	
}