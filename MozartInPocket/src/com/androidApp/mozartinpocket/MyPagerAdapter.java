package com.androidApp.mozartinpocket;

import java.util.Locale;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter{
	FragmentManager fm;
	public MyPagerAdapter(FragmentManager fm){
		super(fm);
		this.fm = fm;
	}

	//@Override
	public Fragment getItem(int position) {
		if(position == 0){
			return(fm.findFragmentByTag("musiclistTag"));
		}
		else{
			return(fm.findFragmentByTag("musiclistTag"));
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return ("music list").toUpperCase(l);
		case 1:
			return ("music detail").toUpperCase(l);
		}
		return(null);

	}
}
