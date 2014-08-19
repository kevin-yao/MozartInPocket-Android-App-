package com.androidApp.mozartinpocket.post;



import com.androidApp.mozartinpocket.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DoPostActivity extends Activity {
//	private String URL="http://192.168.1.30:8080/MozartInPocket/RecieveMusic";
//	private String URL1="http://192.168.1.30:8080/MozartInPocket/RecievePost";
//	private ProgressBar progressBar;
//	private UploadMusicTask uploadMusicTask=null;
//	private static String nullFlag="*$#";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_do_post);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.do_post, menu);
		return true;
	}

}
