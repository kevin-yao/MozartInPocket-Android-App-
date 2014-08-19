package com.androidApp.mozartinpocket;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

public class ChangePhotoDialog extends DialogFragment {
	public static final String PHOTOPATH = "/photo/";
	public static final int LOCAL_IMAGE = 0;
	public static final int REQUEST_IMAGE = 1;
	public static String fileName ;
	public static String photoName = new String("");
	Fragment ft = this;
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    builder.setTitle(R.string.change_photo);
		    builder.setItems(R.array.change_photo_array, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               // The 'which' argument contains the index position
	               // of the selected item
            		   final String start = Environment.getExternalStorageState();
	            	   if(which == 0) {
	            		   if(start.equals(Environment.MEDIA_MOUNTED)){
		       	        		Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
		       					getImage.addCategory(Intent.CATEGORY_OPENABLE);
		       					getImage.setType("image/jpeg");
		       					getActivity().startActivityForResult(getImage, LOCAL_IMAGE);
		                    }
	            	   }else if (which == 1) { 
	            		   if(start.equals(Environment.MEDIA_MOUNTED)){
	            			    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	   	       		    	    File file = new File(Environment.getExternalStorageDirectory()+PHOTOPATH);
	   	       		    	    if(!file.exists()){
	   	       		    	    	file.mkdirs();
	   	       		    	    }
	   	       		    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
	   	       		            String date = dateFormat.format(new Date());
	   	       		            photoName = "Picture_" + date;
	   	       		            String photoFile = "Picture_" + date + ".jpg";
	   	       		    	    StringBuffer buffer = new StringBuffer();
	   	       		    	    buffer.append(Environment.getExternalStorageDirectory()+PHOTOPATH).append(photoFile);
	   	       		    	    fileName = buffer.toString();
	   	       		    	    Uri uri = Uri.fromFile(new File(fileName));
	   	       		    	    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
	   	       		    	    getActivity().startActivityForResult(intent,REQUEST_IMAGE);
	   	       		    	}
	            	   }else if (which == 2) {
	            		   dialog.cancel();
	            	   }
	              } 
		    });
		    return builder.create();
    }
	
}
