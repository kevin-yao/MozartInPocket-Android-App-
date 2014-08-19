package com.androidApp.mozartinpocket.exception;

import com.androidApp.mozartinpocket.EmptyFragment;
import com.androidApp.mozartinpocket.MusicsListActivity;
import com.androidApp.mozartinpocket.R;
import com.androidApp.mozartinpocket.post.PostActivity;
import com.androidApp.mozartinpocket.share.ShareActivity;

import android.app.Activity;
import android.util.Log;

public class MusicListEmptyException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String activityName;
	Activity activity;
	public MusicListEmptyException(Activity activity, String activityName){
		super("music list is empty");
		this.activity = activity;
		this.activityName = activityName;
	}

	public void exceptionFix(){
		EmptyFragment emptyFragment = new EmptyFragment();
		if(activityName.equals("MusicsListActivity")){
			((MusicsListActivity) activity).getSupportFragmentManager().beginTransaction()
			.replace(R.id.musics_detail_container, emptyFragment).commit();
			Log.w("MusicsListActivity","listempty");
		}
		else{
			if(activityName.equals("PostActivity")){
				((PostActivity) activity).getSupportFragmentManager().beginTransaction()
				.replace(R.id.post_detail_container, emptyFragment).commit();

			}
			else{
				if(activityName.equals("ShareActivity")){
					((ShareActivity) activity).getSupportFragmentManager().beginTransaction()
					.replace(R.id.post_detail_container, emptyFragment).commit();
				}

			}

		}
	}
}

