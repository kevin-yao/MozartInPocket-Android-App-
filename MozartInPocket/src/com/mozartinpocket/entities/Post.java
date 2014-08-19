package com.mozartinpocket.entities;

import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Post implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int postId=0;
//	private int musicId=0;
	private String musicName=null;
	private String comment=null;
	private String authorUsername=null;
//	private String commenterUserName=null;
	private String date=null;
	private String musicUrl=null;
	private String userPhotoUrl=null;
	private double latitude=0;
	private double longitude=0;
	
	public Post(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		this.date=df.format(new Date());;
	}
	public int getPostId(){
		return postId;
	}
	public void setPostId(int postId){
		this.postId=postId;
	}
	public void setMusicUrl(String musicUrl){
		this.musicUrl=musicUrl;
	}
	public String getMusicUrl(){
		return musicUrl;
	}
	public void setUserPhotoUrl(String userPhotoUrl){
		this.userPhotoUrl=userPhotoUrl;
	}
	public String getUserPhotoUrl(){
		return userPhotoUrl;
	}
	public void setMusicName(String musicName){
		this.musicName=musicName;
	}
	public String getMusicName(){
		return musicName;
	}
	public void setComment(String comment){
		this.comment=comment;
	}
	public String getComment(){
		return comment;
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
	public void setLatitude(double latitude){
		this.latitude=latitude;
	}
	public double getLatitude(){
		return latitude;
	}
	public void setLongitude(double longitude){
		this.longitude=longitude;
	}
	public double getLongitude(){
		return longitude;
	}
}