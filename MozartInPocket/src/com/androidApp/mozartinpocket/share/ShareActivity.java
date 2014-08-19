package com.androidApp.mozartinpocket.share;




import com.androidApp.mozartinpocket.*;

import com.androidApp.mozartinpocket.post.DoPostActivity;
import com.androidApp.mozartinpocket.util.Navigation;
import com.androidApp.mozartinpocket.util.ScreenShotUtils;
import com.androidApp.mozartinpocket.util.TabsControl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class ShareActivity extends FragmentActivity implements
		MusicsListFragment.Callbacks {
	private boolean mTwoPane;
	
	private static final String POST_SUCCESSFUL_MESSAGE = "Successfully Posted";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		TabsControl.controlTabs(this);
		RadioButton btn = (RadioButton) findViewById(R.id.navigation_bar).findViewById(R.id.main_tab_share);
        btn.setChecked(true);
		
		Log.w(this.toString(), findViewById(R.id.musics_list_in_share).getClass().toString());
		if (findViewById(R.id.share_detail_container) != null) {
			mTwoPane =true;

			((MusicsListFragment) getSupportFragmentManager().findFragmentById(
					R.id.musics_list_in_share)).setActivateOnItemClick(true);

		}
		
	}

@Override
	public void onItemSelected(String id, int position) {
		if (mTwoPane) {
			Bundle args = new Bundle();
			args.putString(ShareDetailFragment.SELECTED_MUSIC_FILE_NAME, id);
			ShareDetailFragment postDetailFragment = new ShareDetailFragment();
			postDetailFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.share_detail_container, postDetailFragment).commit();

		} else {
			
			Intent detailIntent = new Intent(this, DoPostActivity.class);
			detailIntent.putExtra(ShareDetailFragment.SELECTED_MUSIC_FILE_NAME, id);
			startActivity(detailIntent);
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control, menu);
		return true;
	}
	
	public void post(View view){
		Intent intent = new Intent(this,DoPostActivity.class);
		intent.putExtra("POST_SUCCESSFUL_MESSAGE", POST_SUCCESSFUL_MESSAGE);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_logout:
			Navigation.logout(this);
			return true;
		case R.id.screenShot:
			ScreenShotUtils.screenShot(this);
			return true;
		case R.id.userProfile:
			Navigation.editProfile(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode ==  ShareDetailFragment.REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK);
	}
	
}
