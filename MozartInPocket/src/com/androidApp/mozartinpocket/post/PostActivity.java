package com.androidApp.mozartinpocket.post;

import com.androidApp.mozartinpocket.*;
import com.androidApp.mozartinpocket.util.Navigation;
import com.androidApp.mozartinpocket.util.ScreenShotUtils;
import com.androidApp.mozartinpocket.util.TabsControl;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class PostActivity extends FragmentActivity implements
    MusicsListFragment.Callbacks {
	private boolean mTwoPane;
	private static final String POST_SUCCESSFUL_MESSAGE = "Successfully Posted";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		TabsControl.controlTabs(this);
		RadioButton btn = (RadioButton) findViewById(R.id.navigation_bar).findViewById(R.id.main_tab_post);
		btn.setChecked(true);

		Log.w(this.toString(), findViewById(R.id.musics_list_in_post).getClass().toString());
		if (findViewById(R.id.post_detail_container) != null) {
			mTwoPane =true;

			((MusicsListFragment) getSupportFragmentManager().findFragmentById(
					R.id.musics_list_in_post)).setActivateOnItemClick(true);

		}
		if(getIntent().hasExtra(MusicsDetailFragment.SELECTED_MUSIC_FILE_NAME)){
		if(getIntent().getExtras().containsKey(MusicsDetailFragment.SELECTED_MUSIC_FILE_NAME)){
			onItemSelected(getIntent().getStringExtra(MusicsDetailFragment.SELECTED_MUSIC_FILE_NAME)
					,getIntent().getIntExtra(MusicsDetailFragment.SELECTED_MUSIC_POSITION,0));
				}
		}

	}

	@Override
	public void onItemSelected(String musicName, int position) {
		if (mTwoPane) {
			Bundle args = new Bundle();
			args.putString(PostDetailFragment.SELECTED_MUSIC_FILE_NAME, musicName);
			args.putInt(MusicsDetailFragment.SELECTED_MUSIC_POSITION, position);
			PostDetailFragment postDetailFragment = new PostDetailFragment();
			postDetailFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.post_detail_container, postDetailFragment).commit();

		} else {

			Intent detailIntent = new Intent(this, DoPostActivity.class);
			detailIntent.putExtra(PostDetailFragment.SELECTED_MUSIC_FILE_NAME, musicName);
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
}
