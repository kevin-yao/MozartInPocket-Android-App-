package com.androidApp.mozartinpocket.util;

import com.androidApp.mozartinpocket.MusicsListActivity;
import com.androidApp.mozartinpocket.R;
import com.androidApp.mozartinpocket.cloud.MusicInCloudActivity;
import com.androidApp.mozartinpocket.compose.ComposeActivity;
import com.androidApp.mozartinpocket.post.PostActivity;
import com.androidApp.mozartinpocket.share.ShareActivity;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class TabsControl {
	public static void controlTabs(Activity activity) {
		View tab_view = activity.findViewById(R.id.navigation_bar);
		RadioButton composeTab = (RadioButton) tab_view.findViewById(R.id.main_tab_compose);
		final Activity a = activity;
		composeTab.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Navigation.compose(a);
		    }
		});
		
		RadioButton postTab = (RadioButton) tab_view.findViewById(R.id.main_tab_post);
		postTab.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Navigation.post(a);
		    }
		});
		
		RadioButton shareTab = (RadioButton) tab_view.findViewById(R.id.main_tab_share);
		shareTab.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Navigation.share(a);
		    }
		});
		
		RadioButton listTab = (RadioButton) tab_view.findViewById(R.id.main_tab_music_list);
		listTab.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Navigation.musicList(a);
		    }
		});
		
		RadioButton cloudTab = (RadioButton) tab_view.findViewById(R.id.main_tab_music_cloud);
		cloudTab.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Navigation.musicInCloud(a);
		    }
		});
		
		if(activity.equals(ComposeActivity.class)) {
			composeTab.setChecked(true);
		}else if(activity.equals(PostActivity.class)) {
			postTab.setChecked(true);
		}else if(activity.equals(ShareActivity.class)) {
			shareTab.setChecked(true);
		}else if(activity.equals(MusicsListActivity.class)) {
			listTab.setChecked(true);	
		}else if(activity.equals(MusicInCloudActivity.class)) {
			cloudTab.setChecked(true);
		}
	}
}
