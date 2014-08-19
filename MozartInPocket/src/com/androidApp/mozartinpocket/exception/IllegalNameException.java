package com.androidApp.mozartinpocket.exception;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class IllegalNameException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Context context;
	private String username;
	private String password;
	private String[] errorMessage = {"Username can not be empty\n", "Password can not be empty\n","Username can not contain \"\\\\\" \n",
			"Password can not contain \"\\\\\" \n", "Username can not exceed 25 characters", "Password can not exceed 25 characters"};
	public IllegalNameException(Context context, String username, String password){
		super("Illegal File Name Exception");
		this.context = context;
		this.username = username;
		this.password = password;
	}
	
	public void exceptionFix(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Illegal File Name");
		builder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		String illegalStatement = "";
		String regex = "\\\\";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher1 = pattern.matcher(username);
		Matcher matcher2 = pattern.matcher(password);
		String m1=null,m2 = null;
		if(matcher1.find()){
			m1 = matcher1.group();
		}
		if(matcher2.find()){
			m2 = matcher2.group();
		}
		if(username.length()==0){
			illegalStatement = illegalStatement + errorMessage[0];
		}
		if(password.length()==0){
			illegalStatement = illegalStatement + errorMessage[1];
		}
		if(m1!=null){
			illegalStatement = illegalStatement +errorMessage[2];
		}
		if(m2!=null){
			illegalStatement = illegalStatement +errorMessage[3];
		}
		if(username.length()>25){
			illegalStatement = illegalStatement +errorMessage[4];
		}
		if(password.length()>25){
			illegalStatement = illegalStatement +errorMessage[4];
		}
		builder.setMessage(illegalStatement);
		builder.create();
		builder.show();
		
	}
}
