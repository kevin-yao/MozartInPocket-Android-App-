package com.androidApp.mozartinpocket;

import com.androidApp.mozartinpocket.fragments.*;
import com.androidApp.mozartinpocket.util.Navigation;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class ControlActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		startActivity(new Intent(this, TabsFragment.class));
//		ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		ActionBar.Tab composeTab = actionBar.newTab().setText("Compose");
//		ActionBar.Tab postTab = actionBar.newTab().setText("Post");
//		ActionBar.Tab shareTab = actionBar.newTab().setText("Share");
//		ActionBar.Tab listTab = actionBar.newTab().setText("My List");
//		ActionBar.Tab cloudTab = actionBar.newTab().setText("Music in Cloud");
//		
//		android.app.Fragment composeFragment = new ComposeFragment();
//		android.app.Fragment postFragment = new PostFragment();
//		android.app.Fragment shareFragment = new ShareFragment();
//		android.app.Fragment listFragment = new MusicListFragment();
//		android.app.Fragment musicInCloudFragment = new MusicInCloudFragment();
//		
//	    composeTab.setTabListener(new TabsListener(composeFragment));
//		postTab.setTabListener(new TabsListener(postFragment));
//		shareTab.setTabListener(new TabsListener(shareFragment));
//		listTab.setTabListener(new TabsListener(listFragment));
//		cloudTab.setTabListener(new TabsListener(musicInCloudFragment));
//		
//		actionBar.addTab(composeTab);
//		actionBar.addTab(postTab);
//		actionBar.addTab(shareTab);
//		actionBar.addTab(listTab);
//		actionBar.addTab(cloudTab);
	}
	
	@SuppressLint("NewApi")
	protected class TabsListener implements ActionBar.TabListener {
		private android.app.Fragment fragment;
		public TabsListener(android.app.Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.add(R.id.fragment_place, fragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {	
			ft.remove(fragment);
		}
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void ActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control, menu);
		return true;
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
		case R.id.action_logout:
			Navigation.logout(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
