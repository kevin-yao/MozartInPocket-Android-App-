package com.androidApp.mozartinpocket;

import java.util.ArrayList;
import java.util.Iterator;

import com.androidApp.mozartinpocket.R;
import com.androidApp.mozartinpocket.database.DataBaseManager;
import com.androidApp.mozartinpocket.exception.MusicListEmptyException;
import com.androidApp.mozartinpocket.util.Navigation;
import com.androidApp.mozartinpocket.util.ScreenShotUtils;
import com.androidApp.mozartinpocket.util.TabsControl;
import com.mozartinpocket.entities.Music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
public class MusicsListActivity extends FragmentActivity implements MusicsListFragment.Callbacks, MusicsDetailFragment.Callbacks, MusicsDetailFragment.Callbacks2 {
	public int status = 0;
	private boolean mTwoPane;
	MyPagerAdapter pagerAdapter;
	ViewPager viewPager;
	private DataBaseManager dbManager = new DataBaseManager(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_musics_twopane);
		TabsControl.controlTabs(this);
		RadioButton btn = (RadioButton) findViewById(R.id.navigation_bar).findViewById(R.id.main_tab_music_list);
		btn.setChecked(true);

		if(MusicItemList.musicItemList.isEmpty()){
			dbInitialize();
		}


		if (findViewById(R.id.musics_detail_container) != null) {
			mTwoPane =true;

			((MusicsListFragment) getSupportFragmentManager().findFragmentById(
					R.id.musics_list)).setActivateOnItemClick(true);			
		}
		try{
			if(MusicItemList.musicItemList.isEmpty()){
				throw new MusicListEmptyException(this,"MusicsListActivity");
			}
		}
		catch(MusicListEmptyException e){
			e.exceptionFix();
		}
	}
	@Override
	protected void onPause(){
		super.onPause();
		if(this.isFinishing() && !MusicItemList.musicItemList.isEmpty()){
			((MusicsDetailFragment) getSupportFragmentManager()
					.findFragmentById(R.id.musics_detail_container)).stopMediaPlayer();
		}
	}
	@Override
	public void onStop(){
		super.onStop();
		if(!MusicItemList.musicItemList.isEmpty() && status == 2){	
			((MusicsDetailFragment) getSupportFragmentManager()
					.findFragmentById(R.id.musics_detail_container)).stopMediaPlayer();
		}
	}

	@Override
	public void onItemSelected(String musicName, int position) {
		if (mTwoPane) {

			Bundle args = new Bundle();
			args.putString(MusicsDetailFragment.SELECTED_MUSIC_FILE_NAME, musicName);
			args.putInt(MusicsDetailFragment.SELECTED_MUSIC_POSITION, position);
			Log.w("MusicListActivity",musicName);
			MusicsDetailFragment musicDetailFragment = new MusicsDetailFragment();
			musicDetailFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.musics_detail_container, musicDetailFragment).commit();

		} else {
			Intent detailIntent = new Intent(this, MusicsDetailActivity.class);
			detailIntent.putExtra(MusicsDetailFragment.SELECTED_MUSIC_FILE_NAME, musicName);
			startActivity(detailIntent);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//((MusicsDetailFragment) getSupportFragmentManager().findFragmentById(R.id.musics_detail_container)).stopMediaPlayer();
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
	public void deleteMusicFile(String musicName, int position) {
		FragmentManager fm = getSupportFragmentManager();
		MusicsDetailFragment detailFragment = (MusicsDetailFragment) fm.findFragmentById(R.id.musics_detail_container);
		fm.beginTransaction().remove(detailFragment).commit();
		Log.w("selected position", Integer.toString(position));

		MusicItemList.musicItemList.remove(position);
		MusicItemList.musicMap.remove(musicName);
		MusicItemList.print();

		((MusicsListFragment)fm.findFragmentById(R.id.musics_list)).musicArrayAdapter.notifyDataSetChanged();
		fm.executePendingTransactions();

		dbManager.open();
		dbManager.deleteMusic(musicName);
		dbManager.close();
	}
	@Override
	public void changeSelectedPosition(String musicName, int position) {
		String nextMusicName = null;
		int nextPosition = 0;
		if(position<MusicItemList.musicItemList.size()-1){
			nextPosition = position+1;
		}
		nextMusicName = MusicItemList.musicItemList.get(nextPosition);
		onItemSelected(nextMusicName, nextPosition);
		((MusicsListFragment)getSupportFragmentManager().findFragmentById(R.id.musics_list)).notifySelectedItemChange(nextPosition);
		
	}
	private void dbInitialize(){
		dbManager.open();
		ArrayList<Music> musicList =dbManager.getMusicList(LoginActivity.currentUser.getUsername());
		if(musicList == null){
			Log.w("From Database","currently you do not have any music files");
		}
		else{
			Iterator<Music> it = musicList.iterator();
			while(it.hasNext()){
				Music music  = it.next();
				String musicName = music.getName();
				Log.w("From Database",musicName);
				MusicItemList.musicItemList.add(musicName);
				MusicItemList.musicMap.put(musicName,music);
			}
		}
		dbManager.close();
	}
}
