package com.androidApp.mozartinpocket.exception;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class EditTextEmptyException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Context context;
	private String message;
	//private String mode;
	public EditTextEmptyException(String s, Context context){
		super(s);
		this.context = context;
		this.message = s;

	}
	public void exceptionFix(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int whichbutton){
				dialog.dismiss();
			}
		});
		builder.create();
		builder.show();

	}
}
