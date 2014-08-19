package com.androidApp.mozartinpocket.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

public class FileCache {
	private File cacheDir;
	public FileCache(Context context) {
		//Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"cached_images");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
	}
	
	public File getFile(String url) {
		String filename = new String();
		try {
			filename = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File f = new File(cacheDir,filename);
		return f;
	}
	
	public void clear() {
		File[] files = cacheDir.listFiles();
		if(files == null)
			return;
		for(File f:files)
			f.delete();
	}
}
