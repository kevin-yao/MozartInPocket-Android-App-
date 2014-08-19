package com.androidApp.mozartinpocket.util;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class MemoryCache {
	// Global cache of images.
	// Using SoftReference to allow garbage collector to clean cache if needed
	private final Map<String, SoftReference<Bitmap>> cache = Collections.synchronizedMap(new HashMap<String,  SoftReference<Bitmap>>());
	public Bitmap get(String id) {
		if(!cache.containsKey(id))
			return null;
		SoftReference<Bitmap> ref = cache.get(id);
		return ref.get();
	}
	
	public void put(String id, Bitmap bitmap) {
		cache.put(id, new SoftReference<Bitmap>(bitmap));
	}
	
	public void clear() {
		cache.clear();
	}
}
