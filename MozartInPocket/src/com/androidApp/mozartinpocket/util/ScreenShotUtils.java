package com.androidApp.mozartinpocket.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ScreenShotUtils {	
	public static String PHOTOPATH = "/photo/";
	public static String fileName = "";
	
	public static void screenShot(Activity activity) {
		boolean result = shotBitmap(activity);
		final Activity a = activity;
		if(result)  
        {   
			ImageView img = new ImageView(activity);
            img.setImageBitmap(getBitmapFromSDCard(ScreenShotUtils.fileName));
        
            new AlertDialog.Builder(activity).setTitle("Save the screenshot?")
                                         .setView(img)
                                         .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                             public void onClick(DialogInterface dialog, int id) {
                                                 Toast.makeText(a, "Screen shot saved!", Toast.LENGTH_SHORT).show(); 
                                             }
                                         })
                                         .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                             public void onClick(DialogInterface dialog, int id) {
                                                 if(deleteFoder(new File(fileName))) 
                                                	 dialog.cancel();
                                             }
                                         })
                                         .show(); 
        }else {  
            Toast.makeText(activity, "Screen shot failed!", Toast.LENGTH_SHORT).show();  
        }  
	}
	
	public static Bitmap takeScreenShot(Activity activity)
	{
		Bitmap bitmap=null;
		View view=activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		bitmap=view.getDrawingCache();
		
		Rect frame=new Rect();
		view.getWindowVisibleDisplayFrame(frame);
		int stautsHeight=frame.top;
		
		int width=activity.getBaseContext().getResources().getDisplayMetrics().widthPixels;
		int height=activity.getBaseContext().getResources().getDisplayMetrics().heightPixels;
		bitmap=Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height-stautsHeight);
		return bitmap;
	}

	private static boolean savePic(Bitmap bitmap,String strName)
	{
	  FileOutputStream fos=null;
	  try {
		fos=new FileOutputStream(strName);
		if(null!=fos)
		{
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			fos.flush();
			fos.close();
			fileName = strName;
			return true;
		}
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}catch (IOException e) {
		e.printStackTrace();
	}
	  return false;
	} 
	
	public static boolean shotBitmap(Activity activity)
	{
		boolean result = false;
		final String start = Environment.getExternalStorageState();
		if(start.equals(Environment.MEDIA_MOUNTED)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
		    String date = dateFormat.format(new Date());
		    String photoFile = "Screenshot_" + date + ".jpg";
		    StringBuffer buffer = new StringBuffer();
		    buffer.append(Environment.getExternalStorageDirectory()+PHOTOPATH).append(photoFile);
		    String fileName = buffer.toString();
		    Log.v("file", fileName);
		    result = savePic(takeScreenShot(activity), fileName);
		}
		return result;
	}
	
	public static Bitmap getBitmapFromSDCard(String file) 
	   {
	      try  
	      {
	         Bitmap bitmap = BitmapFactory.decodeFile(file);
	         return bitmap;
	      }  
	      catch (Exception e)  
	      {  
	         e.printStackTrace();  
	         return null;  
	      }  
	   }

	public static boolean deleteFoder(File file) {
        if (file.exists()) {
                if (file.isFile()) {
                        file.delete(); 
                } else if (file.isDirectory()) { 
                        File files[] = file.listFiles();
                        if (files != null) {
                                for (int i = 0; i < files.length; i++) { 
                                        deleteFoder(files[i]); 
                                }
                        }
                }
                boolean isSuccess = file.delete();
                if (!isSuccess) {
                        return false;
                }
        }
        return true;
   }
	
//	public static String getFileName() {
//		return fileName;
//	}
//
//	public void setFileName(String fileName) {
//		ScreenShotUtils.fileName = fileName;
//	}

}
