package com.androidApp.mozartinpocket.exception;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class MusicContentEmptyException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Context context;
	private static String message = "  Music Content can not be empty \n  Please save after compose your music!";
	//private String mode;
	public MusicContentEmptyException(Context context){
		super(message);
		this.context = context;
		Log.w("content","music content can not be empty");

	}


	public void exceptionFix(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setTitle("Can not save empty music");
		builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int whichbutton){
				dialog.dismiss();
			}
		});
		
		builder.create();
		Log.w("content","music content can not be empty");

		builder.show();
		Log.w("content","music content can not be empty");
	}
}


