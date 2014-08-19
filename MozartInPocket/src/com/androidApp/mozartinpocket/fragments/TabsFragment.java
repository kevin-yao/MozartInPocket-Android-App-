package com.androidApp.mozartinpocket.fragments;

import com.androidApp.mozartinpocket.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabsFragment extends FragmentActivity {
	private FragmentTabHost mTabHost;
	private LayoutInflater layoutInflater;
	public static int tab_num = 5;
	private String tabNames[] = {"Compose", "Post", "Share", "Music List", "Music in Cloud"}; 
	private int mImageViewArray[] = {R.drawable.tab_compose,R.drawable.tab_post,R.drawable.tab_share,  
            R.drawable.tab_music_list,R.drawable.tab_music_cloud};  
	private Class<?> fragmentArray[] = {ComposeFragment.class,PostFragment.class,ShareFragment.class,MusicListFragment.class,MusicInCloudFragment.class}; 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tabs);
        
        layoutInflater = LayoutInflater.from(this); 
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(),R.id.realtabcontent);
        
        for(int i = 0; i < tab_num; i++){    
            TabSpec tabSpec = mTabHost.newTabSpec(tabNames[i]).setIndicator(getTabItemView(i));  
            mTabHost.addTab(tabSpec, fragmentArray[i], null);  
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_background);  
        }  
    }
	
	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);  
		ImageView image = (ImageView) view.findViewById(R.id.imageview_icon);
		image.setImageResource(mImageViewArray[index]);
		TextView textView = (TextView) view.findViewById(R.id.txtView_tab);          
        textView.setText(tabNames[index]);  
 		return view;	
	}
}
