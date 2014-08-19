package com.androidApp.mozartinpocket.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.androidApp.mozartinpocket.LoginActivity;
import com.androidApp.mozartinpocket.MusicsListActivity;
import com.androidApp.mozartinpocket.activities.UserProfileActivity;
import com.androidApp.mozartinpocket.cloud.MusicInCloudActivity;
import com.androidApp.mozartinpocket.compose.ComposeActivity;
import com.androidApp.mozartinpocket.post.PostActivity;
import com.androidApp.mozartinpocket.share.ShareActivity;

public class Navigation {
	public static void logout(Activity activity) {
		Intent intent = new Intent(activity, LoginActivity.class);
		activity.startActivity(intent);
	}
	
	public static void editProfile(Activity activity) {
		Intent intent = new Intent(activity, UserProfileActivity.class);
		intent.putExtra(LoginActivity.USER_NAME, LoginActivity.currentUser.getUsername());
		intent.putExtra(LoginActivity.PASSWORD, LoginActivity.currentUser.getPassword());
		activity.startActivity(intent);
	}
	
	public static void musicList(Fragment ft) {
		Intent intent = new Intent(ft.getActivity(), MusicsListActivity.class);
        ft.startActivity(intent);
	}
	
	public static void musicList(Activity activity) {
		Intent intent = new Intent(activity, MusicsListActivity.class);
		activity.startActivity(intent);
	}
	
	public static void post(Fragment ft){
		Intent intent = new Intent(ft.getActivity(), PostActivity.class);
		ft.startActivity(intent);
	}
	
	public static void post(Activity activity) {
		Intent intent = new Intent(activity, PostActivity.class);
		activity.startActivity(intent);
	}
	
	public static void share(Fragment ft){
		Intent intent = new Intent(ft.getActivity(), ShareActivity.class);
		ft.startActivity(intent);
	}

	public static void share(Activity activity) {
		Intent intent = new Intent(activity, ShareActivity.class);
		activity.startActivity(intent);
	}
	
	public static void musicInCloud(Fragment ft) {
		Intent intent = new Intent(ft.getActivity(), MusicInCloudActivity.class);
		ft.startActivity(intent);
	}
	
	public static void musicInCloud(Activity activity) {
		Intent intent = new Intent(activity, MusicInCloudActivity.class);
		activity.startActivity(intent);
	}
	
	public static void compose(Fragment ft) {
		Intent intent = new Intent(ft.getActivity(), ComposeActivity.class);
		ft.startActivity(intent);
	}
	
	public static void compose(Activity activity) {
		Intent intent = new Intent(activity, ComposeActivity.class);
		activity.startActivity(intent);
	}
}
