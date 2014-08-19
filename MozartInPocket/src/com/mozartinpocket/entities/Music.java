package com.mozartinpocket.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Music {
	private int musicId=0;
	private String name=null;
	private String date=null;
	private String description=null;
	private String savePath=null;
	private String authorUsername=null;

	public Music(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		this.date=df.format(new Date());;
	}

	public Music(String name, String authorUsername, String date, String description){
		this.name = name;
		this.authorUsername = authorUsername;
		this.date = date;
		this.description = description;
	}
	public int getMusicId(){
		return musicId;
	}
	public void setMusicId(int musicId){
		this.musicId=musicId;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setAuthorUsername(String authorUsername){
		this.authorUsername=authorUsername;
	}
	public String getAuthorUsername(){
		return authorUsername;
	}
	public void setDate(String date){
		this.date=date;
	}
	public String getDate(){
		return date;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public String getDescription(){
		return description;
	}
	public void setSavePath(String savePath){
		this.savePath=savePath;
	}
	public String getSavePath(){
		return savePath;
	}
}

