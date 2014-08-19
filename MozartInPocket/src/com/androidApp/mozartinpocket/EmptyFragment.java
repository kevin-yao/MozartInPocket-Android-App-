package com.androidApp.mozartinpocket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EmptyFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_empty, container,false);
		TextView text = (TextView) rootView.findViewById(R.id.txtView_empty_message);
		text.setText("  Hello!    " +LoginActivity.currentUser.getUsername() + "!\n" + "  You do not have any music files yet\n  Use Compose Function\n to compose your OWN music!");
		return rootView;
	}
}
